package com.moocall.moocall;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.moocall.moocall.adapter.CalvingDetailsListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.CalvingDb;
import com.moocall.moocall.db.CalvingDbDao;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.domain.Calving;
import com.moocall.moocall.domain.CalvingDetails;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.EditCalvingDataUrl;
import com.moocall.moocall.url.GetCalvingDataUrl;
import com.moocall.moocall.url.SaveCalvingDataUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewCalvingActivity extends ActionBarActivity {
    private AnimalDbDao animalDbDao;
    private BroadcastReceiver broadcastReceiver;
    private String bullId;
    private List<String> bullList;
    private HashMap<String, Integer> bullListHash;
    private TextView bullSelect;
    private List<String> calfGenderList;
    private List<String> calfPostnatalList;
    private Calving calving;
    private final Calendar calvingCalendar = Calendar.getInstance();
    private TextView calvingDate;
    private List<CalvingDetails> calvingDetailsList;
    private CalvingDetailsListAdapter calvingDetailsListAdapter;
    private ListView calvingDetailsListView;
    private Integer calvingId;
    private List<String> calvingProcessList;
    private TextView calvingTime;
    private Animal cow;
    private AnimalDb cowDb;
    private TextView cowSelect;
    private Integer historyId;
    private List<String> numberOfCalvesList;
    private TextView numberOfCalvesSelect;
    private View progressView;
    private Toolbar toolbar;

    class C04771 implements OnClickListener {
        C04771() {
        }

        public void onClick(View v) {
            NewCalvingActivity.this.onBackPressed();
        }
    }

    class C04783 implements OnClickListener {
        C04783() {
        }

        public void onClick(View v) {
            NewCalvingActivity.this.openDialog(1);
        }
    }

    class C04794 implements OnClickListener {
        C04794() {
        }

        public void onClick(View v) {
            NewCalvingActivity.this.openDialog(2);
        }
    }

    class C04838 implements OnDateSetListener {
        C04838() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            NewCalvingActivity.this.calvingCalendar.set(1, year);
            NewCalvingActivity.this.calvingCalendar.set(2, monthOfYear);
            NewCalvingActivity.this.calvingCalendar.set(5, dayOfMonth);
            NewCalvingActivity.this.updateDateTime();
        }
    }

    class C04849 implements OnTimeSetListener {
        C04849() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            NewCalvingActivity.this.calvingCalendar.set(11, hourOfDay);
            NewCalvingActivity.this.calvingCalendar.set(12, minute);
            NewCalvingActivity.this.updateDateTime();
        }
    }

    class C10992 implements OnMenuItemClickListener {
        C10992() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_new_calving);
        onResume();
        createAsyncBroadcast();
        Intent intent = getIntent();
        this.cow = (Animal) intent.getSerializableExtra("Cow");
        this.cowDb = (AnimalDb) intent.getSerializableExtra("CowDb");
        this.calvingId = (Integer) intent.getSerializableExtra("calvingId");
        this.historyId = (Integer) intent.getSerializableExtra("historyId");
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.animalDbDao = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getAnimalDbDao();
        showProgress(true);
        if (this.calvingId != null) {
            getCalvingData();
        } else {
            setup();
        }
    }

    private void setup() {
        initLists();
        setupToolbar();
        initElements();
        setupLists();
        implementsListeners();
        implementsPickers();
        showProgress(false);
    }

    private void initLists() {
        this.calvingProcessList = Arrays.asList(new String[]{getString(C0530R.string.she_calved_herself), getString(C0530R.string.needed_minor_assistance), getString(C0530R.string.needed_repositoring), getString(C0530R.string.needed_calving_jack), getString(C0530R.string.c_section), getString(C0530R.string.other_calving)});
        this.calfGenderList = Arrays.asList(new String[]{getString(C0530R.string.heifer_calf), getString(C0530R.string.bull_calf)});
        this.calfPostnatalList = Arrays.asList(new String[]{getString(C0530R.string.lively_and_suckling), getString(C0530R.string.lethargic_but_healthy), getString(C0530R.string.needed_kickstart), getString(C0530R.string.deceased)});
        this.numberOfCalvesList = Arrays.asList(new String[]{String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4), String.valueOf(5)});
        this.bullList = new ArrayList();
        this.bullList.add(getString(C0530R.string.add_new));
        this.bullListHash = new HashMap();
        List<AnimalDb> animalList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), new WhereCondition[0]).whereOr(Properties.Type.eq(Integer.valueOf(2)), Properties.Type.eq(Integer.valueOf(4)), new WhereCondition[0]).orderAsc(Properties.Tag_number).list();
        if (animalList != null) {
            for (AnimalDb bullInList : animalList) {
                this.bullList.add(bullInList.getTag_number());
                this.bullListHash.put(bullInList.getTag_number(), bullInList.getAnimal_id());
            }
        }
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C04771());
        this.toolbar.inflateMenu(C0530R.menu.menu_calving);
        this.toolbar.setOnMenuItemClickListener(new C10992());
        if (this.calving == null) {
            this.toolbar.setTitle(getString(C0530R.string.new_calving));
            this.calving = new Calving();
            this.calvingDetailsList = new ArrayList();
            return;
        }
        this.toolbar.setTitle(getString(C0530R.string.edit_calving));
        this.calvingDetailsList = this.calving.getCalvingDetailsList();
        for (int i = 0; i < this.calvingDetailsList.size(); i++) {
            CalvingDetails cd = (CalvingDetails) this.calvingDetailsList.get(i);
            if (cd.getCalfProcessText() != null && cd.getCalfProcessNumber() == null) {
                cd.setCalfProcessNumber(Integer.valueOf(this.calvingProcessList.indexOf(cd.getCalfProcessText()) + 1));
            }
            if (cd.getCalfGenderText() != null && cd.getCalfGenderNumber() == null) {
                cd.setCalfGenderNumber(Integer.valueOf(this.calfGenderList.indexOf(cd.getCalfGenderText()) + 1));
            }
            if (cd.getCalfStatusText() != null && cd.getCalfStatusNumber() == null) {
                cd.setCalfStatusNumber(Integer.valueOf(this.calfPostnatalList.indexOf(cd.getCalfStatusText()) + 1));
            }
        }
    }

    public void initElements() {
        this.calvingDetailsListView = (ListView) findViewById(C0530R.id.calvingDetailsListView);
        this.calvingDetailsListView.addHeaderView((LinearLayout) getLayoutInflater().inflate(C0530R.layout.header_calving_details, null));
        this.calvingDetailsListAdapter = new CalvingDetailsListAdapter(this, this.calvingDetailsList, this.calving);
        this.calvingDetailsListView.setAdapter(this.calvingDetailsListAdapter);
        this.cowSelect = (TextView) findViewById(C0530R.id.cowSelect);
        this.calvingDate = (TextView) findViewById(C0530R.id.calvingDate);
        this.calvingTime = (TextView) findViewById(C0530R.id.calvingTime);
        this.numberOfCalvesSelect = (TextView) findViewById(C0530R.id.numberOfCalvesSelect);
        this.bullSelect = (TextView) findViewById(C0530R.id.bullSelect);
        if (this.calvingDetailsList.size() > 0) {
            this.numberOfCalvesSelect.setText(String.valueOf(this.calvingDetailsList.size()));
        }
        String bullTagNumber = null;
        if (this.calving != null && this.calving.getBullTag() != null && this.calving.getBullTag().length() > 0) {
            bullTagNumber = this.calving.getBullTag();
        } else if (this.cow != null && this.cow.getInseminationBull() != null && this.cow.getInseminationBull().length() > 0) {
            bullTagNumber = this.cow.getInseminationBull();
        } else if (!(this.cowDb == null || this.cowDb.getInsemenation_bull() == null || this.cowDb.getInsemenation_bull().length() <= 0)) {
            bullTagNumber = this.cowDb.getInsemenation_bull();
        }
        if (this.bullListHash != null && bullTagNumber != null && this.bullId == null && this.bullListHash.get(bullTagNumber) != null) {
            this.bullId = ((Integer) this.bullListHash.get(bullTagNumber)).toString();
            this.bullSelect.setText(bullTagNumber);
        }
    }

    private void setupLists() {
        if (this.cow != null) {
            this.cowSelect.setText(this.cow.getTagNumber());
            this.calving.setCow(this.cow.getTagNumber());
        } else if (this.cowDb != null) {
            this.cowSelect.setText(this.cowDb.getTag_number());
            this.calving.setCow(this.cowDb.getTag_number());
        } else if (this.calving != null) {
            this.cowSelect.setText(this.calving.getCow());
        }
    }

    private void implementsListeners() {
        this.bullSelect.setOnClickListener(new C04783());
        if (this.calving.getId() == null) {
            this.numberOfCalvesSelect.setOnClickListener(new C04794());
        }
    }

    public void onLayoutClick(View view) {
        Utils.hideKeyboard(this);
    }

    private void openDialog(final int listID) {
        initLists();
        Builder builder = new Builder(this);
        CharSequence[] list = new CharSequence[0];
        switch (listID) {
            case 1:
                list = (CharSequence[]) this.bullList.toArray(new CharSequence[this.bullList.size()]);
                break;
            case 2:
                list = (CharSequence[]) this.numberOfCalvesList.toArray(new CharSequence[this.numberOfCalvesList.size()]);
                break;
        }
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (listID) {
                    case 1:
                        if (which > 0) {
                            NewCalvingActivity.this.calving.setBullTag(((String) NewCalvingActivity.this.bullList.get(which)).toString());
                            NewCalvingActivity.this.bullSelect.setText(((String) NewCalvingActivity.this.bullList.get(which)).toString());
                            NewCalvingActivity.this.bullId = ((Integer) NewCalvingActivity.this.bullListHash.get(((String) NewCalvingActivity.this.bullList.get(which)).toString())).toString();
                            return;
                        } else if (NewCalvingActivity.this.cow != null) {
                            intent = new Intent(NewCalvingActivity.this, ManualInputActivity.class);
                            intent.putExtra("newBull", true);
                            NewCalvingActivity.this.startActivity(intent);
                            return;
                        } else if (NewCalvingActivity.this.cowDb != null) {
                            intent = new Intent(NewCalvingActivity.this, OfflineManualInputActivity.class);
                            intent.putExtra("newBull", true);
                            NewCalvingActivity.this.startActivity(intent);
                            return;
                        } else {
                            return;
                        }
                    case 2:
                        NewCalvingActivity.this.numberOfCalvesSelect.setText((CharSequence) NewCalvingActivity.this.numberOfCalvesList.get(which));
                        NewCalvingActivity.this.numberOfCalvesSelect.setError(null);
                        NewCalvingActivity.this.setCalvingDetailsList(which);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.show();
    }

    private void setCalvingDetailsList(int number) {
        if (number < this.calvingDetailsList.size()) {
            while (number < this.calvingDetailsList.size() - 1) {
                this.calvingDetailsList.remove(this.calvingDetailsList.size() - 1);
            }
        } else {
            for (int i = this.calvingDetailsList.size(); i < number + 1; i++) {
                this.calvingDetailsList.add(new CalvingDetails(Integer.valueOf(i), this));
            }
        }
        this.calvingDetailsListAdapter.notifyDataSetChanged();
    }

    public void onDeceaseDateClick(View view) {
        final CalvingDetails cd = (CalvingDetails) this.calvingDetailsList.get(((ListView) view.getParent().getParent().getParent()).getPositionForView((View) view.getParent().getParent()) - 1);
        OnDateSetListener date = new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cd.setDeceasedDate(year, monthOfYear, dayOfMonth);
                NewCalvingActivity.this.calvingDetailsListAdapter.notifyDataSetChanged();
            }
        };
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dateDialog = new DatePickerDialog(this, date, cal.get(1), cal.get(2), cal.get(5));
        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    public void onCalvingProcessClick(View view) {
        openCalvingDialog(1, (CalvingDetails) this.calvingDetailsList.get(((ListView) view.getParent().getParent().getParent()).getPositionForView((View) view.getParent().getParent()) - 1));
    }

    public void onCalvingGenderClick(View view) {
        if (this.calving.getId() == null) {
            openCalvingDialog(2, (CalvingDetails) this.calvingDetailsList.get(((ListView) view.getParent().getParent().getParent()).getPositionForView((View) view.getParent().getParent()) - 1));
        }
    }

    public void onCalvingPostnatalClick(View view) {
        openCalvingDialog(3, (CalvingDetails) this.calvingDetailsList.get(((ListView) view.getParent().getParent().getParent()).getPositionForView((View) view.getParent().getParent()) - 1));
    }

    private void openCalvingDialog(final int listID, final CalvingDetails cd) {
        Builder builder = new Builder(this);
        CharSequence[] list = new CharSequence[0];
        switch (listID) {
            case 1:
                list = (CharSequence[]) this.calvingProcessList.toArray(new CharSequence[this.calvingProcessList.size()]);
                break;
            case 2:
                list = (CharSequence[]) this.calfGenderList.toArray(new CharSequence[this.calfGenderList.size()]);
                break;
            case 3:
                list = (CharSequence[]) this.calfPostnatalList.toArray(new CharSequence[this.calfPostnatalList.size()]);
                break;
        }
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (listID) {
                    case 1:
                        cd.setCalfProcessNumber(Integer.valueOf(which + 1));
                        cd.setCalfProcessText((String) NewCalvingActivity.this.calvingProcessList.get(which));
                        break;
                    case 2:
                        cd.setCalfGenderNumber(Integer.valueOf(which + 1));
                        cd.setCalfGenderText((String) NewCalvingActivity.this.calfGenderList.get(which));
                        break;
                    case 3:
                        cd.setCalfStatusNumber(Integer.valueOf(which + 1));
                        cd.setCalfStatusText((String) NewCalvingActivity.this.calfPostnatalList.get(which));
                        break;
                }
                NewCalvingActivity.this.calvingDetailsListAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void implementsPickers() {
        if (!this.calving.getCalfBornTime().equals("NA")) {
            String[] date = this.calving.getDate().split("-");
            String[] time = this.calving.getTime().split(":");
            int year = Integer.parseInt(date[0]);
            int monthOfYear = Integer.parseInt(date[1]) - 1;
            int dayOfMonth = Integer.parseInt(date[2]);
            int hourOfDay = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            this.calvingCalendar.set(1, year);
            this.calvingCalendar.set(2, monthOfYear);
            this.calvingCalendar.set(5, dayOfMonth);
            this.calvingCalendar.set(11, hourOfDay);
            this.calvingCalendar.set(12, minute);
        }
        updateDateTime();
        final OnDateSetListener date2 = new C04838();
        final OnTimeSetListener time2 = new C04849();
        this.calvingDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(NewCalvingActivity.this, date2, NewCalvingActivity.this.calvingCalendar.get(1), NewCalvingActivity.this.calvingCalendar.get(2), NewCalvingActivity.this.calvingCalendar.get(5));
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });
        this.calvingTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(NewCalvingActivity.this, time2, NewCalvingActivity.this.calvingCalendar.get(11), NewCalvingActivity.this.calvingCalendar.get(12), true).show();
            }
        });
    }

    private void updateDateTime() {
        this.calving.setDateTimePicker(this.calvingCalendar.get(1), this.calvingCalendar.get(2), this.calvingCalendar.get(5), this.calvingCalendar.get(11), this.calvingCalendar.get(12));
        this.calvingDate.setText(this.calving.getDateText());
        this.calvingTime.setText(this.calving.getTimeText());
    }

    public boolean saveCalving(MenuItem view) {
        try {
            if (this.calving != null) {
                if (this.calvingDetailsList.size() > 0) {
                    showProgress(true);
                    String datetime = this.calving.getServerCetTime();
                    String calvesNumber = String.valueOf(this.calvingDetailsList.size());
                    String bull = this.bullSelect.getText().toString();
                    JSONArray calves = new JSONArray();
                    for (int i = 0; i < this.calvingDetailsList.size(); i++) {
                        CalvingDetails cd = (CalvingDetails) this.calvingDetailsList.get(i);
                        JSONObject jo = new JSONObject();
                        if (cd.getCalfProcessNumber() != null) {
                            jo.put("question1", cd.getCalfProcessNumber());
                        }
                        if (cd.getCalfGenderNumber() != null) {
                            jo.put("question2", cd.getCalfGenderNumber());
                        }
                        if (cd.getCalfStatusNumber() != null) {
                            jo.put("question3", cd.getCalfStatusNumber());
                        }
                        if (cd.getDeceasedDateServer() != null) {
                            jo.put("deceaseDate", cd.getDeceasedDateServer());
                        }
                        if (cd.getWeight() != null) {
                            JSONObject jSONObject = jo;
                            jSONObject.put("weight", Utils.getWeight(cd.getWeight(), this));
                        }
                        if (cd.getId() != null) {
                            jo.put("id", cd.getId());
                        }
                        calves.put(jo);
                    }
                    if (this.calvingId != null) {
                        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.EDIT_CALVING_DATA));
                        new AcquireResponseTask(this).execute(new String[]{new EditCalvingDataUrl(this.historyId.toString(), this.cow.getId().toString(), this.calvingId.toString(), this.bullId, datetime, calvesNumber).createAndReturnUrl(this), QuickstartPreferences.EDIT_CALVING_DATA, "calves", calves.toString(), "bull-tag-number", bull});
                    } else if (this.cow != null) {
                        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SAVE_CALVING_DATA));
                        new AcquireResponseTask(this).execute(new String[]{new SaveCalvingDataUrl(this.cow.getId().toString(), this.bullId, datetime, calvesNumber).createAndReturnUrl(this), QuickstartPreferences.SAVE_CALVING_DATA, "calves", calves.toString(), "bull-tag-number", bull});
                    } else if (this.cowDb != null) {
                        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
                        CalvingDbDao calvingDbDao = daoSession.getCalvingDbDao();
                        CalvingDbDao.createTable(daoSession.getDatabase(), true);
                        CalvingDb calvingDb = new CalvingDb();
                        calvingDb.setCow_id(this.cowDb.getAnimal_id().toString());
                        calvingDb.setCow_tag_number(this.cowDb.getTag_number());
                        calvingDb.setBull_id(this.bullId);
                        calvingDb.setBull_tag_number(bull);
                        calvingDb.setDatetime(datetime);
                        calvingDb.setCalves_number(calvesNumber);
                        calvingDb.setCalves(calves.toString());
                        calvingDbDao.insert(calvingDb);
                        this.cowDb.setLast_state(Integer.valueOf(5));
                        this.cowDb.setLast_calving_date(datetime.replace("_", StringUtils.SPACE));
                        this.animalDbDao.update(this.cowDb);
                        showProgress(false);
                        showMessage(Integer.valueOf(1));
                    }
                } else {
                    this.numberOfCalvesSelect.setError(getString(C0530R.string.number_calves_problem));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    NewCalvingActivity.this.unregisterReceiver(this);
                    String action = intent.getAction();
                    if (action.equals(QuickstartPreferences.SAVE_CALVING_DATA)) {
                        NewCalvingActivity.this.onSaveCalvingDataCompleted(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.EDIT_CALVING_DATA)) {
                        NewCalvingActivity.this.onEditCalvingDataCompleted(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.GET_CALVING_DATA)) {
                        NewCalvingActivity.this.onGetCalvingDataCompleted(new JSONObject(intent.getStringExtra("response")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getCalvingData() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_CALVING_DATA));
        new AcquireResponseTask(this).execute(new String[]{new GetCalvingDataUrl(this.calvingId.toString()).createAndReturnUrl(this), QuickstartPreferences.GET_CALVING_DATA});
    }

    public void onGetCalvingDataCompleted(JSONObject result) {
        try {
            this.calving = new Calving(new JSONParserBgw(result), this);
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSaveCalvingDataCompleted(String result) {
        try {
            showProgress(false);
            if (result.equals("\"success\"")) {
                sendBroadcast(new Intent("refresh_lists"));
                sendBroadcast(new Intent("refresh_animal_details"));
                showMessage(Integer.valueOf(1));
                return;
            }
            Toast.makeText(this, result, 1).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditCalvingDataCompleted(String result) {
        try {
            showProgress(false);
            if (result.equals("\"success\"")) {
                sendBroadcast(new Intent("refresh_lists"));
                sendBroadcast(new Intent("refresh_smart_list"));
                sendBroadcast(new Intent("refresh_animal_details"));
                sendBroadcast(new Intent("refresh_animal_list"));
                showMessage(Integer.valueOf(2));
                return;
            }
            Toast.makeText(this, result, 1).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(Integer type) {
        CharSequence message = "";
        CharSequence title = "";
        switch (type.intValue()) {
            case 1:
                message = getString(C0530R.string.new_calving_text);
                title = getString(C0530R.string.new_calving);
                break;
            case 2:
                message = getString(C0530R.string.edit_calving_text);
                title = getString(C0530R.string.edit_calving);
                break;
        }
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(message).setTitle(title);
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                NewCalvingActivity.this.finish();
                NewCalvingActivity.this.sendBroadcast(new Intent("refresh_smart_list"));
                NewCalvingActivity.this.sendBroadcast(new Intent("refresh_animal_list"));
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }
}
