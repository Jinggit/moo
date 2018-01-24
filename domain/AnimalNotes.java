package com.moocall.moocall.domain;

import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class AnimalNotes implements Serializable {
    private String date;
    private Integer id;
    private String showText;
    private String text;
    private String time;

    public AnimalNotes(JSONParserBgw jsonParserNotes) {
        String[] datetime = Utils.calculateTime(jsonParserNotes.getString("date"), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE);
        if (datetime.length > 1) {
            setDate(datetime[0]);
            setTime(datetime[1]);
        }
        setText(jsonParserNotes.getString("text"));
        setShowText(getDate() + " - " + getText());
        setId(jsonParserNotes.getInt("id"));
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getShowText() {
        return this.showText;
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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
