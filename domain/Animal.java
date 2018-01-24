package com.moocall.moocall.domain;

import android.app.Activity;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Animal implements Serializable {
    private String assumeInCalfDate;
    private Integer avgBirthWeight;
    private String avgGestatationLength;
    private Integer avgOffspringWeight;
    private String avgWeightGainDay;
    private String birthDateServer;
    private String birthDateText;
    private String breed;
    private String bullSensor;
    private Integer calvesNumber;
    private Integer calvesSired;
    private String calvingStatus;
    private String castrated;
    private String conditionAtBirth;
    private Integer cowState;
    private String cycleDate;
    private Integer cyclesNumber;
    private String dam;
    private Integer daysInCalf;
    private Integer daysSinceCycle;
    private Integer daysSinceInsemination;
    private Integer difficulty;
    private String dueDate;
    private String dueDateServer;
    private Integer gestation;
    private List<AnimalHistory> history;
    private Integer id;
    private String imagePath;
    private String inHeatDate;
    private String inseminationBull;
    private String inseminationDate;
    private Integer inseminationNumber;
    private String lastCalving;
    private String moocallTagNumber;
    private String name;
    private List<AnimalNotes> notes;
    private String replacement;
    private String sensor;
    private Boolean showMoreHistory;
    private Boolean showMoreNotes;
    private String sire;
    private Integer state;
    private String stateDate;
    private String stateDateText;
    private String stateServerCetTime;
    private String stateTime;
    private String stateTimeText;
    private String tagNumber;
    private String tagged;
    private String temperament;
    private Integer type;
    private String vendor;
    private String weight;

    public Animal(Integer id, String tagNumber, Integer type, String name) {
        setId(id);
        setTagNumber(tagNumber);
        setType(type);
        setName(name);
    }

    public Animal(JSONParserBgw jsonParserAnimal, Boolean details, Activity activity) {
        try {
            if (details.booleanValue()) {
                int i;
                List<AnimalNotes> tmpNotes = new ArrayList();
                List<AnimalHistory> tmpHistory = new ArrayList();
                JSONArray notesArray = jsonParserAnimal.getJsonArray("notes");
                JSONArray historyArray = jsonParserAnimal.getJsonArray("history");
                for (i = 0; i < notesArray.length(); i++) {
                    tmpNotes.add(new AnimalNotes(new JSONParserBgw((JSONObject) notesArray.get(i))));
                }
                for (i = 0; i < historyArray.length(); i++) {
                    tmpHistory.add(new AnimalHistory(new JSONParserBgw((JSONObject) historyArray.get(i)), activity));
                }
                setHistory(tmpHistory);
                setShowMoreHistory(Boolean.valueOf(false));
                setNotes(tmpNotes);
                setShowMoreNotes(Boolean.valueOf(false));
                JSONParserBgw jsonParserAD = new JSONParserBgw(jsonParserAnimal.getJsonObject("animal_details"));
                setId(jsonParserAD.getInt("id"));
                setType(jsonParserAD.getInt("type"));
                setTagNumber(jsonParserAD.getString("tagNumber"));
                setName(jsonParserAD.getString("name"));
                setWeight(jsonParserAD.getString("weight"));
                setBreed(jsonParserAD.getString("breed"));
                setTemperament(jsonParserAD.getString("temperament"));
                setVendor(jsonParserAD.getString("vendor"));
                setDifficulty(jsonParserAD.getInt("difficulty"));
                setBirthDateServer(jsonParserAD.getString("dateOfBirth"));
                setImagePath(jsonParserAD.getString("imagePath"));
                setInseminationDate(jsonParserAD.getString("last_insemenation"));
                setDaysInCalf(jsonParserAD.getInt("days_in_calf"));
                setDueDateServer(jsonParserAD.getString("due_date"));
                setAvgGestatationLength(jsonParserAD.getString("avg_gestatation_length"));
                setCalvesNumber(jsonParserAD.getInt("calves_number"));
                setAvgBirthWeight(jsonParserAD.getInt("avg_birth_weight"));
                setCycleDate(jsonParserAD.getString("last_cycle_date"));
                setLastCalving(jsonParserAD.getString("last_calving_date"));
                setDaysSinceCycle(jsonParserAD.getInt("days_since_cycle"));
                setCyclesNumber(jsonParserAD.getInt("cycles_number"));
                setCalvesSired(jsonParserAD.getInt("calves_sired"));
                setAvgOffspringWeight(jsonParserAD.getInt("avg_offspring_weight"));
                setReplacement(jsonParserAD.getInt("replacement"), activity);
                setCastrated(jsonParserAD.getInt("castrated"), activity);
                setTagged(jsonParserAD.getInt("dehorned"), activity);
                setDam(jsonParserAD.getString("dam"));
                setSire(jsonParserAD.getString("sire"));
                setAvgWeightGainDay(jsonParserAD.getString("avg_weight_gain_day"));
                setCalvingStatus(jsonParserAD.getString("calving_status"));
                setConditionAtBirth(jsonParserAD.getString("condition_at_birth"));
                setInHeatDate(jsonParserAD.getString("last_heat"));
                setInseminationBull(jsonParserAD.getString("insemenation_bull"));
                setInseminationNumber(jsonParserAD.getInt("insemenation_number"));
                setDaysSinceInsemination(jsonParserAD.getInt("days_since_insemenation"));
                setSensor(jsonParserAD.getString("sensor"));
                setCowState(jsonParserAD.getInt("cow_state"));
                setGestation(jsonParserAD.getInt("gestation"));
                setMoocallTagNumber(jsonParserAD.getString("moocall_tag_number"));
                setBullSensor(jsonParserAD.getString("bull_sensor"));
                return;
            }
            setId(jsonParserAnimal.getInt("id"));
            setType(jsonParserAnimal.getInt("animal_type"));
            setTagNumber(jsonParserAnimal.getString("tag_number"));
            setName(jsonParserAnimal.getString("name"));
            setWeight(jsonParserAnimal.getString("weight"));
            setLastCalving(jsonParserAnimal.getString("last_calving"));
            setAssumeInCalfDate(jsonParserAnimal.getString("assume_in_calf_date"));
            setInseminationDate(jsonParserAnimal.getString("insemenation_date"));
            setInseminationBull(jsonParserAnimal.getString("insemenation_bull"));
            setCycleDate(jsonParserAnimal.getString("cycle_date"));
            setInHeatDate(jsonParserAnimal.getString("in_heat_date"));
            setBirthDateServer(jsonParserAnimal.getString("birth_date"));
            setDueDateServer(jsonParserAnimal.getString("due_date"));
            setImagePath(jsonParserAnimal.getString("imagePath"));
            setSensor(jsonParserAnimal.getString("sensor"));
            setBullSensor(jsonParserAnimal.getString("bull_sensor"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagNumber() {
        return this.tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<AnimalHistory> getHistory() {
        return this.history;
    }

    public void setHistory(List<AnimalHistory> history) {
        this.history = history;
    }

    public Boolean getShowMoreHistory() {
        return this.showMoreHistory;
    }

    public void setShowMoreHistory(Boolean showMoreHistory) {
        this.showMoreHistory = showMoreHistory;
    }

    public List<AnimalNotes> getNotes() {
        return this.notes;
    }

    public void setNotes(List<AnimalNotes> notes) {
        this.notes = notes;
    }

    public Boolean getShowMoreNotes() {
        return this.showMoreNotes;
    }

    public void setShowMoreNotes(Boolean showMoreNotes) {
        this.showMoreNotes = showMoreNotes;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBirthDateServer() {
        return this.birthDateServer;
    }

    public void setBirthDateServer(String birthDateServer) {
        if (birthDateServer.equals("-0001-11-30")) {
            birthDateServer = "0000-00-00";
        }
        this.birthDateServer = birthDateServer;
        if (birthDateServer.length() > 0 && !birthDateServer.equals("0000-00-00")) {
            setBirthDateText(Utils.fromServerToNormal(birthDateServer));
        }
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
        setBirthDateServer(year + "-" + monthText + "-" + dateText);
        setBirthDateText(dateText + "-" + monthText + "-" + year);
    }

    public String getBirthDateText() {
        return this.birthDateText;
    }

    public void setBirthDateText(String birthDateText) {
        this.birthDateText = birthDateText;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLastCalving() {
        return this.lastCalving;
    }

    public void setLastCalving(String lastCalving) {
        this.lastCalving = lastCalving;
    }

    public String getAssumeInCalfDate() {
        return this.assumeInCalfDate;
    }

    public void setAssumeInCalfDate(String assumeInCalfDate) {
        this.assumeInCalfDate = assumeInCalfDate;
    }

    public String getInseminationDate() {
        return this.inseminationDate;
    }

    public void setInseminationDate(String inseminationDate) {
        this.inseminationDate = inseminationDate;
    }

    public String getCycleDate() {
        return this.cycleDate;
    }

    public void setCycleDate(String cycleDate) {
        this.cycleDate = cycleDate;
    }

    public String getInHeatDate() {
        return this.inHeatDate;
    }

    public void setInHeatDate(String inHeatDate) {
        this.inHeatDate = inHeatDate;
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

    public Integer getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDateServer() {
        return this.dueDateServer;
    }

    public void setDueDateServer(String dueDateServer) {
        this.dueDateServer = dueDateServer;
        if (dueDateServer != null && dueDateServer.length() > 0) {
            setDueDate(Utils.fromServerToNormal(dueDateServer));
        }
    }

    public String getSensor() {
        return this.sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public Integer getDaysInCalf() {
        return this.daysInCalf;
    }

    public void setDaysInCalf(Integer daysInCalf) {
        this.daysInCalf = daysInCalf;
    }

    public String getAvgGestatationLength() {
        return this.avgGestatationLength;
    }

    public void setAvgGestatationLength(String avgGestatationLength) {
        this.avgGestatationLength = avgGestatationLength;
    }

    public Integer getCalvesNumber() {
        return this.calvesNumber;
    }

    public void setCalvesNumber(Integer calvesNumber) {
        this.calvesNumber = calvesNumber;
    }

    public Integer getAvgBirthWeight() {
        return this.avgBirthWeight;
    }

    public void setAvgBirthWeight(Integer avgBirthWeight) {
        this.avgBirthWeight = avgBirthWeight;
    }

    public Integer getDaysSinceCycle() {
        return this.daysSinceCycle;
    }

    public void setDaysSinceCycle(Integer daysSinceCycle) {
        this.daysSinceCycle = daysSinceCycle;
    }

    public Integer getCyclesNumber() {
        return this.cyclesNumber;
    }

    public void setCyclesNumber(Integer cyclesNumber) {
        this.cyclesNumber = cyclesNumber;
    }

    public Integer getCalvesSired() {
        return this.calvesSired;
    }

    public void setCalvesSired(Integer calvesSired) {
        this.calvesSired = calvesSired;
    }

    public Integer getAvgOffspringWeight() {
        return this.avgOffspringWeight;
    }

    public void setAvgOffspringWeight(Integer avgOffspringWeight) {
        this.avgOffspringWeight = avgOffspringWeight;
    }

    public String getReplacement() {
        return this.replacement;
    }

    public void setReplacement(Integer replacement, Activity activity) {
        if (activity == null) {
            return;
        }
        if (replacement == null || replacement.intValue() <= 0) {
            this.replacement = activity.getString(C0530R.string.no);
        } else {
            this.replacement = activity.getString(C0530R.string.yes);
        }
    }

    public String getCastrated() {
        return this.castrated;
    }

    public void setCastrated(Integer castrated, Activity activity) {
        if (activity == null) {
            return;
        }
        if (castrated == null || castrated.intValue() <= 0) {
            this.castrated = activity.getString(C0530R.string.no);
        } else {
            this.castrated = activity.getString(C0530R.string.yes);
        }
    }

    public String getTagged() {
        return this.tagged;
    }

    public void setTagged(Integer tagged, Activity activity) {
        if (activity == null) {
            return;
        }
        if (tagged == null || tagged.intValue() <= 0) {
            this.tagged = activity.getString(C0530R.string.no);
        } else {
            this.tagged = activity.getString(C0530R.string.yes);
        }
    }

    public String getAvgWeightGainDay() {
        return this.avgWeightGainDay;
    }

    public void setAvgWeightGainDay(String avgWeightGainDay) {
        this.avgWeightGainDay = avgWeightGainDay;
    }

    public String getDam() {
        return this.dam;
    }

    public void setDam(String dam) {
        this.dam = dam;
    }

    public String getSire() {
        return this.sire;
    }

    public void setSire(String sire) {
        this.sire = sire;
    }

    public String getCalvingStatus() {
        return this.calvingStatus;
    }

    public void setCalvingStatus(String calvingStatus) {
        this.calvingStatus = calvingStatus;
    }

    public String getConditionAtBirth() {
        return this.conditionAtBirth;
    }

    public void setConditionAtBirth(String conditionAtBirth) {
        this.conditionAtBirth = conditionAtBirth;
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
        setStateServerCetTime(getStateDate() + StringUtils.SPACE + getStateTime());
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

    public String getStateServerCetTime() {
        return this.stateServerCetTime;
    }

    public void setStateServerCetTime(String serverCetTime) {
        String[] parts = Utils.calculateCetTime(serverCetTime).split(StringUtils.SPACE);
        this.stateServerCetTime = parts[0] + "_" + parts[1];
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getInseminationBull() {
        return this.inseminationBull;
    }

    public void setInseminationBull(String inseminationBull) {
        this.inseminationBull = inseminationBull;
    }

    public Integer getInseminationNumber() {
        return this.inseminationNumber;
    }

    public void setInseminationNumber(Integer inseminationNumber) {
        this.inseminationNumber = inseminationNumber;
    }

    public Integer getDaysSinceInsemination() {
        return this.daysSinceInsemination;
    }

    public void setDaysSinceInsemination(Integer daysSinceInsemination) {
        this.daysSinceInsemination = daysSinceInsemination;
    }

    public Integer getCowState() {
        return this.cowState;
    }

    public void setCowState(Integer cowState) {
        this.cowState = cowState;
    }

    public Integer getGestation() {
        return this.gestation;
    }

    public void setGestation(Integer gestation) {
        this.gestation = gestation;
    }

    public String getMoocallTagNumber() {
        return this.moocallTagNumber;
    }

    public void setMoocallTagNumber(String moocallTagNumber) {
        this.moocallTagNumber = moocallTagNumber;
    }

    public String getBullSensor() {
        return this.bullSensor;
    }

    public void setBullSensor(String bullSensor) {
        this.bullSensor = bullSensor;
    }
}
