package com.moocall.moocall;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Phone;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddNewPhoneNumberUrl;
import com.moocall.moocall.url.UpdatePhoneNumberUrl;
import com.moocall.moocall.util.StorageContainer;
import java.net.URLEncoder;

public class EditPhoneActivity extends ActionBarActivity {
    private ActionBar actionBar;
    private BroadcastReceiver broadcastReceiver;
    private Intent intent;
    private Phone phone;
    private TextView phoneNameEdit;
    private TextView phoneNumberEdit;
    private String problem;
    private View progressView;
    private Toolbar toolbar;

    class C04381 implements OnClickListener {
        C04381() {
        }

        public void onClick(View v) {
            EditPhoneActivity.this.onBackPressed();
        }
    }

    class C04393 extends BroadcastReceiver {
        C04393() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                EditPhoneActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.UPDATE_PHONE_NUMBER)) {
                    EditPhoneActivity.this.onUpdatePhoneNumberCompleted(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.ADD_NEW_PHONE)) {
                    EditPhoneActivity.this.onAddNewPhoneNumberCompleted(intent.getStringExtra("response"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C04404 implements DialogInterface.OnClickListener {
        C04404() {
        }

        public void onClick(DialogInterface dialog, int id) {
            if (EditPhoneActivity.this.checkForSave().booleanValue()) {
                EditPhoneActivity.this.saveChanges();
                dialog.cancel();
                EditPhoneActivity.this.finish();
                return;
            }
            dialog.cancel();
        }
    }

    class C04415 implements DialogInterface.OnClickListener {
        C04415() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            EditPhoneActivity.this.finish();
        }
    }

    class C10952 implements OnMenuItemClickListener {
        C10952() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    public String getProblem() {
        return this.problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_edit_phone);
        onResume();
        createAsyncBroadcast();
        this.intent = getIntent();
        if (((String) this.intent.getSerializableExtra("type")).equals("edit")) {
            this.phone = (Phone) this.intent.getSerializableExtra("Phone");
        } else {
            this.phone = null;
        }
        setupToolbar();
        setupLayout();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        if (this.phone != null) {
            this.toolbar.setTitle(getString(C0530R.string.edit));
        } else {
            this.toolbar.setTitle(getString(C0530R.string.new_text));
        }
        this.toolbar.setNavigationOnClickListener(new C04381());
        this.toolbar.inflateMenu(C0530R.menu.only_save);
        this.toolbar.setOnMenuItemClickListener(new C10952());
    }

    private void setupLayout() {
        this.phoneNameEdit = (TextView) findViewById(C0530R.id.phoneNameEdit);
        this.phoneNumberEdit = (TextView) findViewById(C0530R.id.phoneNumberEdit);
        this.progressView = findViewById(C0530R.id.progress_disable);
        if (this.phone != null) {
            this.phoneNameEdit.setText(this.phone.getName());
            this.phoneNumberEdit.setText(this.phone.getPhoneNumber());
        }
    }

    public boolean save(MenuItem view) {
        if (checkForSave().booleanValue()) {
            saveChanges();
        }
        return true;
    }

    private Boolean checkForSave() {
        this.phoneNameEdit.setError(null);
        this.phoneNumberEdit.setError(null);
        String name = this.phoneNameEdit.getText().toString();
        String number = this.phoneNumberEdit.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(number)) {
            this.phoneNumberEdit.setError(getString(C0530R.string.error_field_required));
            focusView = this.phoneNumberEdit;
            cancel = true;
        } else if (!isNumberValid(number).booleanValue()) {
            this.phoneNumberEdit.setError(getString(C0530R.string.error_invalid_number));
            focusView = this.phoneNumberEdit;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            this.phoneNameEdit.setError(getString(C0530R.string.error_field_required));
            focusView = this.phoneNameEdit;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return Boolean.valueOf(false);
        } else if (this.phone == null || !this.phone.getName().equals(name) || !this.phone.getPhoneNumber().equals(number)) {
            return Boolean.valueOf(true);
        } else {
            Toast.makeText(this, getString(C0530R.string.no_changes_to_be_saved), 1).show();
            finish();
            return Boolean.valueOf(false);
        }
    }

    private Boolean checkForBack() {
        String name = this.phoneNameEdit.getText().toString();
        String number = this.phoneNumberEdit.getText().toString();
        if (this.phone != null) {
            if (this.phone.getName().equals(name) && this.phone.getPhoneNumber().equals(number)) {
                return Boolean.valueOf(false);
            }
        } else if (TextUtils.isEmpty(number) && TextUtils.isEmpty(name)) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    private Boolean isNumberValid(String number) {
        if (number.length() <= 5 || number.length() >= 15) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(number.matches("^\\+[0-9]+"));
    }

    private void saveChanges() {
        try {
            showProgress(true);
            String name = this.phoneNameEdit.getText().toString();
            String number = this.phoneNumberEdit.getText().toString();
            name = URLEncoder.encode(name, "UTF-8");
            number = URLEncoder.encode(number, "UTF-8");
            if (this.phone != null) {
                this.phone.setName(this.phoneNameEdit.getText().toString());
                this.phone.setPhoneNumber(this.phoneNumberEdit.getText().toString());
                String deviceIds = "";
                for (int i = 0; i < this.phone.getDeviceIds().size(); i++) {
                    deviceIds = deviceIds + Integer.toString(((Integer) this.phone.getDeviceIds().get(i)).intValue());
                    if (i + 1 < this.phone.getDeviceIds().size()) {
                        deviceIds = deviceIds + ",";
                    }
                }
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.UPDATE_PHONE_NUMBER));
                new AcquireResponseTask(this).execute(new String[]{new UpdatePhoneNumberUrl(this.phone.getId().toString(), number, name, deviceIds).createAndReturnUrl(this), QuickstartPreferences.UPDATE_PHONE_NUMBER});
                return;
            }
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_NEW_PHONE));
            new AcquireResponseTask(this).execute(new String[]{new AddNewPhoneNumberUrl(number, name, "").createAndReturnUrl(this), QuickstartPreferences.ADD_NEW_PHONE});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04393();
    }

    public void onUpdatePhoneNumberCompleted(String result) {
        try {
            showProgress(false);
            Toast.makeText(this, result, 1).show();
            Intent intent = new Intent("refresh_phones");
            Intent intent2 = new Intent("update_phone_data");
            intent2.putExtra("Phone", this.phone);
            sendBroadcast(intent);
            sendBroadcast(intent2);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddNewPhoneNumberCompleted(String result) {
        try {
            showProgress(false);
            Toast.makeText(this, result, 1).show();
            sendBroadcast(new Intent("refresh_phones"));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if (checkForBack().booleanValue()) {
            Builder alertDialogBuilder = new Builder(this);
            alertDialogBuilder.setMessage(getString(C0530R.string.save_settings_text)).setTitle(getString(C0530R.string.save_settings));
            alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new C04404());
            alertDialogBuilder.setNegativeButton(getString(C0530R.string.no), new C04415());
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.create().show();
            return;
        }
        finish();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    protected void onPause() {
        super.onPause();
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }
}
