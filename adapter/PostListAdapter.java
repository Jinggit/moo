package com.moocall.moocall.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.SocialNetworkActivity;
import com.moocall.moocall.domain.Post;
import com.moocall.moocall.domain.PostFilter;
import com.moocall.moocall.domain.User;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

public class PostListAdapter extends BaseAdapter {
    private SocialNetworkActivity activity;
    private Boolean flinging;
    private LayoutInflater inflater;
    private PostFilter postFilter;
    private List<Post> postList;
    private HashMap<Integer, Bitmap> userImages = new HashMap();

    public PostListAdapter(SocialNetworkActivity activity, List<Post> postList, PostFilter postFilter) {
        this.activity = activity;
        this.postList = postList;
        this.postFilter = postFilter;
    }

    public void setFlinging(Boolean flinging) {
        this.flinging = flinging;
    }

    public void setPostFilter(PostFilter postFilter) {
        this.postFilter = postFilter;
    }

    public HashMap<Integer, Bitmap> getUserImages() {
        return this.userImages;
    }

    public void setUserImages(HashMap<Integer, Bitmap> userImages) {
        this.userImages = userImages;
    }

    public int getCount() {
        return this.postList.size();
    }

    public Object getItem(int position) {
        return this.postList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (position > 0) {
            convertView = this.inflater.inflate(C0530R.layout.post_row, null);
            TextView postAuthor = (TextView) convertView.findViewById(C0530R.id.postAuthor);
            TextView postAuthorLocation = (TextView) convertView.findViewById(C0530R.id.postAuthorLocation);
            TextView postText = (TextView) convertView.findViewById(C0530R.id.postText);
            ImageView postImage = (ImageView) convertView.findViewById(C0530R.id.postImage);
            TextView numberOfLikes = (TextView) convertView.findViewById(C0530R.id.numberOfLikes);
            TextView numberOfComments = (TextView) convertView.findViewById(C0530R.id.numberOfComments);
            ImageView postLiked = (ImageView) convertView.findViewById(C0530R.id.postLiked);
            ImageView userImage = (ImageView) convertView.findViewById(C0530R.id.userImage);
            TextView postTime = (TextView) convertView.findViewById(C0530R.id.postTime);
            TextView postCategory = (TextView) convertView.findViewById(C0530R.id.postCategory);
            ImageView commentIcon = (ImageView) convertView.findViewById(C0530R.id.commentIcon);
            Post post = (Post) this.postList.get(position);
            User postUser = post.getUser();
            if (postUser == null && StorageContainer.getUser() != null) {
                postUser = StorageContainer.getUser();
            }
            if (postUser != null) {
                postAuthor.setText(postUser.getNickname());
                postAuthorLocation.setText(postUser.getLocation());
                postText.setText(StringEscapeUtils.unescapeJava(post.getText()));
                numberOfLikes.setText(String.valueOf(post.getLikes().size()));
                numberOfComments.setText(String.valueOf(post.getComments().size()));
                postTime.setText(Utils.getTimeFromPost(post.getTime()));
                if (this.postFilter.getCategories() != null) {
                    postCategory.setText((CharSequence) this.postFilter.getCategories().get(post.getCategory()));
                }
                if (post.getLiked() == null || !post.getLiked().booleanValue()) {
                    postLiked.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.moocall_social_like));
                } else {
                    postLiked.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.moocall_social_liked));
                }
                if (post.getComments().size() > 0) {
                    commentIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.moocall_social_comment_green));
                } else {
                    commentIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.moocall_social_comment));
                }
                if (postUser.getPicture() != null) {
                    if (this.userImages.get(postUser.getId()) == null) {
                        byte[] decodedString = Base64.decode(postUser.getPicture(), 0);
                        this.userImages.put(postUser.getId(), Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this.activity, 40));
                    }
                    userImage.setPadding(10, 10, 10, 10);
                    userImage.setImageBitmap((Bitmap) this.userImages.get(postUser.getId()));
                } else {
                    if (postUser.getPictureUrl() != null) {
                        this.activity.loadImage(postUser.getPictureUrl(), postUser);
                    }
                    userImage.setPadding(0, 0, 0, 0);
                    userImage.setImageResource(C0530R.drawable.moocall_user);
                }
                if (post.getAttachment() == null) {
                    postImage.getLayoutParams().height = 0;
                    postImage.setImageDrawable(null);
                } else if (StorageContainer.getPostImageMemoryCache().get(post.getId()) != null) {
                    Bitmap postImageBitmap;
                    if (this.activity.getResources().getConfiguration().orientation == 2) {
                        postImageBitmap = Utils.getResizedPostBitmap((Bitmap) StorageContainer.getPostImageMemoryCache().get(post.getId()), this.activity, Boolean.valueOf(true));
                    } else {
                        postImageBitmap = Utils.getResizedPostBitmap((Bitmap) StorageContainer.getPostImageMemoryCache().get(post.getId()), this.activity, Boolean.valueOf(false));
                    }
                    postImage.getLayoutParams().height = postImageBitmap.getHeight() * 2;
                    postImage.setImageBitmap(postImageBitmap);
                } else {
                    if (post.getAttachmentHeight() == null || post.getAttachmentWidth() == null) {
                        postImage.getLayoutParams().height = 300;
                    } else {
                        postImage.getLayoutParams().height = Utils.getResizedPostBitmapHeight(post.getAttachmentHeight().intValue(), post.getAttachmentWidth().intValue(), this.activity).intValue();
                    }
                    postImage.setImageResource(C0530R.drawable.small_gray_rect);
                    if (!(post.getImageLoading().booleanValue() || this.flinging.booleanValue())) {
                        this.activity.loadImage(post.getAttachment(), post);
                    }
                }
            }
        } else {
            convertView = this.inflater.inflate(C0530R.layout.filter_text_layout, null);
            ImageView filterCancelIcon = (ImageView) convertView.findViewById(C0530R.id.filterCancelIcon);
            ((TextView) convertView.findViewById(C0530R.id.filterText)).setText(this.postFilter.toString());
            if (this.postFilter.getAllUsers().booleanValue() && this.postFilter.getAllCategory().booleanValue() && this.postFilter.getCountry() == null) {
                filterCancelIcon.setVisibility(8);
            } else {
                filterCancelIcon.setVisibility(0);
            }
        }
        return convertView;
    }
}
