package com.moocall.moocall.db;

import java.io.Serializable;

public class CalvingDb implements Serializable {
    private String bull_id;
    private String bull_tag_number;
    private String calves;
    private String calves_number;
    private String cow_id;
    private String cow_tag_number;
    private String datetime;
    private Long id;

    public CalvingDb(Long id) {
        this.id = id;
    }

    public CalvingDb(Long id, String cow_id, String cow_tag_number, String bull_id, String bull_tag_number, String datetime, String calves_number, String calves) {
        this.id = id;
        this.cow_id = cow_id;
        this.cow_tag_number = cow_tag_number;
        this.bull_id = bull_id;
        this.bull_tag_number = bull_tag_number;
        this.datetime = datetime;
        this.calves_number = calves_number;
        this.calves = calves;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCow_id() {
        return this.cow_id;
    }

    public void setCow_id(String cow_id) {
        this.cow_id = cow_id;
    }

    public String getCow_tag_number() {
        return this.cow_tag_number;
    }

    public void setCow_tag_number(String cow_tag_number) {
        this.cow_tag_number = cow_tag_number;
    }

    public String getBull_id() {
        return this.bull_id;
    }

    public void setBull_id(String bull_id) {
        this.bull_id = bull_id;
    }

    public String getBull_tag_number() {
        return this.bull_tag_number;
    }

    public void setBull_tag_number(String bull_tag_number) {
        this.bull_tag_number = bull_tag_number;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCalves_number() {
        return this.calves_number;
    }

    public void setCalves_number(String calves_number) {
        this.calves_number = calves_number;
    }

    public String getCalves() {
        return this.calves;
    }

    public void setCalves(String calves) {
        this.calves = calves;
    }
}
