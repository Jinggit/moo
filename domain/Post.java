package com.moocall.moocall.domain;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private String attachment;
    private Integer attachmentHeight;
    private Integer attachmentWidth;
    private Integer category;
    private List<Comment> comments;
    private Integer id;
    private Boolean imageLoading;
    private Boolean liked;
    private List<Like> likes;
    private String text;
    private String time;
    private User user;

    public Post(Integer id, String time, User user, String text, Integer category, String attachment, Integer attachmentWidth, Integer attachmentHeight) {
        setId(id);
        setTime(time);
        setUser(user);
        setText(text);
        setCategory(category);
        setAttachment(attachment);
        setAttachmentWidth(attachmentWidth);
        setAttachmentHeight(attachmentHeight);
        setImageLoading(Boolean.valueOf(false));
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        if (text == null || text == "null") {
            this.text = "";
        } else {
            this.text = text;
        }
    }

    public Integer getCategory() {
        return this.category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getAttachment() {
        return this.attachment;
    }

    public void setAttachment(String attachment) {
        if (attachment.length() > 0) {
            this.attachment = attachment;
        }
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return this.likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public Boolean getLiked() {
        return this.liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Integer getAttachmentWidth() {
        return this.attachmentWidth;
    }

    public void setAttachmentWidth(Integer attachmentWidth) {
        this.attachmentWidth = attachmentWidth;
    }

    public Integer getAttachmentHeight() {
        return this.attachmentHeight;
    }

    public void setAttachmentHeight(Integer attachmentHeight) {
        this.attachmentHeight = attachmentHeight;
    }

    public Boolean getImageLoading() {
        return this.imageLoading;
    }

    public void setImageLoading(Boolean imageLoading) {
        this.imageLoading = imageLoading;
    }

    public String toString() {
        return "Post{id=" + this.id + ", time='" + this.time + '\'' + ", user=" + this.user + ", text='" + this.text + '\'' + ", attachment='" + this.attachment + '\'' + ", comments=" + this.comments + ", likes=" + this.likes + ", liked=" + this.liked + '}';
    }
}
