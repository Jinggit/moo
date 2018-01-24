package com.moocall.moocall.domain;

import android.app.Activity;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Calving implements Serializable {
    private Integer bullId;
    private String bullTag;
    private String calfBornTime;
    private Integer calvesNumber;
    private List<CalvingDetails> calvingDetailsList;
    private String cow;
    private String date;
    private String dateText;
    private Integer id;
    private String serverCetTime;
    private String time;
    private String timeText;

    public Calving() {
        setCalfBornTime("NA");
    }

    public Calving(JSONParserBgw jsonParserCalving, Activity activity) {
        try {
            setDateTimeServer(Utils.calculateTime(jsonParserCalving.getString("date"), "yyyy-MM-dd HH:mm:ss"));
            setCalvesNumber(jsonParserCalving.getInt("calvesNumber"));
            setId(jsonParserCalving.getInt("idCalvingHistory"));
            setBullTag(jsonParserCalving.getString("bullTag"));
            setBullId(jsonParserCalving.getInt("bullId"));
            JSONArray calves = jsonParserCalving.getJsonArray("calves");
            List<CalvingDetails> calvingDetailsList = new ArrayList();
            for (int i = 0; i < calves.length(); i++) {
                calvingDetailsList.add(new CalvingDetails(new JSONParserBgw((JSONObject) calves.get(i)), Integer.valueOf(i), activity));
            }
            setCalvingDetailsList(calvingDetailsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCow() {
        return this.cow;
    }

    public void setCow(String cow) {
        this.cow = cow;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateTimeServer(String datetime) {
        if (datetime.length() > 0) {
            String[] parts = datetime.split(StringUtils.SPACE);
            String[] date = parts[0].split("-");
            String[] time = parts[1].split(":");
            String year = date[0];
            String month = date[1];
            String day = date[2];
            String hour = time[0];
            String minute = time[1];
            setDate(parts[0]);
            setDateText(day + "." + month + "." + year + ".");
            setTime(parts[1]);
            setTimeText(hour + ":" + minute);
            setCalfBornTime(day + "-" + month + "-" + year.substring(year.length() - 2));
        }
    }

    public void setDateTimePicker(int year, int month, int day, int hour, int minute) {
        String monthText = "";
        String dateText = "";
        String hourText = "";
        String minuteText = "";
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
        if (hour < 10) {
            hourText = "0" + hour;
        } else {
            hourText = hour + "";
        }
        if (minute < 10) {
            minuteText = "0" + minute;
        } else {
            minuteText = minute + "";
        }
        setDate(year + "-" + monthText + "-" + dateText);
        setDateText(dateText + "." + monthText + "." + year + ".");
        setTime(hourText + ":" + minuteText + ":00");
        setTimeText(hourText + ":" + minuteText);
        setServerCetTime(getDate() + StringUtils.SPACE + getTime());
    }

    public String getServerCetTime() {
        return this.serverCetTime;
    }

    public void setServerCetTime(String serverCetTime) {
        String[] parts = Utils.calculateCetTime(serverCetTime).split(StringUtils.SPACE);
        this.serverCetTime = parts[0] + "_" + parts[1];
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

    public String getDateText() {
        return this.dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getTimeText() {
        return this.timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getCalfBornTime() {
        return this.calfBornTime;
    }

    public void setCalfBornTime(String calfBornTime) {
        this.calfBornTime = calfBornTime;
    }

    public String getBullTag() {
        return this.bullTag;
    }

    public void setBullTag(String bullTag) {
        this.bullTag = bullTag;
    }

    public Integer getBullId() {
        return this.bullId;
    }

    public void setBullId(Integer bullId) {
        this.bullId = bullId;
    }

    public Integer getCalvesNumber() {
        return this.calvesNumber;
    }

    public void setCalvesNumber(Integer calvesNumber) {
        this.calvesNumber = calvesNumber;
    }

    public List<CalvingDetails> getCalvingDetailsList() {
        return this.calvingDetailsList;
    }

    public void setCalvingDetailsList(List<CalvingDetails> calvingDetailsList) {
        this.calvingDetailsList = calvingDetailsList;
    }

    public String toString() {
        return "Calving{cow='" + this.cow + '\'' + ", date='" + this.date + '\'' + ", id=" + this.id + ", time='" + this.time + '\'' + ", dateText='" + this.dateText + '\'' + ", timeText='" + this.timeText + '\'' + ", calfBornTime='" + this.calfBornTime + '\'' + ", serverCetTime='" + this.serverCetTime + '\'' + ", bull='" + this.bullTag + '\'' + ", calvesNumber=" + this.calvesNumber + ", calvingDetailsList=" + this.calvingDetailsList + '}';
    }
}
