package com.moocall.moocall.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.EditPhoneActivity;
import com.moocall.moocall.MyMoocallActivity;
import com.moocall.moocall.PhoneDetailsActivity;
import com.moocall.moocall.adapter.PhoneListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.Device;
import com.moocall.moocall.domain.Phone;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.UpdatePhoneNumberUrl;
import io.intercom.android.sdk.views.IntercomToolbar;
import java.net.URLEncoder;
import java.util.List;

public class PhonesFragment extends Fragment {
    private RelativeLayout addAnotherPhone;
    private Boolean assign;
    private BroadcastReceiver broadcastReceiver;
    private int lastFirstVisibleItem = 0;
    private MyMoocallActivity mActivity;
    private List<Phone> phoneList;
    private PhoneListAdapter phoneListAdapter;
    private ListView phoneListView;
    private SwipeRefreshLayout phoneSwipeRefreshLayout;

    class C06172 implements OnItemClickListener {
        C06172() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i > 0 && PhonesFragment.this.phoneList.size() > i - 1) {
                Phone phone = (Phone) PhonesFragment.this.phoneList.get(i - 1);
                Intent intent = new Intent(PhonesFragment.this.mActivity, PhoneDetailsActivity.class);
                intent.putExtra("Phone", phone);
                PhonesFragment.this.startActivity(intent);
            }
        }
    }

    class C06183 implements OnClickListener {
        C06183() {
        }

        public void onClick(View view) {
            Intent intent = new Intent(PhonesFragment.this.mActivity, EditPhoneActivity.class);
            intent.putExtra("type", "new");
            PhonesFragment.this.startActivity(intent);
        }
    }

    class C06194 implements OnScrollListener {
        C06194() {
        }

        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (PhonesFragment.this.lastFirstVisibleItem < firstVisibleItem) {
                PhonesFragment.this.mActivity.moveContent(2);
                PhonesFragment.this.addAnotherPhone.animate().translationY((float) (((LayoutParams) PhonesFragment.this.addAnotherPhone.getLayoutParams()).bottomMargin + PhonesFragment.this.addAnotherPhone.getHeight())).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
            if (PhonesFragment.this.lastFirstVisibleItem > firstVisibleItem) {
                PhonesFragment.this.mActivity.restoreContent(2);
                PhonesFragment.this.addAnotherPhone.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).start();
            }
            PhonesFragment.this.lastFirstVisibleItem = firstVisibleItem;
        }

        private void isScrollCompleted() {
        }
    }

    class C06205 extends BroadcastReceiver {
        C06205() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                PhonesFragment.this.mActivity.unregisterReceiver(this);
                if (intent.getAction().equals(QuickstartPreferences.UPDATE_PHONE_NUMBER)) {
                    PhonesFragment.this.onUpdatePhoneNumberCompleted(intent.getStringExtra("response"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C11121 implements OnRefreshListener {
        C11121() {
        }

        public void onRefresh() {
            if (PhonesFragment.this.phoneSwipeRefreshLayout.isRefreshing()) {
                PhonesFragment.this.mActivity.sendBroadcast(new Intent("refresh_phones"));
            }
        }
    }

    public int getLastFirstVisibleItem() {
        return this.lastFirstVisibleItem;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0530R.layout.phones_list, container, false);
        setupLayout(rootView);
        createAsyncBroadcast();
        implementListeners();
        return rootView;
    }

    public void isAlive(MyMoocallActivity activity, View view) {
        if (view == null) {
            return;
        }
        if (this.phoneListAdapter == null) {
            this.mActivity = activity;
            setupLayout(view);
            return;
        }
        this.phoneList = this.mActivity.getPhoneList();
        this.phoneListAdapter.setDevice(this.mActivity.getSelectedDevice());
        this.phoneListAdapter.notifyDataSetChanged();
        if (this.phoneSwipeRefreshLayout.isRefreshing()) {
            this.phoneSwipeRefreshLayout.setRefreshing(false);
        }
        this.phoneListView.setSelection(0);
    }

    private void setupLayout(View rootView) {
        this.phoneListView = (ListView) rootView.findViewById(C0530R.id.phoneListView);
        View headerLayout = this.mActivity.getLayoutInflater().inflate(C0530R.layout.home_page_header, null);
        headerLayout.setOnClickListener(null);
        RelativeLayout footerLayout = (RelativeLayout) this.mActivity.getLayoutInflater().inflate(C0530R.layout.phone_list_footer, null);
        footerLayout.setOnClickListener(null);
        this.phoneListView.addHeaderView(headerLayout);
        this.phoneListView.addFooterView(footerLayout);
        this.phoneSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(C0530R.id.phone_swipe_refresh_layout);
        this.phoneSwipeRefreshLayout.setProgressViewOffset(false, 0, IntercomToolbar.TITLE_FADE_DURATION_MS);
        this.phoneList = this.mActivity.getPhoneList();
        this.phoneListAdapter = new PhoneListAdapter(this.mActivity, this.phoneList, this);
        this.phoneListView.setAdapter(this.phoneListAdapter);
        this.phoneListAdapter.setDevice(this.mActivity.getSelectedDevice());
        this.addAnotherPhone = (RelativeLayout) rootView.findViewById(C0530R.id.addAnotherPhone);
    }

    public void implementListeners() {
        this.phoneSwipeRefreshLayout.setOnRefreshListener(new C11121());
        this.phoneListView.setOnItemClickListener(new C06172());
        this.addAnotherPhone.setOnClickListener(new C06183());
        this.phoneListView.setOnScrollListener(new C06194());
    }

    public void onAssignButtonClick(View view) {
        View parentRow = (View) view.getParent();
        int position = ((ListView) parentRow.getParent()).getPositionForView(parentRow);
        if (position > 0 && this.phoneList.size() > position - 1) {
            Phone phone = (Phone) this.phoneList.get(position - 1);
            Device device = this.phoneListAdapter.getDevice();
            this.assign = Boolean.valueOf(true);
            for (int j = 0; j < phone.getDeviceIds().size(); j++) {
                if (device.getId().equals(Integer.valueOf(((Integer) phone.getDeviceIds().get(j)).intValue()))) {
                    phone.getDeviceIds().remove(j);
                    this.assign = Boolean.valueOf(false);
                }
            }
            if (this.assign.booleanValue()) {
                assingPhone(phone, device);
            } else {
                updatePhone(phone);
            }
        }
    }

    private void assingPhone(Phone phone, Device device) {
        try {
            if (device.getAssignedPhones().equals(Account.getMaxPhones())) {
                Toast.makeText(this.mActivity.getApplicationContext(), getString(C0530R.string.max_phones_problem), 1).show();
                return;
            }
            phone.getDeviceIds().add(device.getId());
            updatePhone(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePhone(Phone phone) {
        try {
            this.mActivity.showProgress(true);
            String name = phone.getName();
            String phoneNumber = phone.getPhoneNumber();
            name = URLEncoder.encode(name, "UTF-8");
            phoneNumber = URLEncoder.encode(phoneNumber, "UTF-8");
            String deviceIds = "";
            for (int i = 0; i < phone.getDeviceIds().size(); i++) {
                deviceIds = deviceIds + Integer.toString(((Integer) phone.getDeviceIds().get(i)).intValue());
                if (i + 1 < phone.getDeviceIds().size()) {
                    deviceIds = deviceIds + ",";
                }
            }
            this.mActivity.registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.UPDATE_PHONE_NUMBER));
            new AcquireResponseTask(this.mActivity.getApplicationContext()).execute(new String[]{new UpdatePhoneNumberUrl(phone.getId().toString(), phoneNumber, name, deviceIds).createAndReturnUrl(this.mActivity), QuickstartPreferences.UPDATE_PHONE_NUMBER});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C06205();
    }

    public void onUpdatePhoneNumberCompleted(String result) {
        try {
            Toast.makeText(this.mActivity.getApplicationContext(), result, 1).show();
            if (this.assign.booleanValue()) {
                this.phoneListAdapter.getDevice().assignPhone();
            } else {
                this.phoneListAdapter.getDevice().unassignPhone();
            }
            this.phoneListAdapter.notifyDataSetChanged();
            this.mActivity.setData(Boolean.valueOf(false));
            this.mActivity.sendBroadcast(new Intent("refresh_phones"));
            this.mActivity.showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MyMoocallActivity) context;
        }
    }
}
