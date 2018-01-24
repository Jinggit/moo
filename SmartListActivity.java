package com.moocall.moocall;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.moocall.moocall.adapter.SmartListViewAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.db.SensorDb;
import com.moocall.moocall.db.SensorDbDao;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddAnimalActionUrl;
import com.moocall.moocall.url.ChangeAnimalImageUrl;
import com.moocall.moocall.url.FetchAnimalListUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONArray;
import org.json.JSONObject;

public class SmartListActivity extends Activity {
    private int LOAD_PHOTO_CODE = 1;
    private int RESULT_CROP = 2;
    private int TAKE_PHOTO_CODE = 0;
    private Integer actionAdded;
    private ScrollView actionOverview;
    private BroadcastReceiver additionalBroadcastReceiver;
    private Animal animalActionAdded;
    private Calendar animalCalendar = Calendar.getInstance();
    private AnimalDbDao animalDbDao;
    private Integer animalInList;
    private Integer animalInListAction;
    private ArrayList<Animal> animalList;
    private List<String> assumeList;
    private Integer bottomFilter;
    private Boolean breedingBull = Boolean.valueOf(true);
    private BroadcastReceiver broadcastReceiver;
    private Boolean broadcastRegistered = Boolean.valueOf(false);
    private String bullId = "";
    private List<String> bullList;
    private HashMap<String, Integer> bullListHash;
    private List<String> bullSensorList;
    private String cameraFileName;
    private TextView cancelAction;
    private String cowId = "";
    private List<String> cowList;
    private HashMap<String, Integer> cowListHash;
    private List<String> cowSensorList;
    private RelativeLayout firstAnimalFilterLayout;
    private TextView firstAnimalFilterText;
    private ImageView firstFilterImage;
    private RelativeLayout firstFilterLayout;
    private TextView firstFilterText;
    private RelativeLayout fourthAnimalFilterLayout;
    private TextView fourthAnimalFilterText;
    private List<String> heatList;
    private ImageLoader imageLoader;
    private ArrayList<NameValuePair> nameValuePairs;
    private View progressView;
    private Drawable radioButton;
    private RelativeLayout secondAnimalFilterLayout;
    private TextView secondAnimalFilterText;
    private ImageView secondFilterImage;
    private RelativeLayout secondFilterLayout;
    private TextView secondFilterText;
    private SensorDbDao sensorDbDao;
    private SmartListViewAdapter smartListAdapter;
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

    class C05551 implements OnClickListener {
        C05551() {
        }

        public void onClick(View v) {
            SmartListActivity.this.onBackPressed();
        }
    }

    class C05562 implements OnClickListener {
        C05562() {
        }

        public void onClick(View view) {
            SmartListActivity.this.topFilter = Integer.valueOf(1);
            SmartListActivity.this.setTopFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05573 implements OnClickListener {
        C05573() {
        }

        public void onClick(View view) {
            SmartListActivity.this.topFilter = Integer.valueOf(2);
            SmartListActivity.this.setTopFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05584 implements OnClickListener {
        C05584() {
        }

        public void onClick(View view) {
            SmartListActivity.this.topFilter = Integer.valueOf(3);
            SmartListActivity.this.setTopFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05595 implements OnClickListener {
        C05595() {
        }

        public void onClick(View view) {
            SmartListActivity.this.bottomFilter = Integer.valueOf(1);
            SmartListActivity.this.setBottomFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05606 implements OnClickListener {
        C05606() {
        }

        public void onClick(View view) {
            SmartListActivity.this.bottomFilter = Integer.valueOf(2);
            SmartListActivity.this.setBottomFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05617 implements OnClickListener {
        C05617() {
        }

        public void onClick(View view) {
            SmartListActivity.this.bottomFilter = Integer.valueOf(3);
            SmartListActivity.this.setBottomFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05628 implements OnClickListener {
        C05628() {
        }

        public void onClick(View view) {
            SmartListActivity.this.bottomFilter = Integer.valueOf(4);
            SmartListActivity.this.setBottomFilter();
            SmartListActivity.this.fetchAnimals();
        }
    }

    class C05639 implements OnClickListener {
        C05639() {
        }

        public void onClick(View view) {
            SmartListActivity.this.closeOverlay();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0530R.layout.activity_smart_list);
        onResume();
        this.type = (Integer) getIntent().getSerializableExtra("type");
        this.topFilter = Integer.valueOf(1);
        this.bottomFilter = Integer.valueOf(1);
        if (this.type.intValue() < 5) {
            this.topFilter = Integer.valueOf(2);
        }
        setupToolbar();
        createAsyncBroadcast();
        setupLayout();
        implementsListeners();
        initLists();
        setSmartListType();
        setTopFilter();
        setBottomFilter();
        createBroadcastReceiver();
        fetchAnimals();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        this.toolbar.setTitle((CharSequence) "");
        this.toolbar.setNavigationOnClickListener(new C05551());
    }

    private void setupLayout() {
        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
        this.animalDbDao = daoSession.getAnimalDbDao();
        this.sensorDbDao = daoSession.getSensorDbDao();
        ImageLoaderConfiguration config = new Builder(this).build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.destroy();
        this.imageLoader.init(config);
        this.smartListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.smartListSwipeRefreshLayout);
        this.smartListView = (ListView) findViewById(C0530R.id.smartListView);
        this.animalList = new ArrayList();
        this.smartListAdapter = new SmartListViewAdapter(this, this.animalList, this.type, this.topFilter);
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
        this.firstAnimalFilterText = (TextView) findViewById(C0530R.id.firstAnimalFilterText);
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
        this.firstFilterLayout.setOnClickListener(new C05562());
        this.secondFilterLayout.setOnClickListener(new C05573());
        this.thirdFilterLayout.setOnClickListener(new C05584());
        this.firstAnimalFilterLayout.setOnClickListener(new C05595());
        this.secondAnimalFilterLayout.setOnClickListener(new C05606());
        this.thirdAnimalFilterLayout.setOnClickListener(new C05617());
        this.fourthAnimalFilterLayout.setOnClickListener(new C05628());
        this.cancelAction.setOnClickListener(new C05639());
        this.smartListSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (SmartListActivity.this.smartListSwipeRefreshLayout.isRefreshing()) {
                    SmartListActivity.this.setAnimalList(null);
                    SmartListActivity.this.fetchAnimals();
                }
            }
        });
    }

    public void openAnimalDetails(Animal animal) {
        registerReceiver();
        Intent intent = new Intent(this, AnimalDetailsActivity.class);
        intent.putExtra("animal", animal);
        intent.putExtra("subtype", this.type);
        startActivity(intent);
    }

    private void closeOverlay() {
        this.transparentBackground.setVisibility(8);
        this.actionOverview.setVisibility(8);
        Utils.hideKeyboard(this);
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    SmartListActivity.this.unregisterReceiver(this);
                    String action = intent.getAction();
                    if (action.equals(QuickstartPreferences.FETCH_ANIMAL_LIST)) {
                        SmartListActivity.this.populateSmartList(new JSONArray(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.ADD_ANIMAL_ACTION)) {
                        SmartListActivity.this.actionAdded(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.CHANGE_ANIMAL_IMAGE)) {
                        SmartListActivity.this.imageChanged(intent.getStringExtra("response"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
        List<Animal> list = getAnimalList();
        if (list == null) {
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_ANIMAL_LIST));
            new AcquireResponseTask(this).execute(new String[]{new FetchAnimalListUrl(this.type, this.topFilter, this.bottomFilter).createAndReturnUrl(this), QuickstartPreferences.FETCH_ANIMAL_LIST});
            return;
        }
        this.animalList.clear();
        for (Animal animal : list) {
            this.animalList.add(animal);
        }
        this.smartListAdapter.notifyDataSetChanged();
        showProgress(false);
    }

    private void populateSmartList(JSONArray response) {
        try {
            this.animalList.clear();
            for (int i = 0; i < response.length(); i++) {
                Animal animal = new Animal(new JSONParserBgw((JSONObject) response.get(i)), Boolean.valueOf(false), this);
                this.animalList.add(animal);
                if (!(animal.getImagePath() == null || animal.getImagePath().isEmpty() || StorageContainer.getAnimalImageMemoryCache().get(animal.getTagNumber()) != null)) {
                    loadImage(animal);
                }
            }
            setAnimalList(this.animalList);
            if (this.smartListAdapter.getAnimation() != null) {
                this.smartListAdapter.getAnimation().cancel();
                this.smartListView.setBackgroundColor(getResources().getColor(C0530R.color.white));
            }
            this.smartListAdapter.notifyDataSetChanged();
            showProgress(false);
            this.smartListSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickedAction(Integer position, Boolean right) {
        this.animalInListAction = position;
        if (position.intValue() < this.animalList.size()) {
            Animal animal = (Animal) this.animalList.get(position.intValue());
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

    public void showActionOverview(Integer action, Animal animal) {
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
        final Animal animal2 = animal;
        okAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SmartListActivity.this.prepareSaveAction(num, firstActionSelect, firstActionWeight, secondActionSelect, secondActionPrice, thirdActionSelect, thirdActionEdit, animal2);
            }
        });
    }

    private void setDatePicker(final TextView tv, Animal animal, Integer action) {
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
                DatePickerDialog dateDialog = new DatePickerDialog(SmartListActivity.this, date, SmartListActivity.this.animalCalendar.get(1), SmartListActivity.this.animalCalendar.get(2), SmartListActivity.this.animalCalendar.get(5));
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });
        String year = "";
        String monthText = "";
        String dateText = "";
        if (action.intValue() != 10 || animal.getInseminationDate() == null || animal.getInseminationDate() == "0000-00-00 00:00:00") {
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
            String[] dateParts = Utils.calculateTime(animal.getInseminationDate(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[0].split("-");
            year = dateParts[2];
            monthText = dateParts[1];
            dateText = dateParts[0];
        }
        tv.setText(year + "-" + monthText + "-" + dateText);
    }

    private void setTimePicker(final TextView tv, Animal animal, Integer action) {
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
                new TimePickerDialog(SmartListActivity.this, time, SmartListActivity.this.animalCalendar.get(11), SmartListActivity.this.animalCalendar.get(12), true).show();
            }
        });
        String hourText = "";
        String minuteText = "";
        if (action.intValue() != 10 || animal.getInseminationDate() == null || animal.getInseminationDate() == "0000-00-00 00:00:00") {
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
            String[] timeParts = Utils.calculateTime(animal.getInseminationDate(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[1].split(":");
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

            class C05541 implements DialogInterface.OnClickListener {
                C05541() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent;
                    switch (listID) {
                        case 1:
                            tv.setText(((String) SmartListActivity.this.heatList.get(which)).toString());
                            return;
                        case 2:
                            tv.setText(((String) SmartListActivity.this.cowSensorList.get(which)).toString());
                            return;
                        case 3:
                            tv.setText(((String) SmartListActivity.this.assumeList.get(which)).toString());
                            return;
                        case 5:
                            if (which > 0) {
                                tv.setText(((String) SmartListActivity.this.bullList.get(which)).toString());
                                SmartListActivity.this.bullId = ((Integer) SmartListActivity.this.bullListHash.get(((String) SmartListActivity.this.bullList.get(which)).toString())).toString();
                                return;
                            }
                            SmartListActivity.this.registerReceiver();
                            intent = new Intent(SmartListActivity.this, ManualInputActivity.class);
                            intent.putExtra("newBull", true);
                            SmartListActivity.this.startActivity(intent);
                            return;
                        case 6:
                            if (which > 0) {
                                tv.setText(((String) SmartListActivity.this.cowList.get(which)).toString());
                                SmartListActivity.this.cowId = ((Integer) SmartListActivity.this.cowListHash.get(((String) SmartListActivity.this.cowList.get(which)).toString())).toString();
                                return;
                            }
                            SmartListActivity.this.registerReceiver();
                            intent = new Intent(SmartListActivity.this, ManualInputActivity.class);
                            intent.putExtra("newCow", true);
                            SmartListActivity.this.startActivity(intent);
                            return;
                        case 7:
                            tv.setText(((String) SmartListActivity.this.bullSensorList.get(which)).toString());
                            return;
                        default:
                            return;
                    }
                }
            }

            public void onClick(View v) {
                SmartListActivity.this.initLists();
                AlertDialog.Builder builder = new AlertDialog.Builder(SmartListActivity.this);
                CharSequence[] list = new CharSequence[0];
                switch (listID) {
                    case 1:
                        list = (CharSequence[]) SmartListActivity.this.heatList.toArray(new CharSequence[SmartListActivity.this.heatList.size()]);
                        break;
                    case 2:
                        list = (CharSequence[]) SmartListActivity.this.cowSensorList.toArray(new CharSequence[SmartListActivity.this.cowSensorList.size()]);
                        break;
                    case 3:
                        list = (CharSequence[]) SmartListActivity.this.assumeList.toArray(new CharSequence[SmartListActivity.this.assumeList.size()]);
                        break;
                    case 5:
                        list = (CharSequence[]) SmartListActivity.this.bullList.toArray(new CharSequence[SmartListActivity.this.bullList.size()]);
                        break;
                    case 6:
                        list = (CharSequence[]) SmartListActivity.this.cowList.toArray(new CharSequence[SmartListActivity.this.cowList.size()]);
                        break;
                    case 7:
                        list = (CharSequence[]) SmartListActivity.this.bullSensorList.toArray(new CharSequence[SmartListActivity.this.bullSensorList.size()]);
                        break;
                }
                builder.setItems(list, new C05541());
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
                textView.setTextColor(SmartListActivity.this.getResources().getColor(C0530R.color.green_mh));
                textView2.setTextColor(SmartListActivity.this.getResources().getColor(C0530R.color.gray_mh));
                imageView.setImageDrawable(SmartListActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                imageView2.setImageDrawable(SmartListActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                SmartListActivity.this.breedingBull = Boolean.valueOf(true);
            }
        });
        textView = firstT;
        textView2 = secondT;
        imageView = firstI;
        imageView2 = secondI;
        secondL.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                textView.setTextColor(SmartListActivity.this.getResources().getColor(C0530R.color.gray_mh));
                textView2.setTextColor(SmartListActivity.this.getResources().getColor(C0530R.color.green_mh));
                imageView.setImageDrawable(SmartListActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                imageView2.setImageDrawable(SmartListActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                SmartListActivity.this.breedingBull = Boolean.valueOf(false);
            }
        });
    }

    private void prepareSaveAction(Integer action, TextView fAS, EditText fAW, TextView sAS, EditText sAP, TextView tAS, EditText tAE, Animal animal) {
        this.nameValuePairs = new ArrayList();
        this.actionAdded = action;
        this.animalActionAdded = animal;
        switch (action.intValue()) {
            case 0:
                this.nameValuePairs.add(new BasicNameValuePair("sensor", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 2:
                this.nameValuePairs.add(new BasicNameValuePair("weight", Utils.getWeight(fAW.getText().toString(), this)));
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(sAS.getText().toString() + StringUtils.SPACE + getHours() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 3:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 4:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("bull-tag-number", !this.bullId.equals("") ? tAS.getText().toString() : ""));
                this.nameValuePairs.add(new BasicNameValuePair("bull-id", this.bullId));
                break;
            case 5:
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 9:
                this.nameValuePairs.add(new BasicNameValuePair("weight", Utils.getWeight(fAW.getText().toString(), this)));
                this.nameValuePairs.add(new BasicNameValuePair(Param.PRICE, sAP.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAE.getText().toString()));
                break;
            case 10:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 11:
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 15:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(fAS.getText().toString() + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("cow-tag-number", !this.cowId.equals("") ? tAS.getText().toString() : ""));
                this.nameValuePairs.add(new BasicNameValuePair("cow-id", this.cowId));
                break;
            case 17:
                this.nameValuePairs.add(new BasicNameValuePair("sensor", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
        }
        saveAction(null);
    }

    private void saveAction(String response) {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_ANIMAL_ACTION));
        new AcquireResponseTask(this).execute(new String[]{new AddAnimalActionUrl(this.actionAdded.toString(), this.animalActionAdded.getType().toString(), this.animalActionAdded.getId().toString(), response).createAndReturnUrl(this), QuickstartPreferences.ADD_ANIMAL_ACTION, "tag-number", this.animalActionAdded.getTagNumber(), ((NameValuePair) this.nameValuePairs.get(0)).getName(), ((NameValuePair) this.nameValuePairs.get(0)).getValue(), ((NameValuePair) this.nameValuePairs.get(1)).getName(), ((NameValuePair) this.nameValuePairs.get(1)).getValue(), ((NameValuePair) this.nameValuePairs.get(2)).getName(), ((NameValuePair) this.nameValuePairs.get(2)).getValue()});
    }

    public void addCalving(Animal animal) {
        registerReceiver();
        Intent intent = new Intent(this, NewCalvingActivity.class);
        intent.putExtra("Cow", animal);
        startActivity(intent);
    }

    private void actionAdded(String result) {
        try {
            showProgress(false);
            if (result.equals("\"success\"")) {
                Toast.makeText(this, result, 1).show();
                sendBroadcast(new Intent("refresh_lists"));
                closeOverlay();
                setAnimation();
                this.nameValuePairs = null;
                this.actionAdded = null;
                this.animalActionAdded = null;
            } else if (result.equals("\"action failed\"")) {
                Toast.makeText(this, result, 1).show();
            } else if (this.actionAdded != null && this.nameValuePairs != null && this.animalActionAdded != null) {
                showPopup(result.substring(1, result.length() - 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopup(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage((CharSequence) message);
        if (message.contains("history")) {
            alertDialogBuilder.setPositiveButton((int) C0530R.string.save_in_history, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SmartListActivity.this.saveAction("2");
                }
            });
        } else {
            alertDialogBuilder.setPositiveButton((int) C0530R.string.change_state, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SmartListActivity.this.saveAction("1");
                }
            });
        }
        alertDialogBuilder.setNeutralButton((int) C0530R.string.cancel_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SmartListActivity.this.closeOverlay();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
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
        setAnimalList(null);
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

    private List<Animal> getAnimalList() {
        switch (this.type.intValue()) {
            case 1:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.inCalfAllSmartList;
                            case 2:
                                return StorageContainer.inCalfCowSmartList;
                            case 3:
                                return StorageContainer.inCalfHeiferSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.due7AllSmartList;
                            case 2:
                                return StorageContainer.due7CowSmartList;
                            case 3:
                                return StorageContainer.due7HeiferSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.calved90AllSmartList;
                            case 2:
                                return StorageContainer.calved90CowSmartList;
                            case 3:
                                return StorageContainer.calved90HeiferSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 2:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.cycle90AllSmartList;
                            case 2:
                                return StorageContainer.cycle90CowSmartList;
                            case 3:
                                return StorageContainer.cycle90HeiferSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.cycle1724AllSmartList;
                            case 2:
                                return StorageContainer.cycle1724CowSmartList;
                            case 3:
                                return StorageContainer.cycle1724HeiferSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.cycle3845AllSmartList;
                            case 2:
                                return StorageContainer.cycle3845CowSmartList;
                            case 3:
                                return StorageContainer.cycle3845HeiferSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 3:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.inHeat90AllSmartList;
                            case 2:
                                return StorageContainer.inHeat90CowSmartList;
                            case 3:
                                return StorageContainer.inHeat90HeiferSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.inHeat12AllSmartList;
                            case 2:
                                return StorageContainer.inHeat12CowSmartList;
                            case 3:
                                return StorageContainer.inHeat12HeiferSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 4:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.inseminated90AllSmartList;
                            case 2:
                                return StorageContainer.inseminated90CowSmartList;
                            case 3:
                                return StorageContainer.inseminated90HeiferSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.inseminated1724AllSmartList;
                            case 2:
                                return StorageContainer.inseminated1724CowSmartList;
                            case 3:
                                return StorageContainer.inseminated1724HeiferSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.inseminated3845AllSmartList;
                            case 2:
                                return StorageContainer.inseminated3845CowSmartList;
                            case 3:
                                return StorageContainer.inseminated3845HeiferSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 5:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.bullsByAgeAllSmartList;
                            case 2:
                                return StorageContainer.bullsByAgeBreedingSmartList;
                            case 3:
                                return StorageContainer.bullsByAgeAISmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.bullsByWeightAllSmartList;
                            case 2:
                                return StorageContainer.bullsByWeightBreedingSmartList;
                            case 3:
                                return StorageContainer.bullsByWeightAISmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.bullsByTagAllSmartList;
                            case 2:
                                return StorageContainer.bullsByTagBreedingSmartList;
                            case 3:
                                return StorageContainer.bullsByTagAISmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 6:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.calvesByAgeAllSmartList;
                            case 2:
                                return StorageContainer.calvesByAgeHeifersSmartList;
                            case 3:
                                return StorageContainer.calvesByAgeBullsSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.calvesByWeightAllSmartList;
                            case 2:
                                return StorageContainer.calvesByWeightHeifersSmartList;
                            case 3:
                                return StorageContainer.calvesByWeightBullsSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.calvesByTagAllSmartList;
                            case 2:
                                return StorageContainer.calvesByTagHeifersSmartList;
                            case 3:
                                return StorageContainer.calvesByTagBullsSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 7:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.beefByAgeAllSmartList;
                            case 2:
                                return StorageContainer.beefByAgeHeifersSmartList;
                            case 3:
                                return StorageContainer.beefByAgeBullsSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.beefByWeightAllSmartList;
                            case 2:
                                return StorageContainer.beefByWeightHeifersSmartList;
                            case 3:
                                return StorageContainer.beefByWeightBullsSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.beefByTagAllSmartList;
                            case 2:
                                return StorageContainer.beefByTagHeifersSmartList;
                            case 3:
                                return StorageContainer.beefByTagBullsSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 8:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.historicByCulledAllSmartList;
                            case 2:
                                return StorageContainer.historicByCulledCowsHeifersSmartList;
                            case 3:
                                return StorageContainer.historicByCulledCalvesSmartList;
                            case 4:
                                return StorageContainer.historicByCulledBullsSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.historicBySoldAllSmartList;
                            case 2:
                                return StorageContainer.historicBySoldCowsHeifersSmartList;
                            case 3:
                                return StorageContainer.historicBySoldCalvesSmartList;
                            case 4:
                                return StorageContainer.historicBySoldBullsSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.historicByTagAllSmartList;
                            case 2:
                                return StorageContainer.historicByTagCowsHeifersSmartList;
                            case 3:
                                return StorageContainer.historicByTagCalvesSmartList;
                            case 4:
                                return StorageContainer.historicByTagBullsSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 9:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.breedingByAgeAllSmartList;
                            case 2:
                                return StorageContainer.breedingByAgeCowsSmartList;
                            case 3:
                                return StorageContainer.breedingByAgeHeifersSmartList;
                            default:
                                return null;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.breedingByWeightAllSmartList;
                            case 2:
                                return StorageContainer.breedingByWeightCowsSmartList;
                            case 3:
                                return StorageContainer.breedingByWeightHeifersSmartList;
                            default:
                                return null;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                return StorageContainer.breedingByTagAllSmartList;
                            case 2:
                                return StorageContainer.breedingByTagCowsSmartList;
                            case 3:
                                return StorageContainer.breedingByTagHeifersSmartList;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    private void setAnimalList(List<Animal> list) {
        List<Animal> tmpList = null;
        if (list != null) {
            tmpList = new ArrayList();
            for (Animal animal : list) {
                tmpList.add(animal);
            }
        }
        switch (this.type.intValue()) {
            case 1:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.inCalfAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.inCalfCowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.inCalfHeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.due7AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.due7CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.due7HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.calved90AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.calved90CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.calved90HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 2:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.cycle90AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.cycle90CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.cycle90HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.cycle1724AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.cycle1724CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.cycle1724HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.cycle3845AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.cycle3845CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.cycle3845HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 3:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.inHeat90AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.inHeat90CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.inHeat90HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.inHeat12AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.inHeat12CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.inHeat12HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 4:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.inseminated90AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.inseminated90CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.inseminated90HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.inseminated1724AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.inseminated1724CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.inseminated1724HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.inseminated3845AllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.inseminated3845CowSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.inseminated3845HeiferSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 5:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.bullsByAgeAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.bullsByAgeBreedingSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.bullsByAgeAISmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.bullsByWeightAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.bullsByWeightBreedingSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.bullsByWeightAISmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.bullsByTagAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.bullsByTagBreedingSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.bullsByTagAISmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 6:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.calvesByAgeAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.calvesByAgeHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.calvesByAgeBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.calvesByWeightAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.calvesByWeightHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.calvesByWeightBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.calvesByTagAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.calvesByTagHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.calvesByTagBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 7:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.beefByAgeAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.beefByAgeHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.beefByAgeBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.beefByWeightAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.beefByWeightHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.beefByWeightBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.beefByTagAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.beefByTagHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.beefByTagBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 8:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.historicByCulledAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.historicByCulledCowsHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.historicByCulledCalvesSmartList = tmpList;
                                return;
                            case 4:
                                StorageContainer.historicByCulledBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.historicBySoldAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.historicBySoldCowsHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.historicBySoldCalvesSmartList = tmpList;
                                return;
                            case 4:
                                StorageContainer.historicBySoldBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.historicByTagAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.historicByTagCowsHeifersSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.historicByTagCalvesSmartList = tmpList;
                                return;
                            case 4:
                                StorageContainer.historicByTagBullsSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            case 9:
                switch (this.topFilter.intValue()) {
                    case 1:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.breedingByAgeAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.breedingByAgeCowsSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.breedingByAgeHeifersSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 2:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.breedingByWeightAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.breedingByWeightCowsSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.breedingByWeightHeifersSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    case 3:
                        switch (this.bottomFilter.intValue()) {
                            case 1:
                                StorageContainer.breedingByTagAllSmartList = tmpList;
                                return;
                            case 2:
                                StorageContainer.breedingByTagCowsSmartList = tmpList;
                                return;
                            case 3:
                                StorageContainer.breedingByTagHeifersSmartList = tmpList;
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            default:
                return;
        }
    }

    public void onImageClick(Integer numList) {
        this.animalInList = numList;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] list = new CharSequence[]{getString(C0530R.string.add_image)};
        if (StorageContainer.getAnimalImageMemoryCache().get(((Animal) this.animalList.get(this.animalInList.intValue())).getTagNumber()) != null) {
            list = new CharSequence[]{getString(C0530R.string.change_image), getString(C0530R.string.remove_image)};
        }
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    SmartListActivity.this.onAddImageClick();
                    return;
                }
                StorageContainer.getAnimalImageMemoryCache().remove(((Animal) SmartListActivity.this.animalList.get(SmartListActivity.this.animalInList.intValue())).getTagNumber());
                SmartListActivity.this.showProgress(true);
                SmartListActivity.this.registerReceiver(SmartListActivity.this.broadcastReceiver, new IntentFilter(QuickstartPreferences.CHANGE_ANIMAL_IMAGE));
                new AcquireResponseTask(SmartListActivity.this).execute(new String[]{new ChangeAnimalImageUrl(((Animal) SmartListActivity.this.animalList.get(SmartListActivity.this.animalInList.intValue())).getId().toString(), ((Animal) SmartListActivity.this.animalList.get(SmartListActivity.this.animalInList.intValue())).getType().toString()).createAndReturnUrl(SmartListActivity.this), QuickstartPreferences.CHANGE_ANIMAL_IMAGE});
            }
        });
        builder.show();
    }

    public void onAddImageClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{getString(C0530R.string.take_picture), getString(C0530R.string.from_gallery)}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MoocallImages");
                    imagesFolder.mkdirs();
                    File image = new File(imagesFolder, "MoocallImage_" + timeStamp + ".jpg");
                    SmartListActivity.this.cameraFileName = image.getAbsolutePath();
                    cameraIntent.putExtra("output", Uri.fromFile(image));
                    SmartListActivity.this.startActivityForResult(cameraIntent, SmartListActivity.this.TAKE_PHOTO_CODE);
                    return;
                }
                SmartListActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), SmartListActivity.this.LOAD_PHOTO_CODE);
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.TAKE_PHOTO_CODE && resultCode == -1) {
            performCrop(Uri.fromFile(new File(this.cameraFileName)));
        }
        if (requestCode == this.LOAD_PHOTO_CODE && resultCode == -1 && data != null) {
            String[] filePathColumn = new String[]{"_data"};
            Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            performCrop(Uri.fromFile(new File(picturePath)));
        }
        if (requestCode == this.RESULT_CROP && resultCode == -1) {
            Bundle extras = data.getExtras();
            Uri uri = data.getData();
            Bitmap imageBitmap = null;
            if (extras != null) {
                imageBitmap = (Bitmap) extras.getParcelable("data");
            } else if (uri != null) {
                imageBitmap = decodeUriAsBitmap(uri);
            }
            if (imageBitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(CompressFormat.JPEG, 100, baos);
                String encodedImage = Base64.encodeToString(baos.toByteArray(), 0);
                StorageContainer.getAnimalImageMemoryCache().put(((Animal) this.animalList.get(this.animalInList.intValue())).getTagNumber(), imageBitmap);
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.CHANGE_ANIMAL_IMAGE));
                AcquireResponseTask acquireResponseTask = new AcquireResponseTask(this);
                r6 = new String[4];
                r6[0] = new ChangeAnimalImageUrl(((Animal) this.animalList.get(this.animalInList.intValue())).getId().toString(), ((Animal) this.animalList.get(this.animalInList.intValue())).getType().toString()).createAndReturnUrl(this);
                r6[1] = QuickstartPreferences.CHANGE_ANIMAL_IMAGE;
                r6[2] = "encoded_image";
                r6[3] = encodedImage;
                acquireResponseTask.execute(r6);
            }
        }
    }

    private void imageChanged(String result) {
        showProgress(false);
        if (result.equals("\"success\"")) {
            this.smartListAdapter.notifyDataSetChanged();
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void performCrop(Uri contentUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(contentUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, this.RESULT_CROP);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(C0530R.string.crop_error_message), 0).show();
        }
    }

    public void createBroadcastReceiver() {
        this.additionalBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    if (intent.getAction().equals("refresh_smart_list")) {
                        SmartListActivity.this.actionAdded = Integer.valueOf(99);
                        SmartListActivity.this.setAnimation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void registerReceiver() {
        registerReceiver(this.additionalBroadcastReceiver, new IntentFilter("refresh_smart_list"));
        this.broadcastRegistered = Boolean.valueOf(true);
    }

    public void unregisterReceiver() {
        if (this.broadcastRegistered.booleanValue()) {
            unregisterReceiver(this.additionalBroadcastReceiver);
            this.broadcastRegistered = Boolean.valueOf(false);
        }
    }

    public void onBackPressed() {
        if (this.transparentBackground.getVisibility() == 0) {
            closeOverlay();
            return;
        }
        unregisterReceiver();
        finish();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void loadImage(final Animal animal) {
        try {
            String src = StorageContainer.socialHost + "/moocall_animal_images/" + animal.getImagePath();
            System.out.println("src: " + src);
            this.imageLoader.loadImage(src, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    System.out.println("BRAVOOOOOOOOOO");
                    StorageContainer.getAnimalImageMemoryCache().put(animal.getTagNumber(), loadedImage);
                    SmartListActivity.this.smartListAdapter.notifyDataSetChanged();
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
