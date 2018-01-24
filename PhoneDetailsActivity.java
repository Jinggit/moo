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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.moocall.moocall.adapter.DevicePhoneListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.Device;
import com.moocall.moocall.domain.Phone;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.DeletePhoneNumberUrl;
import com.moocall.moocall.url.UpdatePhoneNumberUrl;
import com.moocall.moocall.util.StorageContainer;
import java.net.URLEncoder;
import java.util.List;

public class PhoneDetailsActivity extends ActionBarActivity {
    private ActionBar actionBar;
    private Boolean assign;
    private BroadcastReceiver broadcastReceiver;
    private List<Device> deviceList;
    private DevicePhoneListAdapter devicePhoneListAdapter;
    private ListView devicePhoneListView;
    private Intent intent;
    private Phone phone;
    private TextView phoneNameDetails;
    private TextView phoneNumberDetails;
    private String problem;
    private View progressView;
    private boolean receiverRegistred = false;
    private Toolbar toolbar;
    private Device unassignDevice;
    private BroadcastReceiver updatePhoneDataBrodcastReceiver;

    class C05201 extends BroadcastReceiver {
        C05201() {
        }

        public void onReceive(Context arg0, Intent intent) {
            PhoneDetailsActivity.this.unregisterReceiver(this);
            PhoneDetailsActivity.this.receiverRegistred = false;
            if (intent.getAction().equals("update_phone_data")) {
                PhoneDetailsActivity.this.phone = (Phone) intent.getSerializableExtra("Phone");
                PhoneDetailsActivity.this.setPhoneData();
            }
        }
    }

    class C05212 implements OnClickListener {
        C05212() {
        }

        public void onClick(View v) {
            PhoneDetailsActivity.this.onBackPressed();
        }
    }

    class C05224 implements DialogInterface.OnClickListener {
        C05224() {
        }

        public void onClick(DialogInterface dialog, int id) {
            PhoneDetailsActivity.this.deleteThisPhone();
            dialog.cancel();
        }
    }

    class C05235 implements DialogInterface.OnClickListener {
        C05235() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C05246 extends BroadcastReceiver {
        C05246() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                PhoneDetailsActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.DELETE_PHONE)) {
                    PhoneDetailsActivity.this.onDeletePhoneCompleted(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.UPDATE_PHONE_NUMBER)) {
                    PhoneDetailsActivity.this.onUpdatePhoneNumberCompleted(intent.getStringExtra("response"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C11033 implements OnMenuItemClickListener {
        C11033() {
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
        setContentView((int) C0530R.layout.activity_phone_details);
        onResume();
        createAsyncBroadcast();
        this.intent = getIntent();
        this.phone = (Phone) this.intent.getSerializableExtra("Phone");
        setupToolbar();
        setupLayout();
        createBroadcastReceiver();
    }

    public void createBroadcastReceiver() {
        this.updatePhoneDataBrodcastReceiver = new C05201();
    }

    public void registerReceiver() {
        this.receiverRegistred = true;
        registerReceiver(this.updatePhoneDataBrodcastReceiver, new IntentFilter("update_phone_data"));
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C05212());
        this.toolbar.inflateMenu(C0530R.menu.edit_delete);
        this.toolbar.setOnMenuItemClickListener(new C11033());
    }

    private void setupLayout() {
        this.devicePhoneListView = (ListView) findViewById(C0530R.id.devicePhoneListView);
        LinearLayout headerLayout = (LinearLayout) getLayoutInflater().inflate(C0530R.layout.phone_details_header, null);
        headerLayout.setOnClickListener(null);
        this.devicePhoneListView.addHeaderView(headerLayout);
        this.phoneNameDetails = (TextView) findViewById(C0530R.id.phoneNameDetails);
        this.phoneNumberDetails = (TextView) findViewById(C0530R.id.phoneNumberDetails);
        this.deviceList = MyMoocallActivity.getDeviceList();
        this.devicePhoneListAdapter = new DevicePhoneListAdapter(this, this.deviceList, this);
        this.devicePhoneListView.setAdapter(this.devicePhoneListAdapter);
        this.progressView = findViewById(C0530R.id.progress_disable);
        setPhoneData();
    }

    private void setPhoneData() {
        if (this.phone != null) {
            this.toolbar.setTitle(this.phone.getName());
            this.phoneNameDetails.setText(this.phone.getName());
            this.phoneNumberDetails.setText(this.phone.getPhoneNumber());
            this.devicePhoneListAdapter.setPhone(this.phone);
        }
    }

    public boolean edit(MenuItem view) {
        registerReceiver();
        Intent intent = new Intent(this, EditPhoneActivity.class);
        intent.putExtra("type", "edit");
        intent.putExtra("Phone", this.phone);
        startActivity(intent);
        return true;
    }

    public boolean delete(MenuItem view) {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.delete_phone_text)).setTitle(getString(C0530R.string.delete_phone));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new C05224());
        alertDialogBuilder.setNegativeButton(getString(C0530R.string.no), new C05235());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
        return true;
    }

    private void deleteThisPhone() {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_PHONE));
        new AcquireResponseTask(this).execute(new String[]{new DeletePhoneNumberUrl(this.phone.getId().toString()).createAndReturnUrl(this), QuickstartPreferences.DELETE_PHONE});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C05246();
    }

    public void onDeletePhoneCompleted(String result) {
        try {
            showProgress(false);
            Toast.makeText(this, result, 1).show();
            sendBroadcast(new Intent("refresh_phones"));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    public void onBackPressed() {
        if (this.receiverRegistred) {
            unregisterReceiver(this.updatePhoneDataBrodcastReceiver);
            this.receiverRegistred = false;
        }
        finish();
    }

    public void onAssignButtonClick(View view) {
        View parentRow = (View) view.getParent();
        this.unassignDevice = (Device) this.deviceList.get(((ListView) parentRow.getParent()).getPositionForView(parentRow) - 1);
        this.assign = Boolean.valueOf(true);
        for (int j = 0; j < this.phone.getDeviceIds().size(); j++) {
            if (this.unassignDevice.getId().equals(Integer.valueOf(((Integer) this.phone.getDeviceIds().get(j)).intValue()))) {
                this.phone.getDeviceIds().remove(j);
                this.assign = Boolean.valueOf(false);
            }
        }
        if (this.assign.booleanValue()) {
            assingPhone();
        } else {
            updatePhone();
        }
    }

    private void assingPhone() {
        try {
            if (this.unassignDevice.getAssignedPhones().equals(Account.getMaxPhones())) {
                Toast.makeText(this, getString(C0530R.string.max_phones_problem), 1).show();
                return;
            }
            this.phone.getDeviceIds().add(this.unassignDevice.getId());
            updatePhone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePhone() {
        try {
            showProgress(true);
            String name = this.phone.getName();
            String phoneNumber = this.phone.getPhoneNumber();
            name = URLEncoder.encode(name, "UTF-8");
            phoneNumber = URLEncoder.encode(phoneNumber, "UTF-8");
            String deviceIds = "";
            for (int i = 0; i < this.phone.getDeviceIds().size(); i++) {
                deviceIds = deviceIds + Integer.toString(((Integer) this.phone.getDeviceIds().get(i)).intValue());
                if (i + 1 < this.phone.getDeviceIds().size()) {
                    deviceIds = deviceIds + ",";
                }
            }
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.UPDATE_PHONE_NUMBER));
            new AcquireResponseTask(this).execute(new String[]{new UpdatePhoneNumberUrl(this.phone.getId().toString(), phoneNumber, name, deviceIds).createAndReturnUrl(this), QuickstartPreferences.UPDATE_PHONE_NUMBER});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpdatePhoneNumberCompleted(String result) {
        try {
            showProgress(false);
            Toast.makeText(this, result, 1).show();
            sendBroadcast(new Intent("refresh_phones"));
            if (this.assign.booleanValue()) {
                this.unassignDevice.assignPhone();
            } else {
                this.unassignDevice.unassignPhone();
            }
            this.devicePhoneListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }
}
