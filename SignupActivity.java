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
import com.moocall.moocall.url.SignupUrl;
import com.moocall.moocall.util.StorageContainer;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignupActivity extends Activity {
    private BroadcastReceiver broadcastReceiver;
    private View progressView;
    private View signupFormView;

    class C05501 implements OnClickListener {
        C05501() {
        }

        public void onClick(View view) {
            SignupActivity.this.startSignup();
        }
    }

    class C05512 implements OnClickListener {
        C05512() {
        }

        public void onClick(View view) {
            SignupActivity.this.onBackPressed();
        }
    }

    class C05523 extends BroadcastReceiver {
        C05523() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                SignupActivity.this.unregisterReceiver(this);
                if (intent.getAction().equals(QuickstartPreferences.USER_SIGNUP)) {
                    SignupActivity.this.onSignupCompleted(new Integer(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0530R.layout.activity_signup);
        onResume();
        createAsyncBroadcast();
        implementListeners();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    private void implementListeners() {
        this.signupFormView = findViewById(C0530R.id.signup_form);
        this.progressView = findViewById(C0530R.id.progress_disable);
        TextView backToLogin = (TextView) findViewById(C0530R.id.backToLogin);
        ((TextView) findViewById(C0530R.id.signupButton)).setOnClickListener(new C05501());
        backToLogin.setOnClickListener(new C05512());
    }

    private void startSignup() {
        View firstNameEdit = (EditText) findViewById(C0530R.id.firstName);
        EditText lastNameEdit = (EditText) findViewById(C0530R.id.lastName);
        View usernameEdit = (EditText) findViewById(C0530R.id.username);
        View passwordEdit = (EditText) findViewById(C0530R.id.password);
        View repeatPasswordEdit = (EditText) findViewById(C0530R.id.repeatPassword);
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String repeatPassword = repeatPasswordEdit.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(firstName)) {
            firstNameEdit.setError(getString(C0530R.string.error_field_required));
            focusView = firstNameEdit;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            usernameEdit.setError(getString(C0530R.string.error_field_required));
            focusView = usernameEdit;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            usernameEdit.setError(getString(C0530R.string.error_field_required));
            focusView = usernameEdit;
            cancel = true;
        } else if (!isEmailValid(username)) {
            usernameEdit.setError(getString(C0530R.string.error_invalid_email));
            focusView = usernameEdit;
            cancel = true;
        }
        if (!(TextUtils.isEmpty(password) || isPasswordValid(password))) {
            passwordEdit.setError(getString(C0530R.string.error_invalid_password));
            focusView = passwordEdit;
            cancel = true;
        }
        if (!repeatPassword.equals(password)) {
            repeatPasswordEdit.setError(getString(C0530R.string.password_mismatch));
            focusView = repeatPasswordEdit;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            signup(firstName, lastName, username, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void signup(String firstName, String lastName, String username, String password) {
        password = StorageContainer.getSha1(username.trim().toLowerCase() + password);
        try {
            username = URLEncoder.encode(username, "UTF-8");
            firstName = URLEncoder.encode(firstName, "UTF-8");
            lastName = URLEncoder.encode(lastName, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.USER_SIGNUP));
        new AcquireResponseTask(this).execute(new String[]{new SignupUrl(firstName, lastName, username, password).createAndReturnUrl(this), QuickstartPreferences.USER_SIGNUP});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C05523();
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
        View view2 = this.signupFormView;
        if (!show) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    public void onSignupCompleted(Integer result) {
        try {
            showProgress(false);
            Boolean success = Boolean.valueOf(false);
            if (result.intValue() > 0) {
                success = Boolean.valueOf(true);
            }
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
            message = getString(C0530R.string.good_signup);
            title = getString(C0530R.string.good_signup_title);
        } else {
            message = getString(C0530R.string.bad_signup);
            title = getString(C0530R.string.bad_signup_title);
        }
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(message).setTitle(title);
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (success.booleanValue()) {
                    SignupActivity.this.finish();
                } else {
                    dialog.cancel();
                }
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }
}
