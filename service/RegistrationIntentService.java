package com.moocall.moocall.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.url.SendTokenUrl;
import com.moocall.moocall.util.StorageContainer;
import io.intercom.android.sdk.Intercom;
import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = new String[]{"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            String token = InstanceID.getInstance(this).getToken(getString(C0530R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            StorageContainer.wakeApp(this);
            sendRegistrationToServer(token);
            if (StorageContainer.getAccount() != null) {
                Intercom.initialize(getApplication(), "android_sdk-2216b491b27bd030661438dce7e7580332fbb44e", "e5jq39ob");
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    private void sendRegistrationToServer(String token) {
        String phoneUid = ((TelephonyManager) getSystemService("phone")).getDeviceId();
        if (phoneUid == null) {
            phoneUid = "simulator";
        }
        sendBroadcast(new Intent(QuickstartPreferences.TOKEN_SEND).putExtra("token", token).putExtra("phoneUid", phoneUid));
        new AcquireResponseTask(this).execute(new String[]{new SendTokenUrl(token, phoneUid).createAndReturnUrl(this), QuickstartPreferences.SAVE_TOKEN});
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
