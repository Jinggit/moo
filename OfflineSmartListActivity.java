package com.moocall.moocall;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.moocall.moocall.adapter.OfflineSmartListViewAdapter;
import com.moocall.moocall.async.CheckInternetConnection;
import com.moocall.moocall.async.SyncOfflineAsync;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.db.SensorDb;
import com.moocall.moocall.db.SensorDbDao;
import com.moocall.moocall.db.StateHistoryDb;
import com.moocall.moocall.db.StateHistoryDbDao;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.UserCredentials;
import com.moocall.moocall.interfaces.OnCheckInternetCompleted;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.WhereCondition;

public class OfflineSmartListActivity extends AppCompatActivity implements OnCheckInternetCompleted {
    private Integer actionAdded;
    private ScrollView actionOverview;
    private AnimalActionDbDao animalActionDbDao;
    private Calendar animalCalendar = Calendar.getInstance();
    private AnimalDbDao animalDbDao;
    private Integer animalInListAction;
    private ArrayList<AnimalDb> animalList;
    private List<String> assumeList;
    private Integer bottomFilter;
    private Boolean breedingBull = Boolean.valueOf(true);
    private String bullId = "";
    private List<String> bullList;
    private HashMap<String, Integer> bullListHash;
    private List<String> bullSensorList;
    private TextView cancelAction;
    private String cowId = "";
    private List<String> cowList;
    private HashMap<String, Integer> cowListHash;
    private List<String> cowSensorList;
    private RelativeLayout firstAnimalFilterLayout;
    private ImageView firstFilterImage;
    private RelativeLayout firstFilterLayout;
    private TextView firstFilterText;
    private RelativeLayout fourthAnimalFilterLayout;
    private TextView fourthAnimalFilterText;
    private List<String> heatList;
    private SharedPreferences pref;
    private View progressView;
    private Drawable radioButton;
    private RelativeLayout secondAnimalFilterLayout;
    private TextView secondAnimalFilterText;
    private ImageView secondFilterImage;
    private RelativeLayout secondFilterLayout;
    private TextView secondFilterText;
    private SensorDbDao sensorDbDao;
    private OfflineSmartListViewAdapter smartListAdapter;
    private SwipeRefreshLayout smartListSwipeRefreshLayout;
    private TextView smartListTitle;
    private ListView smartListView;
    private int textColor;
    private RelativeLayout thirdAnimalFilterLayout;
    private TextView thirdAnimalFilterText;
    private ImageView thirdFilterImage;
    private RelativeLayout thirdFilterLayout;
    private TextView thirdFilterText;
    private Toolbar toolbar;
    private Integer topFilter;
    private RelativeLayout transparentBackground;
    private Integer type;
    private UserCredentials userCredentials;

    class C05111 implements OnClickListener {
        C05111() {
        }

        public void onClick(View v) {
            OfflineSmartListActivity.this.onBackPressed();
        }
    }

    class C05122 implements OnClickListener {
        C05122() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.topFilter = Integer.valueOf(1);
            OfflineSmartListActivity.this.setTopFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05133 implements OnClickListener {
        C05133() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.topFilter = Integer.valueOf(2);
            OfflineSmartListActivity.this.setTopFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05144 implements OnClickListener {
        C05144() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.topFilter = Integer.valueOf(3);
            OfflineSmartListActivity.this.setTopFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05155 implements OnClickListener {
        C05155() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.bottomFilter = Integer.valueOf(1);
            OfflineSmartListActivity.this.setBottomFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05166 implements OnClickListener {
        C05166() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.bottomFilter = Integer.valueOf(2);
            OfflineSmartListActivity.this.setBottomFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05177 implements OnClickListener {
        C05177() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.bottomFilter = Integer.valueOf(3);
            OfflineSmartListActivity.this.setBottomFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05188 implements OnClickListener {
        C05188() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.bottomFilter = Integer.valueOf(4);
            OfflineSmartListActivity.this.setBottomFilter();
            OfflineSmartListActivity.this.fetchAnimals();
        }
    }

    class C05199 implements OnClickListener {
        C05199() {
        }

        public void onClick(View view) {
            OfflineSmartListActivity.this.closeOverlay();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_offline_smart_list);
        this.type = (Integer) getIntent().getSerializableExtra("type");
        this.topFilter = Integer.valueOf(1);
        this.bottomFilter = Integer.valueOf(1);
        if (this.type.intValue() < 5) {
            this.topFilter = Integer.valueOf(2);
        }
        setupToolbar();
        setupLayout();
        implementsListeners();
        initLists();
        setSmartListType();
        setTopFilter();
        setBottomFilter();
        onResume();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        this.toolbar.setTitle((CharSequence) "");
        this.toolbar.setNavigationOnClickListener(new C05111());
    }

    private void setupLayout() {
        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
        this.animalDbDao = daoSession.getAnimalDbDao();
        this.sensorDbDao = daoSession.getSensorDbDao();
        this.animalActionDbDao = daoSession.getAnimalActionDbDao();
        this.smartListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.smartListSwipeRefreshLayout);
        this.smartListView = (ListView) findViewById(C0530R.id.smartListView);
        this.animalList = new ArrayList();
        this.smartListAdapter = new OfflineSmartListViewAdapter(this, this.animalList, this.type, this.topFilter);
        this.smartListView.setAdapter(this.smartListAdapter);
        this.smartListTitle = (TextView) findViewById(C0530R.id.smartListTitle);
        this.firstFilterLayout = (RelativeLayout) findViewById(C0530R.id.firstFilterLayout);
        this.secondFilterLayout = (RelativeLayout) findViewById(C0530R.id.secondFilterLayout);
        this.thirdFilterLayout = (RelativeLayout) findViewById(C0530R.id.thirdFilterLayout);
        this.firstFilterText = (TextView) findViewById(C0530R.id.firstFilterText);
        this.secondFilterText = (TextView) findViewById(C0530R.id.secondFilterText);
        this.thirdFilterText = (TextView) findViewById(C0530R.id.thirdFilterText);
        this.firstFilterImage = (ImageView) findViewById(C0530R.id.firstFilterImage);
        this.secondFilterImage = (ImageView) findViewById(C0530R.id.secondFilterImage);
        this.thirdFilterImage = (ImageView) findViewById(C0530R.id.thirdFilterImage);
        this.firstAnimalFilterLayout = (RelativeLayout) findViewById(C0530R.id.firstAnimalFilterLayout);
        this.secondAnimalFilterLayout = (RelativeLayout) findViewById(C0530R.id.secondAnimalFilterLayout);
        this.thirdAnimalFilterLayout = (RelativeLayout) findViewById(C0530R.id.thirdAnimalFilterLayout);
        this.fourthAnimalFilterLayout = (RelativeLayout) findViewById(C0530R.id.fourthAnimalFilterLayout);
        this.secondAnimalFilterText = (TextView) findViewById(C0530R.id.secondAnimalFilterText);
        this.thirdAnimalFilterText = (TextView) findViewById(C0530R.id.thirdAnimalFilterText);
        this.fourthAnimalFilterText = (TextView) findViewById(C0530R.id.fourthAnimalFilterText);
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.actionOverview = (ScrollView) findViewById(C0530R.id.actionOverview);
        this.transparentBackground = (RelativeLayout) findViewById(C0530R.id.transparentBackground);
        this.cancelAction = (TextView) findViewById(C0530R.id.cancelAction);
    }

    private void initLists() {
        this.heatList = Arrays.asList(new String[]{getString(C0530R.string.observed_standing_mount), getString(C0530R.string.slime_on_vulva), getString(C0530R.string.physical_signs_mounting), getString(C0530R.string.chin_ball_tail_paint), getString(C0530R.string.observed_heat_behaviour), getString(C0530R.string.assumed_heat), getString(C0530R.string.teaser_bull_activity)});
        this.cowSensorList = new ArrayList();
        this.bullSensorList = new ArrayList();
        this.assumeList = Arrays.asList(new String[]{getString(C0530R.string.assumed_in_calf), getString(C0530R.string.scanned_in_calf), getString(C0530R.string.scanned_in_calf_twins)});
        this.bullList = new ArrayList();
        this.bullListHash = new HashMap();
        this.bullList.add(getString(C0530R.string.add_new));
        List<AnimalDb> bullBreedingList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(4))).orderAsc(Properties.Tag_number).list();
        List<AnimalDb> bullAIList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(2))).orderAsc(Properties.Tag_number).list();
        if (this.breedingBull.booleanValue()) {
            if (bullBreedingList != null) {
                for (AnimalDb bullInList : bullBreedingList) {
                    this.bullList.add(bullInList.getTag_number());
                    this.bullListHash.put(bullInList.getTag_number(), bullInList.getAnimal_id());
                }
            }
        } else if (bullAIList != null) {
            for (AnimalDb bullInList2 : bullAIList) {
                this.bullList.add(bullInList2.getTag_number());
                this.bullListHash.put(bullInList2.getTag_number(), bullInList2.getAnimal_id());
            }
        }
        List<SensorDb> cowSensorsList = this.sensorDbDao.queryBuilder().where(SensorDbDao.Properties.Type.eq(Integer.valueOf(0)), new WhereCondition[0]).list();
        List<SensorDb> bullSensorsList = this.sensorDbDao.queryBuilder().where(SensorDbDao.Properties.Type.eq(Integer.valueOf(1)), new WhereCondition[0]).list();
        if (cowSensorsList != null) {
            for (SensorDb sensorInList : cowSensorsList) {
                this.cowSensorList.add(sensorInList.getDevice_code());
            }
        }
        if (bullSensorsList != null) {
            for (SensorDb sensorInList2 : bullSensorsList) {
                this.bullSensorList.add(sensorInList2.getDevice_code());
            }
        }
        this.cowList = new ArrayList();
        this.cowListHash = new HashMap();
        this.cowList.add(getString(C0530R.string.add_new));
        List<AnimalDb> cowsList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), new WhereCondition[0]).whereOr(Properties.Type.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(3)), new WhereCondition[0]).orderAsc(Properties.Tag_number).list();
        if (cowsList != null) {
            for (AnimalDb cowInList : cowsList) {
                this.cowList.add(cowInList.getTag_number());
                this.cowListHash.put(cowInList.getTag_number(), cowInList.getAnimal_id());
            }
        }
    }

    public void implementsListeners() {
        this.firstFilterLayout.setOnClickListener(new C05122());
        this.secondFilterLayout.setOnClickListener(new C05133());
        this.thirdFilterLayout.setOnClickListener(new C05144());
        this.firstAnimalFilterLayout.setOnClickListener(new C05155());
        this.secondAnimalFilterLayout.setOnClickListener(new C05166());
        this.thirdAnimalFilterLayout.setOnClickListener(new C05177());
        this.fourthAnimalFilterLayout.setOnClickListener(new C05188());
        this.cancelAction.setOnClickListener(new C05199());
        this.smartListSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (OfflineSmartListActivity.this.smartListSwipeRefreshLayout.isRefreshing()) {
                    OfflineSmartListActivity.this.fetchAnimals();
                }
            }
        });
        ((RelativeLayout) findViewById(C0530R.id.offlineMenu)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineSmartListActivity.this.showProgress(true);
                new CheckInternetConnection(OfflineSmartListActivity.this, OfflineSmartListActivity.this).execute(new String[0]);
            }
        });
    }

    public void openAnimalDetails(AnimalDb animal) {
        Intent intent = new Intent(this, OfflineAnimalDetailsActivity.class);
        intent.putExtra("id", animal.getId());
        intent.putExtra("subtype", this.type);
        startActivity(intent);
    }

    private void closeOverlay() {
        this.transparentBackground.setVisibility(8);
        this.actionOverview.setVisibility(8);
        Utils.hideKeyboard(this);
    }

    public void setSmartListType() {
        switch (this.type.intValue()) {
            case 1:
                this.radioButton = getResources().getDrawable(C0530R.drawable.green_radio);
                this.textColor = getResources().getColor(C0530R.color.green_mh);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.all_in_calf));
                this.secondFilterText.setText(getString(C0530R.string.due_next_7_days));
                this.thirdFilterText.setText(getString(C0530R.string.calved_last_90_days));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.cows));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.heifers));
                return;
            case 2:
                this.radioButton = getResources().getDrawable(C0530R.drawable.orange_radio);
                this.textColor = getResources().getColor(C0530R.color.orange_mh);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.all_cycled_last_90_days));
                this.secondFilterText.setText(getString(C0530R.string.cycled_24_days_ago));
                this.thirdFilterText.setText(getString(C0530R.string.cycled_45_days_ago));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.cows));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.heifers));
                return;
            case 3:
                this.radioButton = getResources().getDrawable(C0530R.drawable.red_radio);
                this.textColor = getResources().getColor(C0530R.color.red_mh);
                this.thirdFilterLayout.setVisibility(8);
                this.firstFilterText.setText(getString(C0530R.string.all_heats_last_90_days));
                this.secondFilterText.setText(getString(C0530R.string.in_heat_last_24_hours));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.cows));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.heifers));
                return;
            case 4:
                this.radioButton = getResources().getDrawable(C0530R.drawable.blue_radio);
                this.textColor = getResources().getColor(C0530R.color.blue_mh);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.all_inseminations_last_90_days));
                this.secondFilterText.setText(getString(C0530R.string.inseminated_24_days_ago));
                this.thirdFilterText.setText(getString(C0530R.string.inseminated_45days_ago));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.cows));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.heifers));
                return;
            case 5:
                this.radioButton = getResources().getDrawable(C0530R.drawable.dark_green_radio);
                this.textColor = getResources().getColor(C0530R.color.dark_green);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.by_age));
                this.secondFilterText.setText(getString(C0530R.string.by_weight));
                this.thirdFilterText.setText(getString(C0530R.string.by_tag));
                this.fourthAnimalFilterLayout.setVisibility(0);
                this.secondAnimalFilterText.setText(getString(C0530R.string.breeding_bulls));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.ai_bulls));
                this.fourthAnimalFilterText.setText(getString(C0530R.string.vasectomized_bulls));
                return;
            case 6:
                this.radioButton = getResources().getDrawable(C0530R.drawable.dark_green_radio);
                this.textColor = getResources().getColor(C0530R.color.dark_green);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.by_age));
                this.secondFilterText.setText(getString(C0530R.string.by_weight));
                this.thirdFilterText.setText(getString(C0530R.string.by_tag));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.heifers));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.bulls));
                return;
            case 7:
                this.radioButton = getResources().getDrawable(C0530R.drawable.dark_green_radio);
                this.textColor = getResources().getColor(C0530R.color.dark_green);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.by_age));
                this.secondFilterText.setText(getString(C0530R.string.by_weight));
                this.thirdFilterText.setText(getString(C0530R.string.by_tag));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.heifers));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.bulls));
                return;
            case 8:
                this.radioButton = getResources().getDrawable(C0530R.drawable.dark_green_radio);
                this.textColor = getResources().getColor(C0530R.color.dark_green);
                this.thirdFilterLayout.setVisibility(8);
                this.firstFilterText.setText(getString(C0530R.string.by_date_culled));
                this.secondFilterText.setText(getString(C0530R.string.by_date_sold));
                this.thirdFilterText.setText(getString(C0530R.string.by_tag));
                this.fourthAnimalFilterLayout.setVisibility(0);
                this.secondAnimalFilterText.setText(getString(C0530R.string.cows) + " / " + getString(C0530R.string.heifers));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.calves));
                this.fourthAnimalFilterText.setText(getString(C0530R.string.bulls));
                return;
            case 9:
                this.radioButton = getResources().getDrawable(C0530R.drawable.dark_green_radio);
                this.textColor = getResources().getColor(C0530R.color.dark_green);
                this.thirdFilterLayout.setVisibility(0);
                this.firstFilterText.setText(getString(C0530R.string.by_age));
                this.secondFilterText.setText(getString(C0530R.string.by_weight));
                this.thirdFilterText.setText(getString(C0530R.string.by_tag));
                this.fourthAnimalFilterLayout.setVisibility(8);
                this.secondAnimalFilterText.setText(getString(C0530R.string.cows));
                this.thirdAnimalFilterText.setText(getString(C0530R.string.heifers));
                return;
            default:
                return;
        }
    }

    public void setBottomFilter() {
        this.firstAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.secondAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.thirdAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        this.fourthAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_gray_mh));
        switch (this.bottomFilter.intValue()) {
            case 1:
                this.firstAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                break;
            case 2:
                this.secondAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                break;
            case 3:
                this.thirdAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                break;
            case 4:
                this.fourthAnimalFilterLayout.setBackgroundColor(getResources().getColor(C0530R.color.dark_green));
                break;
        }
        setListTitle();
    }

    public void setTopFilter() {
        this.firstFilterText.setTextColor(getResources().getColor(C0530R.color.gray_mh));
        this.secondFilterText.setTextColor(getResources().getColor(C0530R.color.gray_mh));
        this.thirdFilterText.setTextColor(getResources().getColor(C0530R.color.gray_mh));
        this.firstFilterImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.grey_radio));
        this.secondFilterImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.grey_radio));
        this.thirdFilterImage.setImageDrawable(getResources().getDrawable(C0530R.drawable.grey_radio));
        switch (this.topFilter.intValue()) {
            case 1:
                this.firstFilterText.setTextColor(this.textColor);
                this.firstFilterImage.setImageDrawable(this.radioButton);
                break;
            case 2:
                this.secondFilterText.setTextColor(this.textColor);
                this.secondFilterImage.setImageDrawable(this.radioButton);
                break;
            case 3:
                this.thirdFilterText.setTextColor(this.textColor);
                this.thirdFilterImage.setImageDrawable(this.radioButton);
                break;
        }
        this.smartListAdapter.setFilter(this.topFilter);
        setListTitle();
    }

    private void setListTitle() {
        String titleFirst = "";
        String titleSecond = "";
        switch (this.type.intValue()) {
            case 1:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.cows);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.heifers);
                        break;
                }
                switch (this.topFilter.intValue()) {
                    case 1:
                        titleSecond = getString(C0530R.string.incalf_small);
                        break;
                    case 2:
                        titleSecond = getString(C0530R.string.incalf_due7days);
                        break;
                    case 3:
                        titleSecond = getString(C0530R.string.calved_last90days);
                        break;
                    default:
                        break;
                }
            case 2:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.cows);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.heifers);
                        break;
                }
                switch (this.topFilter.intValue()) {
                    case 1:
                        titleSecond = getString(C0530R.string.cycled_last90days);
                        break;
                    case 2:
                        titleSecond = getString(C0530R.string.cycled1724days);
                        break;
                    case 3:
                        titleSecond = getString(C0530R.string.cycled3845days);
                        break;
                    default:
                        break;
                }
            case 3:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.cows);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.heifers);
                        break;
                }
                switch (this.topFilter.intValue()) {
                    case 1:
                        titleSecond = getString(C0530R.string.inheat_last90days);
                        break;
                    case 2:
                        titleSecond = getString(C0530R.string.inheat_last24hours);
                        break;
                    default:
                        break;
                }
            case 4:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.cows);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.heifers);
                        break;
                }
                switch (this.topFilter.intValue()) {
                    case 1:
                        titleSecond = getString(C0530R.string.inseminated_last90days);
                        break;
                    case 2:
                        titleSecond = getString(C0530R.string.inseminated1724days);
                        break;
                    case 3:
                        titleSecond = getString(C0530R.string.inseminated3845days);
                        break;
                    default:
                        break;
                }
            case 5:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all_bulls);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.all_breeding_bulls);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.all_aistraw_bulls);
                        break;
                    case 4:
                        titleFirst = getString(C0530R.string.all_vasectomized_bulls);
                        break;
                    default:
                        break;
                }
            case 6:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all_calves);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.all_heifer_calves);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.all_bull_calves);
                        break;
                    default:
                        break;
                }
            case 7:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all_beef_cattle);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.beef_heifers);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.beef_bulls);
                        break;
                    default:
                        break;
                }
            case 8:
                titleFirst = getString(C0530R.string.historic_animal_list);
                break;
            case 9:
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        titleFirst = getString(C0530R.string.all_breeding);
                        break;
                    case 2:
                        titleFirst = getString(C0530R.string.all_cows);
                        break;
                    case 3:
                        titleFirst = getString(C0530R.string.all_breeding_heifers);
                        break;
                    default:
                        break;
                }
        }
        this.smartListTitle.setText(titleFirst + StringUtils.SPACE + titleSecond);
    }

    public void fetchAnimals() {
        if (!this.smartListSwipeRefreshLayout.isRefreshing()) {
            showProgress(true);
        }
        List<AnimalDb> list = getAnimalList();
        this.animalList.clear();
        for (AnimalDb animal : list) {
            this.animalList.add(animal);
        }
        if (this.smartListAdapter.getAnimation() != null) {
            this.smartListAdapter.getAnimation().cancel();
            this.smartListView.setBackgroundColor(getResources().getColor(C0530R.color.white));
        }
        this.smartListAdapter.notifyDataSetChanged();
        showProgress(false);
        this.smartListSwipeRefreshLayout.setRefreshing(false);
        this.smartListAdapter.notifyDataSetChanged();
        showProgress(false);
    }

    public void clickedAction(Integer position, Boolean right) {
        this.animalInListAction = position;
        if (position.intValue() < this.animalList.size()) {
            AnimalDb animal = (AnimalDb) this.animalList.get(position.intValue());
            switch (this.type.intValue()) {
                case 1:
                    switch (this.topFilter.intValue()) {
                        case 3:
                            showActionOverview(Integer.valueOf(3), animal);
                            break;
                        default:
                            if (!right.booleanValue()) {
                                showActionOverview(Integer.valueOf(0), animal);
                                break;
                            } else {
                                addCalving(animal);
                                break;
                            }
                    }
                case 2:
                    if (!right.booleanValue()) {
                        showActionOverview(Integer.valueOf(3), animal);
                        break;
                    } else {
                        showActionOverview(Integer.valueOf(4), animal);
                        break;
                    }
                case 3:
                    showActionOverview(Integer.valueOf(4), animal);
                    break;
                case 4:
                    if (!right.booleanValue()) {
                        showActionOverview(Integer.valueOf(3), animal);
                        break;
                    } else {
                        showActionOverview(Integer.valueOf(10), animal);
                        break;
                    }
                case 5:
                    if (!right.booleanValue()) {
                        showActionOverview(Integer.valueOf(17), animal);
                        break;
                    } else {
                        showActionOverview(Integer.valueOf(15), animal);
                        break;
                    }
                case 6:
                    if (!right.booleanValue()) {
                        showActionOverview(Integer.valueOf(5), animal);
                        break;
                    } else {
                        showActionOverview(Integer.valueOf(9), animal);
                        break;
                    }
                case 7:
                    if (!right.booleanValue()) {
                        showActionOverview(Integer.valueOf(9), animal);
                        break;
                    } else {
                        showActionOverview(Integer.valueOf(2), animal);
                        break;
                    }
                case 8:
                    showActionOverview(Integer.valueOf(11), animal);
                    break;
                case 9:
                    if (!right.booleanValue()) {
                        showActionOverview(Integer.valueOf(4), animal);
                        break;
                    } else {
                        addCalving(animal);
                        break;
                    }
            }
        }
        this.smartListAdapter.swipeLayout.close();
        this.smartListAdapter.notifyDataSetChanged();
    }

    public void showActionOverview(Integer action, AnimalDb animal) {
        this.transparentBackground.setVisibility(0);
        this.actionOverview.setVisibility(0);
        TextView actionHeadline = (TextView) findViewById(C0530R.id.actionHeadline);
        LinearLayout firstActionsLayout = (LinearLayout) findViewById(C0530R.id.firstActionsLayout);
        TextView firstActionText = (TextView) findViewById(C0530R.id.firstActionText);
        TextView secondActionText = (TextView) findViewById(C0530R.id.secondActionText);
        final TextView firstActionSelect = (TextView) findViewById(C0530R.id.firstActionSelect);
        final EditText firstActionWeight = (EditText) findViewById(C0530R.id.firstActionWeight);
        final TextView secondActionSelect = (TextView) findViewById(C0530R.id.secondActionSelect);
        final EditText secondActionPrice = (EditText) findViewById(C0530R.id.secondActionPrice);
        LinearLayout secondActionsLayout = (LinearLayout) findViewById(C0530R.id.secondActionsLayout);
        LinearLayout inseminationRadio = (LinearLayout) findViewById(C0530R.id.inseminationRadio);
        RelativeLayout firstInseminationRadio = (RelativeLayout) findViewById(C0530R.id.firstInseminationRadio);
        RelativeLayout secondInseminationRadio = (RelativeLayout) findViewById(C0530R.id.secondInseminationRadio);
        ImageView firstInseminationFilterImage = (ImageView) findViewById(C0530R.id.firstInseminationFilterImage);
        ImageView secondInseminationFilterImage = (ImageView) findViewById(C0530R.id.secondInseminationFilterImage);
        TextView firstInseminationFilterText = (TextView) findViewById(C0530R.id.firstInseminationFilterText);
        TextView secondInseminationFilterText = (TextView) findViewById(C0530R.id.secondInseminationFilterText);
        TextView thirdActionText = (TextView) findViewById(C0530R.id.thirdActionText);
        final TextView thirdActionSelect = (TextView) findViewById(C0530R.id.thirdActionSelect);
        TextView okAction = (TextView) findViewById(C0530R.id.okAction);
        TextView actionFirstText = (TextView) findViewById(C0530R.id.actionFirstText);
        final EditText thirdActionEdit = (EditText) findViewById(C0530R.id.thirdActionEdit);
        TextView actionSecondText = (TextView) findViewById(C0530R.id.actionSecondText);
        firstActionsLayout.setVisibility(8);
        firstActionText.setVisibility(8);
        secondActionText.setVisibility(8);
        firstActionSelect.setText("");
        firstActionSelect.setVisibility(8);
        firstActionWeight.setVisibility(8);
        firstActionWeight.setText("");
        secondActionSelect.setVisibility(8);
        secondActionSelect.setText("");
        secondActionPrice.setVisibility(8);
        secondActionPrice.setText("xxxxx");
        secondActionsLayout.setVisibility(8);
        inseminationRadio.setVisibility(8);
        firstInseminationRadio.setVisibility(8);
        secondInseminationRadio.setVisibility(8);
        thirdActionText.setVisibility(8);
        thirdActionSelect.setText("");
        thirdActionSelect.setVisibility(8);
        thirdActionEdit.setVisibility(8);
        thirdActionEdit.setText("");
        actionFirstText.setVisibility(8);
        actionSecondText.setVisibility(8);
        switch (action.intValue()) {
            case 0:
                actionHeadline.setText(getString(C0530R.string.add_calving_sensor));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.dark_gray_mh));
                actionFirstText.setVisibility(0);
                actionFirstText.setText(getString(C0530R.string.add_sensor_first_text));
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.calving_sensor_number));
                thirdActionSelect.setVisibility(0);
                setDropdown(thirdActionSelect, 2);
                break;
            case 2:
                actionHeadline.setText(getString(C0530R.string.add_weight));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.dark_green));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.weight));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.date));
                firstActionWeight.setVisibility(0);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(secondActionSelect, animal, action);
                break;
            case 3:
                actionHeadline.setText(getString(C0530R.string.add_heat));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, animal, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, animal, action);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.description));
                thirdActionSelect.setVisibility(0);
                setDropdown(thirdActionSelect, 1);
                break;
            case 4:
                actionHeadline.setText(getString(C0530R.string.add_insemination));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.blue_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, animal, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, animal, action);
                secondActionsLayout.setVisibility(0);
                inseminationRadio.setVisibility(0);
                firstInseminationRadio.setVisibility(0);
                secondInseminationRadio.setVisibility(0);
                setInseminationRadioClick(firstInseminationRadio, firstInseminationFilterText, firstInseminationFilterImage, secondInseminationRadio, secondInseminationFilterText, secondInseminationFilterImage);
                thirdActionSelect.setVisibility(0);
                thirdActionSelect.setText(getString(C0530R.string.choose_add_new));
                setDropdown(thirdActionSelect, 5);
                break;
            case 5:
                actionHeadline.setText(getString(C0530R.string.mark_as_replacement));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                actionFirstText.setVisibility(0);
                actionFirstText.setText(getString(C0530R.string.mark_as_replacment_first_text));
                actionSecondText.setVisibility(0);
                actionSecondText.setText(getString(C0530R.string.mark_as_replacment_second_text));
                break;
            case 9:
                actionHeadline.setText(getString(C0530R.string.sold));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                actionFirstText.setVisibility(0);
                actionFirstText.setText(getString(C0530R.string.sold_first_text));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.weight));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.price));
                firstActionWeight.setVisibility(0);
                secondActionPrice.setVisibility(0);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.description));
                thirdActionEdit.setVisibility(0);
                thirdActionEdit.setInputType(1);
                break;
            case 10:
                actionHeadline.setText(getString(C0530R.string.assume_in_calf));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.green_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.service_date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.service_time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, animal, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, animal, action);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.description));
                thirdActionSelect.setVisibility(0);
                setDropdown(thirdActionSelect, 3);
                break;
            case 11:
                actionHeadline.setText(getString(C0530R.string.undo_last_action));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.black));
                actionFirstText.setVisibility(0);
                actionFirstText.setText(getString(C0530R.string.undo_first_text));
                break;
            case 15:
                actionHeadline.setText(getString(C0530R.string.add_insemination));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.blue_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, animal, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, animal, action);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.choose_cow));
                thirdActionSelect.setVisibility(0);
                thirdActionSelect.setText(getString(C0530R.string.choose_add_new));
                setDropdown(thirdActionSelect, 6);
                break;
            case 17:
                actionHeadline.setText(getString(C0530R.string.add_calving_sensor));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.dark_gray_mh));
                actionFirstText.setVisibility(0);
                actionFirstText.setText(getString(C0530R.string.add_bull_sensor_first_text));
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.bull_sensor_number));
                thirdActionSelect.setVisibility(0);
                setDropdown(thirdActionSelect, 7);
                break;
        }
        final Integer num = action;
        final AnimalDb animalDb = animal;
        okAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineSmartListActivity.this.prepareSaveAction(num, firstActionSelect, firstActionWeight, secondActionSelect, secondActionPrice, thirdActionSelect, thirdActionEdit, animalDb);
            }
        });
    }

    private void setDatePicker(final TextView tv, AnimalDb animal, Integer action) {
        final OnDateSetListener date = new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthText = "";
                String dateText = "";
                if (dayOfMonth < 10) {
                    dateText = "0" + dayOfMonth;
                } else {
                    dateText = dayOfMonth + "";
                }
                if (monthOfYear < 9) {
                    monthText = "0" + (monthOfYear + 1);
                } else {
                    monthText = (monthOfYear + 1) + "";
                }
                tv.setText(year + "-" + monthText + "-" + dateText);
            }
        };
        tv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(OfflineSmartListActivity.this, date, OfflineSmartListActivity.this.animalCalendar.get(1), OfflineSmartListActivity.this.animalCalendar.get(2), OfflineSmartListActivity.this.animalCalendar.get(5));
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });
        String year = "";
        String monthText = "";
        String dateText = "";
        if (action.intValue() != 10 || animal.getInsemination_date() == null || animal.getInsemination_date() == "0000-00-00 00:00:00") {
            year = Integer.toString(this.animalCalendar.get(1));
            int monthOfYear = this.animalCalendar.get(2);
            int dayOfMonth = this.animalCalendar.get(5);
            if (dayOfMonth < 10) {
                dateText = "0" + dayOfMonth;
            } else {
                dateText = dayOfMonth + "";
            }
            if (monthOfYear < 9) {
                monthText = "0" + (monthOfYear + 1);
            } else {
                monthText = (monthOfYear + 1) + "";
            }
        } else {
            String[] dateParts = Utils.calculateTime(animal.getInsemination_date(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[0].split("-");
            year = dateParts[2];
            monthText = dateParts[1];
            dateText = dateParts[0];
        }
        tv.setText(year + "-" + monthText + "-" + dateText);
    }

    private void setTimePicker(final TextView tv, AnimalDb animal, Integer action) {
        final OnTimeSetListener time = new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourText = "";
                String minuteText = "";
                if (hourOfDay < 10) {
                    hourText = "0" + hourOfDay;
                } else {
                    hourText = hourOfDay + "";
                }
                if (minute < 10) {
                    minuteText = "0" + minute;
                } else {
                    minuteText = minute + "";
                }
                tv.setText(hourText + ":" + minuteText);
            }
        };
        tv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(OfflineSmartListActivity.this, time, OfflineSmartListActivity.this.animalCalendar.get(11), OfflineSmartListActivity.this.animalCalendar.get(12), true).show();
            }
        });
        String hourText = "";
        String minuteText = "";
        if (action.intValue() != 10 || animal.getInsemination_date() == null || animal.getInsemination_date() == "0000-00-00 00:00:00") {
            int hourOfDay = this.animalCalendar.get(11);
            int minute = this.animalCalendar.get(12);
            if (hourOfDay < 10) {
                hourText = "0" + hourOfDay;
            } else {
                hourText = hourOfDay + "";
            }
            if (minute < 10) {
                minuteText = "0" + minute;
            } else {
                minuteText = minute + "";
            }
        } else {
            String[] timeParts = Utils.calculateTime(animal.getInsemination_date(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[1].split(":");
            hourText = timeParts[0];
            minuteText = timeParts[1];
        }
        tv.setText(hourText + ":" + minuteText);
    }

    private String getHours() {
        String hourText;
        String minuteText;
        int hourOfDay = this.animalCalendar.get(11);
        int minute = this.animalCalendar.get(12);
        if (hourOfDay < 10) {
            hourText = "0" + hourOfDay;
        } else {
            hourText = hourOfDay + "";
        }
        if (minute < 10) {
            minuteText = "0" + minute;
        } else {
            minuteText = minute + "";
        }
        return hourText + ":" + minuteText;
    }

    private void setDropdown(final TextView tv, final int listID) {
        tv.setOnClickListener(new OnClickListener() {

            class C05101 implements DialogInterface.OnClickListener {
                C05101() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent;
                    switch (listID) {
                        case 1:
                            tv.setText(((String) OfflineSmartListActivity.this.heatList.get(which)).toString());
                            return;
                        case 2:
                            tv.setText(((String) OfflineSmartListActivity.this.cowSensorList.get(which)).toString());
                            return;
                        case 3:
                            tv.setText(((String) OfflineSmartListActivity.this.assumeList.get(which)).toString());
                            return;
                        case 5:
                            if (which > 0) {
                                tv.setText(((String) OfflineSmartListActivity.this.bullList.get(which)).toString());
                                OfflineSmartListActivity.this.bullId = ((Integer) OfflineSmartListActivity.this.bullListHash.get(((String) OfflineSmartListActivity.this.bullList.get(which)).toString())).toString();
                                return;
                            }
                            intent = new Intent(OfflineSmartListActivity.this, OfflineManualInputActivity.class);
                            intent.putExtra("newBull", true);
                            OfflineSmartListActivity.this.startActivity(intent);
                            return;
                        case 6:
                            if (which > 0) {
                                tv.setText(((String) OfflineSmartListActivity.this.cowList.get(which)).toString());
                                OfflineSmartListActivity.this.cowId = ((Integer) OfflineSmartListActivity.this.cowListHash.get(((String) OfflineSmartListActivity.this.cowList.get(which)).toString())).toString();
                                return;
                            }
                            intent = new Intent(OfflineSmartListActivity.this, OfflineManualInputActivity.class);
                            intent.putExtra("newCow", true);
                            OfflineSmartListActivity.this.startActivity(intent);
                            return;
                        case 7:
                            tv.setText(((String) OfflineSmartListActivity.this.bullSensorList.get(which)).toString());
                            return;
                        default:
                            return;
                    }
                }
            }

            public void onClick(View v) {
                OfflineSmartListActivity.this.initLists();
                Builder builder = new Builder(OfflineSmartListActivity.this);
                CharSequence[] list = new CharSequence[0];
                switch (listID) {
                    case 1:
                        list = (CharSequence[]) OfflineSmartListActivity.this.heatList.toArray(new CharSequence[OfflineSmartListActivity.this.heatList.size()]);
                        break;
                    case 2:
                        list = (CharSequence[]) OfflineSmartListActivity.this.cowSensorList.toArray(new CharSequence[OfflineSmartListActivity.this.cowSensorList.size()]);
                        break;
                    case 3:
                        list = (CharSequence[]) OfflineSmartListActivity.this.assumeList.toArray(new CharSequence[OfflineSmartListActivity.this.assumeList.size()]);
                        break;
                    case 5:
                        list = (CharSequence[]) OfflineSmartListActivity.this.bullList.toArray(new CharSequence[OfflineSmartListActivity.this.bullList.size()]);
                        break;
                    case 6:
                        list = (CharSequence[]) OfflineSmartListActivity.this.cowList.toArray(new CharSequence[OfflineSmartListActivity.this.cowList.size()]);
                        break;
                    case 7:
                        list = (CharSequence[]) OfflineSmartListActivity.this.bullSensorList.toArray(new CharSequence[OfflineSmartListActivity.this.bullSensorList.size()]);
                        break;
                }
                builder.setItems(list, new C05101());
                builder.show();
            }
        });
    }

    private void setInseminationRadioClick(RelativeLayout firstL, TextView firstT, ImageView firstI, RelativeLayout secondL, TextView secondT, ImageView secondI) {
        firstT.setTextColor(getResources().getColor(C0530R.color.green_mh));
        secondT.setTextColor(getResources().getColor(C0530R.color.gray_mh));
        firstI.setImageDrawable(getResources().getDrawable(C0530R.drawable.green_radio));
        secondI.setImageDrawable(getResources().getDrawable(C0530R.drawable.grey_radio));
        final TextView textView = firstT;
        final TextView textView2 = secondT;
        final ImageView imageView = firstI;
        final ImageView imageView2 = secondI;
        firstL.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                textView.setTextColor(OfflineSmartListActivity.this.getResources().getColor(C0530R.color.green_mh));
                textView2.setTextColor(OfflineSmartListActivity.this.getResources().getColor(C0530R.color.gray_mh));
                imageView.setImageDrawable(OfflineSmartListActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                imageView2.setImageDrawable(OfflineSmartListActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                OfflineSmartListActivity.this.breedingBull = Boolean.valueOf(true);
            }
        });
        textView = firstT;
        textView2 = secondT;
        imageView = firstI;
        imageView2 = secondI;
        secondL.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                textView.setTextColor(OfflineSmartListActivity.this.getResources().getColor(C0530R.color.gray_mh));
                textView2.setTextColor(OfflineSmartListActivity.this.getResources().getColor(C0530R.color.green_mh));
                imageView.setImageDrawable(OfflineSmartListActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                imageView2.setImageDrawable(OfflineSmartListActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                OfflineSmartListActivity.this.breedingBull = Boolean.valueOf(false);
            }
        });
    }

    private void prepareSaveAction(Integer action, TextView fAS, EditText fAW, TextView sAS, EditText sAP, TextView tAS, EditText tAE, AnimalDb animal) {
        showProgress(true);
        this.actionAdded = action;
        AnimalActionDb animalActionDb = new AnimalActionDb();
        animalActionDb.setAction(action);
        animalActionDb.setAnimal_id(animal.getAnimal_id().toString());
        animalActionDb.setAnimal_tag_number(animal.getTag_number());
        animalActionDb.setAnimal_type(animal.getType().toString());
        switch (action.intValue()) {
            case 0:
                animalActionDb.setSensor(tAS.getText().toString());
                animal.setSensor(tAS.getText().toString());
                break;
            case 2:
                setLastState(action, animal);
                animalActionDb.setWeight(Utils.getWeight(fAW.getText().toString(), this));
                animalActionDb.setDatetime(Utils.calculateCetTime(sAS.getText().toString() + StringUtils.SPACE + getHours() + ":00"));
                animal.setWeight(Utils.getWeight(fAW.getText().toString(), this));
                break;
            case 3:
                setLastState(action, animal);
                animalActionDb.setDatetime(Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setDescription(tAS.getText().toString());
                animal.setLast_state(Integer.valueOf(3));
                animal.setIn_heat_date(Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                break;
            case 4:
                String str;
                setLastState(action, animal);
                animalActionDb.setDatetime(Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setBull_tag_number(!this.bullId.equals("") ? tAS.getText().toString() : "");
                animalActionDb.setBull_id(this.bullId);
                animal.setLast_state(Integer.valueOf(4));
                animal.setInsemination_date(Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                if (this.bullId.equals("")) {
                    str = "";
                } else {
                    str = tAS.getText().toString();
                }
                animal.setInsemenation_bull(str);
                break;
            case 9:
                animalActionDb.setWeight(Utils.getWeight(fAW.getText().toString(), this));
                animalActionDb.setPrice(sAP.getText().toString());
                animalActionDb.setDescription(tAE.getText().toString());
                animal.setSold(Integer.valueOf(1));
                animal.setStatus(Integer.valueOf(4));
                break;
            case 10:
                setLastState(action, animal);
                animalActionDb.setDatetime(Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setDescription(tAS.getText().toString());
                animal.setLast_state(Integer.valueOf(1));
                Integer gestation = Integer.valueOf(283);
                if (animal.getGestation() != null && animal.getGestation().intValue() > 0) {
                    gestation = animal.getGestation();
                }
                animal.setDue_date(Utils.calculateDueDate(animalActionDb.getDatetime(), gestation));
                break;
            case 11:
                animal.setStatus(Integer.valueOf(1));
                break;
            case 15:
                animalActionDb.setDatetime(Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setCow_tag_number(!this.cowId.equals("") ? tAS.getText().toString() : "");
                animalActionDb.setCow_id(this.cowId);
                break;
            case 17:
                animalActionDb.setSensor(tAS.getText().toString());
                animal.setBull_sensor(tAS.getText().toString());
                break;
        }
        this.animalActionDbDao.insert(animalActionDb);
        this.animalDbDao.update(animal);
        actionAdded();
    }

    public void addCalving(AnimalDb animal) {
        this.actionAdded = Integer.valueOf(99);
        Intent intent = new Intent(this, NewCalvingActivity.class);
        intent.putExtra("CowDb", animal);
        startActivity(intent);
    }

    private void actionAdded() {
        showProgress(false);
        Toast.makeText(this, getString(C0530R.string.success), 1).show();
        closeOverlay();
        setAnimation();
        this.actionAdded = null;
    }

    private void setAnimation() {
        Integer color = checkForAnimation();
        if (color == null || this.animalInListAction == null) {
            reloadAnimals();
            return;
        }
        this.smartListAdapter.setAnimationColor(color);
        this.smartListAdapter.setAnimationItem(this.animalInListAction);
        this.smartListSwipeRefreshLayout.setRefreshing(true);
        this.smartListAdapter.notifyDataSetChanged();
    }

    public void reloadAnimals() {
        this.actionAdded = null;
        this.animalInListAction = null;
        this.smartListAdapter.setAnimationColor(null);
        this.smartListAdapter.setAnimationItem(this.animalInListAction);
        fetchAnimals();
    }

    private Integer checkForAnimation() {
        if (this.type.equals(Integer.valueOf(9)) || this.actionAdded == null) {
            return null;
        }
        switch (this.actionAdded.intValue()) {
            case 3:
                return Integer.valueOf(getResources().getColor(C0530R.color.red_mh));
            case 4:
                return Integer.valueOf(getResources().getColor(C0530R.color.blue_mh));
            case 9:
                return Integer.valueOf(getResources().getColor(C0530R.color.dark_gray_mh));
            case 10:
                return Integer.valueOf(getResources().getColor(C0530R.color.green_mh));
            case 11:
            case 99:
                return Integer.valueOf(getResources().getColor(C0530R.color.dark_green));
            default:
                return null;
        }
    }

    private void setLastState(Integer action, AnimalDb animal) {
        StateHistoryDbDao stateHistoryDbDao = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getStateHistoryDbDao();
        StateHistoryDb stateHistoryDb = new StateHistoryDb();
        if (!action.equals(Integer.valueOf(2))) {
            stateHistoryDb.setLast_state(animal.getLast_state());
            switch (action.intValue()) {
                case 3:
                    stateHistoryDb.setLast_state_datetime(animal.getIn_heat_date());
                    break;
                case 4:
                    stateHistoryDb.setLast_state_datetime(animal.getInsemination_date());
                    break;
                case 10:
                    stateHistoryDb.setLast_state_datetime(animal.getDue_date());
                    break;
                default:
                    break;
            }
        }
        stateHistoryDb.setWeight(animal.getWeight());
        stateHistoryDb.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        stateHistoryDb.setAnimal_id(animal.getId().toString());
        stateHistoryDbDao.insert(stateHistoryDb);
    }

    private List<AnimalDb> getAnimalList() {
        WhereCondition where;
        WhereCondition status;
        Collection types = new ArrayList();
        WhereCondition time = Properties.Status.le(Integer.valueOf(4));
        Property orderAsc = Properties.Status;
        Property orderDesc = Properties.Status;
        SimpleDateFormat formatShort = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (this.type.intValue() < 5) {
            where = Properties.Last_state.eq(this.type);
            switch (this.bottomFilter.intValue()) {
                case 1:
                    types.add(Integer.valueOf(1));
                    types.add(Integer.valueOf(3));
                    break;
                case 2:
                    types.add(Integer.valueOf(1));
                    break;
                case 3:
                    types.add(Integer.valueOf(3));
                    break;
            }
        }
        where = Properties.Last_state.le(Integer.valueOf(5));
        if (this.type.equals(Integer.valueOf(8))) {
            status = Properties.Status.eq(Integer.valueOf(4));
        } else {
            status = Properties.Status.eq(Integer.valueOf(1));
        }
        Calendar cal;
        String date1;
        String date2;
        switch (this.type.intValue()) {
            case 1:
                orderAsc = Properties.Due_date;
                switch (this.topFilter.intValue()) {
                    case 1:
                        break;
                    case 2:
                        cal = Calendar.getInstance();
                        date1 = formatShort.format(cal.getTime());
                        cal.add(5, 7);
                        time = Properties.Due_date.le(formatShort.format(cal.getTime()));
                        break;
                    case 3:
                        where = Properties.Last_state.gt(Integer.valueOf(0));
                        orderDesc = Properties.Last_calving_date;
                        cal = Calendar.getInstance();
                        date2 = formatLong.format(cal.getTime());
                        cal.add(5, -90);
                        time = Properties.Last_calving_date.between(formatLong.format(cal.getTime()), date2);
                        break;
                    default:
                        break;
                }
            case 2:
                orderDesc = Properties.Cycle_date;
                switch (this.topFilter.intValue()) {
                    case 1:
                        cal = Calendar.getInstance();
                        date2 = formatLong.format(cal.getTime());
                        cal.add(5, -90);
                        time = Properties.Cycle_date.between(formatLong.format(cal.getTime()), date2);
                        break;
                    case 2:
                        cal = Calendar.getInstance();
                        cal.add(5, -24);
                        date1 = formatLong.format(cal.getTime());
                        cal = Calendar.getInstance();
                        cal.add(5, -17);
                        time = Properties.Cycle_date.between(date1, formatLong.format(cal.getTime()));
                        break;
                    case 3:
                        cal = Calendar.getInstance();
                        cal.add(5, -45);
                        date1 = formatLong.format(cal.getTime());
                        cal = Calendar.getInstance();
                        cal.add(5, -38);
                        time = Properties.Cycle_date.between(date1, formatLong.format(cal.getTime()));
                        break;
                    default:
                        break;
                }
            case 3:
                orderDesc = Properties.In_heat_date;
                switch (this.topFilter.intValue()) {
                    case 1:
                        cal = Calendar.getInstance();
                        date2 = formatLong.format(cal.getTime());
                        cal.add(5, -90);
                        time = Properties.In_heat_date.between(formatLong.format(cal.getTime()), date2);
                        break;
                    case 2:
                        cal = Calendar.getInstance();
                        date2 = formatLong.format(cal.getTime());
                        cal.add(5, -1);
                        date1 = formatLong.format(cal.getTime());
                        System.out.println(date1);
                        System.out.println(date2);
                        time = Properties.In_heat_date.between(date1, date2);
                        break;
                    default:
                        break;
                }
            case 4:
                orderDesc = Properties.Insemination_date;
                switch (this.topFilter.intValue()) {
                    case 1:
                        cal = Calendar.getInstance();
                        date2 = formatLong.format(cal.getTime());
                        cal.add(5, -90);
                        time = Properties.Insemination_date.between(formatLong.format(cal.getTime()), date2);
                        break;
                    case 2:
                        cal = Calendar.getInstance();
                        cal.add(5, -24);
                        date1 = formatLong.format(cal.getTime());
                        cal = Calendar.getInstance();
                        cal.add(5, -17);
                        time = Properties.Insemination_date.between(date1, formatLong.format(cal.getTime()));
                        break;
                    case 3:
                        cal = Calendar.getInstance();
                        cal.add(5, -45);
                        date1 = formatLong.format(cal.getTime());
                        cal = Calendar.getInstance();
                        cal.add(5, -38);
                        time = Properties.Insemination_date.between(date1, formatLong.format(cal.getTime()));
                        break;
                    default:
                        break;
                }
            case 5:
                switch (this.topFilter.intValue()) {
                    case 1:
                        orderAsc = Properties.Birth_date;
                        break;
                    case 2:
                        orderDesc = Properties.Weight;
                        break;
                    case 3:
                        orderAsc = Properties.Tag_number;
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        types.add(Integer.valueOf(4));
                        types.add(Integer.valueOf(2));
                        types.add(Integer.valueOf(10));
                        break;
                    case 2:
                        types.add(Integer.valueOf(4));
                        break;
                    case 3:
                        types.add(Integer.valueOf(2));
                        break;
                    case 4:
                        types.add(Integer.valueOf(10));
                        break;
                    default:
                        break;
                }
            case 6:
                switch (this.topFilter.intValue()) {
                    case 1:
                        orderAsc = Properties.Birth_date;
                        break;
                    case 2:
                        orderDesc = Properties.Weight;
                        break;
                    case 3:
                        orderAsc = Properties.Tag_number;
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        types.add(Integer.valueOf(7));
                        types.add(Integer.valueOf(8));
                        break;
                    case 2:
                        types.add(Integer.valueOf(7));
                        break;
                    case 3:
                        types.add(Integer.valueOf(8));
                        break;
                    default:
                        break;
                }
            case 7:
                switch (this.topFilter.intValue()) {
                    case 1:
                        orderAsc = Properties.Birth_date;
                        break;
                    case 2:
                        orderDesc = Properties.Weight;
                        break;
                    case 3:
                        orderAsc = Properties.Tag_number;
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        types.add(Integer.valueOf(5));
                        types.add(Integer.valueOf(6));
                        break;
                    case 2:
                        types.add(Integer.valueOf(5));
                        break;
                    case 3:
                        types.add(Integer.valueOf(6));
                        break;
                    default:
                        break;
                }
            case 8:
                switch (this.topFilter.intValue()) {
                    case 1:
                        orderDesc = Properties.Date_slaughtered;
                        break;
                    case 2:
                        orderDesc = Properties.Date_sold;
                        break;
                    case 3:
                        orderAsc = Properties.Tag_number;
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        types.add(Integer.valueOf(1));
                        types.add(Integer.valueOf(2));
                        types.add(Integer.valueOf(3));
                        types.add(Integer.valueOf(4));
                        types.add(Integer.valueOf(5));
                        types.add(Integer.valueOf(6));
                        types.add(Integer.valueOf(7));
                        types.add(Integer.valueOf(8));
                        break;
                    case 2:
                        types.add(Integer.valueOf(1));
                        types.add(Integer.valueOf(3));
                        types.add(Integer.valueOf(5));
                        break;
                    case 3:
                        types.add(Integer.valueOf(7));
                        types.add(Integer.valueOf(8));
                        break;
                    case 4:
                        types.add(Integer.valueOf(2));
                        types.add(Integer.valueOf(4));
                        types.add(Integer.valueOf(6));
                        break;
                    default:
                        break;
                }
            case 9:
                switch (this.topFilter.intValue()) {
                    case 1:
                        orderAsc = Properties.Birth_date;
                        break;
                    case 2:
                        orderAsc = Properties.Weight;
                        break;
                    case 3:
                        orderAsc = Properties.Tag_number;
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        types.add(Integer.valueOf(1));
                        types.add(Integer.valueOf(3));
                        break;
                    case 2:
                        types.add(Integer.valueOf(1));
                        break;
                    case 3:
                        types.add(Integer.valueOf(3));
                        break;
                    default:
                        break;
                }
        }
        return this.animalDbDao.queryBuilder().where(Properties.Type.in(types), where, status, time).orderDesc(orderDesc).orderAsc(orderAsc).list();
    }

    public void onBackPressed() {
        if (this.transparentBackground.getVisibility() == 0) {
            closeOverlay();
        } else {
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        if (this.type != null) {
            setAnimation();
        }
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
