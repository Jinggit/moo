package com.moocall.moocall.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MoocallService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.e("Log", "onCreateMoocallService");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Log.e("Log", "onDestroyMoocallService");
        sendBroadcast(new Intent(QuickstartPreferences.RESTART_SERVICE));
    }
}
