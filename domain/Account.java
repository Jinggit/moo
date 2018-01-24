package com.moocall.moocall.domain;

import java.io.Serializable;

public class Account implements Serializable {
    private static String email;
    private static Integer maxCalving;
    private static Integer maxCow;
    private static Integer maxPhones;
    private static Boolean myMoocall;
    private static String name;
    private static String password;
    private static Boolean update;
    private static String userImage;
    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        password = password;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        email = email;
    }

    public static Integer getMaxPhones() {
        return maxPhones;
    }

    public static void setMaxPhones(Integer maxPhones) {
        maxPhones = maxPhones;
    }

    public static Boolean getMyMoocall() {
        return myMoocall;
    }

    public static void setMyMoocall(Boolean myMoocall) {
        myMoocall = myMoocall;
    }

    public static Integer getMaxCalving() {
        return maxCalving;
    }

    public static void setMaxCalving(Integer maxCalving) {
        maxCalving = maxCalving;
    }

    public static Integer getMaxCow() {
        return maxCow;
    }

    public static void setMaxCow(Integer maxCow) {
        maxCow = maxCow;
    }

    public static Boolean getUpdate() {
        return update;
    }

    public static void setUpdate(Boolean update) {
        update = update;
    }

    public static String getUserImage() {
        return userImage;
    }

    public static void setUserImage(String userImage) {
        userImage = userImage;
    }
}
