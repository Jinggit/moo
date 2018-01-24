package com.moocall.moocall.domain;

import java.io.Serializable;

public class Comment implements Serializable {
    private Integer id;
    private String text;
    private String time;
    private User user;

    public Comment(Integer id, String time, User user, String text) {
        setId(id);
        setTime(time);
        setUser(user);
        setText(text);
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
        this.text = text;
    }
}
