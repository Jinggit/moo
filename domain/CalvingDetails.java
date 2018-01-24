package com.moocall.moocall.domain;

import android.app.Activity;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;

public class CalvingDetails implements Serializable {
    private Integer calfGenderNumber;
    private String calfGenderText;
    private Integer calfProcessNumber;
    private String calfProcessText;
    private Integer calfStatusNumber;
    private String calfStatusText;
    private String deceasedDateServer;
    private String deceasedDateText;
    private Integer difficulty;
    private Integer id;
    private String title;
    private String weight;

    public CalvingDetails(JSONParserBgw jsonParserCalf, Integer number, Activity activity) {
        setTitle(number, activity);
        Integer weight = jsonParserCalf.getInt("weight");
        setId(jsonParserCalf.getInt("calfId"));
        setCalfProcessText(jsonParserCalf.getString("calvingStatus"));
        setCalfGenderText(jsonParserCalf.getString("gender"));
        setCalfStatusText(jsonParserCalf.getString("postnatalStatus"));
        if (weight.intValue() > 0) {
            setWeight(weight.toString());
        }
        setDeceasedDateServer(Utils.calculateShortTime(jsonParserCalf.getString("deceaseDate"), "yyyy-MM-dd"));
        setDifficulty(jsonParserCalf.getInt("difficulty"));
    }

    public CalvingDetails(Integer number, Activity activity) {
        setTitle(number, activity);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(Integer number, Activity activity) {
        String title = null;
        switch (number.intValue()) {
            case 0:
                title = activity.getString(C0530R.string.first_calf) + ":";
                break;
            case 1:
                title = activity.getString(C0530R.string.second_calf) + ":";
                break;
            case 2:
                title = activity.getString(C0530R.string.third_calf) + ":";
                break;
            case 3:
                title = activity.getString(C0530R.string.fourth_calf) + ":";
                break;
            case 4:
                title = activity.getString(C0530R.string.fifth_calf) + ":";
                break;
        }
        this.title = title;
    }

    public Integer getCalfProcessNumber() {
        return this.calfProcessNumber;
    }

    public void setCalfProcessNumber(Integer calfProcessNumber) {
        this.calfProcessNumber = calfProcessNumber;
    }

    public String getCalfProcessText() {
        return this.calfProcessText;
    }

    public void setCalfProcessText(String calfProcessText) {
        if (calfProcessText != null && !calfProcessText.equals("null")) {
            this.calfProcessText = calfProcessText;
        }
    }

    public Integer getCalfGenderNumber() {
        return this.calfGenderNumber;
    }

    public void setCalfGenderNumber(Integer calfGenderNumber) {
        this.calfGenderNumber = calfGenderNumber;
    }

    public String getCalfGenderText() {
        return this.calfGenderText;
    }

    public void setCalfGenderText(String calfGenderText) {
        if (calfGenderText != null && !calfGenderText.equals("null")) {
            this.calfGenderText = calfGenderText;
        }
    }

    public Integer getCalfStatusNumber() {
        return this.calfStatusNumber;
    }

    public void setCalfStatusNumber(Integer calfStatusNumber) {
        this.calfStatusNumber = calfStatusNumber;
    }

    public String getCalfStatusText() {
        return this.calfStatusText;
    }

    public void setCalfStatusText(String calfStatusText) {
        if (calfStatusText != null && !calfStatusText.equals("null")) {
            this.calfStatusText = calfStatusText;
        }
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDeceasedDateServer() {
        return this.deceasedDateServer;
    }

    public void setDeceasedDateServer(String deceasedDateServer) {
        if (deceasedDateServer.length() > 0) {
            setDeceasedDateText(Utils.fromServerToNormal(deceasedDateServer));
            this.deceasedDateServer = deceasedDateServer;
        }
    }

    public String getDeceasedDateText() {
        return this.deceasedDateText;
    }

    public void setDeceasedDateText(String deceasedDateText) {
        this.deceasedDateText = deceasedDateText;
    }

    public void setDeceasedDate(int year, int month, int day) {
        String monthText = "";
        String dateText = "";
        if (day < 10) {
            dateText = "0" + day;
        } else {
            dateText = day + "";
        }
        if (month < 9) {
            monthText = "0" + (month + 1);
        } else {
            monthText = (month + 1) + "";
        }
        setDeceasedDateServer(year + "-" + monthText + "-" + dateText);
    }

    public String getText() {
        String text = "";
        if (getCalfProcessText() != null) {
            text = text + getCalfProcessText() + ". ";
        }
        if (getCalfGenderText() != null) {
            text = text + getCalfGenderText() + ". ";
        }
        if (getCalfStatusText() != null) {
            return text + getCalfStatusText() + ".";
        }
        return text;
    }

    public Integer getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String toString() {
        return "CalvingDetails{title='" + this.title + '\'' + ", calfProcessNumber=" + this.calfProcessNumber + ", calfProcessText='" + this.calfProcessText + '\'' + ", calfGenderNumber=" + this.calfGenderNumber + ", calfGenderText='" + this.calfGenderText + '\'' + ", calfStatusNumber=" + this.calfStatusNumber + ", calfStatusText='" + this.calfStatusText + '\'' + ", weight='" + this.weight + '\'' + '}';
    }
}
