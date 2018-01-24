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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.MyMoocallActivity;
import com.moocall.moocall.adapter.NotificationListAdapter;
import com.moocall.moocall.domain.Notification;
import io.intercom.android.sdk.views.IntercomToolbar;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private int lastFirstVisibleItem = 0;
    private MyMoocallActivity mActivity;
    private List<Notification> notificationList;
    private NotificationListAdapter notificationListAdapter;
    private ListView notificationListView;
    private SwipeRefreshLayout notificationSwipeRefreshLayout;
    private View progressView;

    class C06162 implements OnScrollListener {
        private int currentFirstVisibleItem;
        private int currentScrollState;
        private int currentVisibleItemCount;
        private int totalItem;

        C06162() {
        }

        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            this.currentScrollState = scrollState;
            if (!NotificationsFragment.this.mActivity.getNoMoreToLoad().booleanValue()) {
                isScrollCompleted();
            }
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.currentFirstVisibleItem = firstVisibleItem;
            this.currentVisibleItemCount = visibleItemCount;
            this.totalItem = totalItemCount;
            if (NotificationsFragment.this.lastFirstVisibleItem < firstVisibleItem) {
                NotificationsFragment.this.mActivity.moveContent(1);
            }
            if (NotificationsFragment.this.lastFirstVisibleItem > firstVisibleItem) {
                NotificationsFragment.this.mActivity.restoreContent(1);
            }
            NotificationsFragment.this.lastFirstVisibleItem = firstVisibleItem;
        }

        private void isScrollCompleted() {
            if (this.totalItem != 0 && this.currentFirstVisibleItem + this.currentVisibleItemCount == this.totalItem && this.currentScrollState == 0) {
                NotificationsFragment.this.mActivity.setLoadMore(Boolean.valueOf(true));
                NotificationsFragment.this.mActivity.setData(Boolean.valueOf(false));
                NotificationsFragment.this.mActivity.sendBroadcast(new Intent("refresh_notifications"));
            }
        }
    }

    class C11111 implements OnRefreshListener {
        C11111() {
        }

        public void onRefresh() {
            if (NotificationsFragment.this.notificationSwipeRefreshLayout.isRefreshing()) {
                NotificationsFragment.this.mActivity.setLoadMore(Boolean.valueOf(false));
                NotificationsFragment.this.mActivity.setData(Boolean.valueOf(false));
                NotificationsFragment.this.mActivity.sendBroadcast(new Intent("refresh_notifications"));
            }
        }
    }

    public int getLastFirstVisibleItem() {
        return this.lastFirstVisibleItem;
    }

    public void isAlive(MyMoocallActivity activity, View view) {
        if (view == null) {
            return;
        }
        if (this.notificationListAdapter == null) {
            this.mActivity = activity;
            setupLayout(view);
            return;
        }
        if (this.mActivity.getNoMoreToLoad().booleanValue()) {
            showProgress(false);
        } else {
            showProgress(true);
        }
        this.notificationList = this.mActivity.getNotificationList();
        this.notificationListAdapter.notifyDataSetChanged();
        if (this.notificationSwipeRefreshLayout.isRefreshing()) {
            this.notificationSwipeRefreshLayout.setRefreshing(false);
        }
        if (!this.mActivity.getLoadMore().booleanValue()) {
            this.notificationListView.setSelection(0);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0530R.layout.notifications_list, container, false);
        setupLayout(rootView);
        implementListeners();
        return rootView;
    }

    public void setupLayout(View rootView) {
        this.notificationListView = (ListView) rootView.findViewById(C0530R.id.notificationListView);
        View headerLayout = this.mActivity.getLayoutInflater().inflate(C0530R.layout.home_page_header, null);
        headerLayout.setOnClickListener(null);
        RelativeLayout footerLayout = (RelativeLayout) this.mActivity.getLayoutInflater().inflate(C0530R.layout.notification_list_footer, null);
        footerLayout.setOnClickListener(null);
        this.notificationListView.addHeaderView(headerLayout);
        this.notificationListView.addFooterView(footerLayout);
        this.notificationSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(C0530R.id.notification_swipe_refresh_layout);
        this.notificationSwipeRefreshLayout.setProgressViewOffset(false, 0, IntercomToolbar.TITLE_FADE_DURATION_MS);
        this.notificationList = this.mActivity.getNotificationList();
        this.notificationListAdapter = new NotificationListAdapter(this.mActivity, this.notificationList);
        this.progressView = footerLayout.findViewById(C0530R.id.notification_progress);
    }

    private void implementListeners() {
        this.notificationListView.setAdapter(this.notificationListAdapter);
        this.notificationSwipeRefreshLayout.setOnRefreshListener(new C11111());
        this.notificationListView.setOnScrollListener(new C06162());
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MyMoocallActivity) context;
        }
    }
}
