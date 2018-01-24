package com.moocall.moocall.async;

import android.content.Context;
import android.os.AsyncTask;
import com.moocall.moocall.interfaces.OnCheckInternetCompleted;
import com.moocall.moocall.util.UrlContent;

public class CheckInternetConnection extends AsyncTask<String, String, Boolean> {
    private Context context;
    private OnCheckInternetCompleted listener;

    public CheckInternetConnection(Context context, OnCheckInternetCompleted listener) {
        this.context = context;
        this.listener = listener;
    }

    protected Boolean doInBackground(String... params) {
        if (UrlContent.hasActiveInternetConnection(this.context)) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    protected void onPostExecute(Boolean internet) {
        if (internet.booleanValue()) {
            System.out.println("YEEEEEEEEEEEEEES");
        } else {
            System.out.println("NOOOOOOOOOOOOOOO");
        }
        this.listener.onCheckInternetCompleted(internet);
    }
}
