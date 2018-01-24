package com.moocall.moocall;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.moocall.moocall.adapter.AnimalDetailsAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.db.SensorDb;
import com.moocall.moocall.db.SensorDbDao;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.domain.AnimalHistory;
import com.moocall.moocall.domain.AnimalNotes;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddAnimalActionUrl;
import com.moocall.moocall.url.DeleteAnimalActionUrl;
import com.moocall.moocall.url.DeleteCalvingDataUrl;
import com.moocall.moocall.url.EditAnimalActionUrl;
import com.moocall.moocall.url.GetAnimalDetailsUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONObject;

public class AnimalDetailsActivity extends Activity {
    private Integer actionAdded;
    private ScrollView actionOverview;
    private List<String> aiBullList;
    private HashMap<String, Integer> aiBullListHash;
    private Animal animal;
    private Calendar animalCalendar = Calendar.getInstance();
    private AnimalDbDao animalDbDao;
    private AnimalDetailsAdapter animalDetailsAdapter;
    private TextView animalDetailsCode;
    private ListView animalDetailsList;
    private SwipeRefreshLayout animalDetailsSwipeRefreshLayout;
    private TextView animalDetailsTitle;
    private List<String> assumeList;
    private Boolean breedingBull = Boolean.valueOf(true);
    private List<String> breedingBullList;
    private HashMap<String, Integer> breedingBullListHash;
    private BroadcastReceiver broadcastReceiver;
    private Boolean broadcastRegistred = Boolean.valueOf(false);
    private String bullId = "";
    private List<String> bullSensorList;
    private TextView cancelAction;
    private String cowId = "";
    private List<String> cowList;
    private HashMap<String, Integer> cowListHash;
    private List<String> cowSensorList;
    private List<String> fatScoreList;
    private Boolean first;
    private List<String> gradeList;
    private List<String> heatList;
    private ImageLoader imageLoader;
    private ArrayList<NameValuePair> nameValuePairs;
    private View progressView;
    private BroadcastReceiver refreshAnimalDetailsBroadcastReceiver;
    private SensorDbDao sensorDbDao;
    private Integer subtype;
    private Animal tmpAnimal;
    private Toolbar toolbar;
    private RelativeLayout transparentBackground;
    private List<String> weaningList;

    class C04201 implements OnClickListener {
        C04201() {
        }

        public void onClick(View v) {
            AnimalDetailsActivity.this.onBackPressed();
        }
    }

    class C04212 implements OnClickListener {
        C04212() {
        }

        public void onClick(View view) {
            AnimalDetailsActivity.this.closeOverlay();
        }
    }

    class C04224 extends BroadcastReceiver {
        C04224() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                AnimalDetailsActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.GET_ANIMAL_DETAILS)) {
                    AnimalDetailsActivity.this.populateAnimalDetails(new JSONObject(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.ADD_ANIMAL_ACTION)) {
                    AnimalDetailsActivity.this.actionAdded(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.EDIT_ANIMAL_ACTION)) {
                    AnimalDetailsActivity.this.actionAdded(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.DELETE_ANIMAL_ACTION)) {
                    AnimalDetailsActivity.this.actionAdded(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.DELETE_CALVING_DATA)) {
                    AnimalDetailsActivity.this.onDeleteCalvingDataCompleted(intent.getStringExtra("response"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C10913 implements OnRefreshListener {
        C10913() {
        }

        public void onRefresh() {
            if (AnimalDetailsActivity.this.animalDetailsSwipeRefreshLayout.isRefreshing()) {
                AnimalDetailsActivity.this.getAnimalDetails();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0530R.layout.activity_animal_details);
        onResume();
        Intent intent = getIntent();
        this.tmpAnimal = (Animal) intent.getSerializableExtra("animal");
        this.subtype = (Integer) intent.getSerializableExtra("subtype");
        this.first = Boolean.valueOf(true);
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.animalDetailsSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.animalDetailsSwipeRefreshLayout);
        setupToolbar();
        createAsyncBroadcast();
        getAnimalDetails();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C04201());
    }

    private void setupLayout() {
        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
        this.animalDbDao = daoSession.getAnimalDbDao();
        this.sensorDbDao = daoSession.getSensorDbDao();
        ImageLoaderConfiguration config = new Builder(this).build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.destroy();
        this.imageLoader.init(config);
        this.animalDetailsList = (ListView) findViewById(C0530R.id.animalDetailsList);
        this.animalDetailsAdapter = new AnimalDetailsAdapter(this, this.animal.getCowState());
        this.animalDetailsList.setAdapter(this.animalDetailsAdapter);
        this.animalDetailsTitle = (TextView) findViewById(C0530R.id.animalDetailsTitle);
        this.animalDetailsCode = (TextView) findViewById(C0530R.id.animalDetailsCode);
        this.actionOverview = (ScrollView) findViewById(C0530R.id.actionOverview);
        this.transparentBackground = (RelativeLayout) findViewById(C0530R.id.transparentBackground);
        this.cancelAction = (TextView) findViewById(C0530R.id.cancelAction);
    }

    public void implementListeners() {
        this.cancelAction.setOnClickListener(new C04212());
        this.animalDetailsSwipeRefreshLayout.setOnRefreshListener(new C10913());
    }

    private void closeOverlay() {
        this.transparentBackground.setVisibility(8);
        this.actionOverview.setVisibility(8);
        Utils.hideKeyboard(this);
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04224();
    }

    private void initLists() {
        this.heatList = Arrays.asList(new String[]{getString(C0530R.string.observed_standing_mount), getString(C0530R.string.slime_on_vulva), getString(C0530R.string.physical_signs_mounting), getString(C0530R.string.chin_ball_tail_paint), getString(C0530R.string.observed_heat_behaviour), getString(C0530R.string.assumed_heat), getString(C0530R.string.teaser_bull_activity)});
        this.weaningList = Arrays.asList(new String[]{getString(C0530R.string.yes), getString(C0530R.string.no)});
        this.fatScoreList = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});
        this.gradeList = Arrays.asList(new String[]{"E", "U", "R", "O", "P"});
        this.cowSensorList = new ArrayList();
        this.bullSensorList = new ArrayList();
        this.assumeList = Arrays.asList(new String[]{getString(C0530R.string.assumed_in_calf), getString(C0530R.string.scanned_in_calf), getString(C0530R.string.scanned_in_calf_twins)});
        this.aiBullList = new ArrayList();
        this.aiBullListHash = new HashMap();
        this.aiBullList.add(getString(C0530R.string.add_new));
        this.breedingBullList = new ArrayList();
        this.breedingBullListHash = new HashMap();
        this.breedingBullList.add(getString(C0530R.string.add_new));
        List<AnimalDb> bullBreedingList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(4))).orderAsc(Properties.Tag_number).list();
        if (bullBreedingList != null) {
            for (AnimalDb bullInList : bullBreedingList) {
                this.breedingBullList.add(bullInList.getTag_number());
                this.breedingBullListHash.put(bullInList.getTag_number(), bullInList.getAnimal_id());
            }
        }
        List<AnimalDb> bullAIList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(2))).orderAsc(Properties.Tag_number).list();
        if (bullAIList != null) {
            for (AnimalDb bullInList2 : bullAIList) {
                this.aiBullList.add(bullInList2.getTag_number());
                this.aiBullListHash.put(bullInList2.getTag_number(), bullInList2.getAnimal_id());
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

    public void getAnimalDetails() {
        if (!this.animalDetailsSwipeRefreshLayout.isRefreshing()) {
            showProgress(true);
        }
        try {
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_ANIMAL_DETAILS));
            new AcquireResponseTask(this).execute(new String[]{new GetAnimalDetailsUrl(this.tmpAnimal.getId().toString(), this.tmpAnimal.getType().toString()).createAndReturnUrl(this), QuickstartPreferences.GET_ANIMAL_DETAILS});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateAnimalDetails(JSONObject response) {
        try {
            this.animal = new Animal(new JSONParserBgw(response), Boolean.valueOf(true), this);
            if (!(this.animal.getImagePath() == null || this.animal.getImagePath().isEmpty() || StorageContainer.getAnimalImageMemoryCache().get(this.animal.getTagNumber()) != null)) {
                loadImage(this.animal);
            }
            if (this.first.booleanValue()) {
                this.first = Boolean.valueOf(false);
                setupLayout();
                implementListeners();
                initLists();
                createBroadcastReceiver();
            }
            this.toolbar.setTitle(this.animal.getName());
            setTitle();
            this.animalDetailsCode.setText(this.animal.getTagNumber());
            this.animalDetailsAdapter.setAnimal(this.animal);
            this.animalDetailsAdapter.notifyDataSetChanged();
            showProgress(false);
            this.animalDetailsSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTitle() {
        String dueDate;
        String dueDateText;
        switch (this.animal.getType().intValue()) {
            case 1:
                switch (this.animal.getCowState().intValue()) {
                    case 1:
                        dueDate = Utils.getTimeShorterReverse(this.animal.getDueDateServer());
                        dueDateText = "";
                        if (dueDate.equals("overdue")) {
                            dueDateText = getString(C0530R.string.overdue) + StringUtils.SPACE + Utils.getTimeShorter(this.animal.getDueDateServer() + " 00:00");
                        } else {
                            dueDateText = getString(C0530R.string.due_in) + StringUtils.SPACE + dueDate;
                        }
                        this.animalDetailsTitle.setText(getString(C0530R.string.incalf) + StringUtils.SPACE + getString(C0530R.string.cow) + " (" + dueDateText + ")");
                        return;
                    case 2:
                        this.animalDetailsTitle.setText(getString(C0530R.string.cycling) + StringUtils.SPACE + getString(C0530R.string.cow));
                        return;
                    case 3:
                        this.animalDetailsTitle.setText(getString(C0530R.string.inheat) + StringUtils.SPACE + getString(C0530R.string.cow));
                        return;
                    case 4:
                        this.animalDetailsTitle.setText(getString(C0530R.string.inseminated) + StringUtils.SPACE + getString(C0530R.string.cow));
                        return;
                    default:
                        this.animalDetailsTitle.setText(getString(C0530R.string.fresh) + StringUtils.SPACE + getString(C0530R.string.cow));
                        return;
                }
            case 2:
                this.animalDetailsTitle.setText(getString(C0530R.string.ai_straw) + StringUtils.SPACE + getString(C0530R.string.bull));
                return;
            case 3:
                switch (this.animal.getCowState().intValue()) {
                    case 1:
                        dueDate = Utils.getTimeShorterReverse(this.animal.getDueDateServer());
                        dueDateText = "";
                        if (dueDate.equals("overdue")) {
                            dueDateText = getString(C0530R.string.overdue) + StringUtils.SPACE + Utils.getTimeShorter(this.animal.getDueDateServer() + " 00:00");
                        } else {
                            dueDateText = getString(C0530R.string.due_in) + StringUtils.SPACE + dueDate;
                        }
                        this.animalDetailsTitle.setText(getString(C0530R.string.incalf) + StringUtils.SPACE + getString(C0530R.string.heifer) + " (" + dueDateText + ")");
                        return;
                    case 2:
                        this.animalDetailsTitle.setText(getString(C0530R.string.cycling) + StringUtils.SPACE + getString(C0530R.string.heifer));
                        return;
                    case 3:
                        this.animalDetailsTitle.setText(getString(C0530R.string.inheat) + StringUtils.SPACE + getString(C0530R.string.heifer));
                        return;
                    case 4:
                        this.animalDetailsTitle.setText(getString(C0530R.string.inseminated) + StringUtils.SPACE + getString(C0530R.string.heifer));
                        return;
                    default:
                        this.animalDetailsTitle.setText(getString(C0530R.string.fresh) + StringUtils.SPACE + getString(C0530R.string.heifer));
                        return;
                }
            case 4:
                this.animalDetailsTitle.setText(getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.bull));
                return;
            case 5:
                this.animalDetailsTitle.setText(getString(C0530R.string.beef) + StringUtils.SPACE + getString(C0530R.string.heifer));
                return;
            case 6:
                this.animalDetailsTitle.setText(getString(C0530R.string.beef) + StringUtils.SPACE + getString(C0530R.string.bull));
                return;
            case 7:
                this.animalDetailsTitle.setText(getString(C0530R.string.calf) + StringUtils.SPACE + getString(C0530R.string.heifer));
                return;
            case 8:
                this.animalDetailsTitle.setText(getString(C0530R.string.calf) + StringUtils.SPACE + getString(C0530R.string.bull));
                return;
            case 9:
                this.animalDetailsTitle.setText(getString(C0530R.string.bullock));
                return;
            case 10:
                this.animalDetailsTitle.setText(getString(C0530R.string.vasectomized) + StringUtils.SPACE + getString(C0530R.string.bull));
                return;
            default:
                return;
        }
    }

    public void showPreActionOverview(Integer action, AnimalHistory history) {
        if (this.subtype.intValue() != 8) {
            showActionOverview(action, history, null);
        } else {
            showUndoOverlay();
        }
    }

    public void showActionOverview(Integer action, AnimalHistory history, AnimalNotes note) {
        this.transparentBackground.setVisibility(0);
        this.actionOverview.setVisibility(0);
        TextView actionHeadline = (TextView) findViewById(C0530R.id.actionHeadline);
        LinearLayout firstActionsLayout = (LinearLayout) findViewById(C0530R.id.firstActionsLayout);
        TextView firstActionText = (TextView) findViewById(C0530R.id.firstActionText);
        TextView secondActionText = (TextView) findViewById(C0530R.id.secondActionText);
        TextView firstActionSelect = (TextView) findViewById(C0530R.id.firstActionSelect);
        EditText firstActionWeight = (EditText) findViewById(C0530R.id.firstActionWeight);
        TextView secondActionSelect = (TextView) findViewById(C0530R.id.secondActionSelect);
        EditText secondActionPrice = (EditText) findViewById(C0530R.id.secondActionPrice);
        LinearLayout secondActionsLayout = (LinearLayout) findViewById(C0530R.id.secondActionsLayout);
        LinearLayout inseminationRadio = (LinearLayout) findViewById(C0530R.id.inseminationRadio);
        RelativeLayout firstInseminationRadio = (RelativeLayout) findViewById(C0530R.id.firstInseminationRadio);
        RelativeLayout secondInseminationRadio = (RelativeLayout) findViewById(C0530R.id.secondInseminationRadio);
        ImageView firstInseminationFilterImage = (ImageView) findViewById(C0530R.id.firstInseminationFilterImage);
        ImageView secondInseminationFilterImage = (ImageView) findViewById(C0530R.id.secondInseminationFilterImage);
        TextView firstInseminationFilterText = (TextView) findViewById(C0530R.id.firstInseminationFilterText);
        TextView secondInseminationFilterText = (TextView) findViewById(C0530R.id.secondInseminationFilterText);
        TextView thirdActionText = (TextView) findViewById(C0530R.id.thirdActionText);
        TextView thirdActionSelect = (TextView) findViewById(C0530R.id.thirdActionSelect);
        TextView okAction = (TextView) findViewById(C0530R.id.okAction);
        TextView actionFirstText = (TextView) findViewById(C0530R.id.actionFirstText);
        EditText thirdActionEdit = (EditText) findViewById(C0530R.id.thirdActionEdit);
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
        secondActionPrice.setText("");
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
                setDropdown(thirdActionSelect, 8);
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
                setDatePicker(secondActionSelect, action);
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
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
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
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
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
            case 6:
                actionHeadline.setText(getString(C0530R.string.add_weaning));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.dark_green));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.weaned));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.date));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.spinner_bg));
                setDropdown(firstActionSelect, 2);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(secondActionSelect, action);
                break;
            case 7:
                actionHeadline.setText(getString(C0530R.string.slaughtered));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.fat_score));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.grade));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.spinner_bg));
                setDropdown(firstActionSelect, 3);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.spinner_bg));
                setDropdown(secondActionSelect, 4);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.dead_weight));
                thirdActionEdit.setVisibility(0);
                thirdActionEdit.setInputType(2);
                break;
            case 8:
                actionHeadline.setText(getString(C0530R.string.deceased));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, action);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.description));
                thirdActionEdit.setVisibility(0);
                thirdActionEdit.setInputType(1);
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
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.description));
                thirdActionSelect.setVisibility(0);
                setDropdown(thirdActionSelect, 7);
                break;
            case 12:
                actionHeadline.setText(getString(C0530R.string.add_tagging));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
                break;
            case 13:
                actionHeadline.setText(getString(C0530R.string.add_castration));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.red_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
                break;
            case 14:
                actionHeadline.setText(getString(C0530R.string.new_note));
                actionHeadline.setTextColor(getResources().getColor(C0530R.color.dark_gray_mh));
                firstActionsLayout.setVisibility(0);
                firstActionText.setVisibility(0);
                firstActionText.setText(getString(C0530R.string.date));
                secondActionText.setVisibility(0);
                secondActionText.setText(getString(C0530R.string.time));
                firstActionSelect.setVisibility(0);
                firstActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
                secondActionsLayout.setVisibility(0);
                thirdActionText.setVisibility(0);
                thirdActionText.setText(getString(C0530R.string.description));
                thirdActionEdit.setVisibility(0);
                thirdActionEdit.setInputType(1);
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
                setDatePicker(firstActionSelect, action);
                secondActionSelect.setVisibility(0);
                secondActionSelect.setBackgroundDrawable(getResources().getDrawable(C0530R.drawable.edit_text_background));
                setTimePicker(secondActionSelect, action);
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
                setDropdown(thirdActionSelect, 9);
                break;
        }
        if (!(history == null && note == null)) {
            populateOverview(action, firstActionSelect, firstActionWeight, secondActionSelect, secondActionPrice, thirdActionSelect, thirdActionEdit, history, note);
        }
        final Integer num = action;
        final TextView textView = firstActionSelect;
        final EditText editText = firstActionWeight;
        final TextView textView2 = secondActionSelect;
        final EditText editText2 = secondActionPrice;
        final TextView textView3 = thirdActionSelect;
        final EditText editText3 = thirdActionEdit;
        final AnimalHistory animalHistory = history;
        final AnimalNotes animalNotes = note;
        okAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsActivity.this.prepareSaveAction(num, textView, editText, textView2, editText2, textView3, editText3, animalHistory, animalNotes);
            }
        });
    }

    private void setDatePicker(final TextView tv, Integer action) {
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
                tv.setText(dateText + "-" + monthText + "-" + year);
            }
        };
        tv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(AnimalDetailsActivity.this, date, AnimalDetailsActivity.this.animalCalendar.get(1), AnimalDetailsActivity.this.animalCalendar.get(2), AnimalDetailsActivity.this.animalCalendar.get(5));
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });
        String year = "";
        String monthText = "";
        String dateText = "";
        if (action.intValue() != 10 || this.animal.getInseminationDate() == null || this.animal.getInseminationDate().equals("0000-00-00 00:00:00")) {
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
            String[] dateParts = Utils.calculateTime(this.animal.getInseminationDate(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[0].split("-");
            year = dateParts[2];
            monthText = dateParts[1];
            dateText = dateParts[0];
        }
        tv.setText(dateText + "-" + monthText + "-" + year);
    }

    private void setTimePicker(final TextView tv, Integer action) {
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
                new TimePickerDialog(AnimalDetailsActivity.this, time, AnimalDetailsActivity.this.animalCalendar.get(11), AnimalDetailsActivity.this.animalCalendar.get(12), true).show();
            }
        });
        String hourText = "";
        String minuteText = "";
        if (action.intValue() != 10 || this.animal.getInseminationDate() == null || this.animal.getInseminationDate().equals("0000-00-00 00:00:00")) {
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
            String[] timeParts = Utils.calculateTime(this.animal.getInseminationDate(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[1].split(":");
            hourText = timeParts[0];
            minuteText = timeParts[1];
        }
        tv.setText(hourText + ":" + minuteText);
    }

    private void setDropdown(final TextView tv, final int listID) {
        tv.setOnClickListener(new OnClickListener() {

            class C04191 implements DialogInterface.OnClickListener {
                C04191() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent;
                    switch (listID) {
                        case 1:
                            tv.setText(((String) AnimalDetailsActivity.this.heatList.get(which)).toString());
                            return;
                        case 2:
                            tv.setText(((String) AnimalDetailsActivity.this.weaningList.get(which)).toString());
                            return;
                        case 3:
                            tv.setText(((String) AnimalDetailsActivity.this.fatScoreList.get(which)).toString());
                            return;
                        case 4:
                            tv.setText(((String) AnimalDetailsActivity.this.gradeList.get(which)).toString());
                            return;
                        case 5:
                            if (which <= 0) {
                                AnimalDetailsActivity.this.registerReceiver();
                                intent = new Intent(AnimalDetailsActivity.this, ManualInputActivity.class);
                                intent.putExtra("newBull", true);
                                AnimalDetailsActivity.this.startActivity(intent);
                                return;
                            } else if (AnimalDetailsActivity.this.breedingBull.booleanValue()) {
                                tv.setText(((String) AnimalDetailsActivity.this.breedingBullList.get(which)).toString());
                                AnimalDetailsActivity.this.bullId = ((Integer) AnimalDetailsActivity.this.breedingBullListHash.get(((String) AnimalDetailsActivity.this.breedingBullList.get(which)).toString())).toString();
                                return;
                            } else {
                                tv.setText(((String) AnimalDetailsActivity.this.aiBullList.get(which)).toString());
                                AnimalDetailsActivity.this.bullId = ((Integer) AnimalDetailsActivity.this.aiBullListHash.get(((String) AnimalDetailsActivity.this.aiBullList.get(which)).toString())).toString();
                                return;
                            }
                        case 6:
                            if (which > 0) {
                                tv.setText(((String) AnimalDetailsActivity.this.cowList.get(which)).toString());
                                AnimalDetailsActivity.this.cowId = ((Integer) AnimalDetailsActivity.this.cowListHash.get(((String) AnimalDetailsActivity.this.cowList.get(which)).toString())).toString();
                                return;
                            }
                            AnimalDetailsActivity.this.registerReceiver();
                            intent = new Intent(AnimalDetailsActivity.this, ManualInputActivity.class);
                            intent.putExtra("newCow", true);
                            AnimalDetailsActivity.this.startActivity(intent);
                            return;
                        case 7:
                            tv.setText(((String) AnimalDetailsActivity.this.assumeList.get(which)).toString());
                            return;
                        case 8:
                            tv.setText(((String) AnimalDetailsActivity.this.cowSensorList.get(which)).toString());
                            return;
                        case 9:
                            tv.setText(((String) AnimalDetailsActivity.this.bullSensorList.get(which)).toString());
                            return;
                        default:
                            return;
                    }
                }
            }

            public void onClick(View v) {
                AnimalDetailsActivity.this.initLists();
                AlertDialog.Builder builder = new AlertDialog.Builder(AnimalDetailsActivity.this);
                CharSequence[] list = new CharSequence[0];
                switch (listID) {
                    case 1:
                        list = (CharSequence[]) AnimalDetailsActivity.this.heatList.toArray(new CharSequence[AnimalDetailsActivity.this.heatList.size()]);
                        break;
                    case 2:
                        list = (CharSequence[]) AnimalDetailsActivity.this.weaningList.toArray(new CharSequence[AnimalDetailsActivity.this.weaningList.size()]);
                        break;
                    case 3:
                        list = (CharSequence[]) AnimalDetailsActivity.this.fatScoreList.toArray(new CharSequence[AnimalDetailsActivity.this.fatScoreList.size()]);
                        break;
                    case 4:
                        list = (CharSequence[]) AnimalDetailsActivity.this.gradeList.toArray(new CharSequence[AnimalDetailsActivity.this.gradeList.size()]);
                        break;
                    case 5:
                        if (!AnimalDetailsActivity.this.breedingBull.booleanValue()) {
                            list = (CharSequence[]) AnimalDetailsActivity.this.aiBullList.toArray(new CharSequence[AnimalDetailsActivity.this.aiBullList.size()]);
                            break;
                        } else {
                            list = (CharSequence[]) AnimalDetailsActivity.this.breedingBullList.toArray(new CharSequence[AnimalDetailsActivity.this.breedingBullList.size()]);
                            break;
                        }
                    case 6:
                        list = (CharSequence[]) AnimalDetailsActivity.this.cowList.toArray(new CharSequence[AnimalDetailsActivity.this.cowList.size()]);
                        break;
                    case 7:
                        list = (CharSequence[]) AnimalDetailsActivity.this.assumeList.toArray(new CharSequence[AnimalDetailsActivity.this.assumeList.size()]);
                        break;
                    case 8:
                        list = (CharSequence[]) AnimalDetailsActivity.this.cowSensorList.toArray(new CharSequence[AnimalDetailsActivity.this.cowSensorList.size()]);
                        break;
                    case 9:
                        list = (CharSequence[]) AnimalDetailsActivity.this.bullSensorList.toArray(new CharSequence[AnimalDetailsActivity.this.bullSensorList.size()]);
                        break;
                }
                builder.setItems(list, new C04191());
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
                textView.setTextColor(AnimalDetailsActivity.this.getResources().getColor(C0530R.color.green_mh));
                textView2.setTextColor(AnimalDetailsActivity.this.getResources().getColor(C0530R.color.gray_mh));
                imageView.setImageDrawable(AnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                imageView2.setImageDrawable(AnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                AnimalDetailsActivity.this.breedingBull = Boolean.valueOf(true);
            }
        });
        textView = firstT;
        textView2 = secondT;
        imageView = firstI;
        imageView2 = secondI;
        secondL.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                textView.setTextColor(AnimalDetailsActivity.this.getResources().getColor(C0530R.color.gray_mh));
                textView2.setTextColor(AnimalDetailsActivity.this.getResources().getColor(C0530R.color.green_mh));
                imageView.setImageDrawable(AnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                imageView2.setImageDrawable(AnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                AnimalDetailsActivity.this.breedingBull = Boolean.valueOf(false);
            }
        });
    }

    public void editAnimal() {
        if (this.subtype.intValue() != 8) {
            registerReceiver();
            Intent intent = new Intent(this, ManualInputActivity.class);
            intent.putExtra("animal", this.animal);
            startActivity(intent);
            return;
        }
        showUndoOverlay();
    }

    public void addCalving() {
        registerReceiver();
        Intent intent = new Intent(this, NewCalvingActivity.class);
        intent.putExtra("Cow", this.animal);
        startActivity(intent);
    }

    private void populateOverview(Integer action, TextView fAS, EditText fAW, TextView sAS, EditText sAP, TextView tAS, EditText tAE, AnimalHistory history, AnimalNotes note) {
        switch (action.intValue()) {
            case 0:
                tAS.setText(history.getSensor());
                return;
            case 2:
                fAW.setText(history.getText().split(StringUtils.SPACE)[0]);
                sAS.setText(history.getDate());
                return;
            case 3:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                tAS.setText(history.getText());
                return;
            case 4:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                String[] bullNameTag = history.getText().split(StringUtils.SPACE);
                if (bullNameTag.length > 1) {
                    if (this.breedingBullList.contains(bullNameTag[1])) {
                        this.bullId = ((Integer) this.breedingBullListHash.get(((String) this.breedingBullList.get(this.breedingBullList.indexOf(bullNameTag[1]))).toString())).toString();
                    }
                    if (this.aiBullList.contains(bullNameTag[1])) {
                        this.bullId = ((Integer) this.aiBullListHash.get(((String) this.aiBullList.get(this.aiBullList.indexOf(bullNameTag[1]))).toString())).toString();
                    }
                    tAS.setText(bullNameTag[1]);
                    return;
                }
                return;
            case 6:
                fAW.setText(getString(C0530R.string.weaned));
                sAS.setText(history.getDate());
                return;
            case 7:
                fAS.setText(history.getFatScore());
                sAS.setText(history.getGrade());
                tAE.setText(history.getDeadWeight());
                return;
            case 8:
                fAS.setText(history.getDate());
                tAE.setText(history.getText());
                return;
            case 9:
                fAW.setText(history.getSoldWeight());
                sAP.setText(history.getSoldPrice());
                tAE.setText(history.getText());
                return;
            case 10:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                tAS.setText(history.getText());
                return;
            case 12:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                return;
            case 13:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                return;
            case 14:
                if (note != null) {
                    if (note.getDate() != null) {
                        fAS.setText(note.getDate());
                    } else {
                        fAS.setText("");
                    }
                    sAS.setText(note.getTime());
                    tAE.setText(note.getText());
                    return;
                }
                return;
            case 17:
                tAS.setText(history.getSensor());
                return;
            default:
                return;
        }
    }

    private String getHours() {
        String hourText;
        String minuteText;
        int hourOfDay = Calendar.getInstance().get(11);
        int minute = Calendar.getInstance().get(12);
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

    private void prepareSaveAction(Integer action, TextView fAS, EditText fAW, TextView sAS, EditText sAP, TextView tAS, EditText tAE, AnimalHistory history, AnimalNotes note) {
        this.nameValuePairs = new ArrayList();
        this.actionAdded = action;
        switch (action.intValue()) {
            case 0:
                this.nameValuePairs.add(new BasicNameValuePair("sensor", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 2:
                this.nameValuePairs.add(new BasicNameValuePair("weight", Utils.getWeight(fAW.getText().toString(), this)));
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(sAS.getText().toString()) + StringUtils.SPACE + getHours() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 3:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 4:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("bull-tag-number", !this.bullId.equals("") ? tAS.getText().toString() : ""));
                this.nameValuePairs.add(new BasicNameValuePair("bull-id", this.bullId));
                break;
            case 5:
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 6:
                this.nameValuePairs.add(new BasicNameValuePair("weaned", fAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(sAS.getText().toString()) + StringUtils.SPACE + getHours() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 7:
                this.nameValuePairs.add(new BasicNameValuePair("fat-score", fAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("grade", sAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("dead-weight", tAE.getText().toString()));
                break;
            case 8:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + getHours() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAE.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 9:
                this.nameValuePairs.add(new BasicNameValuePair("weight", Utils.getWeight(fAW.getText().toString(), this)));
                this.nameValuePairs.add(new BasicNameValuePair(Param.PRICE, sAP.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAE.getText().toString()));
                break;
            case 10:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("description", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 12:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 13:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 14:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("text", tAE.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
            case 15:
                this.nameValuePairs.add(new BasicNameValuePair("datetime", Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00")));
                this.nameValuePairs.add(new BasicNameValuePair("cow-tag-number", !this.cowId.equals("") ? tAS.getText().toString() : ""));
                this.nameValuePairs.add(new BasicNameValuePair("cow-id", this.cowId));
                break;
            case 17:
                this.nameValuePairs.add(new BasicNameValuePair("sensor", tAS.getText().toString()));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                this.nameValuePairs.add(new BasicNameValuePair("", ""));
                break;
        }
        if (history != null) {
            saveEditAction(history.getId());
        } else if (note != null) {
            saveEditAction(note.getId());
        } else {
            saveAction(null);
        }
    }

    private void saveAction(String response) {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_ANIMAL_ACTION));
        try {
            new AcquireResponseTask(this).execute(new String[]{new AddAnimalActionUrl(this.actionAdded.toString(), this.animal.getType().toString(), this.animal.getId().toString(), response).createAndReturnUrl(this), QuickstartPreferences.ADD_ANIMAL_ACTION, "tag-number", this.animal.getTagNumber(), ((NameValuePair) this.nameValuePairs.get(0)).getName(), ((NameValuePair) this.nameValuePairs.get(0)).getValue(), ((NameValuePair) this.nameValuePairs.get(1)).getName(), ((NameValuePair) this.nameValuePairs.get(1)).getValue(), ((NameValuePair) this.nameValuePairs.get(2)).getName(), ((NameValuePair) this.nameValuePairs.get(2)).getValue()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveEditAction(Integer actionId) {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.EDIT_ANIMAL_ACTION));
        new AcquireResponseTask(this).execute(new String[]{new EditAnimalActionUrl(actionId.toString(), this.actionAdded.toString(), this.animal.getType().toString(), this.animal.getId().toString()).createAndReturnUrl(this), QuickstartPreferences.EDIT_ANIMAL_ACTION, ((NameValuePair) this.nameValuePairs.get(0)).getName(), ((NameValuePair) this.nameValuePairs.get(0)).getValue(), ((NameValuePair) this.nameValuePairs.get(1)).getName(), ((NameValuePair) this.nameValuePairs.get(1)).getValue(), ((NameValuePair) this.nameValuePairs.get(2)).getName(), ((NameValuePair) this.nameValuePairs.get(2)).getValue()});
    }

    private void actionAdded(String result) {
        try {
            showProgress(false);
            if (result.equals("\"success\"")) {
                Toast.makeText(this, result, 1).show();
                closeOverlay();
                getAnimalDetails();
                sendBroadcast(new Intent("refresh_lists"));
                sendBroadcast(new Intent("refresh_smart_list"));
                sendBroadcast(new Intent("refresh_animal_list"));
                this.nameValuePairs = null;
                this.actionAdded = null;
            } else if (result.equals("\"action failed\"")) {
                Toast.makeText(this, result, 1).show();
            } else if (this.actionAdded != null && this.nameValuePairs != null) {
                showPopup(result.substring(1, result.length() - 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeleteCalvingDataCompleted(String result) {
        try {
            showProgress(false);
            if (result.equals("\"success\"")) {
                getAnimalDetails();
                sendBroadcast(new Intent("refresh_lists"));
                sendBroadcast(new Intent("refresh_smart_list"));
                sendBroadcast(new Intent("refresh_animal_list"));
                showMessage();
                return;
            }
            Toast.makeText(this, result, 1).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopup(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage((CharSequence) message);
        if (message.contains("history")) {
            alertDialogBuilder.setPositiveButton(getString(C0530R.string.save_in_history), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AnimalDetailsActivity.this.saveAction("2");
                }
            });
        } else {
            alertDialogBuilder.setPositiveButton(getString(C0530R.string.change_state), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AnimalDetailsActivity.this.saveAction("1");
                }
            });
        }
        alertDialogBuilder.setNeutralButton(getString(C0530R.string.cancel_action), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AnimalDetailsActivity.this.closeOverlay();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private void showMessage() {
        CharSequence message = getString(C0530R.string.delete_calving_text);
        CharSequence title = getString(C0530R.string.delete_calving);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message).setTitle(title);
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AnimalDetailsActivity.this.finish();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    public void historyClicked(AnimalHistory history) {
        if (this.subtype.intValue() != 8) {
            showActionOverlay(history, null);
        } else {
            showUndoOverlay();
        }
    }

    public void noteClicked(AnimalNotes note) {
        if (this.subtype.intValue() != 8) {
            showActionOverlay(null, note);
        } else {
            showUndoOverlay();
        }
    }

    private void showUndoOverlay() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.undo_text)).setTitle(getString(C0530R.string.undo_title));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private void showActionOverlay(final AnimalHistory history, final AnimalNotes note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{getString(C0530R.string.edit).toUpperCase(), getString(C0530R.string.delete).toUpperCase()}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (history == null || history.getType().intValue() != 1) {
                            AnimalDetailsActivity.this.editAction(history, note);
                            return;
                        }
                        AnimalDetailsActivity.this.registerReceiver();
                        Intent intent = new Intent(AnimalDetailsActivity.this, NewCalvingActivity.class);
                        intent.putExtra("Cow", AnimalDetailsActivity.this.tmpAnimal);
                        intent.putExtra("calvingId", history.getCalvingId());
                        intent.putExtra("historyId", history.getId());
                        AnimalDetailsActivity.this.startActivity(intent);
                        return;
                    case 1:
                        if (history == null || history.getType().intValue() != 1) {
                            AnimalDetailsActivity.this.deleteAction(history, note);
                            return;
                        } else {
                            AnimalDetailsActivity.this.deleteCalving(history);
                            return;
                        }
                    default:
                        return;
                }
            }
        });
        builder.show();
    }

    private void editAction(AnimalHistory history, AnimalNotes note) {
        if (history != null) {
            showActionOverview(history.getType(), history, null);
        } else {
            showActionOverview(Integer.valueOf(14), null, note);
        }
    }

    public void deleteCalving(final AnimalHistory history) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.calving_delete_text)).setTitle(getString(C0530R.string.delete_calving));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AnimalDetailsActivity.this.deleteThisCalving(history);
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(C0530R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private void deleteThisCalving(AnimalHistory history) {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_CALVING_DATA));
        new AcquireResponseTask(this).execute(new String[]{new DeleteCalvingDataUrl(history.getCalvingId().toString(), history.getId().toString(), this.animal.getId().toString(), this.animal.getType().toString(), String.valueOf(history.getCalvingNumber())).createAndReturnUrl(this), QuickstartPreferences.DELETE_CALVING_DATA});
    }

    private void deleteAction(final AnimalHistory history, final AnimalNotes note) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.item_delete_text)).setTitle(getString(C0530R.string.delete_item));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AnimalDetailsActivity.this.showProgress(true);
                AnimalDetailsActivity.this.registerReceiver(AnimalDetailsActivity.this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_ANIMAL_ACTION));
                String historyType = "";
                String historyId = "";
                if (history != null) {
                    historyType = history.getType().toString();
                    historyId = history.getId().toString();
                } else {
                    historyType = "14";
                    historyId = note.getId().toString();
                }
                new AcquireResponseTask(AnimalDetailsActivity.this).execute(new String[]{new DeleteAnimalActionUrl(historyId, historyType, AnimalDetailsActivity.this.animal.getType().toString(), AnimalDetailsActivity.this.animal.getId().toString()).createAndReturnUrl(AnimalDetailsActivity.this), QuickstartPreferences.DELETE_ANIMAL_ACTION});
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(C0530R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    public void openMoocallTagOverlay() {
        final RelativeLayout moocallTagOverlay = (RelativeLayout) findViewById(C0530R.id.moocallTagOverlay);
        LinearLayout rescanTag = (LinearLayout) findViewById(C0530R.id.rescanTag);
        final TextView moocallTagNumber = (TextView) findViewById(C0530R.id.moocallTagNumber);
        TextView closeTagNumber = (TextView) findViewById(C0530R.id.closeTagNumber);
        moocallTagOverlay.setVisibility(0);
        moocallTagNumber.setText(this.animal.getMoocallTagNumber());
        rescanTag.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AnimalDetailsActivity.this, ScanMoocallTagActivity.class);
                intent.putExtra("animalTag", AnimalDetailsActivity.this.animal.getTagNumber());
                AnimalDetailsActivity.this.startActivity(intent);
            }
        });
        closeTagNumber.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                moocallTagOverlay.setVisibility(8);
                moocallTagNumber.setText("");
            }
        });
    }

    public void createBroadcastReceiver() {
        this.refreshAnimalDetailsBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("refresh_animal_details")) {
                    AnimalDetailsActivity.this.getAnimalDetails();
                } else if (action.equals("animal_deleted")) {
                    AnimalDetailsActivity.this.unregisterReceiver();
                    AnimalDetailsActivity.this.finish();
                }
            }
        };
    }

    public void registerReceiver() {
        registerReceiver(this.refreshAnimalDetailsBroadcastReceiver, new IntentFilter("refresh_animal_details"));
        registerReceiver(this.refreshAnimalDetailsBroadcastReceiver, new IntentFilter("animal_deleted"));
        this.broadcastRegistred = Boolean.valueOf(true);
    }

    public void unregisterReceiver() {
        if (this.broadcastRegistred.booleanValue()) {
            unregisterReceiver(this.refreshAnimalDetailsBroadcastReceiver);
            this.broadcastRegistred = Boolean.valueOf(false);
        }
    }

    public void onBackPressed() {
        if (this.transparentBackground == null || this.transparentBackground.getVisibility() != 0) {
            unregisterReceiver();
            finish();
            return;
        }
        closeOverlay();
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
                    AnimalDetailsActivity.this.animalDetailsAdapter.notifyDataSetChanged();
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
