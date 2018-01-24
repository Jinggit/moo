package com.moocall.moocall.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.moocall.moocall.ManageHerdActivity;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.dialogs.Dialog;
import com.moocall.moocall.exception.ErrorException;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.util.HttpGetContent;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetAnimalsAsyncTask extends AsyncTask<String, String, JSONObject> {
    private String TIMESTAMP_IN_FUTURE = "Timestamp in future.";
    private String TIMESTAMP_TOO_OLD = "Timestamp too old.";
    private Context context;
    private Exception exception;

    public GetAnimalsAsyncTask(Context context) {
        this.context = context;
    }

    protected JSONObject doInBackground(String... strings) {
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair(strings[1], strings[2]));
            return new JSONObject(new HttpGetContent(this.context).getContentWithArray(strings[0], nameValuePairs));
        } catch (IllegalStateException e) {
            this.exception = e;
            e.printStackTrace();
        } catch (ErrorException e2) {
            this.exception = e2;
            e2.printStackTrace();
        } catch (Exception e3) {
            this.exception = e3;
            e3.printStackTrace();
            Log.d("mreza", "nije ukljucena");
        }
        return null;
    }

    protected void onPostExecute(JSONObject response) {
        try {
            if ((this.exception instanceof IllegalStateException) || (this.exception instanceof ErrorException)) {
                if (!this.exception.getMessage().equals(this.TIMESTAMP_IN_FUTURE) && !this.exception.getMessage().equals(this.TIMESTAMP_TOO_OLD)) {
                    StorageContainer.removeCredentialsFromPreferences(this.context);
                } else if ((this.context instanceof Activity) && !((Activity) this.context).isFinishing()) {
                    Dialog.getInstance();
                    Dialog.alertClockProblemWithMessage(this.context).show();
                }
            } else if (this.exception instanceof Exception) {
                if (this.exception.getMessage().contains("Connection") && this.exception.getMessage().contains("refused") && (this.context instanceof Activity) && !((Activity) this.context).isFinishing()) {
                    Dialog.getInstance();
                    Dialog.alertNoInternetWithMessage(this.context).show();
                }
            } else if (response != null) {
                int i;
                AnimalDb animalDb;
                DaoSession daoSession = ((MoocallAnalyticsApplication) ((Activity) this.context).getApplication()).getDaoSession();
                AnimalDbDao animalDbDao = daoSession.getAnimalDbDao();
                AnimalDbDao.createTable(daoSession.getDatabase(), true);
                JSONArray newAnimals = response.getJSONArray("new");
                JSONArray updateAnimals = response.getJSONArray("update");
                for (i = 0; i < newAnimals.length(); i++) {
                    animalDb = new AnimalDb(new JSONParserBgw((JSONObject) newAnimals.get(i)));
                    animalDbDao.insert(animalDb);
                    Log.d("DaoExample", "Inserted new animal, ID: " + animalDb.getId());
                    Log.d("DaoExample", "Total animals: " + animalDbDao.count());
                }
                for (i = 0; i < updateAnimals.length(); i++) {
                    JSONParserBgw jsonParserAnimal = new JSONParserBgw((JSONObject) updateAnimals.get(i));
                    AnimalDb animalDbUpd = new AnimalDb(jsonParserAnimal);
                    List<Integer> cowList = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(7)});
                    List<Integer> bullList = Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(9)});
                    Collection typeList = new ArrayList();
                    if (cowList.contains(animalDbUpd.getType())) {
                        typeList = cowList;
                    } else if (bullList.contains(animalDbUpd.getType())) {
                        Object typeList2 = bullList;
                    }
                    List<AnimalDb> animal = animalDbDao.queryBuilder().where(Properties.Animal_id.eq(jsonParserAnimal.getInt("id")), Properties.Type.in(typeList)).list();
                    if (animal.size() > 0) {
                        animalDb = (AnimalDb) animal.get(0);
                        animalDb.setTag_number(animalDbUpd.getTag_number());
                        animalDb.setName(animalDbUpd.getName());
                        animalDb.setType(animalDbUpd.getType());
                        animalDb.setTime_update(animalDbUpd.getTime_update());
                        animalDb.setBirth_date(animalDbUpd.getBirth_date());
                        animalDb.setWeight(animalDbUpd.getWeight());
                        animalDb.setInsemination_date(animalDbUpd.getInsemination_date());
                        animalDb.setCycle_date(animalDbUpd.getCycle_date());
                        animalDb.setIn_heat_date(animalDbUpd.getIn_heat_date());
                        animalDb.setDue_date(animalDbUpd.getDue_date());
                        animalDb.setLast_calving_date(animalDbUpd.getLast_calving_date());
                        animalDb.setSensor(animalDbUpd.getSensor());
                        animalDb.setBreed(animalDbUpd.getBreed());
                        animalDb.setTemperament(animalDbUpd.getTemperament());
                        animalDb.setVendor(animalDbUpd.getVendor());
                        animalDb.setGestation(animalDbUpd.getGestation());
                        animalDb.setLast_state(animalDbUpd.getLast_state());
                        animalDb.setMoocall_tag_number(animalDbUpd.getMoocall_tag_number());
                        animalDb.setStatus(animalDbUpd.getStatus());
                        animalDb.setSold(animalDbUpd.getSold());
                        animalDb.setSlaughtered(animalDbUpd.getSlaughtered());
                        animalDb.setDied(animalDbUpd.getDied());
                        animalDb.setDate_sold(animalDbUpd.getDate_sold());
                        animalDb.setDate_slaughtered(animalDbUpd.getDate_slaughtered());
                        animalDbDao.update(animalDb);
                        Log.d("DaoExample", "Updated new animal, ID: " + animalDb.getId());
                        Log.d("DaoExample", "Total animals: " + animalDbDao.count());
                    } else {
                        animalDbDao.insert(animalDbUpd);
                        Log.d("DaoExample", "Inserted new animal, ID: " + animalDbUpd.getId());
                        Log.d("DaoExample", "Total animals: " + animalDbDao.count());
                    }
                }
                ManageHerdActivity activity = this.context;
                ManageHerdActivity.showProgress(false);
            } else if ((this.context instanceof Activity) && !((Activity) this.context).isFinishing()) {
                Dialog.getInstance();
                Dialog.alertWithMessage(this.context).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
