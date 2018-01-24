package com.moocall.moocall;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.ResetPasswordUrl;
import com.moocall.moocall.util.StorageContainer;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ResetPasswordActivity extends Activity {
    private BroadcastReceiver broadcastReceiver;
    private EditText emailEdit;
    private View progressView;
    private View resetFormView;

    class C05311 implements OnClickListener {
        C05311() {
        }

        public void onClick(View view) {
            ResetPasswordActivity.this.startReset();
        }
    }

    class C05322 implements OnClickListener {
        C05322() {
        }

        public void onClick(View view) {
            ResetPasswordActivity.this.onBackPressed();
        }
    }

    class C05333 extends BroadcastReceiver {
        C05333() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                ResetPasswordActivity.this.unregisterReceiver(this);
                if (intent.getAction().equals(QuickstartPreferences.RESET_PASSWORD)) {
                    ResetPasswordActivity.this.onResetPasswordCompleted(new Boolean(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0530R.layout.activity_reset_password);
        onResume();
        createAsyncBroadcast();
        implementListeners();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    private void implementListeners() {
        this.resetFormView = findViewById(C0530R.id.reset_password_form);
        this.progressView = findViewById(C0530R.id.progress_disable);
        TextView backToLogin = (TextView) findViewById(C0530R.id.backToLogin);
        ((TextView) findViewById(C0530R.id.resetButton)).setOnClickListener(new C05311());
        backToLogin.setOnClickListener(new C05322());
    }

    private void startReset() {
        this.emailEdit = (EditText) findViewById(C0530R.id.emailForReset);
        String email = this.emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.emailEdit.setError(getString(C0530R.string.error_field_required));
            this.emailEdit.requestFocus();
            return;
        }
        reset(email);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void reset(String email) {
        try {
            email = URLEncoder.encode(email, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.RESET_PASSWORD));
        new AcquireResponseTask(this).execute(new String[]{new ResetPasswordUrl(email).createAndReturnUrl(this), QuickstartPreferences.RESET_PASSWORD});
    }

    public void showProgress(boolean show) {
        int i;
        int i2 = 8;
        View view = this.progressView;
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = this.resetFormView;
        if (!show) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C05333();
    }

    public void onResetPasswordCompleted(Boolean success) {
        try {
            showProgress(false);
            showMessage(success);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(final Boolean success) {
        CharSequence message;
        CharSequence title;
        String message2 = "";
        String title2 = "";
        if (success.booleanValue()) {
            message = getString(C0530R.string.good_reset);
            title = getString(C0530R.string.good_reset_title);
        } else {
            message = getString(C0530R.string.bad_reset);
            title = getString(C0530R.string.bad_reset_title);
        }
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(message).setTitle(title);
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (success.booleanValue()) {
                    ResetPasswordActivity.this.finish();
                    return;
                }
                dialog.cancel();
                ResetPasswordActivity.this.emailEdit.setError(ResetPasswordActivity.this.getString(C0530R.string.incorrect_username));
                ResetPasswordActivity.this.emailEdit.requestFocus();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }
}
