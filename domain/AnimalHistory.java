package com.moocall.moocall.domain;

import android.app.Activity;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class AnimalHistory implements Serializable {
    private Integer calvingId;
    private Integer calvingNumber;
    private String date;
    private String deadWeight;
    private String fatScore;
    private String grade;
    private Integer id;
    private String name;
    private String sensor;
    private String soldPrice;
    private String soldWeight;
    private String tagNumber;
    private String text;
    private String time;
    private Integer type;

    public AnimalHistory(JSONParserBgw jsonParserHistory, Activity activity) {
        String text = "";
        setName(jsonParserHistory.getString("history_name"));
        setId(jsonParserHistory.getInt("id_history"));
        setFatScore(jsonParserHistory.getString("fat_score"));
        setGrade(jsonParserHistory.getString("grade"));
        setDeadWeight(jsonParserHistory.getString("dead_weight"));
        setSoldWeight(jsonParserHistory.getString("sold_weight"));
        setSoldPrice(jsonParserHistory.getString("sold_price"));
        setSensor(jsonParserHistory.getString("sensor"));
        setCalvingId(jsonParserHistory.getInt("id_calving"));
        setCalvingNumber(jsonParserHistory.getInt("twin_number"));
        setTagNumber(jsonParserHistory.getString("tag_number"));
        Integer historyType = jsonParserHistory.getInt("history_type");
        String[] datetime = Utils.calculateTime(jsonParserHistory.getString("value_datetime"), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE);
        if (datetime.length > 1) {
            setDate(datetime[0]);
            setTime(datetime[1]);
        }
        setType(historyType);
        switch (historyType.intValue()) {
            case 0:
                text = activity.getString(C0530R.string.added_calving_sensor);
                break;
            case 1:
                text = jsonParserHistory.getString("born_description");
                break;
            case 2:
                text = Utils.getWeightText(jsonParserHistory.getString("type_value"), activity);
                break;
            case 3:
                text = jsonParserHistory.getString("heat_description");
                break;
            case 4:
                text = jsonParserHistory.getString("insemenation_bull_name") + StringUtils.SPACE + jsonParserHistory.getString("insemenation_bull_tag");
                break;
            case 5:
                text = activity.getString(C0530R.string.keep_as_replacement);
                break;
            case 8:
                text = jsonParserHistory.getString("decreased_description");
                break;
            case 9:
                text = jsonParserHistory.getString("sold_description");
                break;
            case 10:
                text = jsonParserHistory.getString("assume_incalf_description");
                break;
            case 12:
                text = activity.getString(C0530R.string.tagged_dehorned);
                break;
            case 13:
                text = activity.getString(C0530R.string.castrated);
                break;
            case 14:
                text = jsonParserHistory.getString("notes");
                break;
            case 17:
                text = activity.getString(C0530R.string.added_bull_sensor);
                break;
        }
        setText(text);
    }

    public AnimalHistory(AnimalActionDb animalAction, Activity activity) {
        String text = "";
        setFatScore(animalAction.getFat_score());
        setGrade(animalAction.getGrade());
        setDeadWeight(animalAction.getDead_weight());
        setSoldWeight(animalAction.getWeight());
        setSoldPrice(animalAction.getPrice());
        setSensor(animalAction.getSensor());
        setTagNumber(animalAction.getAnimal_tag_number());
        Integer historyType = animalAction.getAction();
        String[] datetime = Utils.calculateTime(animalAction.getDatetime(), "dd-MM-yyyy HH:mm").split(StringUtils.SPACE);
        if (datetime.length > 1) {
            setDate(datetime[0]);
            setTime(datetime[1]);
        }
        setType(historyType);
        switch (historyType.intValue()) {
            case 0:
                setName(activity.getString(C0530R.string.sensor));
                text = activity.getString(C0530R.string.added_calving_sensor);
                break;
            case 2:
                setName(activity.getString(C0530R.string.weight));
                text = Utils.getWeightText(animalAction.getWeight(), activity);
                break;
            case 3:
                setName(activity.getString(C0530R.string.heat));
                text = animalAction.getDescription();
                break;
            case 4:
                setName(activity.getString(C0530R.string.inseminated));
                text = animalAction.getBull_tag_number();
                break;
            case 5:
                setName(activity.getString(C0530R.string.replaced));
                text = activity.getString(C0530R.string.keep_as_replacement);
                break;
            case 6:
                setName(activity.getString(C0530R.string.weaning));
                break;
            case 7:
                setName(activity.getString(C0530R.string.slaughtered));
                break;
            case 8:
                setName(activity.getString(C0530R.string.deceased));
                text = animalAction.getDescription();
                break;
            case 9:
                setName(activity.getString(C0530R.string.sold));
                text = animalAction.getDescription();
                break;
            case 10:
                setName(activity.getString(C0530R.string.incalf));
                text = animalAction.getDescription();
                break;
            case 11:
                setName(activity.getString(C0530R.string.undo));
                break;
            case 12:
                break;
            case 13:
                setName(activity.getString(C0530R.string.castrated));
                text = activity.getString(C0530R.string.castrated);
                break;
            case 14:
                setName(activity.getString(C0530R.string.notes));
                text = animalAction.getText();
                break;
            case 17:
                setName(activity.getString(C0530R.string.sensor));
                text = activity.getString(C0530R.string.added_bull_sensor);
                break;
        }
        setName(activity.getString(C0530R.string.tagged_dehorned));
        text = activity.getString(C0530R.string.tagged_dehorned);
        setText(text);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFatScore() {
        return this.fatScore;
    }

    public void setFatScore(String fatScore) {
        this.fatScore = fatScore;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDeadWeight() {
        return this.deadWeight;
    }

    public void setDeadWeight(String deadWeight) {
        this.deadWeight = deadWeight;
    }

    public String getSoldWeight() {
        return this.soldWeight;
    }

    public void setSoldWeight(String soldWeight) {
        this.soldWeight = soldWeight;
    }

    public String getSoldPrice() {
        return this.soldPrice;
    }

    public void setSoldPrice(String soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getSensor() {
        return this.sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public Integer getCalvingId() {
        return this.calvingId;
    }

    public void setCalvingId(Integer calvingId) {
        this.calvingId = calvingId;
    }

    public Integer getCalvingNumber() {
        return this.calvingNumber;
    }

    public void setCalvingNumber(Integer calvingNumber) {
        this.calvingNumber = calvingNumber;
    }

    public String getTagNumber() {
        return this.tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }
}
