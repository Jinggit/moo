package com.moocall.moocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.google.android.gms.analytics.Tracker;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.UserCredentials;
import com.moocall.moocall.exception.UnCaughtException;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.UserLoginUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity {
    private static String NO_AUTHORIZATION_DATA = "No authorization data.";
    private static String SIGNATURE_MISMATCH = "Signature mismatch.";
    private static EditText mEmailView;
    private static View mLoginFormView;
    private static EditText mPasswordView;
    private static View mProgressView;
    private BroadcastReceiver broadcastReceiver;
    private Editor editor;
    private Boolean hasInternetAccess = Boolean.valueOf(false);
    private Tracker mTracker;
    private SharedPreferences pref;

    class C04421 implements OnEditorActionListener {
        C04421() {
        }

        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id != C0530R.id.email && id != 0) {
                return false;
            }
            LoginActivity.this.attemptLogin();
            return true;
        }
    }

    class C04432 implements OnClickListener {
        C04432() {
        }

        public void onClick(View view) {
            LoginActivity.this.attemptLogin();
        }
    }

    class C04443 implements OnClickListener {
        C04443() {
        }

        public void onClick(View view) {
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        }
    }

    class C04454 implements OnClickListener {
        C04454() {
        }

        public void onClick(View view) {
            LoginActivity.this.about();
        }
    }

    class C04465 implements OnClickListener {
        C04465() {
        }

        public void onClick(View view) {
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
        }
    }

    class C04476 extends BroadcastReceiver {
        C04476() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                LoginActivity.this.unregisterReceiver(this);
                if (intent.getAction().equals(QuickstartPreferences.USER_LOGIN)) {
                    LoginActivity.this.onLoginCompleted(new JSONObject(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));
        this.mTracker = ((MoocallAnalyticsApplication) getApplication()).getDefaultTracker();
        createAsyncBroadcast();
        setContentView((int) C0530R.layout.activity_login);
        this.editor = StorageContainer.getEditor();
        mLoginFormView = findViewById(C0530R.id.login_form);
        mProgressView = findViewById(C0530R.id.progress_disable);
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.host = getResources().getString(C0530R.string.host);
        StorageContainer.socialHost = getResources().getString(C0530R.string.socialHost);
        UserCredentials userCredentials = (UserCredentials) getIntent().getSerializableExtra("UserCredentials");
        enableViewForLogin();
        if (userCredentials != null) {
            tryLogin();
        }
        this.mTracker.setScreenName("Login Activity");
        this.mTracker.send(new ScreenViewBuilder().build());
    }

    private void enableViewForLogin() {
        mEmailView = (EditText) findViewById(C0530R.id.email);
        mPasswordView = (EditText) findViewById(C0530R.id.password);
        mPasswordView.setOnEditorActionListener(new C04421());
        ((TextView) findViewById(C0530R.id.email_sign_in_button)).setOnClickListener(new C04432());
        ((TextView) findViewById(C0530R.id.email_signup_button)).setOnClickListener(new C04443());
        ((TextView) findViewById(C0530R.id.email_about_button)).setOnClickListener(new C04454());
        ((TextView) findViewById(C0530R.id.reset_password)).setOnClickListener(new C04465());
    }

    public void about() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://moocall.com")));
    }

    public void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!(TextUtils.isEmpty(password) || isPasswordValid(password))) {
            mPasswordView.setError(getString(C0530R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(C0530R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(C0530R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            login(email, password);
        }
    }

    public void login(String username, String password) {
        String shaUsername = username.trim().toLowerCase();
        System.out.println("Od ovog stringa trazim SHA1: " + shaUsername + password);
        String sha1Pass = StorageContainer.getSha1(shaUsername + password);
        try {
            username = URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Account.setUsername(username);
        Account.setPassword(sha1Pass);
        tryLogin();
    }

    public void tryLogin() {
        Toast.makeText(this, getString(C0530R.string.please_wait), 1).show();
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.USER_LOGIN));
        new AcquireResponseTask(this).execute(new String[]{new UserLoginUrl(String.valueOf(44)).createAndReturnUrl(this), QuickstartPreferences.USER_LOGIN});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04476();
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static void showProgress(boolean show) {
        int i;
        int i2 = 8;
        View view = mProgressView;
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = mLoginFormView;
        if (!show) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    public void onLoginCompleted(JSONObject result) {
        if (result != null) {
            try {
                JSONParserBgw jsonParserLogin = new JSONParserBgw(result);
                Integer forceReset = jsonParserLogin.getInt("loginStatus");
                String username = jsonParserLogin.getString("username");
                if (forceReset == null || !forceReset.equals(Integer.valueOf(0))) {
                    String firstName = jsonParserLogin.getString("firstName");
                    String lastName = jsonParserLogin.getString("lastName");
                    String email = jsonParserLogin.getString("email");
                    Integer maxPhones = jsonParserLogin.getInt("maxPhones");
                    Integer maxCalving = jsonParserLogin.getInt("calvingLimit");
                    Integer maxCow = jsonParserLogin.getInt("cowLimt");
                    JSONArray rolesJson = result.getJSONArray("role");
                    Boolean update = jsonParserLogin.getBoolean("update");
                    List<String> roles = new ArrayList();
                    for (int j = 0; j < rolesJson.length(); j++) {
                        roles.add((String) rolesJson.get(j));
                    }
                    Account.setName(firstName + StringUtils.SPACE + lastName);
                    Account.setEmail(email);
                    Account.setMaxPhones(maxPhones);
                    Account.setMaxCalving(maxCalving);
                    Account.setMaxCow(maxCow);
                    Account.setUpdate(update);
                    if (roles.contains("4")) {
                        Account.setMyMoocall(Boolean.valueOf(true));
                    } else {
                        Account.setMyMoocall(Boolean.valueOf(false));
                    }
                    this.editor.putString("username", Account.getUsername());
                    this.editor.putString("password", Account.getPassword());
                    this.editor.putString("email", Account.getEmail());
                    this.editor.putBoolean("myMoocall", Account.getMyMoocall().booleanValue());
                    this.editor.putString("name", Account.getName());
                    this.editor.putInt("maxPhones", Account.getMaxPhones().intValue());
                    this.editor.putInt("maxCalving", Account.getMaxCalving().intValue());
                    this.editor.putInt("maxCow", Account.getMaxCow().intValue());
                    this.editor.putBoolean("update", Account.getUpdate().booleanValue());
                    this.editor.commit();
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("signout", false).apply();
                    startActivity(new Intent(this, ManageHerdActivity.class));
                    return;
                }
                StorageContainer.removeCredentialsFromPreferences(getApplicationContext());
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(StorageContainer.host + "/account/reset-password/first/" + Base64.encodeToString(username.getBytes("UTF-8"), 0))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void credentialProblem(String problem, Context context) {
        showProgress(false);
        if (problem.equals(NO_AUTHORIZATION_DATA)) {
            if (mEmailView != null) {
                mEmailView.setError(context.getString(C0530R.string.incorrect_username));
                mEmailView.requestFocus();
                return;
            }
            StorageContainer.removeCredentialsFromPreferences(context.getApplicationContext());
        } else if (!problem.equals(SIGNATURE_MISMATCH)) {
        } else {
            if (mPasswordView != null) {
                mPasswordView.setError(context.getString(C0530R.string.incorrect_password));
                mPasswordView.requestFocus();
                return;
            }
            StorageContainer.removeCredentialsFromPreferences(context.getApplicationContext());
        }
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

    private void initCredentials() {
        Account.setUsername(this.pref.getString("username", null));
        Account.setPassword(this.pref.getString("password", null));
    }
}
