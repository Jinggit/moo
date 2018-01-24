package com.moocall.moocall.domain;

public class ClientSettings {
    private Boolean changed;
    private String email1;
    private String email2;
    private String email3;
    private Integer emailOption;
    private Boolean enableNotification;
    private Boolean notificationChanged;
    private String ringtone;
    private String timezone;
    private Integer timezoneListId;

    public ClientSettings(Integer emailOption, String timezone, String email1, String email2, String email3) {
        setEmailOption(emailOption);
        setTimezone(timezone);
        setEmail1(email1);
        setEmail2(email2);
        setEmail3(email3);
        setChanged(Boolean.valueOf(false));
        setEnableNotification(Boolean.valueOf(false));
        setNotificationChanged(Boolean.valueOf(false));
    }

    public Integer getEmailOption() {
        return this.emailOption;
    }

    public void setEmailOption(Integer emailOption) {
        this.emailOption = emailOption;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getEmail1() {
        return this.email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return this.email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return this.email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public Boolean getChanged() {
        return this.changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    public Integer getTimezoneListId() {
        return this.timezoneListId;
    }

    public void setTimezoneListId(Integer timezoneListId) {
        this.timezoneListId = timezoneListId;
    }

    public Boolean getEnableNotification() {
        return this.enableNotification;
    }

    public void setEnableNotification(Boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    public String getRingtone() {
        return this.ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public Boolean getNotificationChanged() {
        return this.notificationChanged;
    }

    public void setNotificationChanged(Boolean notificationChanged) {
        this.notificationChanged = notificationChanged;
    }

    public String toString() {
        return "ClientSettings{emailOption=" + this.emailOption + ", timezone='" + this.timezone + '\'' + ", email1='" + this.email1 + '\'' + ", email2='" + this.email2 + '\'' + ", email3='" + this.email3 + '\'' + ", changed=" + this.changed + ", timezoneListId=" + this.timezoneListId + ", enableNotification=" + this.enableNotification + '}';
    }
}
