package com.moocall.moocall;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.moocall.moocall.adapter.OfflineAnimalDetailsAdapter;
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
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.WhereCondition;

public class OfflineAnimalDetailsActivity extends AppCompatActivity implements OnCheckInternetCompleted {
    private ScrollView actionOverview;
    private List<String> aiBullList;
    private HashMap<String, Integer> aiBullListHash;
    private AnimalDb animal;
    private AnimalActionDbDao animalActionDbDao;
    private Calendar animalCalendar = Calendar.getInstance();
    private AnimalDbDao animalDbDao;
    private OfflineAnimalDetailsAdapter animalDetailsAdapter;
    private TextView animalDetailsCode;
    private ListView animalDetailsList;
    private SwipeRefreshLayout animalDetailsSwipeRefreshLayout;
    private TextView animalDetailsTitle;
    private List<String> assumeList;
    private Boolean breedingBull = Boolean.valueOf(true);
    private List<String> breedingBullList;
    private HashMap<String, Integer> breedingBullListHash;
    private String bullId = "";
    private List<String> bullSensorList;
    private TextView cancelAction;
    private String cowId = "";
    private List<String> cowList;
    private HashMap<String, Integer> cowListHash;
    private List<String> cowSensorList;
    private List<String> fatScoreList;
    private List<String> gradeList;
    private List<String> heatList;
    private Long id;
    private SharedPreferences pref;
    private View progressView;
    private SensorDbDao sensorDbDao;
    private Integer subtype;
    private Toolbar toolbar;
    private RelativeLayout transparentBackground;
    private UserCredentials userCredentials;
    private List<String> weaningList;

    class C04861 implements OnClickListener {
        C04861() {
        }

        public void onClick(View v) {
            OfflineAnimalDetailsActivity.this.onBackPressed();
        }
    }

    class C04872 implements OnClickListener {
        C04872() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsActivity.this.closeOverlay();
        }
    }

    class C04884 implements OnClickListener {
        C04884() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsActivity.this.showProgress(true);
            new CheckInternetConnection(OfflineAnimalDetailsActivity.this, OfflineAnimalDetailsActivity.this).execute(new String[0]);
        }
    }

    class C11003 implements OnRefreshListener {
        C11003() {
        }

        public void onRefresh() {
            if (OfflineAnimalDetailsActivity.this.animalDetailsSwipeRefreshLayout.isRefreshing()) {
                OfflineAnimalDetailsActivity.this.getAnimalDetails();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_offline_animal_details);
        Intent intent = getIntent();
        this.id = (Long) intent.getSerializableExtra("id");
        this.subtype = (Integer) intent.getSerializableExtra("subtype");
        setupToolbar();
        setupLayout();
        implementListeners();
        initLists();
        onResume();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        this.toolbar.setNavigationOnClickListener(new C04861());
    }

    private void setupLayout() {
        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
        this.animalDbDao = daoSession.getAnimalDbDao();
        this.sensorDbDao = daoSession.getSensorDbDao();
        this.animalActionDbDao = daoSession.getAnimalActionDbDao();
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.animalDetailsSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.animalDetailsSwipeRefreshLayout);
        this.animalDetailsList = (ListView) findViewById(C0530R.id.animalDetailsList);
        this.animalDetailsAdapter = new OfflineAnimalDetailsAdapter(this);
        this.animalDetailsList.setAdapter(this.animalDetailsAdapter);
        this.animalDetailsTitle = (TextView) findViewById(C0530R.id.animalDetailsTitle);
        this.animalDetailsCode = (TextView) findViewById(C0530R.id.animalDetailsCode);
        this.actionOverview = (ScrollView) findViewById(C0530R.id.actionOverview);
        this.transparentBackground = (RelativeLayout) findViewById(C0530R.id.transparentBackground);
        this.cancelAction = (TextView) findViewById(C0530R.id.cancelAction);
    }

    public void implementListeners() {
        this.cancelAction.setOnClickListener(new C04872());
        this.animalDetailsSwipeRefreshLayout.setOnRefreshListener(new C11003());
        ((RelativeLayout) findViewById(C0530R.id.offlineMenu)).setOnClickListener(new C04884());
    }

    private void closeOverlay() {
        this.transparentBackground.setVisibility(8);
        this.actionOverview.setVisibility(8);
        Utils.hideKeyboard(this);
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

    private void setTitle() {
        String dueDate;
        String dueDateText;
        switch (this.animal.getType().intValue()) {
            case 1:
                switch (this.animal.getLast_state().intValue()) {
                    case 1:
                        dueDate = Utils.getTimeShorterReverse(this.animal.getDue_date());
                        dueDateText = "";
                        if (dueDate.equals("overdue")) {
                            dueDateText = getString(C0530R.string.overdue) + StringUtils.SPACE + Utils.getTimeShorter(this.animal.getDue_date() + " 00:00");
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
                switch (this.animal.getLast_state().intValue()) {
                    case 1:
                        dueDate = Utils.getTimeShorterReverse(this.animal.getDue_date());
                        dueDateText = "";
                        if (dueDate.equals("overdue")) {
                            dueDateText = getString(C0530R.string.overdue) + StringUtils.SPACE + Utils.getTimeShorter(this.animal.getDue_date() + " 00:00");
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

    public void showPreActionOverview(Integer action, AnimalActionDb history) {
        if (this.subtype.intValue() != 8) {
            showActionOverview(action, history);
        } else {
            showUndoOverlay();
        }
    }

    public void showActionOverview(Integer action, AnimalActionDb history) {
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
        if (history != null) {
            populateOverview(action, firstActionSelect, firstActionWeight, secondActionSelect, secondActionPrice, thirdActionSelect, thirdActionEdit, history);
        }
        final Integer num = action;
        final TextView textView = firstActionSelect;
        final EditText editText = firstActionWeight;
        final TextView textView2 = secondActionSelect;
        final EditText editText2 = secondActionPrice;
        final TextView textView3 = thirdActionSelect;
        final EditText editText3 = thirdActionEdit;
        final AnimalActionDb animalActionDb = history;
        okAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsActivity.this.prepareSaveAction(num, textView, editText, textView2, editText2, textView3, editText3, animalActionDb);
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
                DatePickerDialog dateDialog = new DatePickerDialog(OfflineAnimalDetailsActivity.this, date, OfflineAnimalDetailsActivity.this.animalCalendar.get(1), OfflineAnimalDetailsActivity.this.animalCalendar.get(2), OfflineAnimalDetailsActivity.this.animalCalendar.get(5));
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });
        String year = "";
        String monthText = "";
        String dateText = "";
        if (action.intValue() != 10 || this.animal.getInsemination_date() == null || this.animal.getInsemination_date().equals("0000-00-00 00:00:00")) {
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
            String[] dateParts = Utils.calculateTime(this.animal.getInsemination_date(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[0].split("-");
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
                new TimePickerDialog(OfflineAnimalDetailsActivity.this, time, OfflineAnimalDetailsActivity.this.animalCalendar.get(11), OfflineAnimalDetailsActivity.this.animalCalendar.get(12), true).show();
            }
        });
        String hourText = "";
        String minuteText = "";
        if (action.intValue() != 10 || this.animal.getInsemination_date() == null || this.animal.getInsemination_date().equals("0000-00-00 00:00:00")) {
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
            String[] timeParts = Utils.calculateTime(this.animal.getInsemination_date(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE)[1].split(":");
            hourText = timeParts[0];
            minuteText = timeParts[1];
        }
        tv.setText(hourText + ":" + minuteText);
    }

    private void setDropdown(final TextView tv, final int listID) {
        tv.setOnClickListener(new OnClickListener() {

            class C04851 implements DialogInterface.OnClickListener {
                C04851() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent;
                    switch (listID) {
                        case 1:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.heatList.get(which)).toString());
                            return;
                        case 2:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.weaningList.get(which)).toString());
                            return;
                        case 3:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.fatScoreList.get(which)).toString());
                            return;
                        case 4:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.gradeList.get(which)).toString());
                            return;
                        case 5:
                            if (which <= 0) {
                                intent = new Intent(OfflineAnimalDetailsActivity.this, OfflineManualInputActivity.class);
                                intent.putExtra("newBull", true);
                                OfflineAnimalDetailsActivity.this.startActivity(intent);
                                return;
                            } else if (OfflineAnimalDetailsActivity.this.breedingBull.booleanValue()) {
                                tv.setText(((String) OfflineAnimalDetailsActivity.this.breedingBullList.get(which)).toString());
                                OfflineAnimalDetailsActivity.this.bullId = ((Integer) OfflineAnimalDetailsActivity.this.breedingBullListHash.get(((String) OfflineAnimalDetailsActivity.this.breedingBullList.get(which)).toString())).toString();
                                return;
                            } else {
                                tv.setText(((String) OfflineAnimalDetailsActivity.this.aiBullList.get(which)).toString());
                                OfflineAnimalDetailsActivity.this.bullId = ((Integer) OfflineAnimalDetailsActivity.this.aiBullListHash.get(((String) OfflineAnimalDetailsActivity.this.aiBullList.get(which)).toString())).toString();
                                return;
                            }
                        case 6:
                            if (which > 0) {
                                tv.setText(((String) OfflineAnimalDetailsActivity.this.cowList.get(which)).toString());
                                OfflineAnimalDetailsActivity.this.cowId = ((Integer) OfflineAnimalDetailsActivity.this.cowListHash.get(((String) OfflineAnimalDetailsActivity.this.cowList.get(which)).toString())).toString();
                                return;
                            }
                            intent = new Intent(OfflineAnimalDetailsActivity.this, OfflineManualInputActivity.class);
                            intent.putExtra("newCow", true);
                            OfflineAnimalDetailsActivity.this.startActivity(intent);
                            return;
                        case 7:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.assumeList.get(which)).toString());
                            return;
                        case 8:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.cowSensorList.get(which)).toString());
                            return;
                        case 9:
                            tv.setText(((String) OfflineAnimalDetailsActivity.this.bullSensorList.get(which)).toString());
                            return;
                        default:
                            return;
                    }
                }
            }

            public void onClick(View v) {
                OfflineAnimalDetailsActivity.this.initLists();
                Builder builder = new Builder(OfflineAnimalDetailsActivity.this);
                CharSequence[] list = new CharSequence[0];
                switch (listID) {
                    case 1:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.heatList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.heatList.size()]);
                        break;
                    case 2:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.weaningList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.weaningList.size()]);
                        break;
                    case 3:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.fatScoreList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.fatScoreList.size()]);
                        break;
                    case 4:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.gradeList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.gradeList.size()]);
                        break;
                    case 5:
                        if (!OfflineAnimalDetailsActivity.this.breedingBull.booleanValue()) {
                            list = (CharSequence[]) OfflineAnimalDetailsActivity.this.aiBullList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.aiBullList.size()]);
                            break;
                        } else {
                            list = (CharSequence[]) OfflineAnimalDetailsActivity.this.breedingBullList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.breedingBullList.size()]);
                            break;
                        }
                    case 6:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.cowList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.cowList.size()]);
                        break;
                    case 7:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.assumeList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.assumeList.size()]);
                        break;
                    case 8:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.cowSensorList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.cowSensorList.size()]);
                        break;
                    case 9:
                        list = (CharSequence[]) OfflineAnimalDetailsActivity.this.bullSensorList.toArray(new CharSequence[OfflineAnimalDetailsActivity.this.bullSensorList.size()]);
                        break;
                }
                builder.setItems(list, new C04851());
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
                textView.setTextColor(OfflineAnimalDetailsActivity.this.getResources().getColor(C0530R.color.green_mh));
                textView2.setTextColor(OfflineAnimalDetailsActivity.this.getResources().getColor(C0530R.color.gray_mh));
                imageView.setImageDrawable(OfflineAnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                imageView2.setImageDrawable(OfflineAnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                OfflineAnimalDetailsActivity.this.breedingBull = Boolean.valueOf(true);
            }
        });
        textView = firstT;
        textView2 = secondT;
        imageView = firstI;
        imageView2 = secondI;
        secondL.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                textView.setTextColor(OfflineAnimalDetailsActivity.this.getResources().getColor(C0530R.color.gray_mh));
                textView2.setTextColor(OfflineAnimalDetailsActivity.this.getResources().getColor(C0530R.color.green_mh));
                imageView.setImageDrawable(OfflineAnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.grey_radio));
                imageView2.setImageDrawable(OfflineAnimalDetailsActivity.this.getResources().getDrawable(C0530R.drawable.green_radio));
                OfflineAnimalDetailsActivity.this.breedingBull = Boolean.valueOf(false);
            }
        });
    }

    public void editAnimal() {
        if (this.subtype.intValue() != 8) {
            Intent intent = new Intent(this, OfflineManualInputActivity.class);
            intent.putExtra("animal", this.animal);
            startActivity(intent);
            return;
        }
        showUndoOverlay();
    }

    public void addCalving() {
        Intent intent = new Intent(this, NewCalvingActivity.class);
        intent.putExtra("CowDb", this.animal);
        startActivity(intent);
    }

    private void populateOverview(Integer action, TextView fAS, EditText fAW, TextView sAS, EditText sAP, TextView tAS, EditText tAE, AnimalActionDb history) {
        switch (action.intValue()) {
            case 0:
                tAS.setText(history.getSensor());
                return;
            case 2:
                fAW.setText(history.getWeight());
                sAS.setText(history.getDate());
                return;
            case 3:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                tAS.setText(history.getDescription());
                return;
            case 4:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                tAS.setText(history.getBull_tag_number());
                this.bullId = history.getBull_id();
                return;
            case 6:
                fAW.setText(getString(C0530R.string.weaned));
                sAS.setText(history.getDate());
                return;
            case 7:
                fAS.setText(history.getFat_score());
                sAS.setText(history.getGrade());
                tAE.setText(history.getDead_weight());
                return;
            case 8:
                fAS.setText(history.getDate());
                tAE.setText(history.getDescription());
                return;
            case 9:
                fAW.setText(history.getWeight());
                sAP.setText(history.getPrice());
                tAE.setText(history.getDescription());
                return;
            case 10:
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                tAS.setText(history.getDescription());
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
                fAS.setText(history.getDate());
                sAS.setText(history.getTime());
                tAE.setText(history.getText());
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

    private void prepareSaveAction(Integer action, TextView fAS, EditText fAW, TextView sAS, EditText sAP, TextView tAS, EditText tAE, AnimalActionDb history) {
        showProgress(true);
        AnimalActionDb animalActionDb = new AnimalActionDb();
        animalActionDb.setAction(action);
        animalActionDb.setAnimal_id(this.animal.getAnimal_id().toString());
        animalActionDb.setAnimal_tag_number(this.animal.getTag_number());
        animalActionDb.setAnimal_type(this.animal.getType().toString());
        switch (action.intValue()) {
            case 0:
                animalActionDb.setSensor(tAS.getText().toString());
                this.animal.setSensor(tAS.getText().toString());
                break;
            case 2:
                if (history == null) {
                    setLastState(action);
                }
                animalActionDb.setWeight(Utils.getWeight(fAW.getText().toString(), this));
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(sAS.getText().toString()) + StringUtils.SPACE + getHours() + ":00"));
                this.animal.setWeight(Utils.getWeight(fAW.getText().toString(), this));
                break;
            case 3:
                if (history == null) {
                    setLastState(action);
                }
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setDescription(tAS.getText().toString());
                this.animal.setLast_state(Integer.valueOf(3));
                this.animal.setIn_heat_date(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                break;
            case 4:
                if (history == null) {
                    setLastState(action);
                }
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setBull_tag_number(!this.bullId.equals("") ? tAS.getText().toString() : "");
                animalActionDb.setBull_id(this.bullId);
                this.animal.setLast_state(Integer.valueOf(4));
                this.animal.setInsemination_date(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                this.animal.setInsemenation_bull(!this.bullId.equals("") ? tAS.getText().toString() : "");
                break;
            case 6:
                animalActionDb.setWeaned(fAS.getText().toString());
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(sAS.getText().toString()) + StringUtils.SPACE + getHours() + ":00"));
                break;
            case 7:
                animalActionDb.setFat_score(fAS.getText().toString());
                animalActionDb.setGrade(sAS.getText().toString());
                animalActionDb.setDead_weight(tAE.getText().toString());
                this.animal.setSlaughtered(Integer.valueOf(1));
                this.animal.setStatus(Integer.valueOf(4));
                break;
            case 8:
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + getHours() + ":00"));
                animalActionDb.setDescription(tAE.getText().toString());
                this.animal.setDied(Integer.valueOf(1));
                this.animal.setStatus(Integer.valueOf(4));
                break;
            case 9:
                animalActionDb.setWeight(Utils.getWeight(fAW.getText().toString(), this));
                animalActionDb.setPrice(sAP.getText().toString());
                animalActionDb.setDescription(tAE.getText().toString());
                this.animal.setSold(Integer.valueOf(1));
                this.animal.setStatus(Integer.valueOf(4));
                break;
            case 10:
                if (history == null) {
                    setLastState(action);
                }
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setDescription(tAS.getText().toString());
                this.animal.setLast_state(Integer.valueOf(1));
                Integer gestation = Integer.valueOf(283);
                if (this.animal.getGestation() != null && this.animal.getGestation().intValue() > 0) {
                    gestation = this.animal.getGestation();
                }
                this.animal.setDue_date(Utils.calculateDueDate(animalActionDb.getDatetime(), gestation));
                break;
            case 12:
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                break;
            case 13:
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                break;
            case 14:
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setText(tAE.getText().toString());
                break;
            case 15:
                animalActionDb.setDatetime(Utils.calculateCetTime(Utils.fromNormalToServer(fAS.getText().toString()) + StringUtils.SPACE + sAS.getText().toString() + ":00"));
                animalActionDb.setCow_tag_number(!this.cowId.equals("") ? tAS.getText().toString() : "");
                animalActionDb.setCow_id(this.cowId);
                break;
            case 17:
                animalActionDb.setSensor(tAS.getText().toString());
                this.animal.setBull_sensor(tAS.getText().toString());
                break;
        }
        if (history != null) {
            animalActionDb.setId(history.getId());
            this.animalActionDbDao.update(animalActionDb);
            if (action.equals(Integer.valueOf(17)) || action.equals(Integer.valueOf(0))) {
                this.animalDbDao.update(this.animal);
            }
        } else {
            this.animalActionDbDao.insert(animalActionDb);
            this.animalDbDao.update(this.animal);
        }
        actionAdded();
    }

    private void actionAdded() {
        try {
            showProgress(false);
            Toast.makeText(this, "success", 1).show();
            closeOverlay();
            getAnimalDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void historyClicked(AnimalActionDb history) {
        if (this.subtype.intValue() != 8) {
            showActionOverlay(history);
        } else {
            showUndoOverlay();
        }
    }

    private void showUndoOverlay() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.undo_text)).setTitle(getString(C0530R.string.undo_title));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private void showActionOverlay(final AnimalActionDb history) {
        Builder builder = new Builder(this);
        builder.setItems(new CharSequence[]{getString(C0530R.string.edit).toUpperCase(), getString(C0530R.string.delete).toUpperCase()}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        OfflineAnimalDetailsActivity.this.editAction(history);
                        return;
                    case 1:
                        OfflineAnimalDetailsActivity.this.deleteAction(history);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.show();
    }

    private void editAction(AnimalActionDb history) {
        showActionOverview(history.getAction(), history);
    }

    private void deleteAction(final AnimalActionDb history) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.item_delete_text)).setTitle(getString(C0530R.string.delete_item));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfflineAnimalDetailsActivity.this.actionDeleted(history);
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

    private void actionDeleted(AnimalActionDb history) {
        switch (history.getAction().intValue()) {
            case 0:
                this.animal.setSensor("");
                break;
            case 2:
                getLastState(history.getAction());
                break;
            case 3:
                getLastState(history.getAction());
                break;
            case 4:
                getLastState(history.getAction());
                break;
            case 7:
                this.animal.setSlaughtered(Integer.valueOf(0));
                this.animal.setStatus(Integer.valueOf(1));
                break;
            case 8:
                this.animal.setDied(Integer.valueOf(0));
                this.animal.setStatus(Integer.valueOf(1));
                break;
            case 9:
                this.animal.setSold(Integer.valueOf(0));
                this.animal.setStatus(Integer.valueOf(1));
                break;
            case 10:
                getLastState(history.getAction());
                break;
            case 11:
                this.animal.setStatus(Integer.valueOf(4));
                break;
            case 17:
                this.animal.setBull_sensor("");
                break;
        }
        this.animalActionDbDao.delete(history);
        this.animalDbDao.update(this.animal);
        getAnimalDetails();
    }

    private void getLastState(Integer action) {
        StateHistoryDbDao stateHistoryDbDao = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getStateHistoryDbDao();
        List<StateHistoryDb> stateHistoryDbs = stateHistoryDbDao.queryBuilder().where(StateHistoryDbDao.Properties.Animal_id.eq(this.animal.getId().toString()), new WhereCondition[0]).orderDesc(StateHistoryDbDao.Properties.Datetime).limit(1).list();
        if (stateHistoryDbs.size() == 1) {
            StateHistoryDb stateHistoryDb = (StateHistoryDb) stateHistoryDbs.get(0);
            if (!action.equals(Integer.valueOf(2))) {
                this.animal.setLast_state(stateHistoryDb.getLast_state());
                switch (action.intValue()) {
                    case 3:
                        this.animal.setIn_heat_date(stateHistoryDb.getLast_state_datetime());
                        break;
                    case 4:
                        this.animal.setInsemination_date(stateHistoryDb.getLast_state_datetime());
                        break;
                    case 10:
                        this.animal.setDue_date(stateHistoryDb.getLast_state_datetime());
                        break;
                    default:
                        break;
                }
            }
            this.animal.setWeight(stateHistoryDb.getWeight());
            stateHistoryDbDao.delete(stateHistoryDb);
        }
    }

    private void setLastState(Integer action) {
        StateHistoryDbDao stateHistoryDbDao = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getStateHistoryDbDao();
        StateHistoryDb stateHistoryDb = new StateHistoryDb();
        if (!action.equals(Integer.valueOf(2))) {
            stateHistoryDb.setLast_state(this.animal.getLast_state());
            switch (action.intValue()) {
                case 3:
                    stateHistoryDb.setLast_state_datetime(this.animal.getIn_heat_date());
                    break;
                case 4:
                    stateHistoryDb.setLast_state_datetime(this.animal.getInsemination_date());
                    break;
                case 10:
                    stateHistoryDb.setLast_state_datetime(this.animal.getDue_date());
                    break;
                default:
                    break;
            }
        }
        stateHistoryDb.setWeight(this.animal.getWeight());
        stateHistoryDb.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        stateHistoryDb.setAnimal_id(this.animal.getId().toString());
        stateHistoryDbDao.insert(stateHistoryDb);
    }

    private void getAnimalDetails() {
        List<AnimalDb> animala = this.animalDbDao.queryBuilder().where(Properties.Id.eq(this.id), new WhereCondition[0]).list();
        if (animala.size() > 0) {
            this.animal = (AnimalDb) animala.get(0);
            System.out.println(this.animal.getLast_calving_date());
            System.out.println(this.animal.getLast_state());
            if (this.animal.getDeleted_animal() == null || this.animal.getDeleted_animal().equals(Integer.valueOf(0))) {
                setTitle();
                this.animalDetailsCode.setText(this.animal.getTag_number());
                this.animalDetailsAdapter.setAnimal(this.animal);
                showProgress(false);
                this.animalDetailsSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            finish();
            return;
        }
        finish();
    }

    public void openMoocallTagOverlay() {
        final RelativeLayout moocallTagOverlay = (RelativeLayout) findViewById(C0530R.id.moocallTagOverlay);
        LinearLayout rescanTag = (LinearLayout) findViewById(C0530R.id.rescanTag);
        final TextView moocallTagNumber = (TextView) findViewById(C0530R.id.moocallTagNumber);
        TextView closeTagNumber = (TextView) findViewById(C0530R.id.closeTagNumber);
        moocallTagOverlay.setVisibility(0);
        moocallTagNumber.setText(this.animal.getMoocall_tag_number());
        rescanTag.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OfflineAnimalDetailsActivity.this, ScanMoocallTagActivity.class);
                intent.putExtra("offline", true);
                intent.putExtra("animalTag", OfflineAnimalDetailsActivity.this.animal.getTag_number());
                OfflineAnimalDetailsActivity.this.startActivity(intent);
            }
        });
        closeTagNumber.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                moocallTagOverlay.setVisibility(8);
                moocallTagNumber.setText("");
            }
        });
    }

    public void onBackPressed() {
        if (this.transparentBackground == null || this.transparentBackground.getVisibility() != 0) {
            finish();
        } else {
            closeOverlay();
        }
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        getAnimalDetails();
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void onCheckInternetCompleted(Boolean hasInternet) {
        if (!hasInternet.booleanValue()) {
            showProgress(false);
            Toast.makeText(this, C0530R.string.internet_no, 1).show();
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
