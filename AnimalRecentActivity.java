package com.moocall.moocall;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.moocall.moocall.adapter.AnimalActivitiesAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao.Properties;
import com.moocall.moocall.domain.AnimalHistory;
import com.moocall.moocall.domain.AnimalHistoryRecent;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.GetActivitiesUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnimalRecentActivity extends Activity {
    private SwipeRefreshLayout activitiesListSwipeRefreshLayout;
    private AnimalActivitiesAdapter animalActivitiesAdapter;
    private BroadcastReceiver broadcastReceiver;
    private Boolean offline;
    private View progressView;
    private Toolbar toolbar;

    class C04281 implements OnClickListener {
        C04281() {
        }

        public void onClick(View v) {
            AnimalRecentActivity.this.onBackPressed();
        }
    }

    class C04293 extends BroadcastReceiver {
        C04293() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                AnimalRecentActivity.this.unregisterReceiver(this);
                if (intent.getAction().equals(QuickstartPreferences.GET_ACTIVITIES)) {
                    AnimalRecentActivity.this.setActivities(new JSONArray(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C10922 implements OnRefreshListener {
        C10922() {
        }

        public void onRefresh() {
            if (AnimalRecentActivity.this.activitiesListSwipeRefreshLayout.isRefreshing()) {
                AnimalRecentActivity.this.getActivities();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0530R.layout.activity_animal_recent);
        this.offline = (Boolean) getIntent().getSerializableExtra("offline");
        setupToolbar();
        createAsyncBroadcast();
        setupLayout();
        onResume();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C04281());
        this.toolbar.setTitle(getString(C0530R.string.recent_activities));
    }

    private void setupLayout() {
        ListView animalActivitiesListView = (ListView) findViewById(C0530R.id.animalActivitiesListView);
        this.activitiesListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.activitiesListSwipeRefreshLayout);
        this.animalActivitiesAdapter = new AnimalActivitiesAdapter();
        animalActivitiesListView.setAdapter(this.animalActivitiesAdapter);
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.activitiesListSwipeRefreshLayout.setOnRefreshListener(new C10922());
    }

    private void getActivities() {
        showProgress(true);
        if (this.offline == null || !this.offline.booleanValue()) {
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_ACTIVITIES));
            new AcquireResponseTask(this).execute(new String[]{new GetActivitiesUrl().createAndReturnUrl(this), QuickstartPreferences.GET_ACTIVITIES});
            return;
        }
        setOfflineActivities();
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04293();
    }

    public void setActivities(JSONArray result) {
        try {
            List<AnimalHistoryRecent> historyList = new ArrayList();
            List<String> headers = new ArrayList();
            for (int i = 0; i < result.length(); i++) {
                AnimalHistory animalHistory = new AnimalHistory(new JSONParserBgw((JSONObject) result.get(i)), (Activity) this);
                String date = Utils.getDateHeader(animalHistory.getDate());
                AnimalHistoryRecent animalHistoryRecent = new AnimalHistoryRecent(date, animalHistory);
                if (!headers.contains(date)) {
                    if (historyList.size() > 0) {
                        ((AnimalHistoryRecent) historyList.get(historyList.size() - 1)).setLast(Boolean.valueOf(true));
                    }
                    headers.add(date);
                    historyList.add(new AnimalHistoryRecent(date));
                    animalHistoryRecent.setFirst(Boolean.valueOf(true));
                }
                historyList.add(animalHistoryRecent);
            }
            if (historyList.size() > 0) {
                ((AnimalHistoryRecent) historyList.get(historyList.size() - 1)).setLast(Boolean.valueOf(true));
            }
            this.animalActivitiesAdapter.setActivities(historyList);
            showProgress(false);
            this.activitiesListSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOfflineActivities() {
        try {
            List<AnimalHistoryRecent> historyList = new ArrayList();
            List<AnimalActionDb> animalActionList = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getAnimalActionDbDao().queryBuilder().orderAsc(Properties.Datetime).list();
            List<String> headers = new ArrayList();
            for (AnimalActionDb animalAction : animalActionList) {
                AnimalHistory animalHistory = new AnimalHistory(animalAction, (Activity) this);
                String date = Utils.getDateHeader(animalHistory.getDate());
                AnimalHistoryRecent animalHistoryRecent = new AnimalHistoryRecent(date, animalHistory);
                if (!headers.contains(date)) {
                    if (historyList.size() > 0) {
                        ((AnimalHistoryRecent) historyList.get(historyList.size() - 1)).setLast(Boolean.valueOf(true));
                    }
                    headers.add(date);
                    historyList.add(new AnimalHistoryRecent(date));
                    animalHistoryRecent.setFirst(Boolean.valueOf(true));
                }
                historyList.add(animalHistoryRecent);
            }
            if (historyList.size() > 0) {
                ((AnimalHistoryRecent) historyList.get(historyList.size() - 1)).setLast(Boolean.valueOf(true));
            }
            this.animalActivitiesAdapter.setActivities(historyList);
            showProgress(false);
            this.activitiesListSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        getActivities();
    }
}
