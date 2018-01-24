package com.moocall.moocall;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.moocall.moocall.adapter.PostListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Comment;
import com.moocall.moocall.domain.Like;
import com.moocall.moocall.domain.Post;
import com.moocall.moocall.domain.PostFilter;
import com.moocall.moocall.domain.User;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddNewPostUrl;
import com.moocall.moocall.url.CheckUserHaveAccountUrl;
import com.moocall.moocall.url.DeletePostUrl;
import com.moocall.moocall.url.EditPostUrl;
import com.moocall.moocall.url.FetchPostListUrl;
import com.moocall.moocall.url.GetPostUpdateUrl;
import com.moocall.moocall.url.GetUsersCountries;
import com.moocall.moocall.url.LikePostUrl;
import com.moocall.moocall.url.ReportPostUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.TouchImageView;
import com.moocall.moocall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rd.animation.ScaleAnimation;
import io.intercom.android.sdk.models.Participant;
import io.intercom.android.sdk.views.IntercomToolbar;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SocialNetworkActivity extends MainActivity {
    private int LOAD_PHOTO_CODE = 1;
    private int TAKE_PHOTO_CODE = 0;
    private RelativeLayout addPostButtonLayout;
    private RelativeLayout addPostLayout;
    private ImageView allUsers;
    private TextView applyFilter;
    private RelativeLayout attachmentLayout;
    private BroadcastReceiver broadcastReceiver;
    private Boolean broadcastRegistred = Boolean.valueOf(false);
    private String cameraFileName;
    private ImageView categoryAll;
    private ImageView categoryBusinessOfFarming;
    private ImageView categoryBuySell;
    private ImageView categoryCattleBreeding;
    private ImageView categoryDairyFarming;
    private ImageView categoryFarmingNews;
    private LinearLayout categorySelectLayout;
    private Spinner countrySelect;
    private PostFilter currentPostFilter;
    private RelativeLayout filterLayout;
    private Boolean flinging = Boolean.valueOf(false);
    private TouchImageView imageLarge;
    private ImageLoader imageLoader;
    private String lastFetchTime;
    private Post likedPost;
    private Boolean loadMore;
    private Integer newPostCategoryId;
    private Boolean noMoreToLoad;
    private Bitmap oldImageBitmap;
    private ImageView peopleILiked;
    private ImageView postAttachment;
    private PostFilter postFilter;
    private Bitmap postImageBitmap;
    private List<Post> postList;
    private PostListAdapter postListAdapter;
    ListView postListView;
    private EditText postMessage;
    private SwipeRefreshLayout postsSwipeRefreshLayout;
    private View progressView;
    private BroadcastReceiver refreshPostListBrodcastReceiver;
    private TextView resetFilter;
    private Toolbar toolbar;
    private RelativeLayout transparentBackground;
    private Uri urlData;
    private List<String> usersCountries;
    private List<User> usersList;

    class C05641 implements OnClickListener {
        C05641() {
        }

        public void onClick(View v) {
            SocialNetworkActivity.this.openNewPostLayout();
        }
    }

    class C05654 implements OnClickListener {
        C05654() {
        }

        public void onClick(View v) {
            SocialNetworkActivity.this.openNewPostLayout();
        }
    }

    class C05665 implements OnClickListener {
        C05665() {
        }

        public void onClick(View v) {
            SocialNetworkActivity.this.transparentBackground.setVisibility(8);
            SocialNetworkActivity.this.imageLarge.setVisibility(8);
            SocialNetworkActivity.this.toolbar.setVisibility(0);
        }
    }

    class C05676 implements OnClickListener {
        C05676() {
        }

        public void onClick(View v) {
            SocialNetworkActivity.this.registerReceiver(SocialNetworkActivity.this.refreshPostListBrodcastReceiver, new IntentFilter("refresh_user"));
            SocialNetworkActivity.this.broadcastRegistred = Boolean.valueOf(true);
            SocialNetworkActivity.this.startActivity(new Intent(SocialNetworkActivity.this, ChangeUserDetailsActivity.class));
        }
    }

    class C05687 implements OnScrollListener {
        private int currentFirstVisibleItem;
        private int currentScrollState;
        private int currentVisibleItemCount;
        private int mLastFirstVisibleItem;
        private int totalItem;

        C05687() {
        }

        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            this.currentScrollState = scrollState;
            if (!(SocialNetworkActivity.this.noMoreToLoad == null || SocialNetworkActivity.this.noMoreToLoad.booleanValue())) {
                isScrollCompleted();
            }
            if (scrollState == 0) {
                SocialNetworkActivity.this.flinging = Boolean.valueOf(false);
                int count = absListView.getChildCount();
                for (int i = 0; i < count; i++) {
                    int position = SocialNetworkActivity.this.postListView.getPositionForView(absListView.getChildAt(i));
                    if (SocialNetworkActivity.this.postList.size() > position - 1 && position > 1) {
                        Post post = (Post) SocialNetworkActivity.this.postList.get(position - 1);
                        if (!(post.getAttachment() == null || StorageContainer.getPostImageMemoryCache().get(post.getId()) != null || post.getImageLoading().booleanValue() || SocialNetworkActivity.this.flinging.booleanValue())) {
                            SocialNetworkActivity.this.loadImage(post.getAttachment(), post);
                        }
                    }
                }
            } else {
                SocialNetworkActivity.this.flinging = Boolean.valueOf(true);
            }
            SocialNetworkActivity.this.postListAdapter.setFlinging(SocialNetworkActivity.this.flinging);
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.currentFirstVisibleItem = firstVisibleItem;
            this.currentVisibleItemCount = visibleItemCount;
            this.totalItem = totalItemCount;
            if (this.mLastFirstVisibleItem < firstVisibleItem) {
                SocialNetworkActivity.this.toolbar.animate().translationY((float) (-SocialNetworkActivity.this.toolbar.getBottom())).setInterpolator(new AccelerateInterpolator()).start();
                SocialNetworkActivity.this.addPostButtonLayout.animate().translationY((float) (((LayoutParams) SocialNetworkActivity.this.addPostButtonLayout.getLayoutParams()).bottomMargin + SocialNetworkActivity.this.addPostButtonLayout.getHeight())).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
            if (this.mLastFirstVisibleItem > firstVisibleItem) {
                SocialNetworkActivity.this.toolbar.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator()).start();
                SocialNetworkActivity.this.addPostButtonLayout.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).start();
            }
            this.mLastFirstVisibleItem = firstVisibleItem;
            boolean enable = false;
            if (SocialNetworkActivity.this.postListView != null && SocialNetworkActivity.this.postListView.getChildCount() > 0) {
                boolean topOfFirstItemVisible;
                boolean firstItemVisible = SocialNetworkActivity.this.postListView.getFirstVisiblePosition() == 0;
                if (SocialNetworkActivity.this.postListView.getChildAt(0).getTop() == 0) {
                    topOfFirstItemVisible = true;
                } else {
                    topOfFirstItemVisible = false;
                }
                enable = firstItemVisible && topOfFirstItemVisible;
            }
            if (SocialNetworkActivity.this.postsSwipeRefreshLayout.isRefreshing() && !enable) {
                SocialNetworkActivity.this.postsSwipeRefreshLayout.setRefreshing(enable);
            }
        }

        private void isScrollCompleted() {
            if (this.totalItem != 0 && this.currentFirstVisibleItem + this.currentVisibleItemCount > this.totalItem - 3 && this.currentScrollState == 0) {
                SocialNetworkActivity.this.loadMore = Boolean.valueOf(true);
                SocialNetworkActivity.this.fetchPosts();
            }
        }
    }

    class C11093 implements OnRefreshListener {
        C11093() {
        }

        public void onRefresh() {
            if (SocialNetworkActivity.this.postsSwipeRefreshLayout.isRefreshing()) {
                SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                SocialNetworkActivity.this.fetchPosts();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onResume();
        createAsyncBroadcast();
        setupDrawer();
        setupLayout();
        implementListeners();
        createBroadcastReceiver();
        this.urlData = getIntent().getData();
        if (StorageContainer.getUser() == null) {
            checkUserHaveAccount();
            return;
        }
        this.loadMore = Boolean.valueOf(false);
        this.noMoreToLoad = Boolean.valueOf(false);
        fetchPosts();
        if (this.urlData != null) {
            openNewPostLayout();
        }
    }

    private void setupDrawer() {
        getLayoutInflater().inflate(C0530R.layout.activity_social_network, this.frameLayout);
        this.drawerList.setItemChecked(position, true);
        this.homePage.setVisibility(8);
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
    }

    private void setupLayout() {
        createPostImageMemoryCache();
        ImageLoaderConfiguration config = new Builder(this).build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.destroy();
        this.imageLoader.init(config);
        this.postListView = (ListView) findViewById(C0530R.id.postListView);
        View headerLayout = getLayoutInflater().inflate(C0530R.layout.social_network_header, null);
        headerLayout.setOnClickListener(new C05641());
        RelativeLayout footerLayout = (RelativeLayout) getLayoutInflater().inflate(C0530R.layout.notification_list_footer, null);
        footerLayout.setOnClickListener(null);
        this.postListView.addHeaderView(headerLayout);
        this.postListView.addFooterView(footerLayout);
        this.postsSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0530R.id.postsSwipeRefreshLayout);
        this.postsSwipeRefreshLayout.setProgressViewOffset(false, 0, IntercomToolbar.TITLE_FADE_DURATION_MS);
        this.postList = new ArrayList();
        this.postFilter = new PostFilter((Activity) this);
        this.postListAdapter = new PostListAdapter(this, this.postList, this.postFilter);
        this.postListAdapter.setFlinging(this.flinging);
        this.postListView.setAdapter(this.postListAdapter);
        this.progressView = footerLayout.findViewById(C0530R.id.notification_progress);
        this.addPostButtonLayout = (RelativeLayout) findViewById(C0530R.id.addPostButtonLayout);
        this.addPostLayout = (RelativeLayout) findViewById(C0530R.id.addPostLayout);
        this.transparentBackground = (RelativeLayout) findViewById(C0530R.id.transparentBackground);
        this.imageLarge = (TouchImageView) findViewById(C0530R.id.postImageLarge);
    }

    private void createPostImageMemoryCache() {
        StorageContainer.setPostImageMemoryCache(new LruCache<Integer, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 4) {
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        });
    }

    private void implementListeners() {
        this.postsSwipeRefreshLayout.setOnRefreshListener(new C11093());
        registerForContextMenu(this.postListView);
        this.addPostButtonLayout.setOnClickListener(new C05654());
        this.transparentBackground.setOnClickListener(new C05665());
        this.userImage.setOnClickListener(new C05676());
        this.postListView.setOnScrollListener(new C05687());
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        if (this.postList.size() > info.position - 1) {
            final Post post = (Post) this.postList.get(info.position - 1);
            if (post.getUser() != null || StorageContainer.getUser() == null) {
                if (StorageContainer.getUser() != null && StorageContainer.getUser().getModerator().booleanValue()) {
                    menu.add(getString(C0530R.string.edit)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            SocialNetworkActivity.this.edit(post);
                            return true;
                        }
                    });
                    menu.add(getString(C0530R.string.delete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            SocialNetworkActivity.this.delete(post);
                            return true;
                        }
                    });
                }
                menu.add(getString(C0530R.string.report)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        SocialNetworkActivity.this.report(post);
                        return true;
                    }
                });
                return;
            }
            menu.add(getString(C0530R.string.edit)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    SocialNetworkActivity.this.edit(post);
                    return true;
                }
            });
            menu.add(getString(C0530R.string.delete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    SocialNetworkActivity.this.delete(post);
                    return true;
                }
            });
        }
    }

    private void report(final Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(C0530R.string.report_reason));
        final View input = new EditText(this);
        input.setInputType(1);
        builder.setView(input);
        builder.setPositiveButton(getString(C0530R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SocialNetworkActivity.this.reportThisPost(post, input.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(C0530R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void edit(final Post post) {
        this.addPostLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_in));
        this.addPostLayout.setVisibility(0);
        ImageView userPostImage = (ImageView) findViewById(C0530R.id.userPostImage);
        TextView userPostName = (TextView) findViewById(C0530R.id.userPostName);
        TextView userPostLocation = (TextView) findViewById(C0530R.id.userPostLocation);
        TextView addPostIcon = (TextView) findViewById(C0530R.id.addPostIcon);
        ImageView addPictureFromCameraIcon = (ImageView) findViewById(C0530R.id.addPictureFromCameraIcon);
        ImageView addPictureFromGalleryIcon = (ImageView) findViewById(C0530R.id.addPictureFromGalleryIcon);
        LinearLayout selectCategoryIcon = (LinearLayout) findViewById(C0530R.id.selectCategoryIcon);
        this.postMessage = (EditText) findViewById(C0530R.id.postMessage);
        this.postAttachment = (ImageView) findViewById(C0530R.id.postAttachment);
        this.attachmentLayout = (RelativeLayout) findViewById(C0530R.id.attachmentLayout);
        this.postAttachment.setImageBitmap(null);
        this.attachmentLayout.setVisibility(8);
        ImageView removeAttachment = (ImageView) findViewById(C0530R.id.removeAttachment);
        this.categorySelectLayout = (LinearLayout) findViewById(C0530R.id.categorySelectLayout);
        this.categorySelectLayout.setVisibility(8);
        this.newPostCategoryId = post.getCategory();
        this.postMessage.setText(post.getText());
        if (!(post.getAttachment() == null || StorageContainer.getPostImageMemoryCache().get(post.getId()) == null)) {
            this.attachmentLayout.setVisibility(0);
            if (getResources().getConfiguration().orientation == 2) {
                this.oldImageBitmap = Utils.getResizedPostBitmap((Bitmap) StorageContainer.getPostImageMemoryCache().get(post.getId()), this, Boolean.valueOf(true));
            } else {
                this.oldImageBitmap = Utils.getResizedPostBitmap((Bitmap) StorageContainer.getPostImageMemoryCache().get(post.getId()), this, Boolean.valueOf(false));
            }
            this.postAttachment.setImageBitmap(this.oldImageBitmap);
        }
        if (StorageContainer.getUser().getPicture() != null) {
            byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
            userPostImage.setPadding(10, 10, 10, 10);
            userPostImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this, 50));
        } else {
            userPostImage.setPadding(0, 0, 0, 0);
            userPostImage.setImageResource(C0530R.drawable.moocall_user);
        }
        userPostName.setText(StorageContainer.getUser().getNickname());
        userPostLocation.setText(StorageContainer.getUser().getLocation());
        addPictureFromCameraIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MoocallImages");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, "MoocallImage_" + timeStamp + ".jpg");
                SocialNetworkActivity.this.cameraFileName = image.getAbsolutePath();
                cameraIntent.putExtra("output", Uri.fromFile(image));
                SocialNetworkActivity.this.startActivityForResult(cameraIntent, SocialNetworkActivity.this.TAKE_PHOTO_CODE);
            }
        });
        addPictureFromGalleryIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), SocialNetworkActivity.this.LOAD_PHOTO_CODE);
            }
        });
        removeAttachment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.oldImageBitmap != null) {
                    SocialNetworkActivity.this.oldImageBitmap = null;
                }
                if (SocialNetworkActivity.this.postImageBitmap != null) {
                    SocialNetworkActivity.this.postImageBitmap.recycle();
                    SocialNetworkActivity.this.postImageBitmap = null;
                }
                SocialNetworkActivity.this.postAttachment.setImageBitmap(SocialNetworkActivity.this.postImageBitmap);
                SocialNetworkActivity.this.attachmentLayout.setVisibility(8);
            }
        });
        addPostIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.editPost(post);
            }
        });
        selectCategoryIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.categorySelectLayout.getVisibility() != 0) {
                    SocialNetworkActivity.this.showSelectCategoryLayout(Boolean.valueOf(true));
                    Utils.hideKeyboard(SocialNetworkActivity.this);
                    return;
                }
                SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
            }
        });
        this.addPostLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.hideKeyboard(SocialNetworkActivity.this);
            }
        });
    }

    private void editPost(Post post) {
        String encodedImage = null;
        String imageWidth = null;
        String imageHeight = null;
        Boolean deletedImage = Boolean.valueOf(false);
        if (this.postImageBitmap != null) {
            imageWidth = String.valueOf(this.postImageBitmap.getWidth());
            imageHeight = String.valueOf(this.postImageBitmap.getHeight());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.postImageBitmap.compress(CompressFormat.JPEG, 100, baos);
            encodedImage = Base64.encodeToString(baos.toByteArray(), 0);
        } else if (this.oldImageBitmap == null && post.getAttachment() != null) {
            deletedImage = Boolean.valueOf(true);
        }
        this.postMessage.setError(null);
        editThisPost(this.postMessage.getText().toString(), encodedImage, imageWidth, imageHeight, post, String.valueOf(deletedImage));
    }

    private void editThisPost(String postMessageText, String encodedImage, String imageWidth, String imageHeight, Post post, String deletedImage) {
        try {
            if (this.newPostCategoryId == null || this.newPostCategoryId.equals(Integer.valueOf(0))) {
                showSelectCategoryLayout(Boolean.valueOf(true));
                return;
            }
            findViewById(C0530R.id.progress_disable).setVisibility(0);
            if (post.getId() != null) {
                postMessageText = StringEscapeUtils.escapeJava(postMessageText).replaceAll("\\\\n", System.getProperty("line.separator")).replaceAll("\\\\\"", "\"");
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.EDIT_POST));
                new AcquireResponseTask(this).execute(new String[]{new EditPostUrl(post.getId().toString(), this.newPostCategoryId.toString()).createAndReturnUrl(this), QuickstartPreferences.EDIT_POST, "encoded_image", encodedImage, "post-message", postMessageText, "deleted-image", deletedImage, "image-width", imageWidth, "image-height", imageHeight});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete(final Post post) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(C0530R.string.delete_post_text)).setTitle(getString(C0530R.string.delete_post));
        alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SocialNetworkActivity.this.deleteThisPost(post);
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

    private void deleteThisPost(Post post) {
        findViewById(C0530R.id.progress_disable).setVisibility(0);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_POST));
        new AcquireResponseTask(this).execute(new String[]{new DeletePostUrl(post.getId().toString()).createAndReturnUrl(this), QuickstartPreferences.DELETE_POST});
    }

    private void reportThisPost(Post post, String reason) {
        try {
            reason = URLEncoder.encode(reason, "UTF-8");
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.REPORT_POST));
            new AcquireResponseTask(this).execute(new String[]{new ReportPostUrl(post.getId().toString(), reason).createAndReturnUrl(this), QuickstartPreferences.REPORT_POST});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onUserImageClick(View view) {
        View parentRow = (View) view.getParent().getParent();
        User postUser = ((Post) this.postList.get(((ListView) parentRow.getParent()).getPositionForView(parentRow) - 1)).getUser();
        if (postUser == null && StorageContainer.getUser() != null) {
            postUser = StorageContainer.getUser();
        }
        if (postUser != null && postUser.getPicture() != null) {
            byte[] decodedString = Base64.decode(postUser.getPicture(), 0);
            showBigImage(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        }
    }

    public void onPostImageClick(View view) {
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        if (listView != null) {
            Post post = (Post) this.postList.get(listView.getPositionForView(parentRow) - 1);
            if (post.getAttachment() != null && StorageContainer.getPostImageMemoryCache().get(post.getId()) != null) {
                showBigImage((Bitmap) StorageContainer.getPostImageMemoryCache().get(post.getId()));
            }
        }
    }

    private void showBigImage(Bitmap image) {
        this.toolbar.setVisibility(8);
        this.transparentBackground.setVisibility(0);
        this.imageLarge.setVisibility(0);
        this.imageLarge.resetZoom();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        float widthZoom = ((float) dm.widthPixels) / ((float) image.getWidth());
        float heightZoom = ((float) dm.heightPixels) / ((float) image.getHeight());
        this.imageLarge.setImageBitmap(image);
        if (widthZoom < heightZoom) {
            if (widthZoom > ScaleAnimation.MAX_SCALE_FACTOR) {
                this.imageLarge.setZoom(widthZoom);
            }
        } else if (heightZoom > ScaleAnimation.MAX_SCALE_FACTOR) {
            this.imageLarge.setZoom(heightZoom);
        }
    }

    private void openNewPostLayout() {
        if (StorageContainer.getUser() != null) {
            this.addPostLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_in));
            this.addPostLayout.setVisibility(0);
            ImageView userPostImage = (ImageView) findViewById(C0530R.id.userPostImage);
            TextView userPostName = (TextView) findViewById(C0530R.id.userPostName);
            TextView userPostLocation = (TextView) findViewById(C0530R.id.userPostLocation);
            TextView addPostIcon = (TextView) findViewById(C0530R.id.addPostIcon);
            ImageView addPictureFromCameraIcon = (ImageView) findViewById(C0530R.id.addPictureFromCameraIcon);
            ImageView addPictureFromGalleryIcon = (ImageView) findViewById(C0530R.id.addPictureFromGalleryIcon);
            LinearLayout selectCategoryIcon = (LinearLayout) findViewById(C0530R.id.selectCategoryIcon);
            this.postMessage = (EditText) findViewById(C0530R.id.postMessage);
            this.postAttachment = (ImageView) findViewById(C0530R.id.postAttachment);
            this.attachmentLayout = (RelativeLayout) findViewById(C0530R.id.attachmentLayout);
            this.postAttachment.setImageBitmap(null);
            this.attachmentLayout.setVisibility(8);
            ImageView removeAttachment = (ImageView) findViewById(C0530R.id.removeAttachment);
            this.categorySelectLayout = (LinearLayout) findViewById(C0530R.id.categorySelectLayout);
            this.categorySelectLayout.setVisibility(8);
            if (StorageContainer.getUser().getPicture() != null) {
                byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
                userPostImage.setPadding(10, 10, 10, 10);
                userPostImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this, 50));
            } else {
                userPostImage.setPadding(0, 0, 0, 0);
                userPostImage.setImageResource(C0530R.drawable.moocall_user);
            }
            userPostName.setText(StorageContainer.getUser().getNickname());
            userPostLocation.setText(StorageContainer.getUser().getLocation());
            addPictureFromCameraIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MoocallImages");
                    imagesFolder.mkdirs();
                    File image = new File(imagesFolder, "MoocallImage_" + timeStamp + ".jpg");
                    SocialNetworkActivity.this.cameraFileName = image.getAbsolutePath();
                    cameraIntent.putExtra("output", Uri.fromFile(image));
                    SocialNetworkActivity.this.startActivityForResult(cameraIntent, SocialNetworkActivity.this.TAKE_PHOTO_CODE);
                }
            });
            addPictureFromGalleryIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SocialNetworkActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), SocialNetworkActivity.this.LOAD_PHOTO_CODE);
                }
            });
            removeAttachment.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (SocialNetworkActivity.this.postImageBitmap != null) {
                        SocialNetworkActivity.this.postImageBitmap.recycle();
                    }
                    SocialNetworkActivity.this.postImageBitmap = null;
                    SocialNetworkActivity.this.postAttachment.setImageBitmap(SocialNetworkActivity.this.postImageBitmap);
                    SocialNetworkActivity.this.attachmentLayout.setVisibility(8);
                }
            });
            addPostIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SocialNetworkActivity.this.savePost();
                }
            });
            selectCategoryIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (SocialNetworkActivity.this.categorySelectLayout.getVisibility() != 0) {
                        SocialNetworkActivity.this.showSelectCategoryLayout(Boolean.valueOf(true));
                        Utils.hideKeyboard(SocialNetworkActivity.this);
                        return;
                    }
                    SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
                }
            });
            this.addPostLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Utils.hideKeyboard(SocialNetworkActivity.this);
                }
            });
        }
    }

    private void showSelectCategoryLayout(Boolean show) {
        if (show.booleanValue() && this.categorySelectLayout.getVisibility() != 0) {
            this.categorySelectLayout.setVisibility(0);
        }
        final ImageView farmingNewsCategory = (ImageView) findViewById(C0530R.id.farmingNewsCategory);
        final ImageView dairyFarmingCategory = (ImageView) findViewById(C0530R.id.dairyFarmingCategory);
        final ImageView cattleBreedingCategory = (ImageView) findViewById(C0530R.id.cattleBreedingCategory);
        final ImageView buySellCategory = (ImageView) findViewById(C0530R.id.buySellCategory);
        final ImageView businessOfFarmingCategory = (ImageView) findViewById(C0530R.id.businessOfFarmingCategory);
        final TextView selectCategoryText = (TextView) findViewById(C0530R.id.selectCategoryText);
        if (this.newPostCategoryId != null) {
            switch (this.newPostCategoryId.intValue()) {
                case 1:
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    break;
                case 2:
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    break;
                case 3:
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    break;
                case 4:
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    break;
                case 5:
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    break;
            }
        }
        farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        selectCategoryText.setText(getString(C0530R.string.select_category));
        farmingNewsCategory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.newPostCategoryId == null || !SocialNetworkActivity.this.newPostCategoryId.equals(Integer.valueOf(1))) {
                    SocialNetworkActivity.this.newPostCategoryId = Integer.valueOf(1);
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.farming_news));
                    SocialNetworkActivity.this.categorySelectLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
                    return;
                }
                SocialNetworkActivity.this.newPostCategoryId = null;
                farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.select_category));
            }
        });
        dairyFarmingCategory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.newPostCategoryId == null || !SocialNetworkActivity.this.newPostCategoryId.equals(Integer.valueOf(2))) {
                    SocialNetworkActivity.this.newPostCategoryId = Integer.valueOf(2);
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.dairy_farming));
                    SocialNetworkActivity.this.categorySelectLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
                    return;
                }
                SocialNetworkActivity.this.newPostCategoryId = null;
                dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.select_category));
            }
        });
        cattleBreedingCategory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.newPostCategoryId == null || !SocialNetworkActivity.this.newPostCategoryId.equals(Integer.valueOf(3))) {
                    SocialNetworkActivity.this.newPostCategoryId = Integer.valueOf(3);
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.cattle_breeding));
                    SocialNetworkActivity.this.categorySelectLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
                    return;
                }
                SocialNetworkActivity.this.newPostCategoryId = null;
                cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.select_category));
            }
        });
        buySellCategory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.newPostCategoryId == null || !SocialNetworkActivity.this.newPostCategoryId.equals(Integer.valueOf(4))) {
                    SocialNetworkActivity.this.newPostCategoryId = Integer.valueOf(4);
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.buy_sell));
                    SocialNetworkActivity.this.categorySelectLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
                    return;
                }
                SocialNetworkActivity.this.newPostCategoryId = null;
                buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.select_category));
            }
        });
        businessOfFarmingCategory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SocialNetworkActivity.this.newPostCategoryId == null || !SocialNetworkActivity.this.newPostCategoryId.equals(Integer.valueOf(5))) {
                    SocialNetworkActivity.this.newPostCategoryId = Integer.valueOf(5);
                    farmingNewsCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    dairyFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    cattleBreedingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    buySellCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                    businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_checked);
                    selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.farming_business));
                    SocialNetworkActivity.this.categorySelectLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.categorySelectLayout.setVisibility(8);
                    return;
                }
                SocialNetworkActivity.this.newPostCategoryId = null;
                businessOfFarmingCategory.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
                selectCategoryText.setText(SocialNetworkActivity.this.getString(C0530R.string.select_category));
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.TAKE_PHOTO_CODE && resultCode == -1) {
            this.attachmentLayout.setVisibility(0);
            this.postImageBitmap = Utils.scaleBitmap(this.cameraFileName);
            this.postAttachment.setImageBitmap(this.postImageBitmap);
        }
        if (requestCode == this.LOAD_PHOTO_CODE && resultCode == -1 && data != null) {
            String[] filePathColumn = new String[]{"_data"};
            Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            if (this.attachmentLayout != null) {
                this.attachmentLayout.setVisibility(0);
            }
            this.postImageBitmap = Utils.scaleBitmap(picturePath);
            this.postAttachment.setImageBitmap(this.postImageBitmap);
        }
    }

    private void savePost() {
        String encodedImage = null;
        String imageWidth = null;
        String imageHeight = null;
        if (this.postImageBitmap != null) {
            imageWidth = String.valueOf(this.postImageBitmap.getWidth());
            imageHeight = String.valueOf(this.postImageBitmap.getHeight());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.postImageBitmap.compress(CompressFormat.JPEG, 100, baos);
            encodedImage = Base64.encodeToString(baos.toByteArray(), 0);
        }
        this.postMessage.setError(null);
        saveThisPost(this.postMessage.getText().toString(), encodedImage, imageWidth, imageHeight);
    }

    private void saveThisPost(String postMessageText, String encodedImage, String imageWidth, String imageHeight) {
        try {
            if (this.newPostCategoryId == null) {
                showSelectCategoryLayout(Boolean.valueOf(true));
                return;
            }
            postMessageText = StringEscapeUtils.escapeJava(postMessageText);
            System.out.println(postMessageText);
            postMessageText = postMessageText.replaceAll("\\\\n", System.getProperty("line.separator")).replaceAll("\\\\\"", "\"");
            findViewById(C0530R.id.progress_disable).setVisibility(0);
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_NEW_POST));
            new AcquireResponseTask(this).execute(new String[]{new AddNewPostUrl(this.newPostCategoryId.toString()).createAndReturnUrl(this), QuickstartPreferences.ADD_NEW_POST, "encoded_image", encodedImage, "post-message", postMessageText, "image-width", imageWidth, "image-height", imageHeight});
            this.newPostCategoryId = null;
            this.postMessage.setText("");
            showSelectCategoryLayout(Boolean.valueOf(false));
            this.postImageBitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchPosts() {
        try {
            String offset = "0";
            if (this.loadMore.booleanValue()) {
                offset = String.valueOf(this.postList.size());
                showProgress(true);
            } else {
                if (this.lastFetchTime != null) {
                    offset = null;
                    this.lastFetchTime = URLEncoder.encode(this.lastFetchTime, "UTF-8");
                }
                this.postsSwipeRefreshLayout.setRefreshing(true);
            }
            if (offset != null) {
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.FETCH_POST_LIST));
                new AcquireResponseTask(this).execute(new String[]{new FetchPostListUrl(offset).createAndReturnUrl(this), QuickstartPreferences.FETCH_POST_LIST, "filters", this.postFilter.getFilters().toString()});
                return;
            }
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_POST_UPDATE));
            new AcquireResponseTask(this).execute(new String[]{new GetPostUpdateUrl(this.lastFetchTime).createAndReturnUrl(this), QuickstartPreferences.GET_POST_UPDATE, "filters", this.postFilter.getFilters().toString()});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void createAsyncBroadcast() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                try {
                    SocialNetworkActivity.this.unregisterReceiver(this);
                    String action = intent.getAction();
                    if (action.equals(QuickstartPreferences.FETCH_POST_LIST)) {
                        SocialNetworkActivity.this.onFetchPostListCompleted(new JSONObject(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.LIKE_POST)) {
                        SocialNetworkActivity.this.onLikePostCompleted(Boolean.valueOf(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.ADD_NEW_POST)) {
                        SocialNetworkActivity.this.onAddNewPostCompleted(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT)) {
                        SocialNetworkActivity.this.onCheckUserHaveAccountCompleted(new JSONObject(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.EDIT_POST)) {
                        SocialNetworkActivity.this.onEditPostCompleted(intent.getStringExtra("response"));
                    } else if (action.equals(QuickstartPreferences.DELETE_POST)) {
                        SocialNetworkActivity.this.onDeletePostCompleted(Boolean.valueOf(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.GET_POST_UPDATE)) {
                        SocialNetworkActivity.this.onGetPostUpdateCompleted(new JSONObject(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.REPORT_POST)) {
                        SocialNetworkActivity.this.onReportPostCompleted(Boolean.valueOf(intent.getStringExtra("response")));
                    } else if (action.equals(QuickstartPreferences.GET_ALL_COUNTRIES)) {
                        SocialNetworkActivity.this.onGetUsersCountriesCompleted(new JSONArray(intent.getStringExtra("response")));
                    }
                } catch (Exception e) {
                    SocialNetworkActivity.this.postsSwipeRefreshLayout.setRefreshing(false);
                }
            }
        };
    }

    public void onReportPostCompleted(Boolean result) {
        try {
            if (result.booleanValue()) {
                Toast.makeText(this, getString(C0530R.string.post_reported), 1).show();
            } else {
                Toast.makeText(this, "failed", 1).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeletePostCompleted(Boolean result) {
        try {
            this.loadMore = Boolean.valueOf(false);
            this.noMoreToLoad = Boolean.valueOf(false);
            fetchPosts();
            findViewById(C0530R.id.progress_disable).setVisibility(8);
            if (result.booleanValue()) {
                Toast.makeText(this, "success", 1).show();
            } else {
                Toast.makeText(this, "failed", 1).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditPostCompleted(String result) {
        Utils.hideKeyboard(this);
        this.loadMore = Boolean.valueOf(false);
        this.noMoreToLoad = Boolean.valueOf(false);
        fetchPosts();
        findViewById(C0530R.id.progress_disable).setVisibility(8);
        if (this.addPostLayout.getVisibility() == 0) {
            this.addPostLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_out));
            this.addPostLayout.setVisibility(8);
        }
        Toast.makeText(this, result, 1).show();
    }

    private void onCheckUserHaveAccountCompleted(JSONObject result) {
        try {
            findViewById(C0530R.id.progress_disable).setVisibility(8);
            JSONObject userObject = new JSONParserBgw(result).getJsonObject(Participant.USER_TYPE);
            if (userObject != null) {
                JSONParserBgw jsonParserUser = new JSONParserBgw(userObject);
                StorageContainer.setUser(new User(jsonParserUser.getInt("id"), jsonParserUser.getString("nickname"), jsonParserUser.getString("picture"), jsonParserUser.getString("country"), jsonParserUser.getString("city"), jsonParserUser.getBoolean("moderator")));
                if (StorageContainer.getUser().getPictureUrl() == null || StorageContainer.getUser().getPicture() != null) {
                    this.loadMore = Boolean.valueOf(false);
                    this.noMoreToLoad = Boolean.valueOf(false);
                    fetchPosts();
                    return;
                }
                loadImage();
                return;
            }
            startActivity(new Intent(this, ChangeUserDetailsActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddNewPostCompleted(String result) {
        Utils.hideKeyboard(this);
        findViewById(C0530R.id.progress_disable).setVisibility(8);
        Toast.makeText(this, result, 1).show();
        if (this.addPostLayout.getVisibility() == 0) {
            this.addPostLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_out));
            this.addPostLayout.setVisibility(8);
        }
        this.loadMore = Boolean.valueOf(false);
        this.noMoreToLoad = Boolean.valueOf(false);
        fetchPosts();
    }

    private void onFetchPostListCompleted(JSONObject result) {
        if (!this.loadMore.booleanValue()) {
            this.postList.clear();
        }
        this.lastFetchTime = Utils.calculateCurrentCetTime();
        if (result.length() > 0) {
            populatePostList(result);
            return;
        }
        this.postsSwipeRefreshLayout.setRefreshing(false);
        showProgress(false);
        this.noMoreToLoad = Boolean.valueOf(true);
        this.postListAdapter.notifyDataSetChanged();
    }

    private void onGetPostUpdateCompleted(JSONObject result) {
        this.lastFetchTime = Utils.calculateCurrentCetTime();
        populateUpdatePostList(result);
    }

    private void populateUpdatePostList(JSONObject result) {
        try {
            int i;
            int j;
            JSONParserBgw jSONParserBgw = new JSONParserBgw(result);
            JSONArray userArray = jSONParserBgw.getJsonArray("users");
            for (i = 0; i < userArray.length(); i++) {
                jSONParserBgw = new JSONParserBgw((JSONObject) userArray.get(i));
                Integer userId = jSONParserBgw.getInt("id");
                String userNickname = jSONParserBgw.getString("nickname");
                String userPictureUrl = jSONParserBgw.getString("picture");
                User tmpUser = new User(userId, userNickname, userPictureUrl, jSONParserBgw.getString("country"), jSONParserBgw.getString("city"), jSONParserBgw.getBoolean("moderator"));
                Boolean newUser = Boolean.valueOf(true);
                if (StorageContainer.getUser().getId().equals(tmpUser.getId())) {
                    newUser = Boolean.valueOf(false);
                } else {
                    for (User userInList : this.usersList) {
                        if (tmpUser.getId().equals(userInList.getId())) {
                            if (tmpUser.getId().equals(StorageContainer.getUser().getId())) {
                                tmpUser.setPicture(StorageContainer.getUser().getPicture());
                            } else if (!(tmpUser.getPictureUrl() == null || userPictureUrl.equals(userInList.getPictureUrl()))) {
                                loadImage(tmpUser.getPictureUrl(), tmpUser);
                            }
                            this.postListAdapter.getUserImages().remove(tmpUser.getId());
                            newUser = Boolean.valueOf(false);
                            this.usersList.set(this.usersList.indexOf(userInList), tmpUser);
                        }
                    }
                }
                if (newUser.booleanValue()) {
                    this.usersList.add(tmpUser);
                }
            }
            System.out.println("D: " + this.usersList.size());
            JSONArray postsArray = jSONParserBgw.getJsonArray("posts").getJSONArray(0);
            for (i = 0; i < postsArray.length(); i++) {
                jSONParserBgw = new JSONParserBgw((JSONObject) postsArray.get(i));
                List<Comment> comments = new ArrayList();
                List<Like> likes = new ArrayList();
                Integer postId = jSONParserBgw.getInt("id");
                String postTime = Utils.calculateTime(jSONParserBgw.getString("time"), "yyyy-MM-dd HH:mm");
                String postMessage = jSONParserBgw.getString("message");
                Integer postCategory = jSONParserBgw.getInt("category");
                String postAttachmentUrl = jSONParserBgw.getString("attachment");
                Integer postUserId = jSONParserBgw.getInt("id_user");
                Boolean postNew = jSONParserBgw.getBoolean("new");
                Boolean postDelete = jSONParserBgw.getBoolean("delete");
                Integer postAttachmentWidth = jSONParserBgw.getInt("attachment_width");
                Integer postAttachmentHeight = jSONParserBgw.getInt("attachment_height");
                if (postDelete.booleanValue()) {
                    for (Post postInList : this.postList) {
                        if (postId.equals(postInList.getId())) {
                            this.postList.remove(postInList);
                            break;
                        }
                    }
                }
                User postUser = null;
                if (!StorageContainer.getUser().getId().equals(postUserId)) {
                    for (User userPost : this.usersList) {
                        if (postUserId.equals(userPost.getId())) {
                            postUser = userPost;
                            break;
                        }
                    }
                }
                Post post = new Post(postId, postTime, postUser, postMessage, postCategory, postAttachmentUrl, postAttachmentWidth, postAttachmentHeight);
                post.setLikes(likes);
                post.setComments(comments);
                for (Post postInList2 : this.postList) {
                    if (post.getId().equals(postInList2.getId())) {
                        if (post.getAttachment() != null && !postAttachmentUrl.equals(postInList2.getAttachment()) && StorageContainer.getPostImageMemoryCache().get(post.getId()) != null) {
                            loadImage(post.getAttachment(), post);
                        } else if (post.getAttachment() == null && StorageContainer.getPostImageMemoryCache().get(post.getId()) != null) {
                            StorageContainer.getPostImageMemoryCache().remove(post.getId());
                        }
                        this.postList.set(this.postList.indexOf(postInList2), post);
                        postNew = Boolean.valueOf(false);
                        if (postNew.booleanValue()) {
                            this.postList.add(1, post);
                        }
                    }
                }
                if (postNew.booleanValue()) {
                    this.postList.add(1, post);
                }
            }
            JSONArray likesArray = jSONParserBgw.getJsonArray("likes").getJSONArray(0);
            for (j = 0; j < likesArray.length(); j++) {
                jSONParserBgw = new JSONParserBgw((JSONObject) likesArray.get(j));
                Integer likeUserId = jSONParserBgw.getInt("id_user");
                Integer likePostId = jSONParserBgw.getInt("id_post");
                Boolean likeNew = Boolean.valueOf(true);
                Like like = new Like(likeUserId);
                for (Post postLike : this.postList) {
                    if (likePostId.equals(postLike.getId())) {
                        for (Like likeInPostList : postLike.getLikes()) {
                            if (likeInPostList.getUserId().equals(like.getUserId())) {
                                likeNew = Boolean.valueOf(false);
                            }
                        }
                        if (likeNew.booleanValue()) {
                            postLike.getLikes().add(like);
                            if (likeUserId.equals(StorageContainer.getUser().getId())) {
                                postLike.setLiked(Boolean.valueOf(true));
                            }
                        }
                    }
                }
            }
            JSONArray commentsArray = jSONParserBgw.getJsonArray("comments").getJSONArray(0);
            for (j = 0; j < commentsArray.length(); j++) {
                jSONParserBgw = new JSONParserBgw((JSONObject) commentsArray.get(j));
                Integer commentId = jSONParserBgw.getInt("id");
                String commentTime = Utils.calculateTime(jSONParserBgw.getString("time"), "yyyy-MM-dd HH:mm");
                String message = jSONParserBgw.getString("message");
                Integer commentUserId = jSONParserBgw.getInt("id_user");
                Integer commentPostId = jSONParserBgw.getInt("id_post");
                Boolean commentNew = jSONParserBgw.getBoolean("new");
                Boolean commentDelete = jSONParserBgw.getBoolean("delete");
                for (Post postComment : this.postList) {
                    if (commentPostId.equals(postComment.getId())) {
                        if (commentDelete.booleanValue()) {
                            postComment.getComments().remove(commentId);
                        } else {
                            User commentUser = null;
                            if (!StorageContainer.getUser().getId().equals(commentUserId)) {
                                for (User userComment : this.usersList) {
                                    if (commentUserId.equals(userComment.getId())) {
                                        commentUser = userComment;
                                    }
                                }
                            }
                            Comment comment = new Comment(commentId, commentTime, commentUser, message);
                            for (Comment commentInPostList : postComment.getComments()) {
                                if (commentInPostList.getId().equals(comment.getId())) {
                                    postComment.getComments().set(postComment.getComments().indexOf(commentInPostList), comment);
                                    commentNew = Boolean.valueOf(false);
                                }
                            }
                            if (commentNew.booleanValue()) {
                                postComment.getComments().add(comment);
                            }
                        }
                    }
                }
            }
            this.postsSwipeRefreshLayout.setRefreshing(false);
            this.postListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatePostList(JSONObject result) {
        try {
            int i;
            JSONParserBgw jSONParserBgw = new JSONParserBgw(result);
            if (this.postList.size() == 0) {
                this.postList.add(new Post());
                this.usersList = new ArrayList();
            }
            JSONArray userArray = jSONParserBgw.getJsonArray("users");
            for (i = 0; i < userArray.length(); i++) {
                jSONParserBgw = new JSONParserBgw((JSONObject) userArray.get(i));
                User tmpUser = new User(jSONParserBgw.getInt("id"), jSONParserBgw.getString("nickname"), jSONParserBgw.getString("picture"), jSONParserBgw.getString("country"), jSONParserBgw.getString("city"), jSONParserBgw.getBoolean("moderator"));
                if (tmpUser.getPictureUrl() != null && tmpUser.getPicture() == null && tmpUser.getId().equals(StorageContainer.getUser().getId())) {
                    tmpUser.setPicture(StorageContainer.getUser().getPicture());
                }
                Boolean newUser = Boolean.valueOf(true);
                if (StorageContainer.getUser().getId().equals(tmpUser.getId())) {
                    newUser = Boolean.valueOf(false);
                } else {
                    for (User userPost : this.usersList) {
                        if (userPost.getId().equals(tmpUser.getId())) {
                            newUser = Boolean.valueOf(false);
                        }
                    }
                }
                if (newUser.booleanValue()) {
                    this.usersList.add(tmpUser);
                }
            }
            JSONArray postsArray = jSONParserBgw.getJsonArray("posts");
            for (i = 0; i < postsArray.length(); i++) {
                int j;
                jSONParserBgw = new JSONParserBgw((JSONObject) postsArray.get(i));
                List<Comment> comments = new ArrayList();
                List<Like> likes = new ArrayList();
                Integer postId = jSONParserBgw.getInt("id");
                String postTime = Utils.calculateTime(jSONParserBgw.getString("time"), "yyyy-MM-dd HH:mm");
                String postMessage = jSONParserBgw.getString("message");
                Integer postCategory = jSONParserBgw.getInt("category");
                String postAttachmentUrl = jSONParserBgw.getString("attachment");
                Integer postUserId = jSONParserBgw.getInt("id_user");
                Integer postAttachmentWidth = jSONParserBgw.getInt("attachment_width");
                Integer postAttachmentHeight = jSONParserBgw.getInt("attachment_height");
                User postUser = null;
                if (!StorageContainer.getUser().getId().equals(postUserId)) {
                    for (User userPost2 : this.usersList) {
                        if (postUserId.equals(userPost2.getId())) {
                            postUser = userPost2;
                        }
                    }
                }
                Post post = new Post(postId, postTime, postUser, postMessage, postCategory, postAttachmentUrl, postAttachmentWidth, postAttachmentHeight);
                JSONArray likesArray = jSONParserBgw.getJsonArray("likes");
                for (j = 0; j < likesArray.length(); j++) {
                    Integer likeUserId = new JSONParserBgw((JSONObject) likesArray.get(j)).getInt("id_user");
                    likes.add(new Like(likeUserId));
                    if (likeUserId.equals(StorageContainer.getUser().getId())) {
                        post.setLiked(Boolean.valueOf(true));
                    }
                }
                post.setLikes(likes);
                JSONArray commentsArray = jSONParserBgw.getJsonArray("comments");
                for (j = 0; j < commentsArray.length(); j++) {
                    jSONParserBgw = new JSONParserBgw((JSONObject) commentsArray.get(j));
                    Integer commentId = jSONParserBgw.getInt("id");
                    String commentTime = Utils.calculateTime(jSONParserBgw.getString("time"), "yyyy-MM-dd HH:mm");
                    String message = jSONParserBgw.getString("message");
                    Integer commentUserId = jSONParserBgw.getInt("id_user");
                    User commentUser = null;
                    if (!StorageContainer.getUser().getId().equals(commentUserId)) {
                        for (User userComment : this.usersList) {
                            if (commentUserId.equals(userComment.getId())) {
                                commentUser = userComment;
                            }
                        }
                    }
                    comments.add(new Comment(commentId, commentTime, commentUser, message));
                }
                post.setLikes(likes);
                post.setComments(comments);
                this.postList.add(post);
            }
            this.postListAdapter.notifyDataSetChanged();
            this.postsSwipeRefreshLayout.setRefreshing(false);
            if (postsArray.length() < 9) {
                showProgress(false);
                this.noMoreToLoad = Boolean.valueOf(true);
            } else {
                showProgress(true);
            }
            getUsersCountries();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCommentsClicked(View view) {
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        if (listView != null) {
            openPostDetailsActivity((Post) this.postList.get(listView.getPositionForView(parentRow) - 1));
        }
    }

    public void onLikeClicked(View view) {
        this.postsSwipeRefreshLayout.setRefreshing(true);
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        if (listView != null) {
            this.likedPost = (Post) this.postList.get(listView.getPositionForView(parentRow) - 1);
            User likedPostUser = this.likedPost.getUser();
            if (likedPostUser == null && StorageContainer.getUser() != null) {
                likedPostUser = StorageContainer.getUser();
            }
            if (likedPostUser != null) {
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.LIKE_POST));
                new AcquireResponseTask(this).execute(new String[]{new LikePostUrl(this.likedPost.getId().toString(), likedPostUser.getId().toString()).createAndReturnUrl(this), QuickstartPreferences.LIKE_POST});
            }
        }
    }

    public void onOptionsClicked(View view) {
        this.postListView.showContextMenuForChild((View) view.getParent().getParent());
    }

    private void onLikePostCompleted(Boolean result) {
        this.postsSwipeRefreshLayout.setRefreshing(false);
        if (result.booleanValue() && this.likedPost != null) {
            List<Like> likes = new ArrayList(this.likedPost.getLikes());
            if (this.likedPost.getLiked() == null || !this.likedPost.getLiked().booleanValue()) {
                this.likedPost.setLiked(Boolean.valueOf(true));
                this.likedPost.getLikes().add(new Like(StorageContainer.getUser().getId()));
            } else {
                this.likedPost.setLiked(Boolean.valueOf(false));
                for (Like like : this.likedPost.getLikes()) {
                    if (like.getUserId().equals(StorageContainer.getUser().getId())) {
                        likes.remove(like);
                    }
                }
                this.likedPost.setLikes(likes);
            }
            this.postListAdapter.notifyDataSetChanged();
        }
    }

    public void loadImage(String url, final User tmpUser) {
        try {
            String src = StorageContainer.socialHost + "/moocall_social_images/" + url;
            System.out.println("src: " + src);
            tmpUser.setImageLoading(Boolean.valueOf(true));
            this.imageLoader.loadImage(src, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    System.out.println("BRAVOOOOOOOOOO");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    loadedImage.compress(CompressFormat.JPEG, 100, baos);
                    tmpUser.setPicture(Base64.encodeToString(baos.toByteArray(), 0));
                    tmpUser.setImageLoading(Boolean.valueOf(false));
                    SocialNetworkActivity.this.postListAdapter.notifyDataSetChanged();
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(String url, final Post post) {
        try {
            String src = StorageContainer.socialHost + "/moocall_social_images/" + url;
            System.out.println("src: " + src);
            post.setImageLoading(Boolean.valueOf(true));
            this.imageLoader.loadImage(src, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    System.out.println("BRAVOOOOOOOOOO");
                    StorageContainer.getPostImageMemoryCache().put(post.getId(), loadedImage);
                    post.setImageLoading(Boolean.valueOf(false));
                    SocialNetworkActivity.this.postListAdapter.notifyDataSetChanged();
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPostDetailsActivity(Post post) {
        registerReceiver();
        Intent intent = new Intent(this, PostDetailsActivity.class);
        intent.putExtra("post-id", post.getId().toString());
        startActivity(intent);
    }

    public void createBroadcastReceiver() {
        this.refreshPostListBrodcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("refresh_posts")) {
                    SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                    SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                    SocialNetworkActivity.this.fetchPosts();
                } else if (action.equals("refresh_user")) {
                    StorageContainer.setUser(null);
                    SocialNetworkActivity.this.checkUserHaveAccount();
                }
            }
        };
    }

    private void checkUserHaveAccount() {
        if (StorageContainer.getUser() == null) {
            findViewById(C0530R.id.progress_disable).setVisibility(0);
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT));
            new AcquireResponseTask(this).execute(new String[]{new CheckUserHaveAccountUrl(String.valueOf(44)).createAndReturnUrl(this), QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT});
        } else if (StorageContainer.getUser().getPictureUrl() == null) {
            this.loadMore = Boolean.valueOf(false);
            this.noMoreToLoad = Boolean.valueOf(false);
            fetchPosts();
            if (this.urlData != null) {
                openNewPostLayout();
            }
        } else if (StorageContainer.getUser().getPicture() == null) {
            loadImage();
        } else {
            byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
            this.userImage.setPadding(10, 10, 10, 10);
            this.userImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this, 50));
            this.loadMore = Boolean.valueOf(false);
            this.noMoreToLoad = Boolean.valueOf(false);
            fetchPosts();
            if (this.urlData != null) {
                openNewPostLayout();
            }
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
                    byte[] imageBytes = baos.toByteArray();
                    if (StorageContainer.getUser() != null) {
                        StorageContainer.getUser().setPicture(Base64.encodeToString(imageBytes, 0));
                        byte[] decodedString = Base64.decode(StorageContainer.getUser().getPicture(), 0);
                        SocialNetworkActivity.this.userImage.setPadding(10, 10, 10, 10);
                        SocialNetworkActivity.this.userImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), SocialNetworkActivity.this, 50));
                    }
                    SocialNetworkActivity.this.findViewById(C0530R.id.progress_disable).setVisibility(8);
                    SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                    SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                    SocialNetworkActivity.this.fetchPosts();
                    if (SocialNetworkActivity.this.urlData != null) {
                        SocialNetworkActivity.this.openNewPostLayout();
                    }
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                    SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                    SocialNetworkActivity.this.fetchPosts();
                    if (SocialNetworkActivity.this.urlData != null) {
                        SocialNetworkActivity.this.openNewPostLayout();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerReceiver() {
        registerReceiver(this.refreshPostListBrodcastReceiver, new IntentFilter("refresh_posts"));
        this.broadcastRegistred = Boolean.valueOf(true);
    }

    public void unregisterReceiver() {
        if (this.broadcastRegistred.booleanValue()) {
            unregisterReceiver(this.refreshPostListBrodcastReceiver);
        }
    }

    public void onBackPressed() {
        if (this.categorySelectLayout != null && this.categorySelectLayout.getVisibility() == 0) {
            this.categorySelectLayout.setVisibility(8);
        } else if (this.addPostLayout.getVisibility() == 0) {
            this.addPostLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_out));
            this.addPostLayout.setVisibility(8);
        } else if (this.transparentBackground.getVisibility() == 0) {
            this.transparentBackground.setVisibility(8);
            this.imageLarge.setVisibility(8);
            this.toolbar.setVisibility(0);
        } else if (this.homePage.getVisibility() == 0) {
            this.homePage.setVisibility(8);
        } else if (this.filterLayout == null || this.filterLayout.getVisibility() != 0) {
            unregisterReceiver();
            finish();
        } else if (this.currentPostFilter.getAllUsers().equals(this.postFilter.getAllUsers()) && this.currentPostFilter.getCountry() == this.postFilter.getCountry() && this.currentPostFilter.getAllCategory().equals(this.postFilter.getAllCategory()) && this.currentPostFilter.getFarmingNewsCategory().equals(this.postFilter.getFarmingNewsCategory()) && this.currentPostFilter.getDairyFarmingCategory().equals(this.postFilter.getDairyFarmingCategory()) && this.currentPostFilter.getCattleBreedingCategory().equals(this.postFilter.getCattleBreedingCategory()) && this.currentPostFilter.getBuySellCategory().equals(this.postFilter.getBuySellCategory()) && this.currentPostFilter.getFarmingBusinessCategory().equals(this.postFilter.getFarmingBusinessCategory())) {
            this.filterLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_out));
            this.filterLayout.setVisibility(8);
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(C0530R.string.apply_filter_text)).setTitle(getString(C0530R.string.apply_filter));
            alertDialogBuilder.setPositiveButton(getString(C0530R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SocialNetworkActivity.this.lastFetchTime = null;
                    SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                    SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                    SocialNetworkActivity.this.fetchPosts();
                    SocialNetworkActivity.this.filterLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.filterLayout.setVisibility(8);
                    dialog.cancel();
                }
            });
            alertDialogBuilder.setNegativeButton(getString(C0530R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SocialNetworkActivity.this.postFilter = new PostFilter(SocialNetworkActivity.this.currentPostFilter);
                    SocialNetworkActivity.this.postListAdapter.setPostFilter(SocialNetworkActivity.this.postFilter);
                    SocialNetworkActivity.this.setFilters();
                    SocialNetworkActivity.this.filterLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                    SocialNetworkActivity.this.filterLayout.setVisibility(8);
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.create().show();
        }
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        this.mTracker.setScreenName("Social Network Activity");
        this.mTracker.send(new ScreenViewBuilder().build());
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.postListAdapter.notifyDataSetChanged();
        if (this.transparentBackground.getVisibility() == 0) {
            showBigImage(((BitmapDrawable) this.imageLarge.getDrawable()).getBitmap());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0530R.menu.menu_social_network, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0530R.id.filterPost) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean filterPost(MenuItem view) {
        filter();
        return true;
    }

    private void filter() {
        this.filterLayout = (RelativeLayout) findViewById(C0530R.id.filterLayout);
        this.filterLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_in));
        this.filterLayout.setVisibility(0);
        this.allUsers = (ImageView) findViewById(C0530R.id.allUsers);
        this.peopleILiked = (ImageView) findViewById(C0530R.id.peopleILiked);
        this.categoryAll = (ImageView) findViewById(C0530R.id.categoryAll);
        this.categoryFarmingNews = (ImageView) findViewById(C0530R.id.categoryFarmingNews);
        this.categoryDairyFarming = (ImageView) findViewById(C0530R.id.categoryDairyFarming);
        this.categoryCattleBreeding = (ImageView) findViewById(C0530R.id.categoryCattleBreeding);
        this.categoryBuySell = (ImageView) findViewById(C0530R.id.categoryBuySell);
        this.categoryBusinessOfFarming = (ImageView) findViewById(C0530R.id.categoryBusinessOfFarming);
        this.resetFilter = (TextView) findViewById(C0530R.id.resetFilter);
        this.applyFilter = (TextView) findViewById(C0530R.id.applyFilter);
        this.countrySelect = (Spinner) findViewById(C0530R.id.countrySelect);
        if (this.countrySelect.getAdapter() == null && this.usersCountries != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter(this, 17367048, this.usersCountries);
            adapter.setDropDownViewResource(17367049);
            this.countrySelect.setAdapter(adapter);
        }
        this.currentPostFilter = new PostFilter(this.postFilter);
        setFilters();
        implementFilterListeners();
    }

    private void setFilters() {
        if (this.postFilter.getAllUsers().booleanValue()) {
            this.allUsers.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.allUsers.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getPeopleILiked().booleanValue()) {
            this.peopleILiked.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.peopleILiked.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getAllCategory().booleanValue()) {
            this.categoryAll.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.categoryAll.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getFarmingNewsCategory().booleanValue()) {
            this.categoryFarmingNews.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.categoryFarmingNews.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getDairyFarmingCategory().booleanValue()) {
            this.categoryDairyFarming.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.categoryDairyFarming.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getCattleBreedingCategory().booleanValue()) {
            this.categoryCattleBreeding.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.categoryCattleBreeding.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getBuySellCategory().booleanValue()) {
            this.categoryBuySell.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.categoryBuySell.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getFarmingBusinessCategory().booleanValue()) {
            this.categoryBusinessOfFarming.setImageResource(C0530R.drawable.settings_chechbox_checked);
        } else {
            this.categoryBusinessOfFarming.setImageResource(C0530R.drawable.settings_chechbox_unchecked);
        }
        if (this.postFilter.getCountry() == null) {
            this.countrySelect.setSelection(0);
        } else {
            this.countrySelect.setSelection(this.usersCountries.indexOf(this.postFilter.getCountry()));
        }
    }

    private void implementFilterListeners() {
        this.allUsers.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleAllUsers();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.peopleILiked.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.togglePeopleILiked();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.categoryAll.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleAllCategory();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.categoryFarmingNews.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleFarmingNewsCategory();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.categoryDairyFarming.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleDairyFarmingCategor();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.categoryCattleBreeding.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleCattleBreedingCategory();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.categoryBuySell.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleBuySellCategory();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.categoryBusinessOfFarming.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter.toggleFarmingBusinessCategory();
                SocialNetworkActivity.this.setFilters();
            }
        });
        this.resetFilter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.postFilter = new PostFilter(SocialNetworkActivity.this);
                SocialNetworkActivity.this.postListAdapter.setPostFilter(SocialNetworkActivity.this.postFilter);
                SocialNetworkActivity.this.setFilters();
                SocialNetworkActivity.this.lastFetchTime = null;
                SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                SocialNetworkActivity.this.fetchPosts();
                SocialNetworkActivity.this.filterLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                SocialNetworkActivity.this.filterLayout.setVisibility(8);
            }
        });
        this.applyFilter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SocialNetworkActivity.this.lastFetchTime = null;
                SocialNetworkActivity.this.loadMore = Boolean.valueOf(false);
                SocialNetworkActivity.this.noMoreToLoad = Boolean.valueOf(false);
                SocialNetworkActivity.this.fetchPosts();
                SocialNetworkActivity.this.filterLayout.startAnimation(AnimationUtils.loadAnimation(SocialNetworkActivity.this, C0530R.anim.abc_fade_out));
                SocialNetworkActivity.this.filterLayout.setVisibility(8);
            }
        });
        this.countrySelect.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0) {
                    SocialNetworkActivity.this.postFilter.setCountry((String) SocialNetworkActivity.this.usersCountries.get(position));
                } else {
                    SocialNetworkActivity.this.postFilter.setCountry(null);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getUsersCountries() {
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_ALL_COUNTRIES));
        new AcquireResponseTask(this).execute(new String[]{new GetUsersCountries().createAndReturnUrl(this), QuickstartPreferences.GET_ALL_COUNTRIES});
    }

    private void onGetUsersCountriesCompleted(JSONArray result) {
        try {
            this.usersCountries = new ArrayList(result.length() + 1);
            this.usersCountries.add(getString(C0530R.string.choose_country));
            for (int i = 0; i < result.length(); i++) {
                if (result.getString(i).length() > 0) {
                    this.usersCountries.add(result.getString(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCancelFilterClicked(View view) {
        this.postFilter = new PostFilter((Activity) this);
        this.postListAdapter.setPostFilter(this.postFilter);
        this.lastFetchTime = null;
        this.loadMore = Boolean.valueOf(false);
        this.noMoreToLoad = Boolean.valueOf(false);
        fetchPosts();
    }

    public void onFilterTextClicked(View view) {
        filter();
    }
}
