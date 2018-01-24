package com.moocall.moocall.adapter;

import android.app.Activity;
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
import com.moocall.moocall.domain.Comment;
import com.moocall.moocall.domain.User;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

public class CommentsListAdapter extends BaseAdapter {
    private Activity activity;
    private List<Comment> commentList;
    private LayoutInflater inflater;
    private HashMap<Integer, Bitmap> userImages = new HashMap();

    public CommentsListAdapter(Activity activity, List<Comment> commentList) {
        this.activity = activity;
        this.commentList = commentList;
    }

    public int getCount() {
        return this.commentList.size();
    }

    public Object getItem(int position) {
        return this.commentList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (convertView == null) {
            convertView = this.inflater.inflate(C0530R.layout.comment_row, null);
        }
        TextView commentAuthor = (TextView) convertView.findViewById(C0530R.id.commentAuthor);
        TextView commentText = (TextView) convertView.findViewById(C0530R.id.commentText);
        ImageView userImage = (ImageView) convertView.findViewById(C0530R.id.userImage);
        TextView commentTime = (TextView) convertView.findViewById(C0530R.id.commentTime);
        Comment comment = (Comment) this.commentList.get(position);
        User commentUser = comment.getUser();
        if (commentUser == null && StorageContainer.getUser() != null) {
            commentUser = StorageContainer.getUser();
        }
        commentAuthor.setText(commentUser.getNickname());
        commentText.setText(StringEscapeUtils.unescapeJava(comment.getText()));
        commentTime.setText(Utils.getTimeFromPost(comment.getTime()));
        if (commentUser.getPicture() != null) {
            if (this.userImages.get(Integer.valueOf(position)) == null) {
                byte[] decodedString = Base64.decode(commentUser.getPicture(), 0);
                this.userImages.put(Integer.valueOf(position), Utils.getCroppedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), this.activity, 50));
            }
            userImage.setImageBitmap((Bitmap) this.userImages.get(Integer.valueOf(position)));
        } else {
            userImage.setImageResource(C0530R.drawable.moocall_user);
        }
        return convertView;
    }
}
