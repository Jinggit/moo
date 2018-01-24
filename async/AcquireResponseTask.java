package com.moocall.moocall.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.moocall.moocall.LoginActivity;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.CalvingDb;
import com.moocall.moocall.db.CalvingDbDao;
import com.moocall.moocall.db.CalvingDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.dialogs.Dialog;
import com.moocall.moocall.exception.ErrorException;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.util.HttpGetContent;
import com.moocall.moocall.util.StorageContainer;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.greendao.query.WhereCondition;

public class AcquireResponseTask extends AsyncTask<String, String, String> {
    private static final String TAG = "AcquireResponseTask";
    private String NO_AUTHORIZATION_DATA = "No authorization data.";
    private String SIGNATURE_MISMATCH = "Signature mismatch.";
    private String TIMESTAMP_IN_FUTURE = "Timestamp in future.";
    private String TIMESTAMP_TOO_OLD = "Timestamp too old.";
    private AnimalActionDb animalActionDb;
    private AnimalDb animalDb;
    private CalvingDb calvingDb;
    private Context context;
    private Exception exception;
    private String notifyChange;

    public AnimalDb getAnimalDb() {
        return this.animalDb;
    }

    public void setAnimalDb(AnimalDb animalDb) {
        this.animalDb = animalDb;
    }

    public AnimalActionDb getAnimalActionDb() {
        return this.animalActionDb;
    }

    public void setAnimalActionDb(AnimalActionDb animalActionDb) {
        this.animalActionDb = animalActionDb;
    }

    public CalvingDb getCalvingDb() {
        return this.calvingDb;
    }

    public void setCalvingDb(CalvingDb calvingDb) {
        this.calvingDb = calvingDb;
    }

    public AcquireResponseTask(Context context) {
        this.context = context;
    }

    protected void onPostExecute(String response) {
        if ((this.exception instanceof IllegalStateException) || (this.exception instanceof ErrorException)) {
            if (this.exception.getMessage().equals(this.TIMESTAMP_IN_FUTURE) || this.exception.getMessage().equals(this.TIMESTAMP_TOO_OLD)) {
                if ((this.context instanceof Activity) && !((Activity) this.context).isFinishing()) {
                    Dialog.getInstance();
                    Dialog.alertClockProblemWithMessage(this.context).show();
                }
            } else if (!this.exception.getMessage().equals(this.SIGNATURE_MISMATCH) && !this.exception.getMessage().equals(this.NO_AUTHORIZATION_DATA)) {
                StorageContainer.removeCredentialsFromPreferences(this.context);
            } else if (this.notifyChange.equals(QuickstartPreferences.USER_LOGIN)) {
                LoginActivity.credentialProblem(this.exception.getMessage(), this.context);
            } else {
                StorageContainer.removeCredentialsFromPreferences(this.context);
            }
        } else if (!(this.exception instanceof Exception)) {
            Intent intent = new Intent(this.notifyChange);
            if (response != null) {
                if ((this.notifyChange.equals(QuickstartPreferences.ADD_NEW_ANIMAL_SYNC) || this.notifyChange.equals(QuickstartPreferences.SCAN_MOOCALL_TAG)) && this.animalDb != null && this.animalDb.getAnimal_id().equals(Integer.valueOf(0))) {
                    Integer result = Integer.valueOf(Integer.parseInt(response));
                    DaoSession daoSession = ((MoocallAnalyticsApplication) ((Activity) this.context).getApplication()).getDaoSession();
                    CalvingDbDao calvingDbDao = daoSession.getCalvingDbDao();
                    AnimalActionDbDao animalActionDbDao = daoSession.getAnimalActionDbDao();
                    if (result.intValue() > 0) {
                        for (CalvingDb calving : calvingDbDao.queryBuilder().where(Properties.Cow_tag_number.eq(this.animalDb.getTag_number()), new WhereCondition[0]).list()) {
                            calving.setCow_id(result.toString());
                            calvingDbDao.update(calving);
                        }
                        for (CalvingDb calving2 : calvingDbDao.queryBuilder().where(Properties.Bull_tag_number.eq(this.animalDb.getTag_number()), new WhereCondition[0]).list()) {
                            calving2.setBull_id(result.toString());
                            calvingDbDao.update(calving2);
                        }
                        for (AnimalActionDb action : animalActionDbDao.queryBuilder().where(AnimalActionDbDao.Properties.Action.notEq(Integer.valueOf(16)), AnimalActionDbDao.Properties.Animal_tag_number.eq(this.animalDb.getTag_number())).list()) {
                            action.setAnimal_id(result.toString());
                            animalActionDbDao.update(action);
                        }
                    }
                }
                intent.putExtra("response", response);
                this.context.sendBroadcast(intent);
            } else if ((this.context instanceof Activity) && !((Activity) this.context).isFinishing()) {
                Dialog.getInstance();
                Dialog.alertWithMessage(this.context).show();
            }
        } else if (((this.exception.getMessage().contains("Connection") && this.exception.getMessage().contains("refused")) || this.exception.getMessage().contains("No address associated with hostname")) && (this.context instanceof Activity) && !((Activity) this.context).isFinishing()) {
            Dialog.getInstance();
            Dialog.alertNoInternetWithMessage(this.context).show();
        }
    }

    protected String doInBackground(String... params) {
        this.notifyChange = params[1];
        Log.d(TAG, this.notifyChange);
        try {
            String responseString;
            if (params.length > 2) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList();
                for (int i = 2; i + 1 < params.length; i += 2) {
                    nameValuePairs.add(new BasicNameValuePair(params[i], params[i + 1]));
                }
                responseString = new String(new HttpGetContent(this.context).getContentWithArray(params[0], nameValuePairs));
            } else if (this.notifyChange.equals(QuickstartPreferences.USER_LOGIN)) {
                responseString = new HttpGetContent(this.context).getLoginContent(params[0]);
            } else {
                responseString = new HttpGetContent(this.context).getContent(params[0]);
            }
            return responseString;
        } catch (IllegalStateException e) {
            this.exception = e;
            e.printStackTrace();
            return null;
        } catch (ErrorException e2) {
            this.exception = e2;
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            this.exception = e3;
            e3.printStackTrace();
            Log.d("mreza", "nije ukljucena");
            return null;
        }
    }
}
