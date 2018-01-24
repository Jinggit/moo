package com.moocall.moocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.moocall.moocall.adapter.CowHeatListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.AnimalHistoryRecent;
import com.moocall.moocall.domain.CowHeat;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.GetCowHeatListUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CowHeatListActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;
    private CowHeatListAdapter cowHeatListAdapter;
    private SwipeRefreshLayout cowHeatSwipeRefreshLayout;
    private View progressView;
    private Toolbar toolbar;

    class C04361 implements OnClickListener {
        C04361() {
        }

        public void onClick(View v) {
            CowHeatListActivity.this.onBackPressed();
        }
    }

    class C04373 extends BroadcastReceiver {
        C04373() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                CowHeatListActivity.this.unregisterReceiver(this);
                if (intent.getAction().equals(QuickstartPreferences.GET_COW_HEAT_LIST)) {
                    CowHeatListActivity.this.setList(new JSONArray(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C10942 implements OnRefreshListener {
        C10942() {
        }

        public void onRefresh() {
            if (CowHeatListActivity.this.cowHeatSwipeRefreshLayout.isRefreshing()) {
                CowHeatListActivity.this.getList();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_cow_heat_list);
        onResume();
        setupToolbar();
        createAsyncBroadcast();
        setupLayout();
        getList();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C04361());
        this.toolbar.setTitle(getString(C0530R.string.cow_heat_list));
    }

    private void setupLayout() {
        ListView cowHeatListView = (ListView) findViewById(C0530R.id.cowHeatListView);
        this.cowHeatSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.cowHeatSwipeRefreshLayout);
        this.cowHeatListAdapter = new CowHeatListAdapter(this);
        cowHeatListView.setAdapter(this.cowHeatListAdapter);
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.cowHeatSwipeRefreshLayout.setOnRefreshListener(new C10942());
    }

    private void getList() {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_COW_HEAT_LIST));
        new AcquireResponseTask(this).execute(new String[]{new GetCowHeatListUrl().createAndReturnUrl(this), QuickstartPreferences.GET_COW_HEAT_LIST});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04373();
    }

    public void setList(JSONArray result) {
        try {
            List<AnimalHistoryRecent> list = new ArrayList();
            List<String> headers = new ArrayList();
            for (int i = 0; i < result.length(); i++) {
                CowHeat cowHeat = new CowHeat(new JSONParserBgw((JSONObject) result.get(i)));
                String date = Utils.getDateHeader(cowHeat.getDate());
                AnimalHistoryRecent animalHistoryRecent = new AnimalHistoryRecent(date, cowHeat);
                if (!headers.contains(date)) {
                    headers.add(date);
                    list.add(new AnimalHistoryRecent(date));
                }
                list.add(animalHistoryRecent);
            }
            this.cowHeatListAdapter.setList(list);
            showProgress(false);
            this.cowHeatSwipeRefreshLayout.setRefreshing(false);
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
    }
}
