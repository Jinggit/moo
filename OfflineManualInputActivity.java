package com.moocall.moocall;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.CalvingDb;
import com.moocall.moocall.db.CalvingDbDao;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.WhereCondition;

public class OfflineManualInputActivity extends AppCompatActivity {
    private AnimalDb animal;
    private TextView animalBirthDate;
    private TextView animalBreed;
    final Calendar animalCalendar = Calendar.getInstance();
    final Calendar animalDatetimeCalendar = Calendar.getInstance();
    private AnimalDbDao animalDbDao;
    private LinearLayout animalEditLayout;
    private EditText animalGestation;
    private LinearLayout animalGestationLayout;
    private TextView animalGestationText;
    private EditText animalName;
    private TextView animalStatus;
    private TextView animalStatusDate;
    private TextView animalStatusDateText;
    private LinearLayout animalStatusLayout;
    private TextView animalStatusTime;
    private LinearLayout animalStatusTimeLayout;
    private TextView animalStatusTimeText;
    private EditText animalTagNumber;
    private TextView animalTemperament;
    private TextView animalType;
    private EditText animalVendor;
    private String[] breedList;
    private String cameraFileName;
    private Boolean editAnimal;
    private HashMap<String, Integer> gestationBreed;
    private Boolean newBull;
    private Boolean newCow;
    private View progressView;
    private List<String> realTypeList;
    private String startTag;
    private Integer startType;
    private List<String> stateList;
    private List<String> temperamentList;
    private Toolbar toolbar;
    private List<String> typeList;

    class C05021 implements OnClickListener {
        C05021() {
        }

        public void onClick(View v) {
            OfflineManualInputActivity.this.onBackPressed();
        }
    }

    class C05033 implements OnDateSetListener {
        C05033() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            OfflineManualInputActivity.this.animalCalendar.set(1, year);
            OfflineManualInputActivity.this.animalCalendar.set(2, monthOfYear);
            OfflineManualInputActivity.this.animalCalendar.set(5, dayOfMonth);
            OfflineManualInputActivity.this.updateDate();
        }
    }

    class C05055 implements OnClickListener {
        C05055() {
        }

        public void onClick(View view) {
            OfflineManualInputActivity.this.setFirstDropdown(Integer.valueOf(1));
        }
    }

    class C05066 implements OnClickListener {
        C05066() {
        }

        public void onClick(View view) {
            OfflineManualInputActivity.this.setFirstDropdown(Integer.valueOf(2));
        }
    }

    class C05077 implements OnClickListener {
        C05077() {
        }

        public void onClick(View view) {
            OfflineManualInputActivity.this.setFirstDropdown(Integer.valueOf(3));
        }
    }

    class C05088 implements OnClickListener {
        C05088() {
        }

        public void onClick(View view) {
            OfflineManualInputActivity.this.setThirdDropdown(null, null);
        }
    }

    class C05099 implements OnTimeSetListener {
        C05099() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            OfflineManualInputActivity.this.animalDatetimeCalendar.set(11, hourOfDay);
            OfflineManualInputActivity.this.animalDatetimeCalendar.set(12, minute);
            OfflineManualInputActivity.this.updateDateTime();
        }
    }

    class C11022 implements OnMenuItemClickListener {
        C11022() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_offline_manual_input);
        onResume();
        Intent intent = getIntent();
        this.animal = (AnimalDb) intent.getSerializableExtra("animal");
        this.newBull = (Boolean) intent.getSerializableExtra("newBull");
        this.newCow = (Boolean) intent.getSerializableExtra("newCow");
        setupToolbar();
        initLists();
        setupLayout();
        implementListeners();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C05021());
        this.toolbar.inflateMenu(C0530R.menu.save_delete);
        this.toolbar.setOnMenuItemClickListener(new C11022());
        MenuItem deleteItem = this.toolbar.getMenu().findItem(C0530R.id.delete);
        if (this.animal == null) {
            deleteItem.setVisible(false);
            this.toolbar.setTitle(getString(C0530R.string.new_animal));
            this.editAnimal = Boolean.valueOf(false);
            return;
        }
        deleteItem.setVisible(true);
        this.toolbar.setTitle(getString(C0530R.string.edit_animal));
        this.editAnimal = Boolean.valueOf(true);
    }

    private void setupLayout() {
        this.animalDbDao = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getAnimalDbDao();
        this.animalName = (EditText) findViewById(C0530R.id.animalName);
        this.animalTagNumber = (EditText) findViewById(C0530R.id.animalTagNumber);
        this.animalBirthDate = (TextView) findViewById(C0530R.id.animalBirthDate);
        this.animalBreed = (TextView) findViewById(C0530R.id.animalBreed);
        this.animalType = (TextView) findViewById(C0530R.id.animalType);
        this.animalEditLayout = (LinearLayout) findViewById(C0530R.id.animalEditLayout);
        this.animalVendor = (EditText) findViewById(C0530R.id.animalVendor);
        this.animalTemperament = (TextView) findViewById(C0530R.id.animalTemperament);
        this.animalStatusLayout = (LinearLayout) findViewById(C0530R.id.animalStatusLayout);
        this.animalStatus = (TextView) findViewById(C0530R.id.animalStatus);
        this.animalStatusDateText = (TextView) findViewById(C0530R.id.animalStatusDateText);
        this.animalStatusDate = (TextView) findViewById(C0530R.id.animalStatusDate);
        this.animalStatusTimeLayout = (LinearLayout) findViewById(C0530R.id.animalStatusTimeLayout);
        this.animalStatusTimeText = (TextView) findViewById(C0530R.id.animalStatusTimeText);
        this.animalStatusTime = (TextView) findViewById(C0530R.id.animalStatusTime);
        this.animalGestationLayout = (LinearLayout) findViewById(C0530R.id.animalGestationLayout);
        this.animalGestationText = (TextView) findViewById(C0530R.id.animalGestationText);
        this.animalGestation = (EditText) findViewById(C0530R.id.animalGestation);
        this.animalStatusLayout.setVisibility(8);
        this.progressView = findViewById(C0530R.id.progress_disable);
        if (this.animal != null) {
            this.animalTagNumber.setText(this.animal.getTag_number().toString());
            if (this.animal.getBirthDateText() != null) {
                if (this.animal.getBirth_date().length() > 0) {
                    String[] date = this.animal.getBirth_date().split("-");
                    int year = Integer.parseInt(date[0]);
                    int monthOfYear = Integer.parseInt(date[1]) - 1;
                    int dayOfMonth = Integer.parseInt(date[2]);
                    this.animalCalendar.set(1, year);
                    this.animalCalendar.set(2, monthOfYear);
                    this.animalCalendar.set(5, dayOfMonth);
                }
                this.animalBirthDate.setText(this.animal.getBirthDateText());
            }
            this.animalName.setText(this.animal.getName());
            this.animalBreed.setText(this.animal.getBreed());
            this.animalType.setText(((String) this.realTypeList.get(this.animal.getType().intValue() - 1)).toString());
            this.startType = this.animal.getType();
            this.animalEditLayout.setVisibility(0);
            this.animalVendor.setText(this.animal.getVendor());
            this.animalTemperament.setText(this.animal.getTemperament());
            if ((this.animal.getType().equals(Integer.valueOf(1)) || this.animal.getType().equals(Integer.valueOf(3))) && this.animal.getLast_state().equals(Integer.valueOf(1))) {
                this.animalStatusLayout.setVisibility(0);
                setStatusLayout(Integer.valueOf(this.animal.getLast_state().intValue() - 1));
                return;
            }
            return;
        }
        this.animal = new AnimalDb();
        this.animal.setBreed("");
        this.animal.setStatus(Integer.valueOf(1));
        this.animal.setLast_state(Integer.valueOf(0));
        this.animalEditLayout.setVisibility(8);
    }

    private void setStatusLayout(Integer which) {
        this.animalStatus.setText(((String) this.stateList.get(which.intValue())).toString());
        this.animal.setLast_state(Integer.valueOf(which.intValue() + 1));
        this.animalStatusTimeLayout.setVisibility(0);
        this.animalGestationLayout.setVisibility(8);
        switch (which.intValue()) {
            case 0:
                this.animalStatusDateText.setText(getString(C0530R.string.due_date) + ":");
                this.animalStatusTimeLayout.setVisibility(8);
                this.animalGestationLayout.setVisibility(0);
                return;
            case 1:
                this.animalStatusDateText.setText(getString(C0530R.string.heat_spotted_date) + ":");
                this.animalStatusTimeText.setText(getString(C0530R.string.heat_spotted_time) + ":");
                return;
            case 2:
                this.animalStatusDateText.setText(getString(C0530R.string.heat_spotted_date) + ":");
                this.animalStatusTimeText.setText(getString(C0530R.string.heat_spotted_time) + ":");
                return;
            case 3:
                this.animalStatusDateText.setText(getString(C0530R.string.inseminated_date) + ":");
                this.animalStatusTimeText.setText(getString(C0530R.string.inseminated_time) + ":");
                return;
            case 4:
                this.animalStatusDateText.setText(getString(C0530R.string.date_of_calving) + ":");
                this.animalStatusTimeText.setVisibility(8);
                this.animalStatusTime.setVisibility(8);
                return;
            default:
                return;
        }
    }

    private void initLists() {
        this.breedList = getResources().getStringArray(C0530R.array.breeds);
        if (this.newBull != null && this.newBull.booleanValue()) {
            this.typeList = Arrays.asList(new String[]{getString(C0530R.string.ai_straw) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.bull)});
        } else if (this.newCow == null || !this.newCow.booleanValue()) {
            this.typeList = Arrays.asList(new String[]{getString(C0530R.string.cow), getString(C0530R.string.heifer), getString(C0530R.string.bull), getString(C0530R.string.calf)});
        } else {
            this.typeList = Arrays.asList(new String[]{getString(C0530R.string.cow), getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.heifer)});
        }
        this.realTypeList = Arrays.asList(new String[]{getString(C0530R.string.cow), getString(C0530R.string.ai_straw) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.heifer), getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.beef) + StringUtils.SPACE + getString(C0530R.string.heifer), getString(C0530R.string.beef) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.calf) + StringUtils.SPACE + getString(C0530R.string.heifer), getString(C0530R.string.calf) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.bullock), getString(C0530R.string.vasectomized) + StringUtils.SPACE + getString(C0530R.string.bull)});
        this.stateList = Arrays.asList(new String[]{getString(C0530R.string.incalf), getString(C0530R.string.cycling), getString(C0530R.string.inheat), getString(C0530R.string.inseminated), getString(C0530R.string.recently_calved)});
        this.temperamentList = Arrays.asList(new String[]{getString(C0530R.string.very_quiet), getString(C0530R.string.quiet), getString(C0530R.string.average), getString(C0530R.string.difficult), getString(C0530R.string.aggressive)});
        this.gestationBreed = new HashMap();
        this.gestationBreed.put("Ayrshire", Integer.valueOf(279));
        this.gestationBreed.put("Jersey", Integer.valueOf(279));
        this.gestationBreed.put("Holstein/Friesian", Integer.valueOf(279));
        this.gestationBreed.put("Shorthorn", Integer.valueOf(282));
        this.gestationBreed.put("Guernsey", Integer.valueOf(283));
        this.gestationBreed.put("Hereford", Integer.valueOf(285));
        this.gestationBreed.put("Charolais", Integer.valueOf(289));
        this.gestationBreed.put("Limousin", Integer.valueOf(289));
        this.gestationBreed.put("Simmental", Integer.valueOf(289));
        this.gestationBreed.put("Brown Swiss", Integer.valueOf(290));
        this.gestationBreed.put("Brahman", Integer.valueOf(292));
    }

    private void implementListeners() {
        final OnDateSetListener date = new C05033();
        this.animalBirthDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(OfflineManualInputActivity.this);
                new DatePickerDialog(OfflineManualInputActivity.this, date, OfflineManualInputActivity.this.animalCalendar.get(1), OfflineManualInputActivity.this.animalCalendar.get(2), OfflineManualInputActivity.this.animalCalendar.get(5)).show();
            }
        });
        this.animalBreed.setOnClickListener(new C05055());
        this.animalTemperament.setOnClickListener(new C05066());
        this.animalType.setOnClickListener(new C05077());
        this.animalStatus.setOnClickListener(new C05088());
        if (this.editAnimal.booleanValue()) {
            checkStatusDateTime();
        } else {
            updateDateTime();
        }
        final OnTimeSetListener time = new C05099();
        final OnDateSetListener date2 = new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                OfflineManualInputActivity.this.animalDatetimeCalendar.set(1, year);
                OfflineManualInputActivity.this.animalDatetimeCalendar.set(2, monthOfYear);
                OfflineManualInputActivity.this.animalDatetimeCalendar.set(5, dayOfMonth);
                OfflineManualInputActivity.this.updateDateTime();
            }
        };
        this.animalStatusDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(OfflineManualInputActivity.this);
                new DatePickerDialog(OfflineManualInputActivity.this, date2, OfflineManualInputActivity.this.animalDatetimeCalendar.get(1), OfflineManualInputActivity.this.animalDatetimeCalendar.get(2), OfflineManualInputActivity.this.animalDatetimeCalendar.get(5)).show();
            }
        });
        this.animalStatusTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(OfflineManualInputActivity.this);
                new TimePickerDialog(OfflineManualInputActivity.this, time, OfflineManualInputActivity.this.animalDatetimeCalendar.get(11), OfflineManualInputActivity.this.animalDatetimeCalendar.get(12), true).show();
            }
        });
    }

    private void checkStatusDateTime() {
        if (this.animal.getGestation() != null) {
            this.animalGestation.setText(this.animal.getGestation().toString());
            String[] date;
            String[] datetime;
            String[] time;
            switch (this.animal.getLast_state().intValue() - 1) {
                case 0:
                    if (!(this.animal.getDue_date() == null || this.animal.getDue_date().length() <= 5 || this.animal.getDue_date().equals("0000-00-00"))) {
                        date = this.animal.getDue_date().split("-");
                        this.animalDatetimeCalendar.set(1, Integer.parseInt(date[0]));
                        this.animalDatetimeCalendar.set(2, Integer.parseInt(date[1]) - 1);
                        this.animalDatetimeCalendar.set(5, Integer.parseInt(date[2]));
                        break;
                    }
                case 1:
                    if (!(this.animal.getIn_heat_date() == null || this.animal.getIn_heat_date().length() <= 5 || this.animal.getIn_heat_date().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getIn_heat_date().split(StringUtils.SPACE);
                        date = datetime[0].split("-");
                        time = datetime[1].split(":");
                        this.animalDatetimeCalendar.set(1, Integer.parseInt(date[0]));
                        this.animalDatetimeCalendar.set(2, Integer.parseInt(date[1]) - 1);
                        this.animalDatetimeCalendar.set(5, Integer.parseInt(date[2]));
                        this.animalDatetimeCalendar.set(11, Integer.parseInt(time[0]));
                        this.animalDatetimeCalendar.set(12, Integer.parseInt(time[1]));
                        break;
                    }
                case 2:
                    if (!(this.animal.getIn_heat_date() == null || this.animal.getIn_heat_date().length() <= 5 || this.animal.getIn_heat_date().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getIn_heat_date().split(StringUtils.SPACE);
                        date = datetime[0].split("-");
                        time = datetime[1].split(":");
                        this.animalDatetimeCalendar.set(1, Integer.parseInt(date[0]));
                        this.animalDatetimeCalendar.set(2, Integer.parseInt(date[1]) - 1);
                        this.animalDatetimeCalendar.set(5, Integer.parseInt(date[2]));
                        this.animalDatetimeCalendar.set(11, Integer.parseInt(time[0]));
                        this.animalDatetimeCalendar.set(12, Integer.parseInt(time[1]));
                        break;
                    }
                case 3:
                    if (!(this.animal.getInsemination_date() == null || this.animal.getInsemination_date().length() <= 5 || this.animal.getInsemination_date().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getInsemination_date().split(StringUtils.SPACE);
                        date = datetime[0].split("-");
                        time = datetime[1].split(":");
                        this.animalDatetimeCalendar.set(1, Integer.parseInt(date[0]));
                        this.animalDatetimeCalendar.set(2, Integer.parseInt(date[1]) - 1);
                        this.animalDatetimeCalendar.set(5, Integer.parseInt(date[2]));
                        this.animalDatetimeCalendar.set(11, Integer.parseInt(time[0]));
                        this.animalDatetimeCalendar.set(12, Integer.parseInt(time[1]));
                        break;
                    }
                case 4:
                    if (!(this.animal.getLast_calving_date() == null || this.animal.getLast_calving_date().length() <= 5 || this.animal.getLast_calving_date().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getLast_calving_date().split(StringUtils.SPACE);
                        date = datetime[0].split("-");
                        time = datetime[1].split(":");
                        this.animalDatetimeCalendar.set(1, Integer.parseInt(date[0]));
                        this.animalDatetimeCalendar.set(2, Integer.parseInt(date[1]) - 1);
                        this.animalDatetimeCalendar.set(5, Integer.parseInt(date[2]));
                        this.animalDatetimeCalendar.set(11, Integer.parseInt(time[0]));
                        this.animalDatetimeCalendar.set(12, Integer.parseInt(time[1]));
                        break;
                    }
            }
            updateDateTime();
        }
    }

    private void updateDateTime() {
        this.animal.setDateTimePicker(this.animalDatetimeCalendar.get(1), this.animalDatetimeCalendar.get(2), this.animalDatetimeCalendar.get(5), this.animalDatetimeCalendar.get(11), this.animalDatetimeCalendar.get(12));
        this.animalStatusDate.setText(this.animal.getStateDateText());
        this.animalStatusTime.setText(this.animal.getStateTimeText());
    }

    private void setCrossbreedSelector(final Integer first) {
        Utils.hideKeyboard(this);
        Builder builder = new Builder(this);
        builder.setItems(this.breedList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    OfflineManualInputActivity.this.setCustomInput();
                    return;
                }
                OfflineManualInputActivity.this.animalBreed.setText(OfflineManualInputActivity.this.breedList[first.intValue()] + " x " + OfflineManualInputActivity.this.breedList[which]);
                OfflineManualInputActivity.this.animal.setBreed(OfflineManualInputActivity.this.breedList[first.intValue()] + " x " + OfflineManualInputActivity.this.breedList[which]);
                OfflineManualInputActivity.this.animalGestation.setText("283");
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfflineManualInputActivity.this.setCrossbreedQuestion(first);
            }
        });
        builder.setPositiveButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void setCrossbreedQuestion(final Integer first) {
        Utils.hideKeyboard(this);
        List<String> crossbreedList = Arrays.asList(new String[]{getString(C0530R.string.Crossbreed), getString(C0530R.string.finish)});
        CharSequence[] list = (CharSequence[]) crossbreedList.toArray(new CharSequence[crossbreedList.size()]);
        Builder builder = new Builder(this);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    OfflineManualInputActivity.this.setCrossbreedSelector(first);
                    return;
                }
                OfflineManualInputActivity.this.animalBreed.setText(OfflineManualInputActivity.this.breedList[first.intValue()]);
                OfflineManualInputActivity.this.animal.setBreed(OfflineManualInputActivity.this.breedList[first.intValue()]);
                if (OfflineManualInputActivity.this.gestationBreed.containsKey(OfflineManualInputActivity.this.breedList[first.intValue()])) {
                    OfflineManualInputActivity.this.animalGestation.setText(((Integer) OfflineManualInputActivity.this.gestationBreed.get(OfflineManualInputActivity.this.breedList[first.intValue()])).toString());
                } else {
                    OfflineManualInputActivity.this.animalGestation.setText("283");
                }
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfflineManualInputActivity.this.setFirstDropdown(Integer.valueOf(1));
            }
        });
        builder.setPositiveButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void setCustomInput() {
        Builder builder = new Builder(this);
        builder.setTitle((int) C0530R.string.custom_breed);
        final View input = new EditText(this);
        input.setInputType(1);
        builder.setView(input);
        builder.setPositiveButton((int) C0530R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OfflineManualInputActivity.this.animalBreed.setText(input.getText().toString());
                OfflineManualInputActivity.this.animal.setBreed(input.getText().toString());
                OfflineManualInputActivity.this.animalGestation.setText("283");
            }
        });
        builder.setNegativeButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void setFirstDropdown(final Integer listID) {
        Utils.hideKeyboard(this);
        Builder builder = new Builder(this);
        CharSequence[] list = new CharSequence[0];
        switch (listID.intValue()) {
            case 1:
                list = this.breedList;
                break;
            case 2:
                list = (CharSequence[]) this.temperamentList.toArray(new CharSequence[this.temperamentList.size()]);
                break;
            case 3:
                list = (CharSequence[]) this.typeList.toArray(new CharSequence[this.typeList.size()]);
                break;
        }
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (listID.intValue()) {
                    case 1:
                        if (which == 0) {
                            OfflineManualInputActivity.this.setCustomInput();
                            return;
                        } else {
                            OfflineManualInputActivity.this.setCrossbreedQuestion(Integer.valueOf(which));
                            return;
                        }
                    case 2:
                        OfflineManualInputActivity.this.animalTemperament.setText((CharSequence) OfflineManualInputActivity.this.temperamentList.get(which));
                        OfflineManualInputActivity.this.animal.setTemperament((String) OfflineManualInputActivity.this.temperamentList.get(which));
                        return;
                    case 3:
                        if (OfflineManualInputActivity.this.newBull != null && OfflineManualInputActivity.this.newBull.booleanValue()) {
                            OfflineManualInputActivity.this.animalType.setText(((String) OfflineManualInputActivity.this.typeList.get(which)).toString());
                            switch (which) {
                                case 0:
                                    OfflineManualInputActivity.this.animal.setType(Integer.valueOf(2));
                                    return;
                                case 1:
                                    OfflineManualInputActivity.this.animal.setType(Integer.valueOf(4));
                                    return;
                                default:
                                    return;
                            }
                        } else if (OfflineManualInputActivity.this.newCow == null || !OfflineManualInputActivity.this.newCow.booleanValue()) {
                            OfflineManualInputActivity.this.setSecondDropdown(Integer.valueOf(which), listID);
                            return;
                        } else {
                            OfflineManualInputActivity.this.animalType.setText(((String) OfflineManualInputActivity.this.typeList.get(which)).toString());
                            switch (which) {
                                case 0:
                                    OfflineManualInputActivity.this.animal.setType(Integer.valueOf(1));
                                    break;
                                case 1:
                                    OfflineManualInputActivity.this.animal.setType(Integer.valueOf(3));
                                    break;
                            }
                            OfflineManualInputActivity.this.animalStatusLayout.setVisibility(0);
                            OfflineManualInputActivity.this.setThirdDropdown(listID, null);
                            return;
                        }
                    default:
                        return;
                }
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void setSecondDropdown(final Integer type, final Integer parentType) {
        Utils.hideKeyboard(this);
        List<String> secondList = null;
        switch (type.intValue()) {
            case 0:
                this.animalType.setText(((String) this.typeList.get(type.intValue())).toString());
                this.animal.setType(Integer.valueOf(1));
                setThirdDropdown(type, parentType);
                this.animalStatusLayout.setVisibility(0);
                return;
            case 1:
                secondList = Arrays.asList(new String[]{getString(C0530R.string.beef) + StringUtils.SPACE + getString(C0530R.string.heifer), getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.heifer)});
                break;
            case 2:
                secondList = Arrays.asList(new String[]{getString(C0530R.string.beef) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.breeding) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.ai_straw) + StringUtils.SPACE + getString(C0530R.string.bull), getString(C0530R.string.vasectomized) + StringUtils.SPACE + getString(C0530R.string.bull)});
                break;
            case 3:
                secondList = Arrays.asList(new String[]{getString(C0530R.string.heifer) + StringUtils.SPACE + getString(C0530R.string.calf), getString(C0530R.string.bull) + StringUtils.SPACE + getString(C0530R.string.calf)});
                break;
        }
        final CharSequence[] list = (CharSequence[]) secondList.toArray(new CharSequence[secondList.size()]);
        Builder builder = new Builder(this);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OfflineManualInputActivity.this.animalType.setText(list[which]);
                switch (type.intValue()) {
                    case 1:
                        switch (which) {
                            case 0:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(5));
                                break;
                            case 1:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(3));
                                break;
                            default:
                                break;
                        }
                    case 2:
                        switch (which) {
                            case 0:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(6));
                                break;
                            case 1:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(4));
                                break;
                            case 2:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(2));
                                break;
                            case 3:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(10));
                                break;
                            default:
                                break;
                        }
                    case 3:
                        switch (which) {
                            case 0:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(7));
                                break;
                            case 1:
                                OfflineManualInputActivity.this.animal.setType(Integer.valueOf(8));
                                break;
                            default:
                                break;
                        }
                }
                if (OfflineManualInputActivity.this.animal.getType().intValue() == 3) {
                    OfflineManualInputActivity.this.setThirdDropdown(type, parentType);
                    OfflineManualInputActivity.this.animalStatusLayout.setVisibility(0);
                    return;
                }
                OfflineManualInputActivity.this.animalStatusLayout.setVisibility(8);
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfflineManualInputActivity.this.setFirstDropdown(parentType);
            }
        });
        builder.setPositiveButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void setThirdDropdown(final Integer type, final Integer parentType) {
        Utils.hideKeyboard(this);
        CharSequence[] list = (CharSequence[]) this.stateList.toArray(new CharSequence[this.stateList.size()]);
        Builder builder = new Builder(this);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OfflineManualInputActivity.this.setStatusLayout(Integer.valueOf(which));
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (parentType == null || type == null) {
                    if (type != null) {
                        OfflineManualInputActivity.this.setFirstDropdown(type);
                    } else {
                        dialog.dismiss();
                    }
                } else if (type.intValue() == 0) {
                    OfflineManualInputActivity.this.setFirstDropdown(parentType);
                } else {
                    OfflineManualInputActivity.this.setSecondDropdown(type, parentType);
                }
            }
        });
        builder.setPositiveButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void updateDate() {
        this.animal.setBirthDate(this.animalCalendar.get(1), this.animalCalendar.get(2), this.animalCalendar.get(5));
        this.animalBirthDate.setText(this.animal.getBirthDateText());
    }

    public boolean save(MenuItem view) {
        this.animalTagNumber.setError(null);
        this.animalType.setError(null);
        this.animalBirthDate.setError(null);
        if (this.animal.getType() != null && ((this.animal.getType().intValue() == 1 || this.animal.getType().intValue() == 3) && !this.editAnimal.booleanValue())) {
            this.animalStatus.setError(null);
        }
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(this.animalTagNumber.getText().toString())) {
            this.animalTagNumber.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalTagNumber;
            cancel = true;
        } else if (this.animalTagNumber.getText().toString().length() < 3) {
            this.animalTagNumber.setError(getString(C0530R.string.tag_problem_text));
            focusView = this.animalTagNumber;
            cancel = true;
        }
        if (this.animal.getType() == null) {
            this.animalType.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalType;
            cancel = true;
        }
        System.out.println(this.animal.getType());
        if (this.animal.getType() != null && ((this.animal.getType().intValue() == 7 || this.animal.getType().intValue() == 8) && this.animal.getBirth_date() == null)) {
            this.animalBirthDate.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalBirthDate;
            cancel = true;
        }
        if (this.animal.getType() != null && ((this.animal.getType().intValue() == 1 || this.animal.getType().intValue() == 3) && !this.editAnimal.booleanValue() && this.animal.getLast_state() == null)) {
            this.animalStatus.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalStatus;
            cancel = true;
        }
        if ((this.animal.getTag_number() == null || !this.animal.getTag_number().equals(this.animalTagNumber.getText().toString())) && this.animalDbDao.queryBuilder().where(Properties.Tag_number.eq(this.animalTagNumber.getText().toString()), new WhereCondition[0]).list().size() > 0) {
            this.animalTagNumber.setError(getString(C0530R.string.tag_problem));
            focusView = this.animalTagNumber;
            cancel = true;
        }
        if (!(this.animal.getType() == null || this.animal.getType().equals(this.startType))) {
            List<Integer> cowList = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(7)});
            List<Integer> bullList = Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(9)});
            if (cowList.contains(this.animal.getType())) {
                if (bullList.contains(this.startType)) {
                    this.animalType.setError(getString(C0530R.string.gender_cannot_be_changed));
                    focusView = this.animalType;
                    cancel = true;
                }
            } else if (bullList.contains(this.animal.getType()) && cowList.contains(this.startType)) {
                this.animalType.setError(getString(C0530R.string.gender_cannot_be_changed));
                focusView = this.animalType;
                cancel = true;
            }
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            saveCow();
        }
        return true;
    }

    private void saveCow() {
        try {
            showProgress(true);
            this.animal.setName(this.animalName.getText().toString());
            this.startTag = this.animal.getTag_number();
            this.animal.setTag_number(this.animalTagNumber.getText().toString());
            this.animal.setGestation(Integer.valueOf(Integer.parseInt(this.animalGestation.getText().toString())));
            this.animal.setState(this.animal.getLast_state());
            switch (this.animal.getLast_state().intValue()) {
                case 1:
                    this.animal.setDue_date(this.animal.getServer_state_cet_time().split("_")[0]);
                    break;
                case 2:
                    this.animal.setIn_heat_date(this.animal.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
                    this.animal.setCycle_date(this.animal.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
                    break;
                case 3:
                    this.animal.setIn_heat_date(this.animal.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
                    break;
                case 4:
                    this.animal.setInsemination_date(this.animal.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
                    break;
                case 5:
                    this.animal.setLast_calving_date(this.animal.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
                    break;
            }
            if (this.animal.getId() != null) {
                if (this.animal.getAnimal_id().equals(Integer.valueOf(0))) {
                    this.animal.setUpdated_animal(Integer.valueOf(0));
                    this.animal.setDeleted_animal(Integer.valueOf(0));
                } else {
                    this.animal.setUpdated_animal(Integer.valueOf(1));
                    this.animal.setDeleted_animal(Integer.valueOf(0));
                }
                this.animal.setVendor(this.animalVendor.getText().toString());
                this.animalDbDao.update(this.animal);
                if (this.startTag != this.animal.getTag_number()) {
                    DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
                    CalvingDbDao calvingDbDao = daoSession.getCalvingDbDao();
                    AnimalActionDbDao animalActionDbDao = daoSession.getAnimalActionDbDao();
                    for (CalvingDb calving : calvingDbDao.queryBuilder().where(CalvingDbDao.Properties.Cow_tag_number.eq(this.startTag), new WhereCondition[0]).list()) {
                        calving.setCow_tag_number(this.animal.getTag_number());
                        calvingDbDao.update(calving);
                    }
                    for (CalvingDb calving2 : calvingDbDao.queryBuilder().where(CalvingDbDao.Properties.Bull_tag_number.eq(this.startTag), new WhereCondition[0]).list()) {
                        calving2.setBull_tag_number(this.animal.getTag_number());
                        calvingDbDao.update(calving2);
                    }
                    for (AnimalActionDb action : animalActionDbDao.queryBuilder().where(AnimalActionDbDao.Properties.Animal_tag_number.eq(this.startTag), new WhereCondition[0]).list()) {
                        action.setAnimal_tag_number(this.animal.getTag_number());
                        animalActionDbDao.update(action);
                    }
                }
            } else {
                this.animal.setStatus(Integer.valueOf(1));
                this.animal.setNew_animal(Integer.valueOf(1));
                this.animal.setAnimal_id(Integer.valueOf(0));
                this.animal.setUpdated_animal(Integer.valueOf(0));
                this.animal.setDeleted_animal(Integer.valueOf(0));
                if (this.animal.getBirth_date() == null) {
                    this.animal.setBirth_date("0000-00-00");
                }
                if (this.animal.getWeight() == null) {
                    this.animal.setWeight("0");
                }
                this.animalDbDao.insert(this.animal);
            }
            onEditAnimalCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean delete(MenuItem view) {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.delete_animal_text)).setTitle(getString(C0530R.string.delete_animal));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfflineManualInputActivity.this.deleteThisAnimal();
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
        return true;
    }

    private void deleteThisAnimal() {
        showProgress(true);
        if (this.animal.getAnimal_id().equals(Integer.valueOf(0))) {
            this.animalDbDao.delete(this.animal);
        } else {
            if (this.animal.getUpdated_animal().equals(Integer.valueOf(1))) {
                this.animal.setUpdated_animal(Integer.valueOf(0));
            }
            this.animal.setDeleted_animal(Integer.valueOf(1));
            this.animal.setStatus(Integer.valueOf(5));
            this.animalDbDao.update(this.animal);
        }
        onDeleteCowCompleted();
    }

    public void onEditAnimalCompleted() {
        showProgress(false);
        Toast.makeText(this, C0530R.string.success, 1).show();
        finish();
    }

    public void onDeleteCowCompleted() {
        showProgress(false);
        showMessage();
    }

    private void showMessage() {
        CharSequence message = getString(C0530R.string.animal_deleted_text);
        CharSequence title = getString(C0530R.string.animal_deleted);
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(message).setTitle(title);
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfflineManualInputActivity.this.finish();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }
}
