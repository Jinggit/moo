package com.moocall.moocall.service;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.db.DaoMaster;
import com.moocall.moocall.db.DaoMaster.DevOpenHelper;
import com.moocall.moocall.db.DaoSession;

public class MoocallAnalyticsApplication extends Application {
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;
    private Tracker mTracker;

    public synchronized Tracker getDefaultTracker() {
        if (this.mTracker == null) {
            this.mTracker = GoogleAnalytics.getInstance(this).newTracker((int) C0530R.xml.global_tracker);
        }
        return this.mTracker;
    }

    public void onCreate() {
        super.onCreate();
        this.daoSession = new DaoMaster(new DevOpenHelper(this, "moocall-db").getWritableDb()).newSession();
    }

    public DaoSession getDaoSession() {
        return this.daoSession;
    }
}
