package com.moocall.moocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.moocall.moocall.adapter.DeviceListAdapter;
import com.moocall.moocall.adapter.TabsPagerAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Device;
import com.moocall.moocall.domain.DeviceMessages;
import com.moocall.moocall.domain.Notification;
import com.moocall.moocall.domain.Phone;
import com.moocall.moocall.fragment.DevicesFragment;
import com.moocall.moocall.fragment.NotificationsFragment;
import com.moocall.moocall.fragment.PhonesFragment;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.FetchAllDevicesUrl;
import com.moocall.moocall.url.FetchCalvingItemsUrl;
import com.moocall.moocall.url.FetchPhoneItemsUrl;
import com.moocall.moocall.util.ActionBarUtils;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyMoocallActivity extends MainActivity implements TabListener {
    private static List<Device> deviceList;
    private ImageView arrowFilterList;
    private BroadcastReceiver broadcastReceiver;
    private Boolean broadcastRegistred = Boolean.valueOf(false);
    private Boolean contentMoved = Boolean.valueOf(false);
    private Boolean data;
    private TextView deviceBattery;
    private ImageView deviceBatteryIcon;
    private TextView deviceCode;
    private RelativeLayout deviceFilter;
    private LinearLayout deviceFilterRow;
    private LinearLayout deviceFilterText;
    private TextView deviceLastTime;
    private DeviceListAdapter deviceListAdapter;
    private RelativeLayout deviceListLayout;
    private ListView deviceListView;
    private TextView deviceName;
    private TextView filterText;
    private ImageView firmwareIcon;
    private TextView firmwareVersion;
    private Boolean loadMore;
    private TabsPagerAdapter mTabAdapter;
    private Boolean noMoreToLoad;
    private Boolean none = Boolean.valueOf(false);
    private Boolean notification = Boolean.valueOf(false);
    private List<Notification> notificationList;
    private Boolean onlyOne = Boolean.valueOf(false);
    private List<Phone> phoneList;
    private JSONArray phoneListJson;
    private View progressView;
    private BroadcastReceiver refreshListBroadcastReceiver;
    private Device selectedDevice = null;
    private RelativeLayout shadowUnder;
    private PagerSlidingTabStrip tabs;
    private Toolbar toolbar;
    private ViewPager viewPager;

    class C04732 implements OnItemClickListener {
        C04732() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (MyMoocallActivity.this.selectedDevice == null) {
                MyMoocallActivity.this.selectedDevice = (Device) MyMoocallActivity.deviceList.get(position - 1);
                MyMoocallActivity.this.selectedDevice();
            } else if (!MyMoocallActivity.this.selectedDevice.getId().equals(((Device) MyMoocallActivity.deviceList.get(position - 1)).getId())) {
                MyMoocallActivity.this.selectedDevice = (Device) MyMoocallActivity.deviceList.get(position - 1);
                MyMoocallActivity.this.selectedDevice();
            }
            MyMoocallActivity.this.hideDeviceList();
        }
    }

    class C04743 implements OnClickListener {
        C04743() {
        }

        public void onClick(View view) {
            if (MyMoocallActivity.this.deviceListLayout.getVisibility() == 0) {
                MyMoocallActivity.this.hideDeviceList();
            } else {
                MyMoocallActivity.this.showDeviceList();
            }
        }
    }

    class C04754 extends BroadcastReceiver {
        C04754() {
        }

        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("refresh_phones")) {
                MyMoocallActivity.this.fetchPhones();
            }
            if (action.equals("refresh_devices")) {
                MyMoocallActivity.this.fetchDevices();
            }
            if (action.equals("refresh_notifications")) {
                MyMoocallActivity.this.fetchNotification();
            }
            if (action.equals("device_selected")) {
                MyMoocallActivity.this.selectedDevice();
                MyMoocallActivity.this.hideDeviceList();
            }
        }
    }

    class C04765 extends BroadcastReceiver {
        C04765() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                MyMoocallActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.FETCH_DEVICES)) {
                    MyMoocallActivity.this.populateDeviceList(new JSONArray(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.FETCH_CALVING_ITEMS)) {
                    MyMoocallActivity.this.onFetchCalvingItemsCompleted(new JSONArray(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.FETCH_PHONES)) {
                    MyMoocallActivity.this.onFetchPhoneItemsCompleted(new JSONArray(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C10981 implements OnPageChangeListener {
        C10981() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            MyMoocallActivity.this.hideDeviceList();
        }

        public void onPageSelected(int position) {
            int lastFirstVisibleItem = 0;
            switch (position) {
                case 0:
                    lastFirstVisibleItem = ((DevicesFragment) MyMoocallActivity.this.mTabAdapter.getItem(position)).getLastFirstVisibleItem();
                    break;
                case 1:
                    lastFirstVisibleItem = ((NotificationsFragment) MyMoocallActivity.this.mTabAdapter.getItem(position)).getLastFirstVisibleItem();
                    break;
                case 2:
                    lastFirstVisibleItem = ((PhonesFragment) MyMoocallActivity.this.mTabAdapter.getItem(position)).getLastFirstVisibleItem();
                    break;
            }
            if (lastFirstVisibleItem == 0) {
                MyMoocallActivity.this.restoreContent(position);
            } else {
                MyMoocallActivity.this.moveContent(position);
            }
            if (position == 0) {
                MyMoocallActivity.this.hideDeviceSelector(Boolean.valueOf(true));
            } else {
                MyMoocallActivity.this.hideDeviceSelector(Boolean.valueOf(false));
            }
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public static List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        deviceList = deviceList;
    }

    public List<Notification> getNotificationList() {
        return this.notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public Boolean getLoadMore() {
        return this.loadMore;
    }

    public void setLoadMore(Boolean loadMore) {
        this.loadMore = loadMore;
    }

    public Boolean getNoMoreToLoad() {
        return this.noMoreToLoad;
    }

    public void setNoMoreToLoad(Boolean noMoreToLoad) {
        this.noMoreToLoad = noMoreToLoad;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return this.broadcastReceiver;
    }

    public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }

    public Device getSelectedDevice() {
        return this.selectedDevice;
    }

    public void setSelectedDevice(Device selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public List<Phone> getPhoneList() {
        return this.phoneList;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public Boolean getData() {
        return this.data;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.notification = (Boolean) getIntent().getSerializableExtra("notification");
        onResume();
        createRefreshListBroadcastReceiver();
        createAsyncBroadcast();
        setupDrawer();
        setupTabs();
        setupLayout();
        enableListeners();
        setData(Boolean.valueOf(true));
        fetchDevices();
    }

    private void setupDrawer() {
        getLayoutInflater().inflate(C0530R.layout.activity_my_moocall, this.frameLayout);
        this.drawerList.setItemChecked(position, true);
        this.homePage.setVisibility(8);
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
    }

    private void setupTabs() {
        this.viewPager = (ViewPager) findViewById(C0530R.id.pager);
        this.mTabAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);
        this.viewPager.setAdapter(this.mTabAdapter);
        this.tabs = (PagerSlidingTabStrip) findViewById(C0530R.id.tabs);
        this.tabs.setViewPager(this.viewPager);
    }

    private void setupLayout() {
        this.deviceListView = (ListView) findViewById(C0530R.id.devicesListView);
        this.deviceListView.addHeaderView((RelativeLayout) getLayoutInflater().inflate(C0530R.layout.device_list_header, null));
        deviceList = new ArrayList();
        this.loadMore = Boolean.valueOf(false);
        this.noMoreToLoad = Boolean.valueOf(false);
        this.notificationList = new ArrayList();
        this.phoneList = new ArrayList();
        this.deviceListAdapter = new DeviceListAdapter(this, deviceList);
        this.deviceListView.setAdapter(this.deviceListAdapter);
        this.deviceListView.setClickable(true);
        this.deviceFilter = (RelativeLayout) findViewById(C0530R.id.deviceFilter);
        this.deviceFilterRow = (LinearLayout) findViewById(C0530R.id.device_filter_row);
        this.deviceFilterText = (LinearLayout) findViewById(C0530R.id.device_filter_text);
        this.filterText = (TextView) findViewById(C0530R.id.filterText);
        this.arrowFilterList = (ImageView) findViewById(C0530R.id.arrowFilterList);
        this.deviceName = (TextView) findViewById(C0530R.id.deviceName);
        this.deviceLastTime = (TextView) findViewById(C0530R.id.deviceLastTime);
        this.deviceCode = (TextView) findViewById(C0530R.id.deviceCode);
        this.deviceBatteryIcon = (ImageView) findViewById(C0530R.id.deviceBatteryIcon);
        this.deviceBattery = (TextView) findViewById(C0530R.id.deviceBattery);
        this.firmwareIcon = (ImageView) findViewById(C0530R.id.firmwareIcon);
        this.firmwareVersion = (TextView) findViewById(C0530R.id.firmwareVersion);
        this.deviceListLayout = (RelativeLayout) findViewById(C0530R.id.deviceList);
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.shadowUnder = (RelativeLayout) findViewById(C0530R.id.shadowUnder);
    }

    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        ActionBarUtils.setHasEmbeddedTabs(getSupportActionBar(), false);
    }

    private void enableListeners() {
        this.tabs.setOnPageChangeListener(new C10981());
        this.deviceListView.setOnItemClickListener(new C04732());
        this.deviceFilter.setOnClickListener(new C04743());
    }

    public void onAllDevicesClicked(View view) {
        if (this.selectedDevice != null) {
            this.selectedDevice = null;
            selectedDevice();
        }
        hideDeviceList();
    }

    public void onDeviceRestoreClicked(View view) {
        View parentRow = (View) view.getParent().getParent();
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://r.moocall.com/d/" + ((Device) deviceList.get(((ListView) parentRow.getParent().getParent()).getPositionForView(parentRow))).getCode())));
    }

    public void createRefreshListBroadcastReceiver() {
        this.refreshListBroadcastReceiver = new C04754();
        registerReceiver();
    }

    public void registerReceiver() {
        registerReceiver(this.refreshListBroadcastReceiver, new IntentFilter("refresh_phones"));
        registerReceiver(this.refreshListBroadcastReceiver, new IntentFilter("refresh_devices"));
        registerReceiver(this.refreshListBroadcastReceiver, new IntentFilter("refresh_notifications"));
        registerReceiver(this.refreshListBroadcastReceiver, new IntentFilter("device_selected"));
        this.broadcastRegistred = Boolean.valueOf(true);
    }

    public void unregisterReceiver() {
        if (this.broadcastRegistred.booleanValue()) {
            try {
                unregisterReceiver(this.refreshListBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDeviceList() {
        this.deviceListLayout.setVisibility(0);
        this.deviceListLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_slide_in_bottom));
        this.deviceFilterText.setVisibility(0);
        this.deviceFilterRow.setVisibility(4);
        this.filterText.setText(getResources().getString(C0530R.string.select_device));
        this.arrowFilterList.setImageResource(C0530R.drawable.arrow_drop_up);
    }

    private void hideDeviceList() {
        Animation slideOut = AnimationUtils.loadAnimation(this, C0530R.anim.abc_slide_out_bottom);
        if (this.deviceListLayout.getVisibility() == 0) {
            this.deviceListLayout.startAnimation(slideOut);
        }
        this.deviceListLayout.setVisibility(4);
        if (this.selectedDevice != null) {
            this.deviceFilterText.setVisibility(4);
            this.deviceFilterRow.setVisibility(0);
            this.deviceName.setText(this.selectedDevice.getName());
            this.deviceCode.setText(this.selectedDevice.getCode());
            this.deviceBattery.setText(this.selectedDevice.getBattery().toString() + "%");
            this.deviceLastTime.setText(this.selectedDevice.getLastTime());
            this.firmwareVersion.setText(this.selectedDevice.getFwversion());
            this.deviceBatteryIcon.setImageResource(C0530R.drawable.battery_icon_gray);
            if (this.selectedDevice.getBattery().intValue() > 15) {
                this.deviceBattery.setTextColor(getResources().getColor(C0530R.color.darker_gray));
            } else {
                this.deviceBattery.setTextColor(getResources().getColor(C0530R.color.orange_mh));
            }
            if (this.selectedDevice.getFwstatus().equals(Integer.valueOf(1))) {
                this.firmwareIcon.setImageResource(C0530R.drawable.firmware_update);
            } else {
                this.firmwareIcon.setImageResource(C0530R.drawable.firmware_ok);
            }
            if (this.onlyOne.booleanValue()) {
                this.arrowFilterList.setImageDrawable(null);
                this.deviceFilter.setOnClickListener(null);
                return;
            }
            this.arrowFilterList.setImageResource(C0530R.drawable.arrow_drop_down);
            return;
        }
        this.deviceFilterText.setVisibility(0);
        this.deviceFilterRow.setVisibility(4);
        if (this.none.booleanValue()) {
            this.filterText.setText(getResources().getString(C0530R.string.no_devices));
            this.arrowFilterList.setImageDrawable(null);
            this.deviceFilter.setOnClickListener(null);
            return;
        }
        this.filterText.setText(getResources().getString(C0530R.string.all_devices));
        this.arrowFilterList.setImageResource(C0530R.drawable.arrow_drop_down);
    }

    private void selectedDevice() {
        setData(Boolean.valueOf(true));
        fetchNotification();
    }

    private void refreshLists() {
        Fragment deviceFragment = this.mTabAdapter.getItem(0);
        Fragment notificationFragment = this.mTabAdapter.getItem(1);
        Fragment phonesFragment = this.mTabAdapter.getItem(2);
        ((DevicesFragment) deviceFragment).isAlive(this, findViewById(C0530R.id.deviceListLayout));
        ((NotificationsFragment) notificationFragment).isAlive(this, findViewById(C0530R.id.notificationListLayout));
        ((PhonesFragment) phonesFragment).isAlive(this, findViewById(C0530R.id.phonesListLayout));
        if (this.notification != null && this.notification.booleanValue()) {
            this.notification = Boolean.valueOf(false);
            this.viewPager.setCurrentItem(1);
        }
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    protected void onResume() {
        super.onResume();
        waitForUser(false);
        StorageContainer.wakeApp(this);
        this.mTracker.setScreenName("My Moocall Activity");
        this.mTracker.send(new ScreenViewBuilder().build());
    }

    private void fetchDevices() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_DEVICES));
        new AcquireResponseTask(this).execute(new String[]{new FetchAllDevicesUrl().createAndReturnUrl(this), QuickstartPreferences.FETCH_DEVICES});
    }

    private void fetchNotification() {
        try {
            String p1 = "0";
            String p2 = "0";
            String p3 = "0";
            String deviceId = "0";
            if (this.selectedDevice != null) {
                deviceId = this.selectedDevice.getId().toString();
            }
            if (this.loadMore.booleanValue() && this.notificationList.size() > 0) {
                Notification notification = (Notification) this.notificationList.get(this.notificationList.size() - 1);
                p1 = URLEncoder.encode(notification.getId(), "UTF-8");
                p2 = URLEncoder.encode(notification.getIdMessageTypeCalving().toString(), "UTF-8");
                p3 = URLEncoder.encode(notification.getCalvingTimer(), "UTF-8");
            }
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_CALVING_ITEMS));
            new AcquireResponseTask(this).execute(new String[]{new FetchCalvingItemsUrl(p1, p2, p3, deviceId).createAndReturnUrl(this), QuickstartPreferences.FETCH_CALVING_ITEMS});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchPhones() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_PHONES));
        new AcquireResponseTask(this).execute(new String[]{new FetchPhoneItemsUrl().createAndReturnUrl(this), QuickstartPreferences.FETCH_PHONES});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04765();
    }

    public void onFetchCalvingItemsCompleted(JSONArray jsonArray) {
        if (!this.loadMore.booleanValue()) {
            this.notificationList.clear();
        }
        if (jsonArray.length() > 0) {
            populateNotificationList(jsonArray);
            return;
        }
        this.noMoreToLoad = Boolean.valueOf(true);
        if (!getData().booleanValue()) {
            refreshLists();
        } else if (this.phoneListJson != null) {
            populatePhoneList();
        } else {
            fetchPhones();
        }
    }

    public void onFetchPhoneItemsCompleted(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            this.phoneListJson = jsonArray;
            populatePhoneList();
            return;
        }
        showProgress(false);
        refreshLists();
    }

    public void populatePhoneList() {
        try {
            this.phoneList.clear();
            for (int i = 0; i < this.phoneListJson.length(); i++) {
                int j;
                JSONObject phoneObject = (JSONObject) this.phoneListJson.get(i);
                JSONParserBgw jsonParserPhone = new JSONParserBgw(phoneObject);
                Integer idPhone = jsonParserPhone.getInt("id");
                Integer idClient = jsonParserPhone.getInt("client_id");
                String namePhone = jsonParserPhone.getString("name");
                String phoneNumber = jsonParserPhone.getString("phone");
                List<Integer> deviceIds = new ArrayList();
                JSONArray device = phoneObject.getJSONArray("device");
                for (j = 0; j < device.length(); j++) {
                    deviceIds.add(Integer.valueOf(device.getInt(j)));
                }
                Phone p = new Phone(idPhone, idClient, namePhone, phoneNumber, deviceIds);
                Boolean assign = Boolean.valueOf(false);
                if (!(p.getDeviceIds() == null || this.selectedDevice == null)) {
                    for (j = 0; j < p.getDeviceIds().size(); j++) {
                        if (this.selectedDevice.getId().equals(Integer.valueOf(((Integer) p.getDeviceIds().get(j)).intValue()))) {
                            assign = Boolean.valueOf(true);
                        }
                    }
                }
                if (assign.booleanValue()) {
                    this.phoneList.add(0, p);
                } else {
                    this.phoneList.add(p);
                }
            }
            setPhoneList(this.phoneList);
            if (getData().booleanValue()) {
                refreshLists();
            } else {
                fetchDevices();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateNotificationList(JSONArray result) {
        int i = 0;
        while (i < result.length()) {
            try {
                this.notificationList.add(new Notification(new JSONParserBgw((JSONObject) result.get(i))));
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        setNotificationList(this.notificationList);
        if (!getData().booleanValue()) {
            refreshLists();
        } else if (this.phoneListJson != null) {
            populatePhoneList();
        } else {
            fetchPhones();
        }
        if (this.notificationList.size() < 9) {
            this.noMoreToLoad = Boolean.valueOf(true);
        }
    }

    private void populateDeviceList(JSONArray result) {
        try {
            deviceList.clear();
            for (int i = 0; i < result.length(); i++) {
                JSONObject deviceObject = (JSONObject) result.get(i);
                JSONParserBgw jSONParserBgw = new JSONParserBgw(deviceObject);
                Integer id = jSONParserBgw.getInt("id");
                Integer sensitivity = jSONParserBgw.getInt("sensitivity");
                Integer battery = jSONParserBgw.getInt("battery");
                String name = jSONParserBgw.getString("name");
                Integer charging = jSONParserBgw.getInt("charging");
                Integer gsm = jSONParserBgw.getInt("gsm");
                String code = jSONParserBgw.getString("code");
                String lastBeat = jSONParserBgw.getString("last_beat");
                String lastTime = getLastTime(lastBeat);
                String fwversion = jSONParserBgw.getString("fwversion");
                Integer fwstatus = jSONParserBgw.getInt("fwstatus");
                String renewal = Utils.calculateTime(jSONParserBgw.getString("renewal"), "yyyy-MM-dd");
                String warranty = Utils.calculateTime(jSONParserBgw.getString("warranty"), "yyyy-MM-dd");
                Boolean inactive = checkInactive(lastBeat);
                List<Phone> phones = new ArrayList();
                JSONArray phonesList = deviceObject.getJSONArray("phones");
                for (int j = 0; j < phonesList.length(); j++) {
                    jSONParserBgw = new JSONParserBgw((JSONObject) phonesList.get(j));
                    phones.add(new Phone(jSONParserBgw.getInt("id"), jSONParserBgw.getInt("id_client"), jSONParserBgw.getString("name"), jSONParserBgw.getString("phone_number"), new ArrayList()));
                }
                jSONParserBgw = new JSONParserBgw(deviceObject.getJSONObject("messages"));
                deviceList.add(new Device(id, sensitivity, battery, name, charging, gsm, code, lastBeat, lastTime, phones, fwversion, fwstatus, new DeviceMessages(jSONParserBgw.getBoolean("renewal_exp"), jSONParserBgw.getBoolean("warranty_exp"), jSONParserBgw.getBoolean("turned_off"), jSONParserBgw.getBoolean("blocked"), jSONParserBgw.getBoolean("warranty_past"), jSONParserBgw.getBoolean("renewal_past"), jSONParserBgw.getBoolean("connected"), jSONParserBgw.getBoolean("noproblem")), renewal, warranty, inactive));
            }
            this.deviceListAdapter.notifyDataSetChanged();
            setDeviceList(deviceList);
            if (deviceList.size() == 0) {
                this.none = Boolean.valueOf(true);
                this.deviceListView.removeHeaderView((RelativeLayout) getLayoutInflater().inflate(C0530R.layout.device_list_header, null));
            } else if (deviceList.size() == 1) {
                this.selectedDevice = (Device) deviceList.get(0);
                this.onlyOne = Boolean.valueOf(true);
            }
            hideDeviceList();
            if (getData().booleanValue()) {
                fetchNotification();
            } else {
                refreshLists();
            }
            if (this.viewPager.getCurrentItem() == 0) {
                hideDeviceSelector(Boolean.valueOf(true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if (this.deviceListLayout.getVisibility() == 0) {
            hideDeviceList();
        } else if (this.homePage.getVisibility() == 0) {
            this.homePage.setVisibility(8);
        } else {
            unregisterReceiver();
            finish();
        }
    }

    private String getLastTime(String lastBeat) {
        String lastTime = "";
        if (lastBeat.equals("0000-00-00 00:00:00")) {
            return "N/A";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentTime = new Date();
        Date lastBeatTime = new Date();
        try {
            lastBeatTime = format.parse(Utils.calculateTime(lastBeat, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (((currentTime.getTime() - lastBeatTime.getTime()) / 1000) / 86400 > 0) {
            return new SimpleDateFormat("dd MMM").format(Long.valueOf(lastBeatTime.getTime()));
        }
        return new SimpleDateFormat("HH:mm").format(Long.valueOf(lastBeatTime.getTime()));
    }

    private Boolean checkInactive(String lastBeat) {
        if (lastBeat.equals("0000-00-00 00:00:00")) {
            return Boolean.valueOf(true);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentTime = new Date();
        Date lastBeatTime = new Date();
        try {
            lastBeatTime = format.parse(Utils.calculateTime(lastBeat, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (((currentTime.getTime() - lastBeatTime.getTime()) / 1000) / 36000 > 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void moveContent(int position) {
        if (this.viewPager.getCurrentItem() == position) {
            this.toolbar.animate().translationY((float) (-this.toolbar.getBottom())).setInterpolator(new AccelerateInterpolator()).start();
            this.tabs.animate().translationY((float) (-this.toolbar.getBottom())).setInterpolator(new AccelerateInterpolator()).start();
            this.contentMoved = Boolean.valueOf(true);
            this.deviceFilter.animate().translationY((float) (-this.toolbar.getBottom())).setInterpolator(new AccelerateInterpolator()).start();
            this.shadowUnder.animate().translationY((float) (-this.toolbar.getBottom())).setInterpolator(new AccelerateInterpolator()).start();
        }
    }

    public void restoreContent(int position) {
        if (this.viewPager.getCurrentItem() == position) {
            this.toolbar.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
            this.tabs.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
            this.contentMoved = Boolean.valueOf(false);
            this.deviceFilter.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
            this.shadowUnder.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    private void hideDeviceSelector(Boolean hide) {
        if (hide.booleanValue()) {
            this.deviceFilter.animate().translationY((float) ((-this.toolbar.getBottom()) + (-this.tabs.getBottom()))).setInterpolator(new AccelerateInterpolator()).start();
            this.shadowUnder.animate().translationY((float) ((-this.toolbar.getBottom()) + (-this.tabs.getBottom()))).setInterpolator(new AccelerateInterpolator()).start();
        } else if (!this.contentMoved.booleanValue()) {
            this.deviceFilter.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
            this.shadowUnder.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    public void onStop() {
        super.onStop();
        if (Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("signout", false)).booleanValue()) {
            unregisterReceiver();
        }
    }
}
