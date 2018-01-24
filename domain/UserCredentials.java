package com.moocall.moocall.domain;

import java.io.Serializable;

public class UserCredentials implements Serializable {
    private String password;
    private String sid;
    private String username;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentials(String username, String password, String sid) {
        this.username = username;
        this.password = password;
        this.sid = sid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String toString() {
        return "UserCredentials{username='" + this.username + '\'' + ", password='" + this.password + '\'' + ", sid='" + this.sid + '\'' + '}';
    }
}
