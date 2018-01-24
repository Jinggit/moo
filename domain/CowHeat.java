package com.moocall.moocall.domain;

import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;

public class CowHeat {
    private String date;
    private String from;
    private String name;
    private String tag;
    private String to;

    public CowHeat(JSONParserBgw jsonParserCowHeat) {
        setTag(jsonParserCowHeat.getString("cow_tag"));
        setName(jsonParserCowHeat.getString("name"));
        setDate(Utils.fromServerToNormal(jsonParserCowHeat.getString("time")));
        setFrom(jsonParserCowHeat.getString("time_from"));
        setTo(jsonParserCowHeat.getString("time_to"));
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
