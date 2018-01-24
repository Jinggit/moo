package com.moocall.moocall.db;

import com.moocall.moocall.util.Utils;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class AnimalActionDb implements Serializable {
    private Integer action;
    private String animal_id;
    private String animal_tag_number;
    private String animal_type;
    private String bull_id;
    private String bull_tag_number;
    private String cow_id;
    private String cow_tag_number;
    private String date;
    private String datetime;
    private String dead_weight;
    private String description;
    private String fat_score;
    private String grade;
    private Long id;
    private String moocall_tag_number;
    private String price;
    private String sensor;
    private String text;
    private String time;
    private String weaned;
    private String weight;

    public AnimalActionDb(Long id) {
        this.id = id;
    }

    public AnimalActionDb(Long id, Integer action, String animal_id, String animal_tag_number, String animal_type, String sensor, String weight, String datetime, String description, String bull_tag_number, String bull_id, String weaned, String fat_score, String grade, String dead_weight, String price, String text, String cow_tag_number, String cow_id, String moocall_tag_number) {
        this.id = id;
        this.action = action;
        this.animal_id = animal_id;
        this.animal_tag_number = animal_tag_number;
        this.animal_type = animal_type;
        this.sensor = sensor;
        this.weight = weight;
        this.datetime = datetime;
        this.description = description;
        this.bull_tag_number = bull_tag_number;
        this.bull_id = bull_id;
        this.weaned = weaned;
        this.fat_score = fat_score;
        this.grade = grade;
        this.dead_weight = dead_weight;
        this.price = price;
        this.text = text;
        this.cow_tag_number = cow_tag_number;
        this.cow_id = cow_id;
        this.moocall_tag_number = moocall_tag_number;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAction() {
        return this.action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getAnimal_id() {
        return this.animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getAnimal_tag_number() {
        return this.animal_tag_number;
    }

    public void setAnimal_tag_number(String animal_tag_number) {
        this.animal_tag_number = animal_tag_number;
    }

    public String getAnimal_type() {
        return this.animal_type;
    }

    public void setAnimal_type(String animal_type) {
        this.animal_type = animal_type;
    }

    public String getSensor() {
        return this.sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
        String[] datetimeArray = Utils.calculateTime(datetime, "dd-MM-yyyy HH:mm").split(StringUtils.SPACE);
        if (datetimeArray.length > 1) {
            setDate(datetimeArray[0]);
            setTime(datetimeArray[1]);
            return;
        }
        setDate("");
        setTime("");
    }

    public String getDate() {
        if (this.date == null) {
            String[] datetimeArray = Utils.calculateTime(getDatetime(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE);
            if (datetimeArray.length > 1) {
                setDate(datetimeArray[0]);
                setTime(datetimeArray[1]);
            } else {
                setDate("");
                setTime("");
            }
        }
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        if (this.time == null) {
            String[] datetimeArray = Utils.calculateTime(getDatetime(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE);
            if (datetimeArray.length > 1) {
                setDate(datetimeArray[0]);
                setTime(datetimeArray[1]);
            } else {
                setDate("");
                setTime("");
            }
        }
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBull_tag_number() {
        return this.bull_tag_number;
    }

    public void setBull_tag_number(String bull_tag_number) {
        this.bull_tag_number = bull_tag_number;
    }

    public String getBull_id() {
        return this.bull_id;
    }

    public void setBull_id(String bull_id) {
        this.bull_id = bull_id;
    }

    public String getWeaned() {
        return this.weaned;
    }

    public void setWeaned(String weaned) {
        this.weaned = weaned;
    }

    public String getFat_score() {
        return this.fat_score;
    }

    public void setFat_score(String fat_score) {
        this.fat_score = fat_score;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDead_weight() {
        return this.dead_weight;
    }

    public void setDead_weight(String dead_weight) {
        this.dead_weight = dead_weight;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCow_tag_number() {
        return this.cow_tag_number;
    }

    public void setCow_tag_number(String cow_tag_number) {
        this.cow_tag_number = cow_tag_number;
    }

    public String getCow_id() {
        return this.cow_id;
    }

    public void setCow_id(String cow_id) {
        this.cow_id = cow_id;
    }

    public String getShowText() {
        return getDate() + " - " + getText();
    }

    public String getMoocall_tag_number() {
        return this.moocall_tag_number;
    }

    public void setMoocall_tag_number(String moocall_tag_number) {
        this.moocall_tag_number = moocall_tag_number;
    }
}
