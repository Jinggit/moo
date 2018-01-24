package com.moocall.moocall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.moocall.moocall.async.CheckInternetConnection;
import com.moocall.moocall.dialogs.AlertDialogCallback;
import com.moocall.moocall.dialogs.Dialog;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.UserCredentials;
import com.moocall.moocall.interfaces.OnCheckInternetCompleted;
import com.moocall.moocall.util.StorageContainer;

public class SplashActivity extends Activity implements OnCheckInternetCompleted, AlertDialogCallback<String> {
    private Editor editor;
    private SharedPreferences pref;
    private UserCredentials userCredentials;

    class C05711 implements Runnable {
        C05711() {
        }

        public void run() {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra("UserCredentials", SplashActivity.this.userCredentials);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageContainer.host = getResources().getString(C0530R.string.host);
        StorageContainer.socialHost = getResources().getString(C0530R.string.socialHost);
        setContentView(C0530R.layout.activity_splash);
        ((TextView) findViewById(C0530R.id.versionName)).setText(BuildConfig.VERSION_NAME);
        if (hasStoredCredentials().booleanValue()) {
            System.out.println("kredencijali postoje");
            initCredentials();
        } else {
            System.out.println("kredencijali ne postoje");
        }
        startLoginIntent();
    }

    public void onCheckInternetCompleted(Boolean hasInternet) {
        if (hasInternet.booleanValue()) {
            if (hasStoredCredentials().booleanValue()) {
                System.out.println("kredencijali postoje");
                initCredentials();
            } else {
                System.out.println("kredencijali ne postoje");
            }
            startLoginIntent();
        } else if (!isFinishing()) {
            Dialog.getInstance();
            Dialog.alertWithPositiveCallback(this, this).show();
        }
    }

    private void startLoginIntent() {
        new Handler().postDelayed(new C05711(), (long) 1000);
    }

    private void initCredentials() {
        this.userCredentials = new UserCredentials(this.pref.getString("username", null), this.pref.getString("password", null));
        Account.setUsername(this.pref.getString("username", null));
        Account.setPassword(this.pref.getString("password", null));
    }

    private Boolean hasStoredCredentials() {
        Log.d("credentials", "checking");
        this.pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        StorageContainer.credentialSetup(this.pref);
        this.editor = StorageContainer.getEditor();
        if (this.pref.getString("username", null) != null) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public void alertDialogCallback(String ret) {
        new CheckInternetConnection(this, this).execute(new String[0]);
    }
}
