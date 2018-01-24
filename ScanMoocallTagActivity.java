package com.moocall.moocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.SaveMoocallTagUrl;
import com.moocall.moocall.util.EditTextBackEvent;
import com.moocall.moocall.util.EditTextBackEvent.EditTextImeBackListener;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.WhereCondition;

public class ScanMoocallTagActivity extends AppCompatActivity {
    private AnimalActionDbDao animalActionDbDao;
    private AnimalDbDao animalDbDao;
    private AnimalDb animalForTag;
    private TextView animalName;
    private EditTextBackEvent animalTag;
    private BroadcastReceiver broadcastReceiver;
    private TextView cancel;
    private TextView moocallTagNumber;
    private TextView moocallTagNumberError;
    private Boolean offline;
    private View progressView;
    private TextView saveTag;
    private Toolbar toolbar;

    class C05351 implements OnClickListener {
        C05351() {
        }

        public void onClick(View v) {
            ScanMoocallTagActivity.this.onBackPressed();
        }
    }

    class C05363 implements OnClickListener {
        C05363() {
        }

        public void onClick(View view) {
            ScanMoocallTagActivity.this.checkForSave();
        }
    }

    class C05374 implements OnClickListener {
        C05374() {
        }

        public void onClick(View view) {
            ScanMoocallTagActivity.this.finish();
        }
    }

    class C05385 implements OnItemClickListener {
        C05385() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ScanMoocallTagActivity.this.setAnimal();
        }
    }

    class C05396 implements OnEditorActionListener {
        C05396() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            ScanMoocallTagActivity.this.setAnimal();
            return true;
        }
    }

    class C05408 implements DialogInterface.OnClickListener {
        C05408() {
        }

        public void onClick(DialogInterface dialog, int id) {
            ScanMoocallTagActivity.this.saveThisTag(Integer.valueOf(1));
        }
    }

    class C05419 implements DialogInterface.OnClickListener {
        C05419() {
        }

        public void onClick(DialogInterface dialog, int id) {
            ScanMoocallTagActivity.this.saveThisTag(Integer.valueOf(3));
        }
    }

    class C11052 implements OnMenuItemClickListener {
        C11052() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    class C11067 implements EditTextImeBackListener {
        C11067() {
        }

        public void onImeBack(EditTextBackEvent ctrl, String text) {
            ScanMoocallTagActivity.this.setAnimal();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_scan_moocall_tag);
        Intent intent = getIntent();
        this.offline = (Boolean) intent.getSerializableExtra("offline");
        String animalTagN = (String) intent.getSerializableExtra("animalTag");
        onResume();
        setupToolbar();
        createAsyncBroadcast();
        setupLayout();
        if (animalTagN != null) {
            this.animalTag.setText(animalTagN);
            this.animalTag.setEnabled(false);
            setAnimal();
        } else {
            this.animalTag.setEnabled(true);
            initList();
        }
        implementListeners();
        startCameraIntent();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C05351());
        this.toolbar.setTitle(getString(C0530R.string.scan_moocall_tag));
        this.toolbar.inflateMenu(C0530R.menu.scan_menu);
        this.toolbar.setOnMenuItemClickListener(new C11052());
    }

    private void setupLayout() {
        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
        this.animalDbDao = daoSession.getAnimalDbDao();
        this.animalActionDbDao = daoSession.getAnimalActionDbDao();
        this.progressView = findViewById(C0530R.id.progress_disable);
        this.moocallTagNumber = (TextView) findViewById(C0530R.id.moocallTagNumber);
        this.moocallTagNumberError = (TextView) findViewById(C0530R.id.moocallTagNumberError);
        this.moocallTagNumberError.setVisibility(8);
        this.saveTag = (TextView) findViewById(C0530R.id.saveTag);
        this.cancel = (TextView) findViewById(C0530R.id.cancel);
        this.animalName = (TextView) findViewById(C0530R.id.animalName);
        this.animalTag = (EditTextBackEvent) findViewById(C0530R.id.animalTag);
    }

    private void initList() {
        List<String> tags = new ArrayList();
        List<AnimalDb> cowsList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), new WhereCondition[0]).whereOr(Properties.Type.eq(Integer.valueOf(1)), Properties.Type.eq(Integer.valueOf(3)), new WhereCondition[0]).orderAsc(Properties.Tag_number).list();
        if (cowsList != null) {
            for (AnimalDb cowInList : cowsList) {
                tags.add(cowInList.getTag_number());
            }
        }
        this.animalTag.setAdapter(new ArrayAdapter(this, 17367043, tags));
    }

    private void implementListeners() {
        this.saveTag.setOnClickListener(new C05363());
        this.cancel.setOnClickListener(new C05374());
        this.animalTag.setOnItemClickListener(new C05385());
        this.animalTag.setOnEditorActionListener(new C05396());
        this.animalTag.setOnEditTextImeBackListener(new C11067());
    }

    private void setAnimal() {
        Utils.hideKeyboard(this);
        if (this.animalTag.getText().toString().isEmpty()) {
            this.animalForTag = null;
            this.animalName.setText("");
            return;
        }
        List<AnimalDb> animalList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Tag_number.eq(this.animalTag.getText().toString())).list();
        if (animalList.size() > 0) {
            this.animalForTag = (AnimalDb) animalList.get(0);
            this.animalName.setText(this.animalForTag.getName());
            return;
        }
        this.animalForTag = null;
        this.animalName.setText(getString(C0530R.string.new_animal));
    }

    private void checkForSave() {
        setAnimal();
        if (this.moocallTagNumber.getText().toString().isEmpty()) {
            this.moocallTagNumberError.setVisibility(0);
            this.moocallTagNumberError.setText(C0530R.string.must_scan_tag_number);
        } else if (this.moocallTagNumber.getText().toString().length() != 9 || !this.moocallTagNumber.getText().toString().toUpperCase().startsWith("ET")) {
            this.moocallTagNumberError.setVisibility(0);
            this.moocallTagNumberError.setText(getString(C0530R.string.invalid_tag));
        } else if (this.animalTag.getText().toString().isEmpty()) {
            this.animalTag.setError(getString(C0530R.string.must_enter_tag_number));
            this.animalTag.requestFocus();
        } else {
            List<AnimalDb> animalList = this.animalDbDao.queryBuilder().where(Properties.Status.eq(Integer.valueOf(1)), Properties.Moocall_tag_number.eq(this.moocallTagNumber.getText().toString())).list();
            if (animalList.size() > 0) {
                this.moocallTagNumberError.setVisibility(0);
                this.moocallTagNumberError.setText(getString(C0530R.string.this_tag_already_assigned) + ": " + ((AnimalDb) animalList.get(0)).getTag_number());
            } else if (this.animalForTag == null || this.animalForTag.getAnimal_id().equals(Integer.valueOf(0))) {
                checkForType();
            } else {
                saveThisTag(this.animalForTag.getType());
            }
        }
    }

    private void checkForType() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setTitle((int) C0530R.string.animal_type);
        alertDialogBuilder.setPositiveButton((int) C0530R.string.cow, new C05408());
        alertDialogBuilder.setNegativeButton((int) C0530R.string.heifer, new C05419());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private void saveThisTag(Integer type) {
        showProgress(true);
        if (this.offline == null || !this.offline.booleanValue()) {
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SCAN_MOOCALL_TAG));
            new AcquireResponseTask(this).execute(new String[]{new SaveMoocallTagUrl(this.animalForTag, type.toString(), null).createAndReturnUrl(this), QuickstartPreferences.SCAN_MOOCALL_TAG, "moocall-tag-number", this.moocallTagNumber.getText().toString(), "tag-number", this.animalTag.getText().toString()});
            return;
        }
        AnimalActionDb animalActionDb;
        if (this.animalForTag != null) {
            if (this.animalForTag.getAnimal_id().equals(Integer.valueOf(0))) {
                List<AnimalActionDb> animalActionList = this.animalActionDbDao.queryBuilder().where(AnimalActionDbDao.Properties.Animal_id.eq("0"), AnimalActionDbDao.Properties.Animal_tag_number.eq(this.animalTag.getText().toString())).list();
                if (animalActionList.size() > 0) {
                    animalActionDb = (AnimalActionDb) animalActionList.get(0);
                    animalActionDb.setMoocall_tag_number(this.moocallTagNumber.getText().toString());
                    this.animalActionDbDao.update(animalActionDb);
                }
            } else {
                animalActionDb = new AnimalActionDb();
                animalActionDb.setAction(Integer.valueOf(16));
                animalActionDb.setAnimal_id(this.animalForTag.getAnimal_id().toString());
                animalActionDb.setAnimal_tag_number(this.animalForTag.getTag_number());
                animalActionDb.setAnimal_type(this.animalForTag.getType().toString());
                animalActionDb.setMoocall_tag_number(this.moocallTagNumber.getText().toString());
                this.animalActionDbDao.insert(animalActionDb);
            }
            this.animalForTag.setMoocall_tag_number(this.moocallTagNumber.getText().toString());
            this.animalDbDao.update(this.animalForTag);
        } else {
            AnimalDb animalDb = new AnimalDb();
            animalDb.setTag_number(this.animalTag.getText().toString());
            animalDb.setName("");
            animalDb.setType(type);
            animalDb.setStatus(Integer.valueOf(1));
            animalDb.setAnimal_id(Integer.valueOf(0));
            animalDb.setBirth_date("0000-00-00");
            animalDb.setWeight("0");
            animalDb.setGestation(Integer.valueOf(283));
            animalDb.setLast_state(Integer.valueOf(2));
            animalDb.setState(Integer.valueOf(2));
            animalDb.setServer_state_cet_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            animalDb.setCycle_date(animalDb.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
            animalDb.setIn_heat_date(animalDb.getServer_state_cet_time().replaceAll("_", StringUtils.SPACE));
            animalDb.setMoocall_tag_number(this.moocallTagNumber.getText().toString());
            animalDb.setDeleted_animal(Integer.valueOf(0));
            animalDb.setUpdated_animal(Integer.valueOf(0));
            animalDb.setNew_animal(Integer.valueOf(1));
            animalActionDb = new AnimalActionDb();
            animalActionDb.setAction(Integer.valueOf(16));
            animalActionDb.setAnimal_id("0");
            animalActionDb.setAnimal_tag_number(this.animalTag.getText().toString());
            animalActionDb.setAnimal_type(type.toString());
            animalActionDb.setMoocall_tag_number(this.moocallTagNumber.getText().toString());
            this.animalActionDbDao.insert(animalActionDb);
            this.animalDbDao.insert(animalDb);
        }
        afterSavePrompt();
    }

    private void afterSavePrompt() {
        showProgress(false);
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((int) C0530R.string.want_enter_new_tag).setTitle((int) C0530R.string.save_successful);
        alertDialogBuilder.setPositiveButton((int) C0530R.string.enter_new, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ScanMoocallTagActivity.this.animalTag.setText("");
                ScanMoocallTagActivity.this.moocallTagNumber.setText("");
                ScanMoocallTagActivity.this.animalName.setText("");
                ScanMoocallTagActivity.this.startCameraIntent();
            }
        });
        alertDialogBuilder.setNegativeButton((int) C0530R.string.finish, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ScanMoocallTagActivity.this.finish();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private void actionAdded(Integer result) {
        try {
            if (result.intValue() > 0) {
                sendBroadcast(new Intent("refresh_lists"));
                sendBroadcast(new Intent("refresh_smart_list"));
                sendBroadcast(new Intent("refresh_animal_list"));
                afterSavePrompt();
                return;
            }
            showProgress(false);
            Toast.makeText(this, C0530R.string.failed, 1).show();
            switch (result.intValue()) {
                case -2:
                    this.moocallTagNumberError.setVisibility(0);
                    this.moocallTagNumberError.setText(getString(C0530R.string.this_tag_not_exit));
                    return;
                case -1:
                    this.moocallTagNumberError.setVisibility(0);
                    this.moocallTagNumberError.setText(getString(C0530R.string.this_tag_is_already_assigned));
                    return;
                default:
                    return;
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    ScanMoocallTagActivity.this.unregisterReceiver(this);
                    if (intent.getAction().equals(QuickstartPreferences.SCAN_MOOCALL_TAG)) {
                        ScanMoocallTagActivity.this.actionAdded(Integer.valueOf(Integer.parseInt((String) intent.getSerializableExtra("response"))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void startCameraIntent() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt(getString(C0530R.string.scanning_message));
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (result.getContents() != null) {
            setMoocallTagNumber(result.getContents());
        }
    }

    private void setMoocallTagNumber(String tag) {
        this.moocallTagNumberError.setVisibility(8);
        this.moocallTagNumberError.setText("");
        this.moocallTagNumber.setText(tag);
    }

    public boolean camera(MenuItem view) {
        startCameraIntent();
        return true;
    }

    public boolean input(MenuItem view) {
        openInputDialog();
        return true;
    }

    private void openInputDialog() {
        Builder builder = new Builder(this);
        builder.setTitle((int) C0530R.string.enter_moocall_tag_number);
        final View input = new EditText(this);
        input.setInputType(1);
        input.setText(this.moocallTagNumber.getText().toString());
        input.requestFocus();
        builder.setView(input);
        builder.setPositiveButton((int) C0530R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ScanMoocallTagActivity.this.setMoocallTagNumber(input.getText().toString());
            }
        });
        builder.setNegativeButton((int) C0530R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
