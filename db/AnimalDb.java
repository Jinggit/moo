package com.moocall.moocall.db;

import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class AnimalDb implements Serializable {
    private Integer animal_id;
    private String birthDateText;
    private String birth_date;
    private String breed;
    private String bull_sensor;
    private String cycle_date;
    private String date_slaughtered;
    private String date_sold;
    private Integer deleted_animal;
    private Integer died;
    private String due_date;
    private Integer gestation;
    private Long id;
    private String in_heat_date;
    private String insemenation_bull;
    private String insemination_date;
    private String last_calving_date;
    private Integer last_state;
    private String moocall_tag_number;
    private String name;
    private Integer new_animal;
    private String sensor;
    private String server_state_cet_time;
    private Integer slaughtered;
    private Integer sold;
    private Integer state;
    private String stateDate;
    private String stateDateText;
    private String stateTime;
    private String stateTimeText;
    private Integer status;
    private String tag_number;
    private String temperament;
    private String time_update;
    private Integer type;
    private Integer updated_animal;
    private String vendor;
    private String weight;

    public AnimalDb(Long id) {
        this.id = id;
    }

    public AnimalDb(JSONParserBgw jsonParserAnimal) {
        setAnimal_id(jsonParserAnimal.getInt("id"));
        setTag_number(jsonParserAnimal.getString("tag_number"));
        setName(jsonParserAnimal.getString("name"));
        setType(jsonParserAnimal.getInt("type"));
        setTime_update(jsonParserAnimal.getString("time_update"));
        setBirth_date(jsonParserAnimal.getString("birth_date"));
        setWeight(jsonParserAnimal.getString("weight"));
        setInsemination_date(jsonParserAnimal.getString("insemination_date"));
        setCycle_date(jsonParserAnimal.getString("cycle_date"));
        setIn_heat_date(jsonParserAnimal.getString("in_heat_date"));
        setDue_date(jsonParserAnimal.getString("due_date"));
        setLast_calving_date(jsonParserAnimal.getString("last_calving_date"));
        setSensor(jsonParserAnimal.getString("sensor"));
        setBreed(jsonParserAnimal.getString("breed"));
        setTemperament(jsonParserAnimal.getString("temperament"));
        setVendor(jsonParserAnimal.getString("vendor"));
        setGestation(jsonParserAnimal.getInt("gestation"));
        setLast_state(jsonParserAnimal.getInt("last_state"));
        setMoocall_tag_number(jsonParserAnimal.getString("moocall_tag_number"));
        setStatus(jsonParserAnimal.getInt("status"));
        setSold(jsonParserAnimal.getInt("sold"));
        setSlaughtered(jsonParserAnimal.getInt("slaughtered"));
        setDied(jsonParserAnimal.getInt("died"));
        setDate_slaughtered(jsonParserAnimal.getString("date_slaughtered"));
        setDate_sold(jsonParserAnimal.getString("date_sold"));
        setInsemenation_bull(jsonParserAnimal.getString("insemenation_bull"));
        setBull_sensor(jsonParserAnimal.getString("bull_sensor"));
        setNew_animal(Integer.valueOf(0));
        setUpdated_animal(Integer.valueOf(0));
        setDeleted_animal(Integer.valueOf(0));
    }

    public AnimalDb(Long id, Integer animal_id, String tag_number, String name, Integer type, String time_update, String birth_date, String weight, String insemination_date, String cycle_date, String in_heat_date, String due_date, String last_calving_date, String sensor, String breed, String temperament, String vendor, Integer gestation, Integer last_state, String moocall_tag_number, Integer status, Integer sold, Integer slaughtered, Integer died, String date_sold, String date_slaughtered, String insemenation_bull, Integer new_animal, Integer updated_animal, Integer deleted_animal, String bull_sensor, Integer state, String server_state_cet_time) {
        this.id = id;
        this.animal_id = animal_id;
        this.tag_number = tag_number;
        this.name = name;
        this.type = type;
        this.time_update = time_update;
        this.birth_date = birth_date;
        this.weight = weight;
        this.insemination_date = insemination_date;
        this.cycle_date = cycle_date;
        this.in_heat_date = in_heat_date;
        this.due_date = due_date;
        this.last_calving_date = last_calving_date;
        this.sensor = sensor;
        this.breed = breed;
        this.temperament = temperament;
        this.vendor = vendor;
        this.gestation = gestation;
        this.last_state = last_state;
        this.moocall_tag_number = moocall_tag_number;
        this.status = status;
        this.sold = sold;
        this.slaughtered = slaughtered;
        this.died = died;
        this.date_sold = date_sold;
        this.date_slaughtered = date_slaughtered;
        this.insemenation_bull = insemenation_bull;
        this.new_animal = new_animal;
        this.updated_animal = updated_animal;
        this.deleted_animal = deleted_animal;
        this.bull_sensor = bull_sensor;
        this.state = state;
        this.server_state_cet_time = server_state_cet_time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnimal_id() {
        return this.animal_id;
    }

    public void setAnimal_id(Integer animal_id) {
        this.animal_id = animal_id;
    }

    public String getTag_number() {
        return this.tag_number;
    }

    public void setTag_number(String tag_number) {
        this.tag_number = tag_number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTime_update() {
        return this.time_update;
    }

    public void setTime_update(String time_update) {
        this.time_update = time_update;
    }

    public String getBirth_date() {
        return this.birth_date;
    }

    public void setBirth_date(String birth_date) {
        if (birth_date.equals("-0001-11-30")) {
            birth_date = "0000-00-00";
        }
        this.birth_date = birth_date;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInsemination_date() {
        return this.insemination_date;
    }

    public void setInsemination_date(String insemination_date) {
        this.insemination_date = insemination_date;
    }

    public String getCycle_date() {
        return this.cycle_date;
    }

    public void setCycle_date(String cycle_date) {
        this.cycle_date = cycle_date;
    }

    public String getIn_heat_date() {
        return this.in_heat_date;
    }

    public void setIn_heat_date(String in_heat_date) {
        this.in_heat_date = in_heat_date;
    }

    public String getDue_date() {
        return this.due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getLast_calving_date() {
        return this.last_calving_date;
    }

    public void setLast_calving_date(String last_calving_date) {
        this.last_calving_date = last_calving_date;
    }

    public String getSensor() {
        return this.sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getBreed() {
        return this.breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getTemperament() {
        return this.temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Integer getGestation() {
        return this.gestation;
    }

    public void setGestation(Integer gestation) {
        this.gestation = gestation;
    }

    public Integer getLast_state() {
        return this.last_state;
    }

    public void setLast_state(Integer last_state) {
        this.last_state = last_state;
    }

    public String getMoocall_tag_number() {
        return this.moocall_tag_number;
    }

    public void setMoocall_tag_number(String moocall_tag_number) {
        this.moocall_tag_number = moocall_tag_number;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSold() {
        return this.sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getSlaughtered() {
        return this.slaughtered;
    }

    public void setSlaughtered(Integer slaughtered) {
        this.slaughtered = slaughtered;
    }

    public Integer getDied() {
        return this.died;
    }

    public void setDied(Integer died) {
        this.died = died;
    }

    public String getDate_sold() {
        return this.date_sold;
    }

    public void setDate_sold(String date_sold) {
        this.date_sold = date_sold;
    }

    public String getDate_slaughtered() {
        return this.date_slaughtered;
    }

    public void setDate_slaughtered(String date_slaughtered) {
        this.date_slaughtered = date_slaughtered;
    }

    public String getInsemenation_bull() {
        return this.insemenation_bull;
    }

    public void setInsemenation_bull(String insemenation_bull) {
        this.insemenation_bull = insemenation_bull;
    }

    public Integer getNew_animal() {
        return this.new_animal;
    }

    public void setNew_animal(Integer new_animal) {
        this.new_animal = new_animal;
    }

    public Integer getUpdated_animal() {
        return this.updated_animal;
    }

    public void setUpdated_animal(Integer updated_animal) {
        this.updated_animal = updated_animal;
    }

    public Integer getDeleted_animal() {
        return this.deleted_animal;
    }

    public void setDeleted_animal(Integer deleted_animal) {
        this.deleted_animal = deleted_animal;
    }

    public String getBull_sensor() {
        return this.bull_sensor;
    }

    public void setBull_sensor(String bull_sensor) {
        this.bull_sensor = bull_sensor;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getServer_state_cet_time() {
        return this.server_state_cet_time;
    }

    public void setServer_state_cet_time(String serverCetTime) {
        String[] parts = Utils.calculateCetTime(serverCetTime).split(StringUtils.SPACE);
        this.server_state_cet_time = parts[0] + "_" + parts[1];
    }

    public String getBirthDateText() {
        if (this.birthDateText == null && this.birth_date.length() > 0 && !this.birth_date.equals("0000-00-00")) {
            setBirthDateText(Utils.fromServerToNormal(this.birth_date));
        }
        return this.birthDateText;
    }

    public void setBirthDateText(String birthDateText) {
        this.birthDateText = birthDateText;
    }

    public void setBirthDate(int year, int month, int day) {
        String monthText = "";
        String dateText = "";
        if (day < 9) {
            dateText = "0" + day;
        } else {
            dateText = day + "";
        }
        if (month < 9) {
            monthText = "0" + (month + 1);
        } else {
            monthText = (month + 1) + "";
        }
        setBirth_date(year + "-" + monthText + "-" + dateText);
        setBirthDateText(dateText + "-" + monthText + "-" + year);
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
        setStateDate(year + "-" + monthText + "-" + dateText);
        setStateDateText(dateText + "-" + monthText + "-" + year);
        setStateTime(hourText + ":" + minuteText + ":00");
        setStateTimeText(hourText + ":" + minuteText);
        setServer_state_cet_time(getStateDate() + StringUtils.SPACE + getStateTime());
    }

    public String getStateDate() {
        return this.stateDate;
    }

    public void setStateDate(String stateDate) {
        this.stateDate = stateDate;
    }

    public String getStateTime() {
        return this.stateTime;
    }

    public void setStateTime(String stateTime) {
        this.stateTime = stateTime;
    }

    public String getStateDateText() {
        return this.stateDateText;
    }

    public void setStateDateText(String stateDateText) {
        this.stateDateText = stateDateText;
    }

    public String getStateTimeText() {
        return this.stateTimeText;
    }

    public void setStateTimeText(String stateTimeText) {
        this.stateTimeText = stateTimeText;
    }
}
