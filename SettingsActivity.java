package com.moocall.moocall;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.google.android.gms.analytics.Tracker;
import com.moocall.moocall.adapter.EmailListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.ClientSettings;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.GetClientSettingsUrl;
import com.moocall.moocall.url.SetClientSettingsUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.MoocallTimezones;
import com.moocall.moocall.util.StorageContainer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SettingsActivity extends ActionBarActivity {
    private ImageView addAnotherEmail;
    private RelativeLayout addAnotherEmailFooter;
    private BroadcastReceiver broadcastReceiver;
    private ClientSettings cs;
    private List<String> emailList;
    private EmailListAdapter emailListAdapter;
    private ListView emailListView;
    private ImageView enableAppNotification;
    private Boolean enableNotification;
    private RelativeLayout footerLayout;
    private RelativeLayout headerLayout;
    private ImageView kgsImage;
    private RelativeLayout kgsSelect;
    private ImageView lbsImage;
    private RelativeLayout lbsSelect;
    private Tracker mTracker;
    private Boolean notificationChanged = Boolean.valueOf(false);
    private View progressView;
    private ImageView receiveEmailNotif;
    private String ringtone;
    private TextView ringtoneSelect;
    private SharedPreferences sharedPreferences;
    private TextView timeZoneSelect;
    private Toolbar toolbar;

    class C05421 implements OnClickListener {
        C05421() {
        }

        public void onClick(View v) {
            SettingsActivity.this.onBackPressed();
        }
    }

    class C05433 implements OnClickListener {
        C05433() {
        }

        public void onClick(View view) {
            if (SettingsActivity.this.emailList.size() < 3) {
                SettingsActivity.this.emailList.add("");
                if (SettingsActivity.this.emailList.size() == 3) {
                    SettingsActivity.this.emailListView.removeFooterView(SettingsActivity.this.addAnotherEmailFooter);
                }
                SettingsActivity.this.emailListAdapter.notifyDataSetChanged();
            }
        }
    }

    class C05444 implements OnClickListener {
        C05444() {
        }

        public void onClick(View view) {
            if (SettingsActivity.this.cs.getEmailOption().equals(Integer.valueOf(1))) {
                SettingsActivity.this.cs.setEmailOption(Integer.valueOf(0));
            } else {
                SettingsActivity.this.cs.setEmailOption(Integer.valueOf(1));
            }
            SettingsActivity.this.cs.setChanged(Boolean.valueOf(true));
            SettingsActivity.this.receiveNotification();
        }
    }

    class C05455 implements OnClickListener {
        C05455() {
        }

        public void onClick(View view) {
            if (SettingsActivity.this.cs.getEnableNotification().booleanValue()) {
                SettingsActivity.this.cs.setEnableNotification(Boolean.valueOf(false));
                SettingsActivity.this.cs.setNotificationChanged(Boolean.valueOf(true));
                SettingsActivity.this.sharedPreferences.edit().putBoolean("enableNotification", false).apply();
            } else {
                SettingsActivity.this.cs.setEnableNotification(Boolean.valueOf(true));
                SettingsActivity.this.cs.setNotificationChanged(Boolean.valueOf(true));
                SettingsActivity.this.sharedPreferences.edit().putBoolean("enableNotification", true).apply();
            }
            SettingsActivity.this.notificationChanged = Boolean.valueOf(true);
            SettingsActivity.this.enableNotification();
        }
    }

    class C05466 implements OnClickListener {
        C05466() {
        }

        public void onClick(View v) {
            SettingsActivity.this.openNotificationDialog();
        }
    }

    class C05477 implements OnClickListener {
        C05477() {
        }

        public void onClick(View view) {
            SettingsActivity.this.sharedPreferences.edit().putBoolean("kgs", true).apply();
            SettingsActivity.this.weightUnitShow();
        }
    }

    class C05488 implements OnClickListener {
        C05488() {
        }

        public void onClick(View view) {
            SettingsActivity.this.sharedPreferences.edit().putBoolean("kgs", false).apply();
            SettingsActivity.this.weightUnitShow();
        }
    }

    class C05499 extends BroadcastReceiver {
        C05499() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                SettingsActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.GET_CLIENT_SETTINGS)) {
                    SettingsActivity.this.onGetClientSettingsCompleted(new JSONObject(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.SET_CLIENT_SETTINGS)) {
                    SettingsActivity.this.onSetClientSettingsCompleted(intent.getStringExtra("response"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C11072 implements OnMenuItemClickListener {
        C11072() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_settings);
        this.mTracker = ((MoocallAnalyticsApplication) getApplication()).getDefaultTracker();
        onResume();
        createAsyncBroadcast();
        setupToolbar();
        initilizeElements();
        showProgress(true);
        getTimezone();
        implementListeners();
        getSettings();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setTitle(getResources().getString(C0530R.string.action_settings));
        this.toolbar.setNavigationOnClickListener(new C05421());
        this.toolbar.inflateMenu(C0530R.menu.only_save);
        this.toolbar.setOnMenuItemClickListener(new C11072());
    }

    public void initilizeElements() {
        this.emailListView = (ListView) findViewById(C0530R.id.emailList);
        this.headerLayout = (RelativeLayout) getLayoutInflater().inflate(C0530R.layout.settings_above_list, null);
        this.emailListView.addHeaderView(this.headerLayout);
        this.addAnotherEmailFooter = (RelativeLayout) getLayoutInflater().inflate(C0530R.layout.email_list_footer, null);
        this.emailListView.addFooterView(this.addAnotherEmailFooter);
        this.addAnotherEmail = (ImageView) findViewById(C0530R.id.addAnotherEmail);
        this.footerLayout = (RelativeLayout) getLayoutInflater().inflate(C0530R.layout.settings_below_list, null);
        this.emailListView.addFooterView(this.footerLayout);
        this.timeZoneSelect = (TextView) findViewById(C0530R.id.timeZoneSelect);
        this.ringtoneSelect = (TextView) findViewById(C0530R.id.ringtoneSelect);
        this.kgsSelect = (RelativeLayout) findViewById(C0530R.id.kgsSelect);
        this.lbsSelect = (RelativeLayout) findViewById(C0530R.id.lbsSelect);
        this.kgsImage = (ImageView) findViewById(C0530R.id.kgsImage);
        this.lbsImage = (ImageView) findViewById(C0530R.id.lbsImage);
        this.receiveEmailNotif = (ImageView) findViewById(C0530R.id.receiveEmailNotif);
        this.emailList = new ArrayList();
        this.emailListAdapter = new EmailListAdapter(this, this.emailList);
        this.emailListView.setAdapter(this.emailListAdapter);
        this.enableAppNotification = (ImageView) findViewById(C0530R.id.customRingtone);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.progressView = findViewById(C0530R.id.progress_disable);
        ((TextView) findViewById(C0530R.id.versionName)).setText(BuildConfig.VERSION_NAME);
    }

    public void implementListeners() {
        if (Account.getMyMoocall().booleanValue()) {
            this.addAnotherEmail.setOnClickListener(new C05433());
            this.receiveEmailNotif.setOnClickListener(new C05444());
        }
        this.enableAppNotification.setOnClickListener(new C05455());
        this.ringtoneSelect.setOnClickListener(new C05466());
        this.kgsSelect.setOnClickListener(new C05477());
        this.lbsSelect.setOnClickListener(new C05488());
    }

    private void openNotificationDialog() {
        Uri defaultNotification = Uri.parse("android.resource://com.moocall.moocall/raw/cowsound_long_long");
        Uri currentNotification = Uri.parse("android.resource://com.moocall.moocall/raw/cowsound_long_long");
        if (!(this.cs.getRingtone() == null || this.cs.getRingtone().equals("Moocall Moo"))) {
            RingtoneManager manager = new RingtoneManager(this);
            manager.setType(1);
            Cursor cursor = manager.getCursor();
            while (cursor.moveToNext()) {
                if (this.cs.getRingtone().equals(cursor.getString(1))) {
                    currentNotification = manager.getRingtoneUri(cursor.getPosition());
                }
            }
        }
        Intent ringtone = new Intent("android.intent.action.RINGTONE_PICKER");
        ringtone.putExtra("android.intent.extra.ringtone.TYPE", 1);
        ringtone.putExtra("android.intent.extra.ringtone.SHOW_SILENT", false);
        ringtone.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
        ringtone.putExtra("android.intent.extra.ringtone.DEFAULT_URI", defaultNotification);
        startActivityForResult(ringtone, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            String title;
            Ringtone ringtone = RingtoneManager.getRingtone(this, (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI"));
            if (ringtone != null) {
                title = ringtone.getTitle(this);
            } else {
                title = "cowsound_long_long";
            }
            if (title.equals("cowsound_long_long")) {
                title = "Moocall Moo";
            }
            if (this.cs != null) {
                this.cs.setRingtone(title);
                this.cs.setNotificationChanged(Boolean.valueOf(true));
                this.sharedPreferences.edit().putString("ringtone", title).apply();
                this.ringtoneSelect.setText(title);
                this.notificationChanged = Boolean.valueOf(true);
            }
        }
    }

    private void getTimezone() {
        this.timeZoneSelect.setText(new MoocallTimezones(this).getTimezone());
    }

    public void getSettings() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_CLIENT_SETTINGS));
        new AcquireResponseTask(this).execute(new String[]{new GetClientSettingsUrl().createAndReturnUrl(this), QuickstartPreferences.GET_CLIENT_SETTINGS});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C05499();
    }

    private void onGetClientSettingsCompleted(JSONObject jsonObject) {
        try {
            JSONParserBgw jsonParserSettings = new JSONParserBgw(jsonObject);
            String email1 = jsonParserSettings.getString("contact_email");
            Integer emailOption = jsonParserSettings.getInt("email_option");
            String timezone = jsonParserSettings.getString("timezone");
            JSONArray clientMails = jsonObject.getJSONArray("client_mails");
            String email2 = null;
            String email3 = null;
            for (int i = 0; i < clientMails.length(); i++) {
                String clientMail = clientMails.getString(i);
                if (clientMail.length() > 0 && clientMail != null) {
                    switch (i) {
                        case 0:
                            email2 = clientMail;
                            break;
                        case 1:
                            email3 = clientMail;
                            break;
                        default:
                            break;
                    }
                }
            }
            this.cs = new ClientSettings(emailOption, timezone, email1, email2, email3);
            StorageContainer.setClientSettings(this.cs);
            this.ringtone = this.sharedPreferences.getString("ringtone", null);
            this.enableNotification = Boolean.valueOf(this.sharedPreferences.getBoolean("enableNotification", true));
            this.cs.setEnableNotification(this.enableNotification);
            this.cs.setRingtone(this.ringtone);
            if (this.ringtone != null) {
                this.ringtoneSelect.setText(this.ringtone);
            } else {
                this.ringtoneSelect.setText("Moocall Moo");
            }
            receiveNotification();
            enableNotification();
            this.emailList.add(email1);
            if (email2 != null) {
                this.emailList.add(email2);
            }
            if (email3 != null) {
                this.emailList.add(email3);
            }
            if (this.emailList.size() == 3) {
                this.emailListView.removeFooterView(this.addAnotherEmailFooter);
            }
            this.emailListAdapter.setCs(this.cs);
            this.emailListAdapter.notifyDataSetChanged();
            weightUnitShow();
            showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void weightUnitShow() {
        Boolean kgs = Boolean.valueOf(this.sharedPreferences.getBoolean("kgs", true));
        this.kgsImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.grey_radio));
        this.lbsImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.grey_radio));
        if (kgs.booleanValue()) {
            this.kgsImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.dark_green_radio));
        } else {
            this.lbsImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.dark_green_radio));
        }
    }

    private void receiveNotification() {
        try {
            if (this.cs.getEmailOption().equals(Integer.valueOf(1))) {
                this.receiveEmailNotif.setImageResource(C0530R.drawable.settings_chechbox_checked);
            } else {
                this.receiveEmailNotif.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableNotification() {
        try {
            if (this.cs.getEnableNotification().booleanValue()) {
                this.enableAppNotification.setImageResource(C0530R.drawable.settings_chechbox_checked);
            } else {
                this.enableAppNotification.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save(MenuItem view) {
        if (this.cs == null || !this.cs.getChanged().equals(Boolean.valueOf(true))) {
            if (!this.notificationChanged.booleanValue()) {
                Toast.makeText(this, getString(C0530R.string.no_changes_to_be_saved), 1).show();
            }
            finish();
        } else {
            saveClientSettings();
        }
        return true;
    }

    public void onSetClientSettingsCompleted(String result) {
        try {
            showProgress(false);
            Toast.makeText(this, result, 1).show();
            this.cs.setChanged(Boolean.valueOf(false));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeNotificationSettings() {
        this.sharedPreferences.edit().putBoolean("enableNotification", this.enableNotification.booleanValue()).apply();
        this.sharedPreferences.edit().putString("ringtone", this.ringtone).apply();
    }

    public void saveClientSettings() {
        try {
            if (validEmail()) {
                showProgress(true);
                String timezoneUrl = URLEncoder.encode(this.cs.getTimezone().replaceAll("/", "@"), "UTF-8");
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SET_CLIENT_SETTINGS));
                new AcquireResponseTask(this).execute(new String[]{new SetClientSettingsUrl(timezoneUrl, this.cs.getEmailOption().toString(), this.cs.getEmail1(), this.cs.getEmail2(), this.cs.getEmail3()).createAndReturnUrl(this), QuickstartPreferences.SET_CLIENT_SETTINGS});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if (this.cs == null || !(this.cs.getChanged().equals(Boolean.valueOf(true)) || this.cs.getNotificationChanged().equals(Boolean.valueOf(true)))) {
            finish();
            return;
        }
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.save_settings_text)).setTitle(getString(C0530R.string.save_settings));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SettingsActivity.this.saveClientSettings();
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(C0530R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (SettingsActivity.this.cs.getNotificationChanged().equals(Boolean.valueOf(true))) {
                    SettingsActivity.this.removeNotificationSettings();
                }
                dialog.cancel();
                SettingsActivity.this.finish();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    private boolean validEmail() {
        View email1View = this.emailListView.getChildAt(1);
        View email2View = this.emailListView.getChildAt(2);
        View email3View = this.emailListView.getChildAt(3);
        View email1TextView = (EditText) email1View.findViewById(C0530R.id.notificationEmail);
        View email2TextView = null;
        View email3TextView = null;
        if (email2View != null) {
            email2TextView = (EditText) email2View.findViewById(C0530R.id.notificationEmail);
        }
        if (email3View != null) {
            email3TextView = (EditText) email3View.findViewById(C0530R.id.notificationEmail);
        }
        String email1 = email1TextView.getText().toString();
        String email2 = null;
        String email3 = null;
        if (email2TextView != null) {
            email2 = email2TextView.getText().toString();
        }
        if (email3TextView != null) {
            email3 = email3TextView.getText().toString();
        }
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email1)) {
            email1TextView.setError(getString(C0530R.string.error_field_required));
            focusView = email1TextView;
            cancel = true;
        } else if (!isEmailValid(email1)) {
            email1TextView.setError(getString(C0530R.string.error_invalid_email));
            focusView = email1TextView;
            cancel = true;
        }
        if (!(email2 == null || email2.equals("") || isEmailValid(email2))) {
            email2TextView.setError(getString(C0530R.string.error_invalid_email));
            focusView = email2TextView;
            cancel = true;
        }
        if (!(email3 == null || email2.equals("") || isEmailValid(email3))) {
            email3TextView.setError(getString(C0530R.string.error_invalid_email));
            focusView = email3TextView;
            cancel = true;
        }
        if (!cancel) {
            return true;
        }
        focusView.requestFocus();
        return false;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        this.mTracker.setScreenName("Settings Activity");
        this.mTracker.send(new ScreenViewBuilder().build());
    }
}
