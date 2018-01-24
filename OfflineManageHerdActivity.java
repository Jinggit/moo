package com.moocall.moocall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.moocall.moocall.async.CheckInternetConnection;
import com.moocall.moocall.async.SyncOfflineAsync;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.UserCredentials;
import com.moocall.moocall.interfaces.OnCheckInternetCompleted;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.util.DonutChart;
import com.moocall.moocall.util.StorageContainer;
import java.util.ArrayList;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

public class OfflineManageHerdActivity extends AppCompatActivity implements OnCheckInternetCompleted {
    private RelativeLayout addNewAnimal;
    private AnimalDbDao animalDbDao;
    private RelativeLayout animalRecent;
    private RelativeLayout firstPageIndicatorLayout;
    private ScrollView herdOverviewPage;
    private TextView pageTitle;
    private SharedPreferences pref;
    private View progressView;
    private RelativeLayout searchAnimal;
    private RelativeLayout secondPageIndicatorLayout;
    private ScrollView smartListPage;
    private Toolbar toolbar;
    private UserCredentials userCredentials;

    class C04941 implements OnClickListener {
        C04941() {
        }

        public void onClick(View v) {
            OfflineManageHerdActivity.this.onBackPressed();
        }
    }

    class C04953 implements OnClickListener {
        C04953() {
        }

        public void onClick(View view) {
            Intent intent = new Intent(OfflineManageHerdActivity.this, AnimalSearchActivity.class);
            intent.putExtra("offline", true);
            OfflineManageHerdActivity.this.startActivity(intent);
        }
    }

    class C04964 implements OnClickListener {
        C04964() {
        }

        public void onClick(View view) {
            Intent intent = new Intent(OfflineManageHerdActivity.this, AnimalRecentActivity.class);
            intent.putExtra("offline", true);
            OfflineManageHerdActivity.this.startActivity(intent);
        }
    }

    class C04975 implements OnClickListener {
        C04975() {
        }

        public void onClick(View view) {
            OfflineManageHerdActivity.this.startActivity(new Intent(OfflineManageHerdActivity.this, OfflineManualInputActivity.class));
        }
    }

    class C04986 implements OnClickListener {
        C04986() {
        }

        public void onClick(View view) {
            OfflineManageHerdActivity.this.setBottomFilter(Integer.valueOf(1));
        }
    }

    class C04997 implements OnClickListener {
        C04997() {
        }

        public void onClick(View view) {
            OfflineManageHerdActivity.this.setBottomFilter(Integer.valueOf(2));
        }
    }

    class C05008 implements OnClickListener {
        C05008() {
        }

        public void onClick(View view) {
            OfflineManageHerdActivity.this.showProgress(true);
            new CheckInternetConnection(OfflineManageHerdActivity.this, OfflineManageHerdActivity.this).execute(new String[0]);
        }
    }

    class C05019 implements OnClickListener {
        C05019() {
        }

        public void onClick(View view) {
            Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
            intent.putExtra("type", 1);
            OfflineManageHerdActivity.this.startActivity(intent);
        }
    }

    class C11012 implements OnMenuItemClickListener {
        C11012() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_offline_manage_herd);
        setupToolbar();
        setupLayout();
        implementListeners();
        setBottomFilter(Integer.valueOf(1));
        onResume();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        this.toolbar.setTitle((CharSequence) "");
        this.toolbar.setNavigationOnClickListener(new C04941());
        this.toolbar.inflateMenu(C0530R.menu.manage_herd);
        this.toolbar.setOnMenuItemClickListener(new C11012());
        this.toolbar.getMenu().findItem(C0530R.id.notification).setVisible(false);
    }

    private void setupLayout() {
        this.animalDbDao = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getAnimalDbDao();
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.searchAnimal = (RelativeLayout) findViewById(C0530R.id.searchAnimal);
        this.animalRecent = (RelativeLayout) findViewById(C0530R.id.animalRecent);
        this.addNewAnimal = (RelativeLayout) findViewById(C0530R.id.addNewAnimal);
        this.firstPageIndicatorLayout = (RelativeLayout) findViewById(C0530R.id.firstPageIndicatorLayout);
        this.secondPageIndicatorLayout = (RelativeLayout) findViewById(C0530R.id.secondPageIndicatorLayout);
        this.pageTitle = (TextView) findViewById(C0530R.id.pageTitle);
        this.smartListPage = (ScrollView) findViewById(C0530R.id.smartListPage);
        this.herdOverviewPage = (ScrollView) findViewById(C0530R.id.herdOverviewPage);
    }

    private void implementListeners() {
        this.searchAnimal.setOnClickListener(new C04953());
        this.animalRecent.setOnClickListener(new C04964());
        this.addNewAnimal.setOnClickListener(new C04975());
        this.firstPageIndicatorLayout.setOnClickListener(new C04986());
        this.secondPageIndicatorLayout.setOnClickListener(new C04997());
        ((RelativeLayout) findViewById(C0530R.id.offlineMenu)).setOnClickListener(new C05008());
    }

    public void setBottomFilter(Integer filter) {
        this.firstPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.secondPageIndicatorLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.smartListPage.setVisibility(8);
        this.herdOverviewPage.setVisibility(8);
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
            default:
                return;
        }
    }

    private void fetchSmartList() {
        showProgress(true);
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
        Long inCalfCow = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        Long inCalfHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(3)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        inCalfTotal.setText(Long.valueOf(inCalfCow.longValue() + inCalfHeifer.longValue()).toString());
        inCalfCows.setText(inCalfCow.toString());
        inCalfHeifers.setText(inCalfHeifer.toString());
        Long inseminatedCow = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(4)), Properties.Type.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        Long inseminatedHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(4)), Properties.Type.eq(Integer.valueOf(3)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        inseminationTotal.setText(Long.valueOf(inseminatedCow.longValue() + inseminatedHeifer.longValue()).toString());
        inseminationCows.setText(inseminatedCow.toString());
        inseminationHeifers.setText(inseminatedHeifer.toString());
        Long cyclingCow = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(2)), Properties.Type.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        Long cyclingHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(2)), Properties.Type.eq(Integer.valueOf(3)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        cyclingTotal.setText(Long.valueOf(cyclingCow.longValue() + cyclingHeifer.longValue()).toString());
        cyclingCows.setText(cyclingCow.toString());
        cyclingHeifers.setText(cyclingHeifer.toString());
        Long inHeatCow = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(3)), Properties.Type.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        Long inHeatHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Last_state.eq(Integer.valueOf(3)), Properties.Type.eq(Integer.valueOf(3)), Properties.Status.eq(Integer.valueOf(1))).orderAsc(Properties.Tag_number).count());
        inHeatTotal.setText(Long.valueOf(inHeatCow.longValue() + inHeatHeifer.longValue()).toString());
        inHeatCows.setText(inHeatCow.toString());
        inHeatHeifers.setText(inHeatHeifer.toString());
        inCalfPage.setOnClickListener(new C05019());
        cyclingPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 2);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        inHeatPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 3);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        inseminationPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 4);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        fetchHerdOverview();
    }

    private void fetchHerdOverview() {
        Long cow = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(1)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long aiBull = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(2)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long breedingHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(3)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long breedingBull = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(4)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long beefHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(5)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long beefBull = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(6)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long calfHeifer = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(7)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long calfBull = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(8)), Properties.Status.eq(Integer.valueOf(1))).count());
        Long bullock = Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Type.eq(Integer.valueOf(9)), Properties.Status.eq(Integer.valueOf(1))).count());
        ((TextView) findViewById(C0530R.id.cowsSize)).setText(cow.toString());
        ((TextView) findViewById(C0530R.id.breedingHeifersSize)).setText(breedingHeifer.toString());
        ((TextView) findViewById(C0530R.id.breedingBullsSize)).setText(breedingBull.toString());
        ((TextView) findViewById(C0530R.id.beefHeifersSize)).setText(beefHeifer.toString());
        ((TextView) findViewById(C0530R.id.beefBullsSize)).setText(beefBull.toString());
        ((TextView) findViewById(C0530R.id.heifersCalvesSize)).setText(calfHeifer.toString());
        ((TextView) findViewById(C0530R.id.bullCalvesSize)).setText(calfBull.toString());
        ((TextView) findViewById(C0530R.id.bullocksSize)).setText(bullock.toString());
        ((TextView) findViewById(C0530R.id.entireHerdTotal)).setText(Long.valueOf(((((((cow.longValue() + breedingBull.longValue()) + breedingHeifer.longValue()) + beefBull.longValue()) + beefHeifer.longValue()) + calfBull.longValue()) + calfHeifer.longValue()) + bullock.longValue()).toString());
        Long breedingHerd = Long.valueOf(cow.longValue() + breedingHeifer.longValue());
        ((TextView) findViewById(C0530R.id.breedingHerdTotal)).setText(breedingHerd.toString());
        QueryBuilder queryBuilder = this.animalDbDao.queryBuilder();
        WhereCondition eq = Properties.Last_state.eq(Integer.valueOf(1));
        WhereCondition[] whereConditionArr = new WhereCondition[2];
        whereConditionArr[0] = Properties.Status.eq(Integer.valueOf(1));
        whereConditionArr[1] = Properties.Type.in(Integer.valueOf(1), Integer.valueOf(3));
        Long inCalf = Long.valueOf(queryBuilder.where(eq, whereConditionArr).count());
        queryBuilder = this.animalDbDao.queryBuilder();
        eq = Properties.Last_state.eq(Integer.valueOf(2));
        whereConditionArr = new WhereCondition[2];
        whereConditionArr[0] = Properties.Status.eq(Integer.valueOf(1));
        whereConditionArr[1] = Properties.Type.in(Integer.valueOf(1), Integer.valueOf(3));
        Long cycle = Long.valueOf(queryBuilder.where(eq, whereConditionArr).count());
        queryBuilder = this.animalDbDao.queryBuilder();
        eq = Properties.Last_state.eq(Integer.valueOf(5));
        whereConditionArr = new WhereCondition[2];
        whereConditionArr[0] = Properties.Status.eq(Integer.valueOf(1));
        whereConditionArr[1] = Properties.Type.in(Integer.valueOf(1), Integer.valueOf(3));
        Long fresh = Long.valueOf(queryBuilder.where(eq, whereConditionArr).count());
        queryBuilder = this.animalDbDao.queryBuilder();
        eq = Properties.Last_state.eq(Integer.valueOf(4));
        whereConditionArr = new WhereCondition[2];
        whereConditionArr[0] = Properties.Status.eq(Integer.valueOf(1));
        whereConditionArr[1] = Properties.Type.in(Integer.valueOf(1), Integer.valueOf(3));
        Long inseminated = Long.valueOf(queryBuilder.where(eq, whereConditionArr).count());
        TextView inCalfPercent = (TextView) findViewById(C0530R.id.inCalfPercent);
        TextView inseminatedPercent = (TextView) findViewById(C0530R.id.inseminatedPercent);
        TextView freshPercent = (TextView) findViewById(C0530R.id.freshPercent);
        TextView cyclePercent = (TextView) findViewById(C0530R.id.cyclePercent);
        TextView inHeatPercent = (TextView) findViewById(C0530R.id.inHeatPercent);
        ((TextView) findViewById(C0530R.id.breedingHerdCH)).setText(breedingHerd.toString());
        DonutChart allHerdChart = (DonutChart) findViewById(C0530R.id.allHerdChart);
        ArrayList<Double> alPercentage = new ArrayList();
        alPercentage.add(Double.valueOf(Long.valueOf(Math.round((inCalf.doubleValue() * 100.0d) / breedingHerd.doubleValue())).doubleValue()));
        alPercentage.add(Double.valueOf(Long.valueOf(Math.round((inseminated.doubleValue() * 100.0d) / breedingHerd.doubleValue())).doubleValue()));
        alPercentage.add(Double.valueOf(Long.valueOf(Math.round((fresh.doubleValue() * 100.0d) / breedingHerd.doubleValue())).doubleValue()));
        alPercentage.add(Double.valueOf(Long.valueOf(Math.round((cycle.doubleValue() * 100.0d) / breedingHerd.doubleValue())).doubleValue()));
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
        ((TextView) findViewById(C0530R.id.breedingTotal)).setText(breedingHerd.toString());
        ((TextView) findViewById(C0530R.id.bullsTotal)).setText(Long.valueOf(aiBull.longValue() + breedingBull.longValue()).toString());
        ((TextView) findViewById(C0530R.id.calvesTotal)).setText(Long.valueOf(calfHeifer.longValue() + calfBull.longValue()).toString());
        ((TextView) findViewById(C0530R.id.beefCattleTotal)).setText(Long.valueOf(beefHeifer.longValue() + beefBull.longValue()).toString());
        ((TextView) findViewById(C0530R.id.historicTotal)).setText(Long.valueOf(this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(4)), new WhereCondition[0]).count()).toString());
        ((TextView) findViewById(C0530R.id.breedingCows)).setText(cow.toString());
        ((TextView) findViewById(C0530R.id.breedingHeifers)).setText(breedingHeifer.toString());
        ((TextView) findViewById(C0530R.id.breedingBulls)).setText(breedingBull.toString());
        ((TextView) findViewById(C0530R.id.aiBulls)).setText(aiBull.toString());
        ((TextView) findViewById(C0530R.id.heiferCalves)).setText(calfHeifer.toString());
        ((TextView) findViewById(C0530R.id.bullCalves)).setText(calfBull.toString());
        ((TextView) findViewById(C0530R.id.heiferBeef)).setText(beefHeifer.toString());
        ((TextView) findViewById(C0530R.id.bullBeef)).setText(beefBull.toString());
        TextView historicCalves = (TextView) findViewById(C0530R.id.historicCalves);
        queryBuilder = this.animalDbDao.queryBuilder();
        eq = Properties.Status.eq(Integer.valueOf(4));
        whereConditionArr = new WhereCondition[1];
        whereConditionArr[0] = Properties.Type.in(Integer.valueOf(7), Integer.valueOf(8));
        historicCalves.setText(Long.valueOf(queryBuilder.where(eq, whereConditionArr).count()).toString());
        TextView historicBeef = (TextView) findViewById(C0530R.id.historicBeef);
        queryBuilder = this.animalDbDao.queryBuilder();
        eq = Properties.Status.eq(Integer.valueOf(4));
        whereConditionArr = new WhereCondition[1];
        whereConditionArr[0] = Properties.Type.in(Integer.valueOf(5), Integer.valueOf(6));
        historicBeef.setText(Long.valueOf(queryBuilder.where(eq, whereConditionArr).count()).toString());
        breedingPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 9);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        bullsPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 5);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        calvesPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 6);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        beefCattlePage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 7);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        historicPage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineManageHerdActivity.this, OfflineSmartListActivity.class);
                intent.putExtra("type", 8);
                OfflineManageHerdActivity.this.startActivity(intent);
            }
        });
        showProgress(false);
    }

    public boolean scan(MenuItem view) {
        startActivity(new Intent(this, ScanMoocallTagActivity.class).putExtra("offline", true));
        return true;
    }

    public boolean notification(MenuItem view) {
        return true;
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        fetchSmartList();
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void onCheckInternetCompleted(Boolean hasInternet) {
        if (!hasInternet.booleanValue()) {
            showProgress(false);
            Toast.makeText(this, getString(C0530R.string.internet_no), 1).show();
        } else if (hasStoredCredentials().booleanValue()) {
            initCredentials();
            SyncOfflineAsync syncOfflineAsync = new SyncOfflineAsync(this, this.userCredentials);
        } else {
            showProgress(false);
            StorageContainer.removeCredentialsFromPreferences(getApplicationContext());
        }
    }

    private void initCredentials() {
        this.userCredentials = new UserCredentials(this.pref.getString("username", null), this.pref.getString("password", null));
        Account.setUsername(this.pref.getString("username", null));
        Account.setPassword(this.pref.getString("password", null));
    }

    private Boolean hasStoredCredentials() {
        this.pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        StorageContainer.credentialSetup(this.pref);
        if (this.pref.getString("username", null) != null) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
