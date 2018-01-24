package com.moocall.moocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.async.GetAnimalsAsyncTask;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.db.SensorDb;
import com.moocall.moocall.db.SensorDbDao;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.FetchAllAnimalListUrl;
import com.moocall.moocall.url.FetchHerdOverviewUrl;
import com.moocall.moocall.url.FetchSensorListUrl;
import com.moocall.moocall.url.FetchSmartListUrl;
import com.moocall.moocall.url.FetchStatisticsUrl;
import com.moocall.moocall.util.DonutChart;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ManageHerdActivity extends MainActivity {
    private static View progressView;
    private static Toolbar toolbar;
    private RelativeLayout addNewAnimal;
    private RelativeLayout animalRecent;
    private BroadcastReceiver broadcastReceiver;
    private Boolean broadcastRegistred = Boolean.valueOf(false);
    private RelativeLayout firstPageIndicatorLayout;
    private ScrollView herdOverviewPage;
    private TextView pageTitle;
    private BroadcastReceiver refreshListsBroadcastReceiver;
    private RelativeLayout searchAnimal;
    private RelativeLayout secondPageIndicatorLayout;
    private ScrollView smartListPage;
    private ScrollView statisticsPage;
    private RelativeLayout thirdPageIndicatorLayout;

    class C04571 extends BroadcastReceiver {
        C04571() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                ManageHerdActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.FETCH_SMART_LIST)) {
                    ManageHerdActivity.this.setSmartListPage(new JSONObject(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.FETCH_HERD_OVERVIEW)) {
                    ManageHerdActivity.this.setHerdOverviewPage(new JSONObject(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.FETCH_STATISTICS)) {
                    ManageHerdActivity.this.setStatisticsPage(new JSONObject(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.FETCH_SENSOR_LIST)) {
                    ManageHerdActivity.this.setSensorList(new JSONArray(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C04583 implements OnClickListener {
        C04583() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.registerReceiver();
            ManageHerdActivity.this.startActivity(new Intent(ManageHerdActivity.this, AnimalSearchActivity.class));
        }
    }

    class C04594 implements OnClickListener {
        C04594() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.registerReceiver();
            ManageHerdActivity.this.startActivity(new Intent(ManageHerdActivity.this, AnimalRecentActivity.class));
        }
    }

    class C04605 implements OnClickListener {
        C04605() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.registerReceiver();
            ManageHerdActivity.this.startActivity(new Intent(ManageHerdActivity.this, ManualInputActivity.class));
        }
    }

    class C04616 implements OnClickListener {
        C04616() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.setBottomFilter(Integer.valueOf(1));
        }
    }

    class C04627 implements OnClickListener {
        C04627() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.setBottomFilter(Integer.valueOf(2));
        }
    }

    class C04638 implements OnClickListener {
        C04638() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.setBottomFilter(Integer.valueOf(3));
        }
    }

    class C04649 implements OnClickListener {
        C04649() {
        }

        public void onClick(View view) {
            ManageHerdActivity.this.registerReceiver();
            Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
            intent.putExtra("type", 1);
            ManageHerdActivity.this.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onResume();
        setupToolbar();
        createAsyncBroadcast();
        setupLayout();
        createBroadcastReceiver();
        implementListeners();
        setBottomFilter(Integer.valueOf(1));
        fetchSmartList();
    }

    private void setupToolbar() {
        getLayoutInflater().inflate(C0530R.layout.activity_manage_herd, this.frameLayout);
        this.drawerList.setItemChecked(position, true);
        this.homePage.setVisibility(8);
        toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0530R.menu.manage_herd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0530R.id.scan || id == C0530R.id.notification) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04571();
    }

    private void setupLayout() {
        createAnimalImageMemoryCache();
        progressView = findViewById(C0530R.id.progress_disable);
        this.searchAnimal = (RelativeLayout) findViewById(C0530R.id.searchAnimal);
        this.animalRecent = (RelativeLayout) findViewById(C0530R.id.animalRecent);
        this.addNewAnimal = (RelativeLayout) findViewById(C0530R.id.addNewAnimal);
        this.firstPageIndicatorLayout = (RelativeLayout) findViewById(C0530R.id.firstPageIndicatorLayout);
        this.secondPageIndicatorLayout = (RelativeLayout) findViewById(C0530R.id.secondPageIndicatorLayout);
        this.thirdPageIndicatorLayout = (RelativeLayout) findViewById(C0530R.id.thirdPageIndicatorLayout);
        this.pageTitle = (TextView) findViewById(C0530R.id.pageTitle);
        this.smartListPage = (ScrollView) findViewById(C0530R.id.smartListPage);
        this.herdOverviewPage = (ScrollView) findViewById(C0530R.id.herdOverviewPage);
        this.statisticsPage = (ScrollView) findViewById(C0530R.id.statisticsPage);
    }

    private void createAnimalImageMemoryCache() {
        StorageContainer.setAnimalImageMemoryCache(new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 4) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        });
    }

    private void implementListeners() {
        this.searchAnimal.setOnClickListener(new C04583());
        this.animalRecent.setOnClickListener(new C04594());
        this.addNewAnimal.setOnClickListener(new C04605());
        this.firstPageIndicatorLayout.setOnClickListener(new C04616());
        this.secondPageIndicatorLayout.setOnClickListener(new C04627());
        this.thirdPageIndicatorLayout.setOnClickListener(new C04638());
    }

    public void setBottomFilter(Integer filter) {
        this.firstPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.secondPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.thirdPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.smartListPage.setVisibility(8);
        this.herdOverviewPage.setVisibility(8);
        this.statisticsPage.setVisibility(8);
        switch (filter.intValue()) {
            case 1:
                this.pageTitle.setText(getString(C0530R.string.smart_lists));
                this.firstPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                this.smartListPage.setVisibility(0);
                return;
            case 2:
                this.pageTitle.setText(getString(C0530R.string.herd_overview));
                this.secondPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                this.herdOverviewPage.setVisibility(0);
                return;
            case 3:
                this.pageTitle.setText(getString(C0530R.string.statistics));
                this.thirdPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                this.statisticsPage.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void fetchSmartList() {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_SMART_LIST));
        new AcquireResponseTask(this).execute(new String[]{new FetchSmartListUrl().createAndReturnUrl(this), QuickstartPreferences.FETCH_SMART_LIST});
    }

    private void setSmartListPage(JSONObject response) {
        RelativeLayout inCalfPage = (RelativeLayout) findViewById(C0530R.id.inCalfPage);
        RelativeLayout cyclingPage = (RelativeLayout) findViewById(C0530R.id.cyclingPage);
        RelativeLayout inHeatPage = (RelativeLayout) findViewById(C0530R.id.inHeatPage);
        RelativeLayout inseminationPage = (RelativeLayout) findViewById(C0530R.id.inseminationPage);
        TextView inCalfTotal = (TextView) findViewById(C0530R.id.inCalfTotal);
        TextView cyclingTotal = (TextView) findViewById(C0530R.id.cyclingTotal);
        TextView inHeatTotal = (TextView) findViewById(C0530R.id.inHeatTotal);
        TextView inseminationTotal = (TextView) findViewById(C0530R.id.inseminationTotal);
        TextView inCalfCows = (TextView) findViewById(C0530R.id.inCalfCows);
        TextView inCalfHeifers = (TextView) findViewById(C0530R.id.inCalfHeifers);
        TextView cyclingCows = (TextView) findViewById(C0530R.id.cyclingCows);
        TextView cyclingHeifers = (TextView) findViewById(C0530R.id.cyclingHeifers);
        TextView inHeatCows = (TextView) findViewById(C0530R.id.inHeatCows);
        TextView inHeatHeifers = (TextView) findViewById(C0530R.id.inHeatHeifers);
        TextView inseminationCows = (TextView) findViewById(C0530R.id.inseminationCows);
        TextView inseminationHeifers = (TextView) findViewById(C0530R.id.inseminationHeifers);
        TextView inCalfFirstTag = (TextView) findViewById(C0530R.id.inCalfFirstTag);
        TextView inCalfFirstTime = (TextView) findViewById(C0530R.id.inCalfFirstTime);
        TextView inCalfSecondTag = (TextView) findViewById(C0530R.id.inCalfSecondTag);
        TextView inCalfSecondTime = (TextView) findViewById(C0530R.id.inCalfSecondTime);
        TextView inCalfThirdTag = (TextView) findViewById(C0530R.id.inCalfThirdTag);
        TextView inCalfThirdTime = (TextView) findViewById(C0530R.id.inCalfThirdTime);
        inCalfFirstTag.setText("");
        inCalfFirstTime.setText("");
        inCalfSecondTag.setText("");
        inCalfSecondTime.setText("");
        inCalfThirdTag.setText("");
        inCalfThirdTime.setText("");
        TextView cyclingFirstTag = (TextView) findViewById(C0530R.id.cyclingFirstTag);
        TextView cyclingFirstTime = (TextView) findViewById(C0530R.id.cyclingFirstTime);
        TextView cyclingSecondTag = (TextView) findViewById(C0530R.id.cyclingSecondTag);
        TextView cyclingSecondTime = (TextView) findViewById(C0530R.id.cyclingSecondTime);
        TextView cyclingThirdTag = (TextView) findViewById(C0530R.id.cyclingThirdTag);
        TextView cyclingThirdTime = (TextView) findViewById(C0530R.id.cyclingThirdTime);
        cyclingFirstTag.setText("");
        cyclingFirstTime.setText("");
        cyclingSecondTag.setText("");
        cyclingSecondTime.setText("");
        cyclingThirdTag.setText("");
        cyclingThirdTime.setText("");
        TextView inHeatFirstTag = (TextView) findViewById(C0530R.id.inHeatFirstTag);
        TextView inHeatFirstTime = (TextView) findViewById(C0530R.id.inHeatFirstTime);
        TextView inHeatSecondTag = (TextView) findViewById(C0530R.id.inHeatSecondTag);
        TextView inHeatSecondTime = (TextView) findViewById(C0530R.id.inHeatSecondTime);
        TextView inHeatThirdTag = (TextView) findViewById(C0530R.id.inHeatThirdTag);
        TextView inHeatThirdTime = (TextView) findViewById(C0530R.id.inHeatThirdTime);
        inHeatFirstTag.setText("");
        inHeatFirstTime.setText("");
        inHeatSecondTag.setText("");
        inHeatSecondTime.setText("");
        inHeatThirdTag.setText("");
        inHeatThirdTime.setText("");
        TextView inseminationFirstTag = (TextView) findViewById(C0530R.id.inseminationFirstTag);
        TextView inseminationFirstTime = (TextView) findViewById(C0530R.id.inseminationFirstTime);
        TextView inseminationSecondTag = (TextView) findViewById(C0530R.id.inseminationSecondTag);
        TextView inseminationSecondTime = (TextView) findViewById(C0530R.id.inseminationSecondTime);
        TextView inseminationThirdTag = (TextView) findViewById(C0530R.id.inseminationThirdTag);
        TextView inseminationThirdTime = (TextView) findViewById(C0530R.id.inseminationThirdTime);
        inseminationFirstTag.setText("");
        inseminationFirstTime.setText("");
        inseminationSecondTag.setText("");
        inseminationSecondTime.setText("");
        inseminationThirdTag.setText("");
        inseminationThirdTime.setText("");
        try {
            int i;
            JSONObject object;
            JSONParserBgw jSONParserBgw = new JSONParserBgw(response);
            Integer inCalfCow = Integer.valueOf(jSONParserBgw.getJsonArray("incalf_number").getJSONArray(0).getJSONObject(0).getInt("cow"));
            Integer inCalfHeifer = Integer.valueOf(jSONParserBgw.getJsonArray("incalf_number").getJSONArray(1).getJSONObject(0).getInt("heifer"));
            inCalfTotal.setText(Integer.valueOf(inCalfCow.intValue() + inCalfHeifer.intValue()).toString());
            inCalfCows.setText(inCalfCow.toString());
            inCalfHeifers.setText(inCalfHeifer.toString());
            Integer inseminatedCow = Integer.valueOf(jSONParserBgw.getJsonArray("insemanated_number").getJSONArray(0).getJSONObject(0).getInt("cow"));
            Integer inseminatedHeifer = Integer.valueOf(jSONParserBgw.getJsonArray("insemanated_number").getJSONArray(1).getJSONObject(0).getInt("heifer"));
            inseminationTotal.setText(Integer.valueOf(inseminatedCow.intValue() + inseminatedHeifer.intValue()).toString());
            inseminationCows.setText(inseminatedCow.toString());
            inseminationHeifers.setText(inseminatedHeifer.toString());
            Integer cyclingCow = Integer.valueOf(jSONParserBgw.getJsonArray("cycling_number").getJSONArray(0).getJSONObject(0).getInt("cow"));
            Integer cyclingHeifer = Integer.valueOf(jSONParserBgw.getJsonArray("cycling_number").getJSONArray(1).getJSONObject(0).getInt("heifer"));
            cyclingTotal.setText(Integer.valueOf(cyclingCow.intValue() + cyclingHeifer.intValue()).toString());
            cyclingCows.setText(cyclingCow.toString());
            cyclingHeifers.setText(cyclingHeifer.toString());
            Integer inHeatCow = Integer.valueOf(jSONParserBgw.getJsonArray("heat_number").getJSONArray(0).getJSONObject(0).getInt("cow"));
            Integer inHeatHeifer = Integer.valueOf(jSONParserBgw.getJsonArray("heat_number").getJSONArray(1).getJSONObject(0).getInt("heifer"));
            inHeatTotal.setText(Integer.valueOf(inHeatCow.intValue() + inHeatHeifer.intValue()).toString());
            inHeatCows.setText(inHeatCow.toString());
            inHeatHeifers.setText(inHeatHeifer.toString());
            for (i = 0; i < jSONParserBgw.getJsonArray("incalf_cows").length(); i++) {
                object = jSONParserBgw.getJsonArray("incalf_cows").getJSONObject(i);
                switch (i) {
                    case 0:
                        inCalfFirstTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inCalfFirstTime.setText(object.has("due_date") ? Utils.getTimeShorterReverse(Utils.calculateTime(object.getString("due_date") + " 00:00:00", "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 1:
                        inCalfSecondTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inCalfSecondTime.setText(object.has("due_date") ? Utils.getTimeShorterReverse(Utils.calculateTime(object.getString("due_date") + " 00:00:00", "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 2:
                        inCalfThirdTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inCalfThirdTime.setText(object.has("due_date") ? Utils.getTimeShorterReverse(Utils.calculateTime(object.getString("due_date") + " 00:00:00", "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    default:
                        break;
                }
            }
            for (i = 0; i < jSONParserBgw.getJsonArray("cycle_cow").length(); i++) {
                object = jSONParserBgw.getJsonArray("cycle_cow").getJSONObject(i);
                switch (i) {
                    case 0:
                        cyclingFirstTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        cyclingFirstTime.setText(object.has("last_cycle_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_cycle_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 1:
                        cyclingSecondTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        cyclingSecondTime.setText(object.has("last_cycle_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_cycle_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 2:
                        cyclingThirdTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        cyclingThirdTime.setText(object.has("last_cycle_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_cycle_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    default:
                        break;
                }
            }
            for (i = 0; i < jSONParserBgw.getJsonArray("inheat_cow").length(); i++) {
                object = jSONParserBgw.getJsonArray("inheat_cow").getJSONObject(i);
                switch (i) {
                    case 0:
                        inHeatFirstTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inHeatFirstTime.setText(object.has("last_heat_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_heat_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 1:
                        inHeatSecondTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inHeatSecondTime.setText(object.has("last_heat_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_heat_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 2:
                        inHeatThirdTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inHeatThirdTime.setText(object.has("last_heat_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_heat_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    default:
                        break;
                }
            }
            for (i = 0; i < jSONParserBgw.getJsonArray("insemanated_cows").length(); i++) {
                object = jSONParserBgw.getJsonArray("insemanated_cows").getJSONObject(i);
                switch (i) {
                    case 0:
                        inseminationFirstTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inseminationFirstTime.setText(object.has("last_insemenation_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_insemenation_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 1:
                        inseminationSecondTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inseminationSecondTime.setText(object.has("last_insemenation_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_insemenation_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    case 2:
                        inseminationThirdTag.setText(object.has("tag_number") ? object.getString("tag_number") : "N/A");
                        inseminationThirdTime.setText(object.has("last_insemenation_date") ? Utils.getTimeShorter(Utils.calculateTime(object.getString("last_insemenation_date"), "yyyy-MM-dd HH:mm")) : "N/A");
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inCalfPage.setOnClickListener(new C04649());
        cyclingPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ManageHerdActivity.this.registerReceiver();
                Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                intent.putExtra("type", 2);
                ManageHerdActivity.this.startActivity(intent);
            }
        });
        inHeatPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ManageHerdActivity.this.registerReceiver();
                Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                intent.putExtra("type", 3);
                ManageHerdActivity.this.startActivity(intent);
            }
        });
        inseminationPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ManageHerdActivity.this.registerReceiver();
                Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                intent.putExtra("type", 4);
                ManageHerdActivity.this.startActivity(intent);
            }
        });
        fetchHerdOverview();
    }

    private void fetchHerdOverview() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_HERD_OVERVIEW));
        new AcquireResponseTask(this).execute(new String[]{new FetchHerdOverviewUrl().createAndReturnUrl(this), QuickstartPreferences.FETCH_HERD_OVERVIEW});
    }

    private void setHerdOverviewPage(JSONObject response) {
        try {
            JSONParserBgw jSONParserBgw = new JSONParserBgw(response);
            ((TextView) findViewById(C0530R.id.entireHerdTotal)).setText(jSONParserBgw.getString("herd"));
            ((TextView) findViewById(C0530R.id.breedingHerdTotal)).setText(jSONParserBgw.getString("breeding_herd"));
            ((TextView) findViewById(C0530R.id.cowsSize)).setText(jSONParserBgw.getJsonObject("details").getString("cow"));
            ((TextView) findViewById(C0530R.id.breedingHeifersSize)).setText(jSONParserBgw.getJsonObject("details").getString("breeding_heifer"));
            ((TextView) findViewById(C0530R.id.breedingBullsSize)).setText(jSONParserBgw.getJsonObject("details").getString("breeding_bull"));
            ((TextView) findViewById(C0530R.id.beefHeifersSize)).setText(jSONParserBgw.getJsonObject("details").getString("beef_heifer"));
            ((TextView) findViewById(C0530R.id.beefBullsSize)).setText(jSONParserBgw.getJsonObject("details").getString("beef_bull"));
            ((TextView) findViewById(C0530R.id.heifersCalvesSize)).setText(jSONParserBgw.getJsonObject("details").getString("heifer_calf"));
            ((TextView) findViewById(C0530R.id.bullCalvesSize)).setText(jSONParserBgw.getJsonObject("details").getString("bull_calf"));
            ((TextView) findViewById(C0530R.id.bullocksSize)).setText(jSONParserBgw.getJsonObject("details").getString("bullock"));
            TextView inCalfPercent = (TextView) findViewById(C0530R.id.inCalfPercent);
            TextView inseminatedPercent = (TextView) findViewById(C0530R.id.inseminatedPercent);
            TextView freshPercent = (TextView) findViewById(C0530R.id.freshPercent);
            TextView cyclePercent = (TextView) findViewById(C0530R.id.cyclePercent);
            TextView inHeatPercent = (TextView) findViewById(C0530R.id.inHeatPercent);
            ((TextView) findViewById(C0530R.id.breedingHerdCH)).setText(jSONParserBgw.getString("breeding_herd"));
            DonutChart allHerdChart = (DonutChart) findViewById(C0530R.id.allHerdChart);
            ArrayList<Double> alPercentage = new ArrayList();
            alPercentage.add(Double.valueOf(jSONParserBgw.getJsonObject("details").getDouble("incalf_p")));
            alPercentage.add(Double.valueOf(jSONParserBgw.getJsonObject("details").getDouble("insemenated_p")));
            alPercentage.add(Double.valueOf(jSONParserBgw.getJsonObject("details").getDouble("fresh_p")));
            alPercentage.add(Double.valueOf(jSONParserBgw.getJsonObject("details").getDouble("cycle_p")));
            alPercentage.add(Double.valueOf(100.0d - (((Double) alPercentage.get(3)).doubleValue() + ((((Double) alPercentage.get(0)).doubleValue() + ((Double) alPercentage.get(1)).doubleValue()) + ((Double) alPercentage.get(2)).doubleValue()))));
            inCalfPercent.setText(Integer.valueOf(((Double) alPercentage.get(0)).intValue()).toString() + " %");
            inseminatedPercent.setText(Integer.valueOf(((Double) alPercentage.get(1)).intValue()).toString() + " %");
            freshPercent.setText(Integer.valueOf(((Double) alPercentage.get(2)).intValue()).toString() + " %");
            cyclePercent.setText(Integer.valueOf(((Double) alPercentage.get(3)).intValue()).toString() + " %");
            inHeatPercent.setText(Integer.valueOf(((Double) alPercentage.get(4)).intValue()).toString() + " %");
            try {
                allHerdChart.setAdapter(alPercentage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RelativeLayout breedingPage = (RelativeLayout) findViewById(C0530R.id.breedingPage);
            RelativeLayout bullsPage = (RelativeLayout) findViewById(C0530R.id.bullsPage);
            RelativeLayout calvesPage = (RelativeLayout) findViewById(C0530R.id.calvesPage);
            RelativeLayout beefCattlePage = (RelativeLayout) findViewById(C0530R.id.beefCattlePage);
            RelativeLayout historicPage = (RelativeLayout) findViewById(C0530R.id.historicPage);
            ((TextView) findViewById(C0530R.id.breedingTotal)).setText(jSONParserBgw.getString("breeding_herd"));
            ((TextView) findViewById(C0530R.id.bullsTotal)).setText(jSONParserBgw.getString("all_bulls"));
            ((TextView) findViewById(C0530R.id.calvesTotal)).setText(jSONParserBgw.getString("all_calves"));
            ((TextView) findViewById(C0530R.id.beefCattleTotal)).setText(jSONParserBgw.getString("beef_cattle"));
            ((TextView) findViewById(C0530R.id.historicTotal)).setText(jSONParserBgw.getJsonObject("history").getString("sum"));
            ((TextView) findViewById(C0530R.id.breedingCows)).setText(jSONParserBgw.getJsonObject("details").getString("cow"));
            ((TextView) findViewById(C0530R.id.breedingHeifers)).setText(jSONParserBgw.getJsonObject("details").getString("breeding_heifer"));
            ((TextView) findViewById(C0530R.id.breedingBulls)).setText(jSONParserBgw.getJsonObject("details").getString("breeding_bull"));
            ((TextView) findViewById(C0530R.id.aiBulls)).setText(jSONParserBgw.getJsonObject("details").getString("ai_bulls"));
            ((TextView) findViewById(C0530R.id.heiferCalves)).setText(jSONParserBgw.getJsonObject("details").getString("heifer_calf"));
            ((TextView) findViewById(C0530R.id.bullCalves)).setText(jSONParserBgw.getJsonObject("details").getString("bull_calf"));
            ((TextView) findViewById(C0530R.id.replacementHeifers)).setText(jSONParserBgw.getString("replacement_heifers"));
            ((TextView) findViewById(C0530R.id.averageWeightCalves)).setText(Utils.getWeightText(jSONParserBgw.getString("calves_weight_gain"), this));
            ((TextView) findViewById(C0530R.id.heiferBeef)).setText(jSONParserBgw.getJsonObject("details").getString("beef_heifer"));
            ((TextView) findViewById(C0530R.id.bullBeef)).setText(jSONParserBgw.getJsonObject("details").getString("beef_bull"));
            ((TextView) findViewById(C0530R.id.ageOver)).setText(jSONParserBgw.getString("age_over_24"));
            ((TextView) findViewById(C0530R.id.averageWeightBeef)).setText(Utils.getWeightText(jSONParserBgw.getString("bull_weight_gain"), this));
            ((TextView) findViewById(C0530R.id.historicCalves)).setText(jSONParserBgw.getJsonObject("history").getString("calves"));
            ((TextView) findViewById(C0530R.id.historicBeef)).setText(jSONParserBgw.getJsonObject("history").getString("beef_animal"));
            ((TextView) findViewById(C0530R.id.ageSale)).setText(jSONParserBgw.getJsonObject("history").getString("age_at_sale"));
            ((TextView) findViewById(C0530R.id.averageWeightSale)).setText(Utils.getWeightText(jSONParserBgw.getJsonObject("history").getString("weight_at_sale"), this));
            breedingPage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ManageHerdActivity.this.registerReceiver();
                    Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                    intent.putExtra("type", 9);
                    ManageHerdActivity.this.startActivity(intent);
                }
            });
            bullsPage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ManageHerdActivity.this.registerReceiver();
                    Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                    intent.putExtra("type", 5);
                    ManageHerdActivity.this.startActivity(intent);
                }
            });
            calvesPage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ManageHerdActivity.this.registerReceiver();
                    Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                    intent.putExtra("type", 6);
                    ManageHerdActivity.this.startActivity(intent);
                }
            });
            beefCattlePage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ManageHerdActivity.this.registerReceiver();
                    Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                    intent.putExtra("type", 7);
                    ManageHerdActivity.this.startActivity(intent);
                }
            });
            historicPage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ManageHerdActivity.this.registerReceiver();
                    Intent intent = new Intent(ManageHerdActivity.this, SmartListActivity.class);
                    intent.putExtra("type", 8);
                    ManageHerdActivity.this.startActivity(intent);
                }
            });
            fetchStatistics();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void fetchStatistics() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_STATISTICS));
        new AcquireResponseTask(this).execute(new String[]{new FetchStatisticsUrl().createAndReturnUrl(this), QuickstartPreferences.FETCH_STATISTICS});
    }

    private void setStatisticsPage(JSONObject response) {
        try {
            JSONParserBgw jSONParserBgw = new JSONParserBgw(response);
            ((TextView) findViewById(C0530R.id.inseminatedFirstCycle)).setText(jSONParserBgw.getString("insemenatde_1cysle"));
            ((TextView) findViewById(C0530R.id.inseminatedSecondCycle)).setText(jSONParserBgw.getString("insemenatde_2cysle"));
            ((TextView) findViewById(C0530R.id.inHeatLastTwelve)).setText(jSONParserBgw.getString("in_heat12"));
            ((TextView) findViewById(C0530R.id.herdServed)).setText(jSONParserBgw.getString("heard_served") + "%");
            ((TextView) findViewById(C0530R.id.easyCalved)).setText(jSONParserBgw.getString("easy1") + "%");
            ((TextView) findViewById(C0530R.id.easyMinor)).setText(jSONParserBgw.getString("easy2") + "%");
            ((TextView) findViewById(C0530R.id.moderateRepositioned)).setText(jSONParserBgw.getString("modareate1") + "%");
            ((TextView) findViewById(C0530R.id.moderateAids)).setText(jSONParserBgw.getString("modareate2") + "%");
            ((TextView) findViewById(C0530R.id.difficultCSection)).setText(jSONParserBgw.getString("difficult1") + "%");
            ((TextView) findViewById(C0530R.id.difficultOther)).setText(jSONParserBgw.getString("difficult2") + "%");
            ((TextView) findViewById(C0530R.id.mortality)).setText(jSONParserBgw.getString("mortality") + "%");
            ((TextView) findViewById(C0530R.id.dueToCalveNumber)).setText(jSONParserBgw.getString("due_to_calve_7days"));
            ((TextView) findViewById(C0530R.id.freshCowsNumber)).setText(jSONParserBgw.getString("fresh_cows"));
            ((TextView) findViewById(C0530R.id.averageBeefCattleWeight)).setText(Utils.getWeightText(jSONParserBgw.getString("avrg_weight_gain"), this));
            ((TextView) findViewById(C0530R.id.beefUnderTwo)).setText(jSONParserBgw.getString("beef_age12"));
            ((TextView) findViewById(C0530R.id.beefOverTwo)).setText(jSONParserBgw.getString("beef_age2"));
            fetchSensorList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchSensorList() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_SENSOR_LIST));
        new AcquireResponseTask(this).execute(new String[]{new FetchSensorListUrl().createAndReturnUrl(this), QuickstartPreferences.FETCH_SENSOR_LIST});
    }

    private void setSensorList(JSONArray response) {
        try {
            DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
            SensorDbDao sensorDbDao = daoSession.getSensorDbDao();
            SensorDbDao.dropTable(daoSession.getDatabase(), true);
            SensorDbDao.createTable(daoSession.getDatabase(), true);
            for (int i = 0; i < response.length(); i++) {
                SensorDb sensorDb = new SensorDb(new JSONParserBgw((JSONObject) response.get(i)));
                sensorDbDao.insert(sensorDb);
                Log.d("DaoExample", "Inserted new sensor, ID: " + sensorDb.getDevice_code());
                Log.d("DaoExample", "Total sensors: " + sensorDbDao.count());
            }
            fetchAnimalList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchAnimalList() {
        List<AnimalDb> animal = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getAnimalDbDao().queryBuilder().orderDesc(Properties.Time_update).limit(1).list();
        String sync = "";
        if (animal.size() > 0) {
            sync = ((AnimalDb) animal.get(0)).getTime_update();
        }
        System.out.println(sync);
        new GetAnimalsAsyncTask(this).execute(new String[]{new FetchAllAnimalListUrl().createAndReturnUrl(this), "sync", sync});
    }

    private void resetSmartLists() {
        StorageContainer.inCalfAllSmartList = null;
        StorageContainer.inCalfCowSmartList = null;
        StorageContainer.inCalfHeiferSmartList = null;
        StorageContainer.due7AllSmartList = null;
        StorageContainer.due7CowSmartList = null;
        StorageContainer.due7HeiferSmartList = null;
        StorageContainer.calved90AllSmartList = null;
        StorageContainer.calved90CowSmartList = null;
        StorageContainer.calved90HeiferSmartList = null;
        StorageContainer.inHeat90AllSmartList = null;
        StorageContainer.inHeat90CowSmartList = null;
        StorageContainer.inHeat90HeiferSmartList = null;
        StorageContainer.inHeat12AllSmartList = null;
        StorageContainer.inHeat12CowSmartList = null;
        StorageContainer.inHeat12HeiferSmartList = null;
        StorageContainer.cycle90AllSmartList = null;
        StorageContainer.cycle90CowSmartList = null;
        StorageContainer.cycle90HeiferSmartList = null;
        StorageContainer.cycle1724AllSmartList = null;
        StorageContainer.cycle1724CowSmartList = null;
        StorageContainer.cycle1724HeiferSmartList = null;
        StorageContainer.cycle3845AllSmartList = null;
        StorageContainer.cycle3845CowSmartList = null;
        StorageContainer.cycle3845HeiferSmartList = null;
        StorageContainer.inseminated90AllSmartList = null;
        StorageContainer.inseminated90CowSmartList = null;
        StorageContainer.inseminated90HeiferSmartList = null;
        StorageContainer.inseminated1724AllSmartList = null;
        StorageContainer.inseminated1724CowSmartList = null;
        StorageContainer.inseminated1724HeiferSmartList = null;
        StorageContainer.inseminated3845AllSmartList = null;
        StorageContainer.inseminated3845CowSmartList = null;
        StorageContainer.inseminated3845HeiferSmartList = null;
        StorageContainer.bullsByAgeAllSmartList = null;
        StorageContainer.bullsByAgeBreedingSmartList = null;
        StorageContainer.bullsByAgeAISmartList = null;
        StorageContainer.bullsByWeightAllSmartList = null;
        StorageContainer.bullsByWeightBreedingSmartList = null;
        StorageContainer.bullsByWeightAISmartList = null;
        StorageContainer.calvesByAgeAllSmartList = null;
        StorageContainer.calvesByAgeHeifersSmartList = null;
        StorageContainer.calvesByAgeBullsSmartList = null;
        StorageContainer.calvesByWeightAllSmartList = null;
        StorageContainer.calvesByWeightHeifersSmartList = null;
        StorageContainer.calvesByWeightBullsSmartList = null;
        StorageContainer.beefByAgeAllSmartList = null;
        StorageContainer.beefByAgeHeifersSmartList = null;
        StorageContainer.beefByAgeBullsSmartList = null;
        StorageContainer.beefByWeightAllSmartList = null;
        StorageContainer.beefByWeightHeifersSmartList = null;
        StorageContainer.beefByWeightBullsSmartList = null;
        StorageContainer.historicByCulledAllSmartList = null;
        StorageContainer.historicByCulledCowsHeifersSmartList = null;
        StorageContainer.historicByCulledCalvesSmartList = null;
        StorageContainer.historicByCulledBullsSmartList = null;
        StorageContainer.historicBySoldAllSmartList = null;
        StorageContainer.historicBySoldCowsHeifersSmartList = null;
        StorageContainer.historicBySoldCalvesSmartList = null;
        StorageContainer.historicBySoldBullsSmartList = null;
    }

    public boolean scan(MenuItem view) {
        registerReceiver();
        startActivity(new Intent(this, ScanMoocallTagActivity.class));
        return true;
    }

    public boolean notification(MenuItem view) {
        startActivity(new Intent(this, CowHeatListActivity.class));
        return true;
    }

    public void onBackPressed() {
        if (this.homePage.getVisibility() == 0) {
            this.homePage.setVisibility(8);
        } else {
            moveTaskToBack(true);
        }
    }

    public void createBroadcastReceiver() {
        this.refreshListsBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                System.out.println("DAnillooo:  " + action);
                if (action.equals("refresh_lists")) {
                    ManageHerdActivity.this.fetchSmartList();
                    ManageHerdActivity.this.resetSmartLists();
                }
            }
        };
    }

    public void registerReceiver() {
        registerReceiver(this.refreshListsBroadcastReceiver, new IntentFilter("refresh_lists"));
        this.broadcastRegistred = Boolean.valueOf(true);
    }

    public void unregisterReceiver() {
        if (this.broadcastRegistred.booleanValue()) {
            unregisterReceiver(this.refreshListsBroadcastReceiver);
            this.broadcastRegistred = Boolean.valueOf(false);
        }
    }

    protected void onResume() {
        super.onResume();
        waitForUser(false);
        StorageContainer.wakeApp(this);
        this.mTracker.setScreenName("Manage Herd Activity");
        this.mTracker.send(new ScreenViewBuilder().build());
    }

    public static void showProgress(boolean show) {
        progressView.setVisibility(show ? 0 : 8);
    }
}
