package com.moocall.moocall;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddNewUserUrl;
import com.moocall.moocall.url.EditUserUrl;
import com.moocall.moocall.util.StorageContainer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChangeUserDetailsActivity extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private int LOAD_PHOTO_CODE = 1;
    private int RESULT_CROP = 2;
    private int TAKE_PHOTO_CODE = 0;
    private BroadcastReceiver broadcastReceiver;
    private String cameraFileName;
    private String city = "Unknown";
    private String country = "Unknown";
    private GoogleApiClient googleApiClient;
    private String latitude = "53.2955476";
    private LocationRequest locationRequest;
    private String longitude = "-6.1789404";
    private View progressView;
    private Toolbar toolbar;
    private EditText userCity;
    private EditText userCountry;
    private Bitmap userImageBitmap;
    private EditText userNickname;
    private ImageView userPicture;

    class C04341 implements OnClickListener {
        C04341() {
        }

        public void onClick(View v) {
            ChangeUserDetailsActivity.this.onBackPressed();
        }
    }

    class C04353 extends BroadcastReceiver {
        C04353() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                ChangeUserDetailsActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.ADD_NEW_USER)) {
                    ChangeUserDetailsActivity.this.onAddNewUserCompleted(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.EDIT_USER)) {
                    ChangeUserDetailsActivity.this.onEditUserCompleted(intent.getStringExtra("response"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C10932 implements OnMenuItemClickListener {
        C10932() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_change_user_details);
        onResume();
        createAsyncBroadcast();
        Intent intent = getIntent();
        setupToolbar();
        setupLayout();
        setLocation();
        setupLocationService();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C04341());
        this.toolbar.inflateMenu(C0530R.menu.only_save);
        this.toolbar.setOnMenuItemClickListener(new C10932());
        if (StorageContainer.getUser() == null) {
            this.toolbar.setTitle(getString(C0530R.string.join_community));
        } else {
            this.toolbar.setTitle(getString(C0530R.string.edit_user));
        }
    }

    public void setupLayout() {
        this.userPicture = (ImageView) findViewById(C0530R.id.userPicture);
        this.userNickname = (EditText) findViewById(C0530R.id.userNickname);
        this.userCountry = (EditText) findViewById(C0530R.id.userCountry);
        this.userCity = (EditText) findViewById(C0530R.id.userCity);
        this.progressView = findViewById(C0530R.id.progress_disable);
        if (StorageContainer.getUser() != null) {
            this.country = StorageContainer.getUser().getCountry();
            this.city = StorageContainer.getUser().getCity();
            this.userNickname.setText(StorageContainer.getUser().getNickname());
            if (StorageContainer.getUser().getPicture() != null && !StorageContainer.getUser().getPicture().equals("")) {
                byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
                this.userPicture.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                this.userPicture.setScaleType(ScaleType.FIT_XY);
                return;
            }
            return;
        }
        EditText editText = this.userNickname;
        StorageContainer.getAccount();
        editText.setText(Account.getName());
    }

    private void setupLocationService() {
        this.googleApiClient = new Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        this.locationRequest = LocationRequest.create().setPriority(100).setInterval(10000).setFastestInterval(1000);
    }

    private void setLocation() {
        this.userCountry.setText(this.country);
        this.userCity.setText(this.city);
    }

    public void onTakePhotoClick(View view) {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MoocallImages");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "MoocallImage_" + timeStamp + ".jpg");
        this.cameraFileName = image.getAbsolutePath();
        cameraIntent.putExtra("output", Uri.fromFile(image));
        startActivityForResult(cameraIntent, this.TAKE_PHOTO_CODE);
    }

    public void onGallerySelectClick(View view) {
        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), this.LOAD_PHOTO_CODE);
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
            if (picturePath != null) {
                performCrop(Uri.fromFile(new File(picturePath)));
            }
        }
        if (requestCode == this.RESULT_CROP && resultCode == -1) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                this.userImageBitmap = (Bitmap) extras.getParcelable("data");
                this.userPicture.setImageBitmap(this.userImageBitmap);
                this.userPicture.setScaleType(ScaleType.FIT_XY);
            }
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
        if (this.userImageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.userImageBitmap.compress(CompressFormat.JPEG, 100, baos);
            encodedImage = Base64.encodeToString(baos.toByteArray(), 0);
        }
        this.userNickname.setError(null);
        String userNicknameText = this.userNickname.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(userNicknameText)) {
            this.userNickname.setError(getString(C0530R.string.error_field_required));
            focusView = this.userNickname;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            saveUser(userNicknameText, encodedImage);
        }
        return true;
    }

    private void saveUser(String userNickname, String encodedImage) {
        try {
            showProgress(true);
            userNickname = URLEncoder.encode(userNickname, "UTF-8");
            if (!this.city.equals(this.userCity.getText().toString())) {
                this.city = this.userCity.getText().toString();
            }
            if (!this.country.equals(this.userCountry.getText().toString())) {
                this.country = this.userCountry.getText().toString();
            }
            this.city = URLEncoder.encode(this.city, "UTF-8");
            this.country = URLEncoder.encode(this.country, "UTF-8");
            AcquireResponseTask acquireResponseTask;
            String[] strArr;
            if (StorageContainer.getUser() == null || StorageContainer.getUser().getId() == null) {
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_NEW_USER));
                acquireResponseTask = new AcquireResponseTask(this);
                strArr = new String[4];
                strArr[0] = new AddNewUserUrl(userNickname, this.latitude, this.longitude, this.city, this.country, String.valueOf(44)).createAndReturnUrl(this);
                strArr[1] = QuickstartPreferences.ADD_NEW_USER;
                strArr[2] = "encoded_image";
                strArr[3] = encodedImage;
                acquireResponseTask.execute(strArr);
                return;
            }
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.EDIT_USER));
            acquireResponseTask = new AcquireResponseTask(this);
            strArr = new String[4];
            strArr[0] = new EditUserUrl(StorageContainer.getUser().getId().toString(), userNickname, this.latitude, this.longitude, this.city, this.country).createAndReturnUrl(this);
            strArr[1] = QuickstartPreferences.EDIT_USER;
            strArr[2] = "encoded_image";
            strArr[3] = encodedImage;
            acquireResponseTask.execute(strArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new C04353();
    }

    public void onAddNewUserCompleted(String result) {
        showProgress(false);
        Toast.makeText(this, result, 1).show();
        startActivity(new Intent(this, SocialNetworkActivity.class));
        finish();
    }

    public void onEditUserCompleted(String result) {
        showProgress(false);
        Toast.makeText(this, result, 1).show();
        sendBroadcast(new Intent("refresh_user"));
        finish();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        if (this.googleApiClient != null) {
            this.googleApiClient.connect();
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.googleApiClient != null && this.googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiClient, (LocationListener) this);
            this.googleApiClient.disconnect();
        }
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, this.locationRequest, (LocationListener) this);
        } else {
            handleNewLocation(location);
        }
    }

    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                return;
            } catch (SendIntentException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("Location services connection failed with code " + connectionResult.getErrorCode());
    }

    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        this.latitude = String.valueOf(location.getLatitude());
        this.longitude = String.valueOf(location.getLongitude());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() <= 0) {
                Toast.makeText(this, getString(C0530R.string.cannot_access_location) + " Unknown, Unknown", 1).show();
            } else if (StorageContainer.getUser() == null || this.city == null || this.city.equals("Unknown") || this.country == null || this.country.equals("Unknown")) {
                this.city = ((Address) addresses.get(0)).getLocality();
                this.country = ((Address) addresses.get(0)).getCountryName();
                setLocation();
            }
        } catch (IOException e) {
            Toast.makeText(this, getString(C0530R.string.cannot_access_location) + " Unknown, Unknown", 1).show();
            e.printStackTrace();
        }
    }
}
