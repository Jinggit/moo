package com.moocall.moocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.moocall.moocall.adapter.CommentsListAdapter;
import com.moocall.moocall.async.AcquireResponseTask;
import com.moocall.moocall.domain.Comment;
import com.moocall.moocall.domain.Like;
import com.moocall.moocall.domain.Post;
import com.moocall.moocall.domain.User;
import com.moocall.moocall.service.QuickstartPreferences;
import com.moocall.moocall.url.AddPostCommentUrl;
import com.moocall.moocall.url.CheckUserHaveAccountUrl;
import com.moocall.moocall.url.DeletePostUrl;
import com.moocall.moocall.url.GetPostForIdUrl;
import com.moocall.moocall.url.LikePostUrl;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.TouchImageView;
import com.moocall.moocall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import io.intercom.android.sdk.models.Participant;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class PostDetailsActivity extends ActionBarActivity {
    private ImageView addNewCommentIcon;
    private EditText addNewCommentView;
    private RelativeLayout addPostLayout;
    private BroadcastReceiver broadcastReceiver;
    private ImageView commentIcon;
    private List<Comment> commentsList;
    private CommentsListAdapter commentsListAdapter;
    private ListView commentsListView;
    private Comment deletedComment;
    private TouchImageView imageLarge;
    private ImageLoader imageLoader;
    private TextView numberOfComments;
    private TextView numberOfLikes;
    private Post post;
    private TextView postAuthor;
    private TextView postAuthorLocation;
    private String postId;
    private ImageView postImage;
    private Bitmap postImageBitmap;
    private ImageView postLiked;
    private TextView postText;
    private View progressView;
    private Toolbar toolbar;
    private RelativeLayout transparentBackground;
    private ImageView userImage;
    private List<User> usersList;

    class C05252 implements OnClickListener {
        C05252() {
        }

        public void onClick(View v) {
            PostDetailsActivity.this.onBackPressed();
        }
    }

    class C05263 implements OnClickListener {
        C05263() {
        }

        public void onClick(View v) {
            PostDetailsActivity.this.save();
        }
    }

    class C05274 implements OnClickListener {
        C05274() {
        }

        public void onClick(View v) {
            PostDetailsActivity.this.transparentBackground.setVisibility(8);
            PostDetailsActivity.this.imageLarge.setVisibility(8);
        }
    }

    class C05296 extends BroadcastReceiver {
        C05296() {
        }

        public void onReceive(Context arg0, Intent intent) {
            try {
                PostDetailsActivity.this.unregisterReceiver(this);
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.DELETE_POST)) {
                    PostDetailsActivity.this.onDeletePostCompleted(new Boolean(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.LIKE_POST)) {
                    PostDetailsActivity.this.onLikePostCompleted(new Boolean(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.ADD_COMMENT)) {
                    PostDetailsActivity.this.onAddCommentCompleted(new Integer(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.DELETE_COMMENT)) {
                    PostDetailsActivity.this.onDeleteCommentCompleted(new Boolean(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.EDIT_POST)) {
                    PostDetailsActivity.this.onEditPostCompleted(intent.getStringExtra("response"));
                } else if (action.equals(QuickstartPreferences.GET_POST_FOR_ID)) {
                    PostDetailsActivity.this.onGetPostDorIdCompleted(new JSONObject(intent.getStringExtra("response")));
                } else if (action.equals(QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT)) {
                    PostDetailsActivity.this.onCheckUserHaveAccountCompleted(new JSONObject(intent.getStringExtra("response")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C13327 extends SimpleImageLoadingListener {
        C13327() {
        }

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            System.out.println("BRAVOOOOOOOOOO");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            loadedImage.compress(CompressFormat.JPEG, 100, baos);
            StorageContainer.getUser().setPicture(Base64.encodeToString(baos.toByteArray(), 0));
            PostDetailsActivity.this.getPostForId();
        }

        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        }
    }

    class C13349 extends SimpleImageLoadingListener {
        C13349() {
        }

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            System.out.println("BRAVOOOOOOOOOO");
            StorageContainer.getPostImageMemoryCache().put(PostDetailsActivity.this.post.getId(), loadedImage);
            PostDetailsActivity.this.setPostData();
        }

        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0530R.layout.activity_post_details);
        onResume();
        createAsyncBroadcast();
        Intent intent = getIntent();
        this.postId = (String) intent.getSerializableExtra("post-id");
        Uri data = intent.getData();
        if (this.postId == null && data != null) {
            this.postId = data.getQueryParameter("post-id");
        }
        if (StorageContainer.getPostImageMemoryCache() == null) {
            createPostImageMemoryCache();
        }
        ImageLoaderConfiguration config = new Builder(this).build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(config);
        this.progressView = findViewById(C0530R.id.progress_disable);
        if (StorageContainer.getUser() == null) {
            checkUserHaveAccount();
        } else {
            getPostForId();
        }
    }

    private void createPostImageMemoryCache() {
        StorageContainer.setPostImageMemoryCache(new LruCache<Integer, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 4) {
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        });
    }

    private void checkUserHaveAccount() {
        showProgress(true);
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT));
        new AcquireResponseTask(this).execute(new String[]{new CheckUserHaveAccountUrl(String.valueOf(44)).createAndReturnUrl(this), QuickstartPreferences.CHECK_USER_HAVE_ACCOUNT});
    }

    private void getPostForId() {
        if (this.postId != null) {
            showProgress(true);
            registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.GET_POST_FOR_ID));
            new AcquireResponseTask(this).execute(new String[]{new GetPostForIdUrl(this.postId).createAndReturnUrl(this), QuickstartPreferences.GET_POST_FOR_ID});
        }
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C05252());
        this.toolbar.setTitle((CharSequence) "");
    }

    private void setupLayout() {
        this.commentsListView = (ListView) findViewById(C0530R.id.commentsListView);
        LinearLayout headerLayout = (LinearLayout) getLayoutInflater().inflate(C0530R.layout.post_row, null);
        headerLayout.setOnClickListener(null);
        this.commentsListView.addHeaderView(headerLayout);
        this.userImage = (ImageView) findViewById(C0530R.id.userImage);
        this.postAuthor = (TextView) findViewById(C0530R.id.postAuthor);
        this.postAuthorLocation = (TextView) findViewById(C0530R.id.postAuthorLocation);
        this.postText = (TextView) findViewById(C0530R.id.postText);
        this.postImage = (ImageView) findViewById(C0530R.id.postImage);
        this.numberOfLikes = (TextView) findViewById(C0530R.id.numberOfLikes);
        this.numberOfComments = (TextView) findViewById(C0530R.id.numberOfComments);
        this.postLiked = (ImageView) findViewById(C0530R.id.postLiked);
        this.commentIcon = (ImageView) findViewById(C0530R.id.commentIcon);
        this.addNewCommentView = (EditText) findViewById(C0530R.id.addNewCommentView);
        this.addNewCommentIcon = (ImageView) findViewById(C0530R.id.addNewCommentIcon);
        this.addPostLayout = (RelativeLayout) findViewById(C0530R.id.addPostLayout);
        this.transparentBackground = (RelativeLayout) findViewById(C0530R.id.transparentBackground);
        this.imageLarge = (TouchImageView) findViewById(C0530R.id.postImageLarge);
        this.commentsList = new ArrayList();
        this.commentsListAdapter = new CommentsListAdapter(this, this.commentsList);
        this.commentsListView.setAdapter(this.commentsListAdapter);
        ((LinearLayout) findViewById(C0530R.id.optionsLayout)).setVisibility(8);
        setPostData();
    }

    private void setPostData() {
        if (this.post != null) {
            this.commentsList.clear();
            User postUser = this.post.getUser();
            if (postUser == null && StorageContainer.getUser() != null) {
                postUser = StorageContainer.getUser();
            }
            if (postUser.getPicture() != null) {
                byte[] decodedString = Base64.decode(postUser.getPicture(), 0);
                this.userImage.setPadding(10, 10, 10, 10);
                this.userImage.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this, 40));
            } else {
                this.userImage.setPadding(0, 0, 0, 0);
                this.userImage.setImageResource(C0530R.drawable.moocall_user);
            }
            if (this.post.getAttachment() == null) {
                this.postImage.getLayoutParams().height = 0;
                this.postImage.setImageDrawable(null);
            } else if (StorageContainer.getPostImageMemoryCache().get(this.post.getId()) != null) {
                Bitmap postImageBitmap;
                if (getResources().getConfiguration().orientation == 2) {
                    postImageBitmap = Utils.getResizedPostBitmap((Bitmap) StorageContainer.getPostImageMemoryCache().get(this.post.getId()), this, Boolean.valueOf(true));
                } else {
                    postImageBitmap = Utils.getResizedPostBitmap((Bitmap) StorageContainer.getPostImageMemoryCache().get(this.post.getId()), this, Boolean.valueOf(false));
                }
                this.postImage.getLayoutParams().height = postImageBitmap.getHeight() * 2;
                this.postImage.setImageBitmap(postImageBitmap);
            }
            this.postAuthor.setText(postUser.getNickname());
            this.postAuthorLocation.setText(postUser.getLocation());
            this.postText.setText(StringEscapeUtils.unescapeJava(this.post.getText()));
            this.numberOfLikes.setText(String.valueOf(this.post.getLikes().size()));
            this.numberOfComments.setText(String.valueOf(this.post.getComments().size()));
            if (this.post.getLiked() == null || !this.post.getLiked().booleanValue()) {
                this.postLiked.setImageDrawable(getResources().getDrawable(C0530R.drawable.moocall_social_like));
            } else {
                this.postLiked.setImageDrawable(getResources().getDrawable(C0530R.drawable.moocall_social_liked));
            }
            if (this.post.getComments().size() > 0) {
                this.commentIcon.setImageDrawable(getResources().getDrawable(C0530R.drawable.moocall_social_comment_green));
            } else {
                this.commentIcon.setImageDrawable(getResources().getDrawable(C0530R.drawable.moocall_social_comment));
            }
            if (this.post.getComments() != null && this.post.getComments().size() > 0) {
                for (Comment comment : this.post.getComments()) {
                    this.commentsList.add(comment);
                }
            }
            this.commentsListAdapter.notifyDataSetChanged();
        }
    }

    private void implementListeners() {
        this.addNewCommentIcon.setOnClickListener(new C05263());
        registerForContextMenu(this.commentsListView);
        this.transparentBackground.setOnClickListener(new C05274());
    }

    private void save() {
        this.addNewCommentView.setError(null);
        String commentText = this.addNewCommentView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(commentText)) {
            this.addNewCommentView.setError(getString(C0530R.string.error_field_required));
            focusView = this.addNewCommentView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            saveComment(commentText);
        }
    }

    private void saveComment(String commentText) {
        try {
            showProgress(true);
            if (this.post.getId() != null) {
                commentText = StringEscapeUtils.escapeJava(commentText).replaceAll("\\\\n", System.getProperty("line.separator")).replaceAll("\\\\\"", "\"");
                registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.ADD_COMMENT));
                new AcquireResponseTask(this).execute(new String[]{new AddPostCommentUrl(this.post.getId().toString()).createAndReturnUrl(this), QuickstartPreferences.ADD_COMMENT, "comment", commentText});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        if (info.position > 0) {
            final Comment comment = (Comment) this.commentsList.get(info.position - 1);
            if (this.post.getUser() == null || comment.getUser() == null || StorageContainer.getUser().getModerator().booleanValue()) {
                menu.add(getString(C0530R.string.delete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        PostDetailsActivity.this.deletedComment = comment;
                        PostDetailsActivity.this.showProgress(true);
                        PostDetailsActivity.this.onDeleteCommentCompleted(Boolean.valueOf(true));
                        PostDetailsActivity.this.registerReceiver(PostDetailsActivity.this.broadcastReceiver, new IntentFilter(QuickstartPreferences.DELETE_COMMENT));
                        new AcquireResponseTask(PostDetailsActivity.this).execute(new String[]{new DeletePostUrl(comment.getId().toString()).createAndReturnUrl(PostDetailsActivity.this), QuickstartPreferences.DELETE_COMMENT});
                        return true;
                    }
                });
            } else {
                menu.add(getString(C0530R.string.cant_delete_comment));
            }
        }
    }

    public void onUserImageClick(View view) {
        User postUser = this.post.getUser();
        if (postUser == null && StorageContainer.getUser() != null) {
            postUser = StorageContainer.getUser();
        }
        if (postUser.getPicture() != null) {
            byte[] decodedString = Base64.decode(postUser.getPicture(), 0);
            showBigImage(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        }
    }

    public void onPostImageClick(View view) {
        if (this.post.getAttachment() != null && StorageContainer.getPostImageMemoryCache().get(this.post.getId()) != null) {
            showBigImage((Bitmap) StorageContainer.getPostImageMemoryCache().get(this.post.getId()));
        }
    }

    private void showBigImage(Bitmap image) {
        this.transparentBackground.setVisibility(0);
        this.imageLarge.setVisibility(0);
        this.imageLarge.setImageBitmap(image);
        this.imageLarge.resetZoom();
    }

    private void createAsyncBroadcast() {
        this.broadcastReceiver = new C05296();
    }

    private void onCheckUserHaveAccountCompleted(JSONObject result) {
        try {
            JSONObject userObject = new JSONParserBgw(result).getJsonObject(Participant.USER_TYPE);
            if (userObject != null) {
                JSONParserBgw jsonParserUser = new JSONParserBgw(userObject);
                StorageContainer.setUser(new User(jsonParserUser.getInt("id"), jsonParserUser.getString("nickname"), jsonParserUser.getString("picture"), jsonParserUser.getString("country"), jsonParserUser.getString("city"), jsonParserUser.getBoolean("moderator")));
                if (StorageContainer.getUser().getPictureUrl() == null || StorageContainer.getUser().getPicture() != null) {
                    getPostForId();
                    return;
                } else {
                    loadImage();
                    return;
                }
            }
            startActivity(new Intent(this, ChangeUserDetailsActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onGetPostDorIdCompleted(JSONObject result) {
        try {
            if (this.post == null) {
                int j;
                this.usersList = new ArrayList();
                JSONParserBgw jSONParserBgw = new JSONParserBgw(result);
                List<Comment> comments = new ArrayList();
                List<Like> likes = new ArrayList();
                Integer postId = jSONParserBgw.getInt("id");
                String postTime = Utils.calculateTime(jSONParserBgw.getString("time"), "yyyy-MM-dd HH:mm");
                String postMessage = jSONParserBgw.getString("message");
                Integer postCategory = jSONParserBgw.getInt("category");
                String postAttachmentUrl = jSONParserBgw.getString("attachment");
                Integer postAttachmentWidth = jSONParserBgw.getInt("attachment_width");
                Integer postAttachmentHeight = jSONParserBgw.getInt("attachment_height");
                jSONParserBgw = new JSONParserBgw(jSONParserBgw.getJsonObject(Participant.USER_TYPE));
                Integer userId = jSONParserBgw.getInt("id");
                String userNickname = jSONParserBgw.getString("nickname");
                String userPictureUrl = jSONParserBgw.getString("picture");
                String userCountry = jSONParserBgw.getString("country");
                String userCity = jSONParserBgw.getString("city");
                Boolean moderator = jSONParserBgw.getBoolean("moderator");
                Boolean newUser = Boolean.valueOf(true);
                if (StorageContainer.getUser().getId().equals(userId)) {
                    newUser = Boolean.valueOf(false);
                } else {
                    for (User userPost : this.usersList) {
                        if (userPost.getId().equals(userId)) {
                            newUser = Boolean.valueOf(false);
                        }
                    }
                }
                if (newUser.booleanValue()) {
                    this.usersList.add(new User(userId, userNickname, userPictureUrl, userCountry, userCity, moderator));
                }
                User postUser = null;
                if (!StorageContainer.getUser().getId().equals(userId)) {
                    for (User userPost2 : this.usersList) {
                        if (userId.equals(userPost2.getId())) {
                            postUser = userPost2;
                        }
                    }
                    if (postUser.getPictureUrl() != null && postUser.getPicture() == null) {
                        loadImage(postUser.getPictureUrl(), postUser);
                    }
                }
                this.post = new Post(postId, postTime, postUser, postMessage, postCategory, postAttachmentUrl, postAttachmentWidth, postAttachmentHeight);
                if (this.post.getAttachment() != null && StorageContainer.getPostImageMemoryCache().get(this.post.getId()) == null) {
                    loadImage(this.post.getAttachment());
                }
                JSONArray likesArray = jSONParserBgw.getJsonArray("likes");
                for (j = 0; j < likesArray.length(); j++) {
                    Integer likeUserId = new JSONParserBgw((JSONObject) likesArray.get(j)).getInt("id_user");
                    likes.add(new Like(likeUserId));
                    if (likeUserId.equals(StorageContainer.getUser().getId())) {
                        this.post.setLiked(Boolean.valueOf(true));
                    }
                }
                this.post.setLikes(likes);
                JSONArray commentsArray = jSONParserBgw.getJsonArray("comments");
                for (j = 0; j < commentsArray.length(); j++) {
                    jSONParserBgw = new JSONParserBgw((JSONObject) commentsArray.get(j));
                    Integer commentId = jSONParserBgw.getInt("id");
                    String commentTime = Utils.calculateTime(jSONParserBgw.getString("time"), "yyyy-MM-dd HH:mm");
                    String message = jSONParserBgw.getString("message");
                    jSONParserBgw = new JSONParserBgw(jSONParserBgw.getJsonObject(Participant.USER_TYPE));
                    Integer userCommentId = jSONParserBgw.getInt("id");
                    String userCommentNickname = jSONParserBgw.getString("nickname");
                    String userCommentPictureUrl = jSONParserBgw.getString("picture");
                    String userCommentCountry = jSONParserBgw.getString("country");
                    String userCommentCity = jSONParserBgw.getString("city");
                    Boolean newCommentUser = Boolean.valueOf(true);
                    if (StorageContainer.getUser().getId().equals(userCommentId)) {
                        newCommentUser = Boolean.valueOf(false);
                    } else {
                        for (User userPost22 : this.usersList) {
                            if (userPost22.getId().equals(userCommentId)) {
                                newCommentUser = Boolean.valueOf(false);
                            }
                        }
                    }
                    if (newCommentUser.booleanValue()) {
                        this.usersList.add(new User(userCommentId, userCommentNickname, userCommentPictureUrl, userCommentCountry, userCommentCity, Boolean.valueOf(false)));
                    }
                    User commentUser = null;
                    if (!StorageContainer.getUser().getId().equals(userCommentId)) {
                        for (User userPost222 : this.usersList) {
                            if (userCommentId.equals(userPost222.getId())) {
                                commentUser = userPost222;
                            }
                        }
                        if (commentUser.getPictureUrl() != null && commentUser.getPicture() == null) {
                            loadImage(commentUser.getPictureUrl(), commentUser);
                        }
                    }
                    comments.add(new Comment(commentId, commentTime, commentUser, message));
                }
                this.post.setLikes(likes);
                this.post.setComments(comments);
            }
            showProgress(false);
            setupToolbar();
            setupLayout();
            implementListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLikeClicked(View view) {
        showProgress(true);
        User postUser = this.post.getUser();
        if (postUser == null && StorageContainer.getUser() != null) {
            postUser = StorageContainer.getUser();
        }
        registerReceiver(this.broadcastReceiver, new IntentFilter(QuickstartPreferences.LIKE_POST));
        new AcquireResponseTask(this).execute(new String[]{new LikePostUrl(this.post.getId().toString(), postUser.getId().toString()).createAndReturnUrl(this), QuickstartPreferences.LIKE_POST});
    }

    private void onLikePostCompleted(Boolean result) {
        showProgress(false);
        sendBroadcast(new Intent("refresh_posts"));
        if (result.booleanValue() && this.post != null) {
            List<Like> likes = new ArrayList(this.post.getLikes());
            if (this.post.getLiked() == null || !this.post.getLiked().booleanValue()) {
                this.post.setLiked(Boolean.valueOf(true));
                this.post.getLikes().add(new Like(StorageContainer.getUser().getId()));
            } else {
                this.post.setLiked(Boolean.valueOf(false));
                for (Like like : this.post.getLikes()) {
                    if (like.getUserId().equals(StorageContainer.getUser().getId())) {
                        likes.remove(like);
                    }
                }
                this.post.setLikes(likes);
            }
            setPostData();
        }
    }

    private void onDeleteCommentCompleted(Boolean result) {
        showProgress(false);
        sendBroadcast(new Intent("refresh_posts"));
        if (result.booleanValue() && this.post != null && this.deletedComment != null) {
            this.post.getComments().remove(this.deletedComment);
            setPostData();
        }
    }

    private void onAddCommentCompleted(Integer result) {
        showProgress(false);
        sendBroadcast(new Intent("refresh_posts"));
        if (result.intValue() > 0 && this.post != null) {
            this.post.getComments().add(new Comment(result, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()), StorageContainer.getUser(), this.addNewCommentView.getText().toString()));
            setPostData();
            this.addNewCommentView.setText(null);
        }
    }

    public void onDeletePostCompleted(Boolean result) {
        try {
            sendBroadcast(new Intent("refresh_posts"));
            showProgress(false);
            if (result.booleanValue()) {
                Toast.makeText(this, "success", 1).show();
                finish();
                return;
            }
            Toast.makeText(this, "failed", 1).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditPostCompleted(String result) {
        showProgress(false);
        Toast.makeText(this, result, 1).show();
        sendBroadcast(new Intent("refresh_posts"));
    }

    public void showProgress(boolean show) {
        this.progressView.setVisibility(show ? 0 : 8);
    }

    public void onCommentsClicked(View view) {
    }

    public void onBackPressed() {
        if (this.addPostLayout != null && this.addPostLayout.getVisibility() == 0) {
            this.addPostLayout.startAnimation(AnimationUtils.loadAnimation(this, C0530R.anim.abc_fade_out));
            this.addPostLayout.setVisibility(8);
        } else if (this.transparentBackground == null || this.transparentBackground.getVisibility() != 0) {
            finish();
        } else {
            this.transparentBackground.setVisibility(8);
            this.imageLarge.setVisibility(8);
        }
    }

    public void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
    }

    private void loadImage() {
        try {
            String src = StorageContainer.socialHost + "/moocall_social_images/" + StorageContainer.getUser().getPictureUrl();
            System.out.println("src: " + src);
            this.imageLoader.loadImage(src, new C13327());
        } catch (Exception e) {
            e.printStackTrace();
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
                    byte[] imageBytes = baos.toByteArray();
                    tmpUser.setImageLoading(Boolean.valueOf(false));
                    tmpUser.setPicture(Base64.encodeToString(imageBytes, 0));
                    PostDetailsActivity.this.setPostData();
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(String url) {
        try {
            String src = StorageContainer.socialHost + "/moocall_social_images/" + url;
            System.out.println("src: " + src);
            this.post.setImageLoading(Boolean.valueOf(true));
            this.imageLoader.loadImage(src, new C13349());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
