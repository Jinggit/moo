package com.moocall.moocall;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddNewAnimalUrl;
import com.moocall.moocall.url.DeleteAnimalUrl;
import com.moocall.moocall.url.EditAnimalUrl;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ManualInputActivity extends ActionBarActivity {
    private int LOAD_PHOTO_CODE = 1;
    private int RESULT_CROP = 2;
    private int TAKE_PHOTO_CODE = 0;
    private Animal animal;
    private TextView animalBirthDate;
    private TextView animalBreed;
    final Calendar animalCalendar = Calendar.getInstance();
    final Calendar animalDatetimeCalendar = Calendar.getInstance();
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
    private BroadcastReceiver broadcastReceiver;
    private String cameraFileName;
    private Bitmap cowImageBitmap;
    private Boolean editAnimal;
    private HashMap<String, Integer> gestationBreed;
    private LinearLayout getFromGallery;
    private Boolean newBull;
    private Boolean newCow;
    private ImageView pictureForCow;
    private View progressView;
    private List<String> realTypeList;
    private Integer startType;
    private List<String> stateList;
    private LinearLayout takePicture;
    private List<String> temperamentList;
    private Toolbar toolbar;
    private List<String> typeList;

    class C04651 implements OnClickListener {
        C04651() {
        }

        public void onClick(View v) {
            ManualInputActivity.this.onBackPressed();
        }
    }

    class C04663 implements OnClickListener {
        C04663() {
        }

        public void onClick(View v) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MoocallImages");
            imagesFolder.mkdirs();
            File image = new File(imagesFolder, "MoocallImage_" + timeStamp + ".jpg");
            ManualInputActivity.this.cameraFileName = image.getAbsolutePath();
            cameraIntent.putExtra("output", Uri.fromFile(image));
            ManualInputActivity.this.startActivityForResult(cameraIntent, ManualInputActivity.this.TAKE_PHOTO_CODE);
        }
    }

    class C04674 implements OnClickListener {
        C04674() {
        }

        public void onClick(View v) {
            ManualInputActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), ManualInputActivity.this.LOAD_PHOTO_CODE);
        }
    }

    class C04685 implements OnDateSetListener {
        C04685() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ManualInputActivity.this.animalCalendar.set(1, year);
            ManualInputActivity.this.animalCalendar.set(2, monthOfYear);
            ManualInputActivity.this.animalCalendar.set(5, dayOfMonth);
            ManualInputActivity.this.updateDate();
        }
    }

    class C04707 implements OnClickListener {
        C04707() {
        }

        public void onClick(View view) {
            ManualInputActivity.this.setFirstDropdown(Integer.valueOf(1));
        }
    }

    class C04718 implements OnClickListener {
        C04718() {
        }

        public void onClick(View view) {
            ManualInputActivity.this.setFirstDropdown(Integer.valueOf(2));
        }
    }

    class C04729 implements OnClickListener {
        C04729() {
        }

        public void onClick(View view) {
            ManualInputActivity.this.setFirstDropdown(Integer.valueOf(3));
        }
    }

    class C10972 implements OnMenuItemClickListener {
        C10972() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_manual_input);
        onResume();
        createAsyncBroadcast();
        Intent intent = getIntent();
        this.animal = (Animal) intent.getSerializableExtra("animal");
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
        this.toolbar.setNavigationOnClickListener(new C04651());
        this.toolbar.inflateMenu(C0530R.menu.save_delete);
        this.toolbar.setOnMenuItemClickListener(new C10972());
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
        this.takePicture = (LinearLayout) findViewById(C0530R.id.takePicture);
        this.getFromGallery = (LinearLayout) findViewById(C0530R.id.getFromGallery);
        this.pictureForCow = (ImageView) findViewById(C0530R.id.pictureForCow);
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
            this.animalTagNumber.setText(this.animal.getTagNumber().toString());
            if (StorageContainer.getAnimalImageMemoryCache().get(this.animal.getTagNumber()) != null) {
                this.pictureForCow.setImageBitmap((Bitmap) StorageContainer.getAnimalImageMemoryCache().get(this.animal.getTagNumber()));
                this.pictureForCow.setScaleType(ScaleType.FIT_XY);
            }
            if (this.animal.getBirthDateText() != null) {
                if (this.animal.getBirthDateServer().length() > 0) {
                    String[] date = this.animal.getBirthDateServer().split("-");
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
            if ((this.animal.getType().equals(Integer.valueOf(1)) || this.animal.getType().equals(Integer.valueOf(3))) && this.animal.getCowState().equals(Integer.valueOf(1))) {
                this.animalStatusLayout.setVisibility(0);
                setStatusLayout(Integer.valueOf(this.animal.getCowState().intValue() - 1));
                return;
            }
            return;
        }
        this.animal = new Animal();
        this.animal.setBreed("");
        this.animalEditLayout.setVisibility(8);
    }

    private void setStatusLayout(Integer which) {
        this.animalStatus.setText(((String) this.stateList.get(which.intValue())).toString());
        this.animal.setState(Integer.valueOf(which.intValue() + 1));
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
        this.takePicture.setOnClickListener(new C04663());
        this.getFromGallery.setOnClickListener(new C04674());
        final OnDateSetListener date = new C04685();
        this.animalBirthDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(ManualInputActivity.this);
                new DatePickerDialog(ManualInputActivity.this, date, ManualInputActivity.this.animalCalendar.get(1), ManualInputActivity.this.animalCalendar.get(2), ManualInputActivity.this.animalCalendar.get(5)).show();
            }
        });
        this.animalBreed.setOnClickListener(new C04707());
        this.animalTemperament.setOnClickListener(new C04718());
        this.animalType.setOnClickListener(new C04729());
        this.animalStatus.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ManualInputActivity.this.setThirdDropdown(null, null);
            }
        });
        if (this.editAnimal.booleanValue()) {
            checkStatusDateTime();
        } else {
            updateDateTime();
        }
        final OnTimeSetListener time = new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ManualInputActivity.this.animalDatetimeCalendar.set(11, hourOfDay);
                ManualInputActivity.this.animalDatetimeCalendar.set(12, minute);
                ManualInputActivity.this.updateDateTime();
            }
        };
        final OnDateSetListener date2 = new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ManualInputActivity.this.animalDatetimeCalendar.set(1, year);
                ManualInputActivity.this.animalDatetimeCalendar.set(2, monthOfYear);
                ManualInputActivity.this.animalDatetimeCalendar.set(5, dayOfMonth);
                ManualInputActivity.this.updateDateTime();
            }
        };
        this.animalStatusDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(ManualInputActivity.this);
                new DatePickerDialog(ManualInputActivity.this, date2, ManualInputActivity.this.animalDatetimeCalendar.get(1), ManualInputActivity.this.animalDatetimeCalendar.get(2), ManualInputActivity.this.animalDatetimeCalendar.get(5)).show();
            }
        });
        this.animalStatusTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(ManualInputActivity.this);
                new TimePickerDialog(ManualInputActivity.this, time, ManualInputActivity.this.animalDatetimeCalendar.get(11), ManualInputActivity.this.animalDatetimeCalendar.get(12), true).show();
            }
        });
    }

    private void checkStatusDateTime() {
        if (this.animal.getGestation() != null) {
            this.animalGestation.setText(this.animal.getGestation().toString());
            String[] date;
            String[] datetime;
            String[] time;
            switch (this.animal.getCowState().intValue() - 1) {
                case 0:
                    if (!(this.animal.getDueDate() == null || this.animal.getDueDate().length() <= 5 || this.animal.getDueDate().equals("00-00-0000"))) {
                        date = this.animal.getDueDate().split("-");
                        this.animalDatetimeCalendar.set(1, Integer.parseInt(date[2]));
                        this.animalDatetimeCalendar.set(2, Integer.parseInt(date[1]) - 1);
                        this.animalDatetimeCalendar.set(5, Integer.parseInt(date[0]));
                        break;
                    }
                case 1:
                    if (!(this.animal.getInHeatDate() == null || this.animal.getInHeatDate().length() <= 5 || this.animal.getInHeatDate().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getInHeatDate().split(StringUtils.SPACE);
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
                    if (!(this.animal.getInHeatDate() == null || this.animal.getInHeatDate().length() <= 5 || this.animal.getInHeatDate().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getInHeatDate().split(StringUtils.SPACE);
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
                    if (!(this.animal.getInseminationDate() == null || this.animal.getInseminationDate().length() <= 5 || this.animal.getInseminationDate().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getInseminationDate().split(StringUtils.SPACE);
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
                    if (!(this.animal.getLastCalving() == null || this.animal.getLastCalving().length() <= 5 || this.animal.getLastCalving().equals("0000-00-00 00:00:00"))) {
                        datetime = this.animal.getLastCalving().split(StringUtils.SPACE);
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
                    ManualInputActivity.this.setCustomInput();
                    return;
                }
                ManualInputActivity.this.animalBreed.setText(ManualInputActivity.this.breedList[first.intValue()] + " x " + ManualInputActivity.this.breedList[which]);
                ManualInputActivity.this.animal.setBreed(ManualInputActivity.this.breedList[first.intValue()] + " x " + ManualInputActivity.this.breedList[which]);
                ManualInputActivity.this.animalGestation.setText("283");
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ManualInputActivity.this.setCrossbreedQuestion(first);
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
                    ManualInputActivity.this.setCrossbreedSelector(first);
                    return;
                }
                ManualInputActivity.this.animalBreed.setText(ManualInputActivity.this.breedList[first.intValue()]);
                ManualInputActivity.this.animal.setBreed(ManualInputActivity.this.breedList[first.intValue()]);
                if (ManualInputActivity.this.gestationBreed.containsKey(ManualInputActivity.this.breedList[first.intValue()])) {
                    ManualInputActivity.this.animalGestation.setText(((Integer) ManualInputActivity.this.gestationBreed.get(ManualInputActivity.this.breedList[first.intValue()])).toString());
                } else {
                    ManualInputActivity.this.animalGestation.setText("283");
                }
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ManualInputActivity.this.setFirstDropdown(Integer.valueOf(1));
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
                ManualInputActivity.this.animalBreed.setText(input.getText().toString());
                ManualInputActivity.this.animal.setBreed(input.getText().toString());
                ManualInputActivity.this.animalGestation.setText("283");
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
                            ManualInputActivity.this.setCustomInput();
                            return;
                        } else {
                            ManualInputActivity.this.setCrossbreedQuestion(Integer.valueOf(which));
                            return;
                        }
                    case 2:
                        ManualInputActivity.this.animalTemperament.setText((CharSequence) ManualInputActivity.this.temperamentList.get(which));
                        ManualInputActivity.this.animal.setTemperament((String) ManualInputActivity.this.temperamentList.get(which));
                        return;
                    case 3:
                        if (ManualInputActivity.this.newBull != null && ManualInputActivity.this.newBull.booleanValue()) {
                            ManualInputActivity.this.animalType.setText(((String) ManualInputActivity.this.typeList.get(which)).toString());
                            switch (which) {
                                case 0:
                                    ManualInputActivity.this.animal.setType(Integer.valueOf(2));
                                    return;
                                case 1:
                                    ManualInputActivity.this.animal.setType(Integer.valueOf(4));
                                    return;
                                default:
                                    return;
                            }
                        } else if (ManualInputActivity.this.newCow == null || !ManualInputActivity.this.newCow.booleanValue()) {
                            ManualInputActivity.this.setSecondDropdown(Integer.valueOf(which), listID);
                            return;
                        } else {
                            ManualInputActivity.this.animalType.setText(((String) ManualInputActivity.this.typeList.get(which)).toString());
                            switch (which) {
                                case 0:
                                    ManualInputActivity.this.animal.setType(Integer.valueOf(1));
                                    break;
                                case 1:
                                    ManualInputActivity.this.animal.setType(Integer.valueOf(3));
                                    break;
                            }
                            ManualInputActivity.this.animalStatusLayout.setVisibility(0);
                            ManualInputActivity.this.setThirdDropdown(listID, null);
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
                ManualInputActivity.this.animalType.setText(list[which]);
                switch (type.intValue()) {
                    case 1:
                        switch (which) {
                            case 0:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(5));
                                break;
                            case 1:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(3));
                                break;
                            default:
                                break;
                        }
                    case 2:
                        switch (which) {
                            case 0:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(6));
                                break;
                            case 1:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(4));
                                break;
                            case 2:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(2));
                                break;
                            case 3:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(10));
                                break;
                            default:
                                break;
                        }
                    case 3:
                        switch (which) {
                            case 0:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(7));
                                break;
                            case 1:
                                ManualInputActivity.this.animal.setType(Integer.valueOf(8));
                                break;
                            default:
                                break;
                        }
                }
                if (ManualInputActivity.this.animal.getType().intValue() == 3) {
                    ManualInputActivity.this.setThirdDropdown(type, parentType);
                    ManualInputActivity.this.animalStatusLayout.setVisibility(0);
                    return;
                }
                ManualInputActivity.this.animalStatusLayout.setVisibility(8);
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ManualInputActivity.this.setFirstDropdown(parentType);
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
                ManualInputActivity.this.setStatusLayout(Integer.valueOf(which));
            }
        });
        builder.setNegativeButton((int) C0530R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (parentType == null || type == null) {
                    if (type != null) {
                        ManualInputActivity.this.setFirstDropdown(type);
                    } else {
                        dialog.dismiss();
                    }
                } else if (type.intValue() == 0) {
                    ManualInputActivity.this.setFirstDropdown(parentType);
                } else {
                    ManualInputActivity.this.setSecondDropdown(type, parentType);
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
            if (extras != null) {
                this.cowImageBitmap = (Bitmap) extras.getParcelable("data");
                this.pictureForCow.setImageBitmap(this.cowImageBitmap);
                this.pictureForCow.setScaleType(ScaleType.FIT_XY);
            } else if (uri != null) {
                this.cowImageBitmap = decodeUriAsBitmap(uri);
                this.pictureForCow.setImageBitmap(this.cowImageBitmap);
                this.pictureForCow.setScaleType(ScaleType.FIT_XY);
            }
            if (extras != null) {
                this.cowImageBitmap = (Bitmap) extras.getParcelable("data");
                this.pictureForCow.setImageBitmap(this.cowImageBitmap);
                this.pictureForCow.setScaleType(ScaleType.FIT_XY);
            }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        try {
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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

    public boolean save(MenuItem view) {
        String encodedImage = null;
        if (this.cowImageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.cowImageBitmap.compress(CompressFormat.JPEG, 100, baos);
            encodedImage = Base64.encodeToString(baos.toByteArray(), 0);
        }
        this.animalTagNumber.setError(null);
        this.animalType.setError(null);
        this.animalBirthDate.setError(null);
        if (this.animal.getType() != null && ((this.animal.getType().intValue() == 1 || this.animal.getType().intValue() == 3) && !this.editAnimal.booleanValue())) {
            this.animalStatus.setError(null);
        }
        String tagNumber = this.animalTagNumber.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(tagNumber)) {
            this.animalTagNumber.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalTagNumber;
            cancel = true;
        } else if (tagNumber.length() < 3) {
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
        if (this.animal.getType() != null && ((this.animal.getType().intValue() == 7 || this.animal.getType().intValue() == 8) && this.animal.getBirthDateServer() == null)) {
            this.animalBirthDate.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalBirthDate;
            cancel = true;
        }
        if (this.animal.getType() != null && ((this.animal.getType().intValue() == 1 || this.animal.getType().intValue() == 3) && !this.editAnimal.booleanValue() && this.animal.getState() == null)) {
            this.animalStatus.setError(getString(C0530R.string.error_field_required));
            focusView = this.animalStatus;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            saveCow(tagNumber, encodedImage);
        }
        return true;
    }

    private void saveCow(String tagNumber, String encodedImage) {
        try {
            String name = URLEncoder.encode(this.animalName.getText().toString(), "UTF-8");
            String temperament = null;
            if (this.animal.getTemperament() != null) {
                temperament = URLEncoder.encode(this.animal.getTemperament(), "UTF-8");
            }
            String vendor = URLEncoder.encode(this.animalVendor.getText().toString(), "UTF-8");
            showProgress(true);
            if (this.animal.getId() != null) {
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.EDIT_ANIMAL));
                new AcquireResponseTask(this).execute(new String[]{new EditAnimalUrl(this.animal.getId().toString(), this.animal.getBirthDateServer(), name, this.startType.toString(), this.animal.getType().toString(), temperament, vendor, this.animal.getState(), this.animal.getStateServerCetTime(), this.animalGestation.getText().toString()).createAndReturnUrl(this), QuickstartPreferences.EDIT_ANIMAL, "breed", this.animal.getBreed(), "encoded_image", encodedImage, "tag-number", tagNumber});
                return;
            }
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_NEW_ANIMAL));
            AcquireResponseTask acquireResponseTask = new AcquireResponseTask(this);
            r2 = new String[8];
            r2[0] = new AddNewAnimalUrl(this.animal.getBirthDateServer(), name, this.animal.getType().toString(), this.animal.getState(), this.animal.getStateServerCetTime(), this.animalGestation.getText().toString()).createAndReturnUrl(this);
            r2[1] = QuickstartPreferences.ADD_NEW_ANIMAL;
            r2[2] = "breed";
            r2[3] = this.animal.getBreed();
            r2[4] = "encoded_image";
            r2[5] = encodedImage;
            r2[6] = "tag-number";
            r2[7] = tagNumber;
            acquireResponseTask.execute(r2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean delete(MenuItem view) {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.delete_animal_text)).setTitle(getString(C0530R.string.delete_animal));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ManualInputActivity.this.deleteThisAnimal();
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
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_ANIMAL));
        new AcquireResponseTask(this).execute(new String[]{new DeleteAnimalUrl(this.animal.getId().toString(), this.animal.getType().toString()).createAndReturnUrl(this), QuickstartPreferences.DELETE_ANIMAL});
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    ManualInputActivity.this.unregisterReceiver(this);
                    String action = intent.getAction();
                    if (action.equals(QuickstartPreferences.ADD_NEW_ANIMAL)) {
                        ManualInputActivity.this.onAddNewAnimalCompleted(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.EDIT_ANIMAL)) {
                        ManualInputActivity.this.onEditAnimalCompleted(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.DELETE_ANIMAL)) {
                        ManualInputActivity.this.onDeleteCowCompleted(intent.getStringExtra("response"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void onAddNewAnimalCompleted(String result) {
        showProgress(false);
        Toast.makeText(this, result, 1).show();
        if (result.equals("\"success\"")) {
            sendBroadcast(new Intent("refresh_lists"));
            finish();
        } else if (result.equals("\"existing tag number\"")) {
            this.animalTagNumber.setError(getString(C0530R.string.tag_problem));
            this.animalTagNumber.requestFocus();
        }
    }

    public void onEditAnimalCompleted(String result) {
        showProgress(false);
        Toast.makeText(this, result, 1).show();
        if (result.equals("\"success\"")) {
            sendBroadcast(new Intent("refresh_lists"));
            sendBroadcast(new Intent("refresh_smart_list"));
            sendBroadcast(new Intent("refresh_animal_details"));
            sendBroadcast(new Intent("refresh_animal_list"));
            finish();
        } else if (result.equals("\"existing tag number\"")) {
            this.animalTagNumber.setError(getString(C0530R.string.tag_problem));
            this.animalTagNumber.requestFocus();
        }
    }

    public void onDeleteCowCompleted(String result) {
        showProgress(false);
        if (result.equals("\"success\"")) {
            sendBroadcast(new Intent("refresh_lists"));
            sendBroadcast(new Intent("refresh_smart_list"));
            sendBroadcast(new Intent("refresh_animal_list"));
            sendBroadcast(new Intent("animal_deleted"));
            showMessage();
            return;
        }
        Toast.makeText(this, result, 1).show();
    }

    private void showMessage() {
        CharSequence message = getString(C0530R.string.animal_deleted_text);
        CharSequence title = getString(C0530R.string.animal_deleted);
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage(message).setTitle(title);
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ManualInputActivity.this.finish();
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
