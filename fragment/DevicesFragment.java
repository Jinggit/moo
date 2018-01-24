package com.moocall.moocall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.MyMoocallActivity;
import com.moocall.moocall.adapter.DeviceBigListAdapter;
import com.moocall.moocall.domain.Device;
import io.intercom.android.sdk.views.IntercomToolbar;
import java.util.List;

public class DevicesFragment extends Fragment {
    private List<Device> deviceList;
    private DeviceBigListAdapter deviceListAdapter;
    private ListView deviceListView;
    private SwipeRefreshLayout deviceSwipeRefreshLayout;
    private int lastFirstVisibleItem = 0;
    private MyMoocallActivity mActivity;

    class C11101 implements OnRefreshListener {
        C11101() {
        }

        public void onRefresh() {
            if (DevicesFragment.this.deviceSwipeRefreshLayout.isRefreshing()) {
                DevicesFragment.this.mActivity.setData(Boolean.valueOf(false));
                DevicesFragment.this.mActivity.sendBroadcast(new Intent("refresh_devices"));
            }
        }
    }

    public int getLastFirstVisibleItem() {
        return this.lastFirstVisibleItem;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0530R.layout.device_list_big, container, false);
        setupLayout(rootView);
        implementListeners();
        return rootView;
    }

    public void isAlive(MyMoocallActivity activity, View view) {
        if (view == null) {
            return;
        }
        if (this.deviceListAdapter == null) {
            this.mActivity = activity;
            setupLayout(view);
            return;
        }
        MyMoocallActivity myMoocallActivity = this.mActivity;
        this.deviceList = MyMoocallActivity.getDeviceList();
        this.deviceListAdapter.notifyDataSetChanged();
        if (this.deviceSwipeRefreshLayout.isRefreshing()) {
            this.deviceSwipeRefreshLayout.setRefreshing(false);
        }
        this.deviceListView.setSelection(0);
    }

    private void setupLayout(View rootView) {
        this.deviceListView = (ListView) rootView.findViewById(C0530R.id.devicesListView);
        this.deviceSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(C0530R.id.device_swipe_refresh_layout);
        this.deviceSwipeRefreshLayout.setProgressViewOffset(false, 0, IntercomToolbar.TITLE_FADE_DURATION_MS);
        MyMoocallActivity myMoocallActivity = this.mActivity;
        this.deviceList = MyMoocallActivity.getDeviceList();
        this.deviceListAdapter = new DeviceBigListAdapter(this.mActivity, this.deviceList);
        this.deviceListView.setAdapter(this.deviceListAdapter);
        this.deviceListView.setClickable(true);
    }

    private void implementListeners() {
        this.deviceSwipeRefreshLayout.setOnRefreshListener(new C11101());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MyMoocallActivity) context;
        }
    }
}
