package com.moocall.moocall;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GoogleApiAvailability;
import com.moocall.moocall.adapter.DrawerListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.async.SyncOfflineAsync;
import com.moocall.moocall.db.AnimalActionDbDao;
import com.moocall.moocall.db.AnimalDbDao;
import com.moocall.moocall.db.CalvingDbDao;
import com.moocall.moocall.db.DaoSession;
import com.moocall.moocall.db.StateHistoryDbDao;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.User;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.CheckUserHaveAccountUrl;
import com.moocall.moocall.url.SendTokenUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.android.sdk.models.Participant;
import java.io.ByteArrayOutputStream;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected static int position;
    private LinearLayout askQuestionTab;
    private BroadcastReceiver broadcastReceiver;
    private Boolean broadcastRegistred = Boolean.valueOf(false);
    public DrawerLayout drawerLayout;
    protected ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    protected FrameLayout frameLayout;
    protected RelativeLayout homePage;
    private ImageLoader imageLoader;
    protected Tracker mTracker;
    private LinearLayout manageHerdTab;
    private LinearLayout moocallScanTag;
    private LinearLayout moocallSocialTab;
    private LinearLayout myMoocallTab;
    private DrawerListAdapter navigationDrawerAdapter;
    private String phoneUid;
    protected TextView profileEmail;
    protected TextView profileName;
    private View progressView;
    private BroadcastReceiver refreshUserBroadcastReceiver;
    private LinearLayout settingsButton;
    private String token;
    private Toolbar toolbar;
    protected Boolean userChecked = Boolean.valueOf(false);
    protected ImageView userImage;

    class C04481 implements OnItemClickListener {
        C04481() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MainActivity.this.openActivity(i);
        }
    }

    class C04492 implements OnClickListener {
        C04492() {
        }

        public void onClick(View v) {
            MainActivity.this.openActivity(1);
        }
    }

    class C04503 implements OnClickListener {
        C04503() {
        }

        public void onClick(View v) {
            MainActivity.this.openActivity(2);
        }
    }

    class C04514 implements OnClickListener {
        C04514() {
        }

        public void onClick(View v) {
            MainActivity.this.openActivity(3);
        }
    }

    class C04525 implements OnClickListener {
        C04525() {
        }

        public void onClick(View v) {
            MainActivity.this.openActivity(4);
        }
    }

    class C04536 implements OnClickListener {
        C04536() {
        }

        public void onClick(View v) {
            MainActivity.this.openActivity(5);
        }
    }

    class C04547 implements OnClickListener {
        C04547() {
        }

        public void onClick(View v) {
            MainActivity.this.openActivity(6);
        }
    }

    class C04558 implements OnClickListener {
        C04558() {
        }

        public void onClick(View v) {
            MainActivity.this.registerReceiver(MainActivity.this.refreshUserBroadcastReceiver, new IntentFilter("refresh_user"));
            MainActivity.this.broadcastRegistred = Boolean.valueOf(true);
            MainActivity.this.startActivity(new Intent(MainActivity.this, ChangeUserDetailsActivity.class));
        }
    }

    class C04569 implements OnClickListener {
        C04569() {
        }

        public void onClick(View v) {
            MainActivity.this.homePage.setVisibility(0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_main);
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        this.toolbar.setTitle((CharSequence) "");
        setSupportActionBar(this.toolbar);
        this.mTracker = ((MoocallAnalyticsApplication) getApplication()).getDefaultTracker();
        onResume();
        checkDb();
        setupLayout();
        addDrawerItems();
        implementsListeners();
        setupDrawer();
        startIntercom();
        if (StorageContainer.isLaunch) {
            startNotificationServices();
            checkUpdate();
            StorageContainer.isLaunch = false;
        }
        createBroadcastReceiver();
    }

    private void checkDb() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Editor editor = StorageContainer.getEditor();
        DaoSession daoSession = ((MoocallAnalyticsApplication) getApplication()).getDaoSession();
        if (pref.getInt("dbVersion", 0) != StorageContainer.dbVersion.intValue()) {
            daoSession.getAnimalDbDao();
            AnimalDbDao.dropTable(daoSession.getDatabase(), true);
            daoSession.getAnimalActionDbDao();
            AnimalActionDbDao.dropTable(daoSession.getDatabase(), true);
            daoSession.getCalvingDbDao();
            CalvingDbDao.dropTable(daoSession.getDatabase(), true);
            daoSession.getStateHistoryDbDao();
            StateHistoryDbDao.dropTable(daoSession.getDatabase(), true);
            editor.putInt("dbVersion", StorageContainer.dbVersion.intValue());
            editor.commit();
        }
        daoSession.getAnimalDbDao();
        AnimalDbDao.createTable(daoSession.getDatabase(), true);
        daoSession.getAnimalActionDbDao();
        AnimalActionDbDao.createTable(daoSession.getDatabase(), true);
        daoSession.getCalvingDbDao();
        CalvingDbDao.createTable(daoSession.getDatabase(), true);
        daoSession.getStateHistoryDbDao();
        StateHistoryDbDao.createTable(daoSession.getDatabase(), true);
        SyncOfflineAsync syncOfflineAsync = new SyncOfflineAsync(this, null);
    }

    private void setupLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(C0530R.id.drawer_layout);
        this.drawerList = (ListView) findViewById(C0530R.id.navDrawerList);
        RelativeLayout drawerHeader = (RelativeLayout) getLayoutInflater().inflate(C0530R.layout.drawer_header, null);
        this.drawerList.addHeaderView(drawerHeader);
        drawerHeader.setOnClickListener(null);
        this.frameLayout = (FrameLayout) findViewById(C0530R.id.container);
        this.profileName = (TextView) findViewById(C0530R.id.profileName);
        this.profileEmail = (TextView) findViewById(C0530R.id.profileEmail);
        this.userImage = (ImageView) findViewById(C0530R.id.userImage);
        setUser();
        this.homePage = (RelativeLayout) findViewById(C0530R.id.homePage);
        this.myMoocallTab = (LinearLayout) findViewById(C0530R.id.myMoocallTab);
        this.manageHerdTab = (LinearLayout) findViewById(C0530R.id.manageHerdTab);
        this.moocallScanTag = (LinearLayout) findViewById(C0530R.id.moocallScanTag);
        this.askQuestionTab = (LinearLayout) findViewById(C0530R.id.askQuestionTab);
        this.moocallSocialTab = (LinearLayout) findViewById(C0530R.id.moocallSocialTab);
        this.settingsButton = (LinearLayout) findViewById(C0530R.id.settingsTab);
        this.progressView = findViewById(C0530R.id.progress_disable);
        ImageLoaderConfiguration config = new Builder(this).build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(config);
    }

    private void setUser() {
        this.profileName.setText(Account.getName());
        this.profileEmail.setText(Account.getEmail());
    }

    private void addDrawerItems() {
        this.navigationDrawerAdapter = new DrawerListAdapter(this);
        this.drawerList.setAdapter(this.navigationDrawerAdapter);
    }

    private void implementsListeners() {
        this.drawerList.setOnItemClickListener(new C04481());
        this.manageHerdTab.setOnClickListener(new C04492());
        this.myMoocallTab.setOnClickListener(new C04503());
        this.moocallScanTag.setOnClickListener(new C04514());
        this.askQuestionTab.setOnClickListener(new C04525());
        this.moocallSocialTab.setOnClickListener(new C04536());
        this.settingsButton.setOnClickListener(new C04547());
        this.userImage.setOnClickListener(new C04558());
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setOnClickListener(new C04569());
    }

    public void startNotificationServices() {
        if (checkPlayServices()) {
            createAsyncBroadcast();
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.TOKEN_SEND));
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SAVE_TOKEN));
            sendBroadcast(new Intent(QuickstartPreferences.RESTART_SERVICE));
        }
    }

    public void createBroadcastReceiver() {
        this.refreshUserBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                if (intent.getAction().equals("refresh_user")) {
                    StorageContainer.setUser(null);
                    MainActivity.this.checkUserHaveAccount();
                }
            }
        };
    }

    private void checkUserHaveAccount() {
        if (StorageContainer.getUser() == null) {
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT));
            new AcquireResponseTask(this).execute(new String[]{new CheckUserHaveAccountUrl(String.valueOf(44)).createAndReturnUrl(this), QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT});
        } else if (StorageContainer.getUser().getPictureUrl() == null) {
            this.userChecked = Boolean.valueOf(true);
        } else if (StorageContainer.getUser().getPicture() == null) {
            loadImage();
        } else {
            byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
            this.userImage.setPadding(10, 10, 10, 10);
            this.userImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this, 50));
            this.userChecked = Boolean.valueOf(true);
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    String action = intent.getAction();
                    if (action.equals(QuickstartPreferences.SAVE_TOKEN)) {
                        MainActivity.this.onSaveTokenCompleted(new Boolean(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.TOKEN_SEND)) {
                        MainActivity.this.token = intent.getStringExtra("token");
                        MainActivity.this.phoneUid = intent.getStringExtra("phoneUid");
                    } else if (action.equals(QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT)) {
                        MainActivity.this.onCheckUserHaveAccountCompleted(new JSONObject(intent.getStringExtra("response")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void onCheckUserHaveAccountCompleted(JSONObject result) {
        try {
            JSONObject userObject = new JSONParserBgw(result).getJsonObject(Participant.USER_TYPE);
            if (userObject != null) {
                JSONParserBgw jsonParserUser = new JSONParserBgw(userObject);
                StorageContainer.setUser(new User(jsonParserUser.getInt("id"), jsonParserUser.getString("nickname"), jsonParserUser.getString("picture"), jsonParserUser.getString("country"), jsonParserUser.getString("city"), jsonParserUser.getBoolean("moderator")));
                if (StorageContainer.getUser().getPictureUrl() == null || StorageContainer.getUser().getPicture() != null) {
                    this.userChecked = Boolean.valueOf(true);
                    if (this.progressView.getVisibility() == 0) {
                        openActivity(4);
                        return;
                    }
                    return;
                }
                loadImage();
                return;
            }
            this.userChecked = Boolean.valueOf(true);
            if (this.progressView.getVisibility() == 0) {
                openActivity(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImage() {
        try {
            String src = StorageContainer.socialHost + "/moocall_social_images/" + StorageContainer.getUser().getPictureUrl();
            System.out.println("src: " + src);
            this.imageLoader.loadImage(src, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    System.out.println("BRAVOOOOOOOOOO");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    loadedImage.compress(CompressFormat.JPEG, 100, baos);
                    StorageContainer.getUser().setPicture(Base64.encodeToString(baos.toByteArray(), 0));
                    byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
                    MainActivity.this.userImage.setPadding(10, 10, 10, 10);
                    MainActivity.this.userImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), MainActivity.this, 50));
                    MainActivity.this.userChecked = Boolean.valueOf(true);
                    if (MainActivity.this.progressView.getVisibility() == 0) {
                        MainActivity.this.openActivity(4);
                    }
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSaveTokenCompleted(Boolean success) {
        if (success.booleanValue()) {
            unregisterReceiver(this.broadcastReceiver);
        } else if (this.token != null) {
            sendRegistrationToServer();
        }
    }

    private void sendRegistrationToServer() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.SAVE_TOKEN));
        new AcquireResponseTask(this).execute(new String[]{new SendTokenUrl(this.token, this.phoneUid).createAndReturnUrl(this), QuickstartPreferences.SAVE_TOKEN});
    }

    private void checkUpdate() {
        if (Account.getUpdate().booleanValue()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(C0530R.string.new_version_available)).setTitle(getString(C0530R.string.update_application));
            alertDialogBuilder.setPositiveButton(getString(C0530R.string.continue_with), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alertDialogBuilder.setNegativeButton(getString(C0530R.string.update), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.moocall.moocall")));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.create().show();
        }
    }

    private void setupDrawer() {
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, C0530R.string.app_name, C0530R.string.app_name) {
            public void onDrawerOpened(View drawerView) {
                MainActivity.this.invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            public void onDrawerClosed(View view) {
                MainActivity.this.invalidateOptionsMenu();
                super.onDrawerClosed(view);
            }
        };
        this.drawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerLayout.setDrawerListener(this.drawerToggle);
    }

    private void startIntercom() {
        Intercom.initialize(getApplication(), "android_sdk-2216b491b27bd030661438dce7e7580332fbb44e", "e5jq39ob");
        Intercom.client().registerIdentifiedUser(new Registration().withEmail(Account.getEmail()));
        Intercom.client().handlePushMessage();
    }

    protected void openActivity(int position) {
        this.drawerLayout.closeDrawer(this.drawerList);
        position = position;
        this.homePage.setVisibility(8);
        waitForUser(true);
        switch (position) {
            case 1:
                startActivity(new Intent(this, ManageHerdActivity.class).addFlags(131072));
                return;
            case 2:
                if (Account.getMyMoocall().booleanValue()) {
                    startActivity(new Intent(this, MyMoocallActivity.class).addFlags(131072));
                    return;
                }
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://moocall.com/products/moocall-sensors")));
                return;
            case 3:
                startActivity(new Intent(this, ScanMoocallTagActivity.class));
                return;
            case 4:
                startIntercomMessiging();
                return;
            case 5:
                startActivity(new Intent(this, SocialNetworkActivity.class).addFlags(131072));
                return;
            case 6:
                startActivity(new Intent(this, SettingsActivity.class));
                return;
            case 8:
                signout();
                return;
            default:
                return;
        }
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(this.drawerList)) {
            this.drawerLayout.closeDrawer(this.drawerList);
        } else {
            moveTaskToBack(true);
        }
    }

    protected void onResume() {
        super.onResume();
        waitForUser(false);
        StorageContainer.wakeApp(this);
        this.mTracker.setScreenName("Main Activity");
        this.mTracker.send(new ScreenViewBuilder().build());
    }

    private void signout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.confirm_logout)).setTitle(getString(C0530R.string.sign_out));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intercom.client().reset();
                StorageContainer.removeCredentialsFromPreferences(MainActivity.this.getApplicationContext());
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

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode == 0) {
            return true;
        }
        if (apiAvailability.isUserResolvableError(resultCode)) {
            apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } else {
            finish();
        }
        return false;
    }

    private void startIntercomMessiging() {
        Intercom.client().displayConversationsList();
    }

    public void waitForUser(boolean show) {
        if (this.progressView != null) {
            this.progressView.setVisibility(show ? 0 : 8);
        }
    }
}
