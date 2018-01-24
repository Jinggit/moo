package com.moocall.moocall.util;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MoocallTimezones {
    private Context context;
    private List<String> eight = new ArrayList();
    private List<String> eleven = new ArrayList();
    private List<String> five = new ArrayList();
    private List<String> four = new ArrayList();
    private List<String> minusEight = new ArrayList();
    private List<String> minusEleven = new ArrayList();
    private List<String> minusFive = new ArrayList();
    private List<String> minusFour = new ArrayList();
    private List<String> minusNine = new ArrayList();
    private List<String> minusOne = new ArrayList();
    private List<String> minusSeven = new ArrayList();
    private List<String> minusSix = new ArrayList();
    private List<String> minusTen = new ArrayList();
    private List<String> minusThree = new ArrayList();
    private List<String> minusTwelve = new ArrayList();
    private List<String> minusTwo = new ArrayList();
    private List<String> nine = new ArrayList();
    private List<String> one = new ArrayList();
    private List<String> seven = new ArrayList();
    private List<String> six = new ArrayList();
    private List<String> ten = new ArrayList();
    private List<String> three = new ArrayList();
    private String timezone;
    private List<String> timezones;
    private List<String> timezonesAndroid;
    private List<String> twelve = new ArrayList();
    private List<String> two = new ArrayList();
    private List<String> zero = new ArrayList();

    public MoocallTimezones(Context context) {
        this.context = context;
        this.timezonesAndroid = new ArrayList();
        setTimezone();
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone() {
        TimeZone tz = TimeZone.getDefault();
        long hours = TimeUnit.MILLISECONDS.toHours((long) tz.getRawOffset());
        long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes((long) tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours));
        String result = "";
        if (hours < 0) {
            result = String.format("(GMT %03d:%02d) %s", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), tz.getID()});
        } else {
            result = String.format("(GMT +%02d:%02d) %s", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), tz.getID()});
        }
        this.timezone = result;
    }

    private void setTimezones() {
        String[] timezonesFromFile = StorageContainer.loadFileFromAsset("timezones_moocall", this.context).split("[\\r\\n]+");
        for (Object add : timezonesFromFile) {
            this.timezones.add(add);
        }
    }

    public List<String> getTimezones() {
        return this.timezones;
    }

    public List<String> getTimezonesAndroid() {
        return this.timezonesAndroid;
    }

    public void setTimezonesAndroid() {
        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone tz = TimeZone.getTimeZone(id);
            long hours = TimeUnit.MILLISECONDS.toHours((long) tz.getRawOffset());
            long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes((long) tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours));
            String result = "";
            if (hours < 0) {
                result = String.format("(GMT %03d:%02d) %s", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), tz.getID()});
            } else {
                result = String.format("(GMT +%02d:%02d) %s", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), tz.getID()});
            }
            putToProperList(hours, result);
        }
        mergeLists();
    }

    private void mergeLists() {
        this.timezonesAndroid.addAll(this.zero);
        this.timezonesAndroid.addAll(this.one);
        this.timezonesAndroid.addAll(this.two);
        this.timezonesAndroid.addAll(this.three);
        this.timezonesAndroid.addAll(this.four);
        this.timezonesAndroid.addAll(this.five);
        this.timezonesAndroid.addAll(this.six);
        this.timezonesAndroid.addAll(this.seven);
        this.timezonesAndroid.addAll(this.eight);
        this.timezonesAndroid.addAll(this.nine);
        this.timezonesAndroid.addAll(this.ten);
        this.timezonesAndroid.addAll(this.eleven);
        this.timezonesAndroid.addAll(this.twelve);
        this.timezonesAndroid.addAll(this.minusOne);
        this.timezonesAndroid.addAll(this.minusTwo);
        this.timezonesAndroid.addAll(this.minusThree);
        this.timezonesAndroid.addAll(this.minusFour);
        this.timezonesAndroid.addAll(this.minusFive);
        this.timezonesAndroid.addAll(this.minusSix);
        this.timezonesAndroid.addAll(this.minusSeven);
        this.timezonesAndroid.addAll(this.minusEight);
        this.timezonesAndroid.addAll(this.minusNine);
        this.timezonesAndroid.addAll(this.minusTen);
        this.timezonesAndroid.addAll(this.minusEleven);
        this.timezonesAndroid.addAll(this.minusTwelve);
    }

    private void putToProperList(long hours, String timezone) {
        switch ((int) hours) {
            case -12:
                this.minusTwelve.add(timezone);
                return;
            case -11:
                this.minusEleven.add(timezone);
                return;
            case -10:
                this.minusTen.add(timezone);
                return;
            case -9:
                this.minusNine.add(timezone);
                return;
            case -8:
                this.minusEight.add(timezone);
                return;
            case -7:
                this.minusSeven.add(timezone);
                return;
            case -6:
                this.minusSix.add(timezone);
                return;
            case -5:
                this.minusFive.add(timezone);
                return;
            case -4:
                this.minusFour.add(timezone);
                return;
            case -3:
                this.minusThree.add(timezone);
                return;
            case -2:
                this.minusTwo.add(timezone);
                return;
            case -1:
                this.minusOne.add(timezone);
                return;
            case 0:
                this.zero.add(timezone);
                return;
            case 1:
                this.one.add(timezone);
                return;
            case 2:
                this.two.add(timezone);
                return;
            case 3:
                this.three.add(timezone);
                return;
            case 4:
                this.four.add(timezone);
                return;
            case 5:
                this.five.add(timezone);
                return;
            case 6:
                this.six.add(timezone);
                return;
            case 7:
                this.seven.add(timezone);
                return;
            case 8:
                this.eight.add(timezone);
                return;
            case 9:
                this.nine.add(timezone);
                return;
            case 10:
                this.ten.add(timezone);
                return;
            case 11:
                this.eleven.add(timezone);
                return;
            case 12:
                this.twelve.add(timezone);
                return;
            default:
                return;
        }
    }
}
