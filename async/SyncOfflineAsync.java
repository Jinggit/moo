package com.moocall.moocall.async;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.moocall.moocall.LoginActivity;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.CalvingDb;
import com.moocall.moocall.db.CalvingDbDao;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.domain.UserCredentials;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddAnimalActionUrl;
import com.moocall.moocall.url.AddNewAnimalSyncUrl;
import com.moocall.moocall.url.DeleteAnimalUrl;
import com.moocall.moocall.url.EditAnimalUrl;
import com.moocall.moocall.url.SaveCalvingDataUrl;
import com.moocall.moocall.url.SaveMoocallTagUrl;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.greendao.query.WhereCondition;

public class SyncOfflineAsync {
    private AnimalActionDbDao animalActionDbDao;
    private AnimalDbDao animalDbDao;
    private BroadcastReceiver broadcastReceiver;
    private CalvingDbDao calvingDbDao;
    private Context context;
    private UserCredentials userCredentials;

    class C06041 extends BroadcastReceiver {
        C06041() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                SyncOfflineAsync.this.context.unregisterReceiver(this);
                String action = intent.getAction();
                if ((action.equals(QuickstartPreferences.ADD_NEW_ANIMAL_SYNC) || action.equals(QuickstartPreferences.SCAN_MOOCALL_TAG)) && Integer.valueOf(Integer.parseInt((String) intent.getSerializableExtra("response"))).intValue() > 0) {
                    SyncOfflineAsync.this.syncCalving();
                    SyncOfflineAsync.this.syncAction();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SyncOfflineAsync(Context context, UserCredentials userCredentials) {
        this.context = context;
        this.userCredentials = userCredentials;
        createAsyncBroadcast();
        doInBackground();
        onPostExecute();
    }

    private void doInBackground() {
        try {
            DaoSession daoSession = ((MoocallAnalyticsApplication) ((Activity) this.context).getApplication()).getDaoSession();
            this.animalDbDao = daoSession.getAnimalDbDao();
            this.calvingDbDao = daoSession.getCalvingDbDao();
            this.animalActionDbDao = daoSession.getAnimalActionDbDao();
            syncUpdatedAnimal();
            syncDeletedAnimal();
            syncCalving();
            syncAction();
            syncNewAnimal();
            syncScanAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncNewAnimal() {
        try {
            for (AnimalDb newAnimal : this.animalDbDao.queryBuilder().where(Properties.New_animal.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).list()) {
                String name = "";
                String temperament = null;
                String vendor = null;
                if (newAnimal.getName() != null) {
                    name = URLEncoder.encode(newAnimal.getName(), "UTF-8");
                }
                if (newAnimal.getTemperament() != null) {
                    temperament = URLEncoder.encode(newAnimal.getTemperament(), "UTF-8");
                }
                if (newAnimal.getVendor() != null) {
                    vendor = URLEncoder.encode(newAnimal.getVendor(), "UTF-8");
                }
                this.context.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_NEW_ANIMAL_SYNC));
                AcquireResponseTask task = new AcquireResponseTask(this.context);
                task.setAnimalDb(newAnimal);
                task.execute(new String[]{new AddNewAnimalSyncUrl(newAnimal.getBirth_date(), name, newAnimal.getType().toString(), newAnimal.getState(), newAnimal.getServer_state_cet_time(), temperament, vendor, newAnimal.getGestation().toString()).createAndReturnUrl(this.context), QuickstartPreferences.ADD_NEW_ANIMAL_SYNC, "breed", newAnimal.getBreed(), "encoded_image", null, "tag-number", newAnimal.getTag_number(), "moocall-tag-number", newAnimal.getMoocall_tag_number()});
                this.animalDbDao.delete(newAnimal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncUpdatedAnimal() {
        try {
            for (AnimalDb updatedAnimal : this.animalDbDao.queryBuilder().where(Properties.Updated_animal.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).list()) {
                String name = URLEncoder.encode(updatedAnimal.getName(), "UTF-8");
                String temperament = null;
                String vendor = null;
                if (updatedAnimal.getTemperament() != null) {
                    temperament = URLEncoder.encode(updatedAnimal.getTemperament(), "UTF-8");
                }
                if (updatedAnimal.getVendor() != null) {
                    vendor = URLEncoder.encode(updatedAnimal.getVendor(), "UTF-8");
                }
                this.context.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.EDIT_ANIMAL));
                AcquireResponseTask task = new AcquireResponseTask(this.context);
                task.setAnimalDb(updatedAnimal);
                task.execute(new String[]{new EditAnimalUrl(updatedAnimal.getAnimal_id().toString(), updatedAnimal.getBirth_date(), name, updatedAnimal.getType().toString(), updatedAnimal.getType().toString(), temperament, vendor, updatedAnimal.getState(), updatedAnimal.getServer_state_cet_time(), updatedAnimal.getGestation().toString()).createAndReturnUrl(this.context), QuickstartPreferences.EDIT_ANIMAL, "breed", updatedAnimal.getBreed(), "encoded_image", null, "tag-number", updatedAnimal.getTag_number()});
                updatedAnimal.setUpdated_animal(Integer.valueOf(0));
                this.animalDbDao.update(updatedAnimal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncDeletedAnimal() {
        for (AnimalDb deletedAnimal : this.animalDbDao.queryBuilder().where(Properties.Deleted_animal.eq(Integer.valueOf(1)), new WhereCondition[0]).list()) {
            this.context.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_ANIMAL));
            AcquireResponseTask task = new AcquireResponseTask(this.context);
            task.setAnimalDb(deletedAnimal);
            task.execute(new String[]{new DeleteAnimalUrl(deletedAnimal.getAnimal_id().toString(), deletedAnimal.getType().toString()).createAndReturnUrl(this.context), QuickstartPreferences.DELETE_ANIMAL});
            this.animalDbDao.delete(deletedAnimal);
        }
    }

    private void syncScanAction() {
        for (AnimalActionDb animalAction : this.animalActionDbDao.queryBuilder().where(AnimalActionDbDao.Properties.Action.eq(Integer.valueOf(16)), new WhereCondition[0]).list()) {
            AnimalDb animalForTag = null;
            List<AnimalDb> animalList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Tag_number.eq(animalAction.getAnimal_tag_number())).list();
            if (animalList.size() > 0) {
                animalForTag = (AnimalDb) animalList.get(0);
            }
            if (!(animalForTag == null || animalForTag.getAnimal_id().equals(Integer.valueOf(0)))) {
                this.context.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SCAN_MOOCALL_TAG));
                AcquireResponseTask task = new AcquireResponseTask(this.context);
                task.setAnimalDb(animalForTag);
                task.setAnimalActionDb(animalAction);
                task.execute(new String[]{new SaveMoocallTagUrl(animalForTag, "1", "1").createAndReturnUrl(this.context), QuickstartPreferences.SCAN_MOOCALL_TAG, "moocall-tag-number", animalAction.getMoocall_tag_number(), "tag-number", animalAction.getAnimal_tag_number()});
            }
            this.animalActionDbDao.delete(animalAction);
        }
    }

    private void syncCalving() {
        for (CalvingDb calving : this.calvingDbDao.queryBuilder().where(CalvingDbDao.Properties.Cow_id.notEq("0"), CalvingDbDao.Properties.Bull_id.notEq("0")).list()) {
            this.context.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SAVE_CALVING_DATA));
            AcquireResponseTask task = new AcquireResponseTask(this.context);
            task.setCalvingDb(calving);
            task.execute(new String[]{new SaveCalvingDataUrl(calving.getCow_id(), calving.getBull_id(), calving.getDatetime(), calving.getCalves_number()).createAndReturnUrl(this.context), QuickstartPreferences.SAVE_CALVING_DATA, "calves", calving.getCalves(), "bull-tag-number", calving.getBull_tag_number()});
            this.calvingDbDao.delete(calving);
        }
    }

    private void syncAction() {
        for (AnimalActionDb animalAction : this.animalActionDbDao.queryBuilder().where(AnimalActionDbDao.Properties.Action.notEq(Integer.valueOf(16)), AnimalActionDbDao.Properties.Animal_id.notEq("0")).list()) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList();
            switch (animalAction.getAction().intValue()) {
                case 0:
                    nameValuePairs.add(new BasicNameValuePair("sensor", animalAction.getSensor()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 2:
                    nameValuePairs.add(new BasicNameValuePair("weight", animalAction.getWeight()));
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 3:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("description", animalAction.getDescription()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 4:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("bull-tag-number", animalAction.getBull_tag_number()));
                    nameValuePairs.add(new BasicNameValuePair("bull-id", animalAction.getBull_id()));
                    break;
                case 5:
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 6:
                    nameValuePairs.add(new BasicNameValuePair("weaned", animalAction.getWeaned()));
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 7:
                    nameValuePairs.add(new BasicNameValuePair("fat-score", animalAction.getFat_score()));
                    nameValuePairs.add(new BasicNameValuePair("grade", animalAction.getGrade()));
                    nameValuePairs.add(new BasicNameValuePair("dead-weight", animalAction.getDead_weight()));
                    break;
                case 8:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("description", animalAction.getDescription()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 9:
                    nameValuePairs.add(new BasicNameValuePair("weight", animalAction.getWeight()));
                    nameValuePairs.add(new BasicNameValuePair(Param.PRICE, animalAction.getPrice()));
                    nameValuePairs.add(new BasicNameValuePair("description", animalAction.getDescription()));
                    break;
                case 10:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("description", animalAction.getDescription()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 12:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 13:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 14:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("text", animalAction.getText()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                case 15:
                    nameValuePairs.add(new BasicNameValuePair("datetime", animalAction.getDatetime()));
                    nameValuePairs.add(new BasicNameValuePair("cow-tag-number", animalAction.getCow_tag_number()));
                    nameValuePairs.add(new BasicNameValuePair("cow-id", animalAction.getCow_id()));
                    break;
                case 17:
                    nameValuePairs.add(new BasicNameValuePair("sensor", animalAction.getSensor()));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    break;
                default:
                    break;
            }
            this.context.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_ANIMAL_ACTION));
            AcquireResponseTask task = new AcquireResponseTask(this.context);
            task.setAnimalActionDb(animalAction);
            task.execute(new String[]{new AddAnimalActionUrl(animalAction.getAction().toString(), animalAction.getAnimal_type().toString(), animalAction.getAnimal_id().toString(), null).createAndReturnUrl(this.context), QuickstartPreferences.ADD_ANIMAL_ACTION, "tag-number", animalAction.getAnimal_tag_number(), ((NameValuePair) nameValuePairs.get(0)).getName(), ((NameValuePair) nameValuePairs.get(0)).getValue(), ((NameValuePair) nameValuePairs.get(1)).getName(), ((NameValuePair) nameValuePairs.get(1)).getValue(), ((NameValuePair) nameValuePairs.get(2)).getName(), ((NameValuePair) nameValuePairs.get(2)).getValue()});
            this.animalActionDbDao.delete(animalAction);
        }
    }

    private void onPostExecute() {
        if (this.userCredentials != null) {
            Intent intent = new Intent(this.context, LoginActivity.class);
            intent.addFlags(67108864);
            intent.addFlags(268435456);
            intent.putExtra("UserCredentials", this.userCredentials);
            this.context.startActivity(intent);
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C06041();
    }
}
