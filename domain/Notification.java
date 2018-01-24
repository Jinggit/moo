package com.moocall.moocall.domain;

import com.moocall.moocall.util.JSONParserBgw;
import com.moocall.moocall.util.Utils;
import java.io.Serializable;

public class Notification implements Serializable {
    private String battery;
    private Integer batteryMessage;
    private String calvingTimer;
    private Integer charging;
    private Integer clientId;
    private String code;
    private String cowId;
    private Integer descriptionCode;
    private Integer deviceId;
    private String fromTime;
    private String gsm;
    private String id;
    private Integer idMessageTypeCalving;
    private String lastBeat;
    private Integer messageGroup;
    private Integer messageType;
    private Integer messageTypeBull;
    private String name;
    private Integer sensitivity;
    private String timeWithBull;
    private String toTime;

    public Notification(JSONParserBgw jsonParserNotification) {
        setId(jsonParserNotification.getString("id"));
        setSensitivity(jsonParserNotification.getInt("sensitivity"));
        setBattery(jsonParserNotification.getString("battery"));
        setName(jsonParserNotification.getString("name"));
        setCharging(jsonParserNotification.getInt("charging"));
        setGsm(jsonParserNotification.getString("gsm"));
        setCode(jsonParserNotification.getString("code"));
        setLastBeat(Utils.calculateTime(jsonParserNotification.getString("last_beat"), "yyyy-MM-dd HH:mm"));
        setIdMessageTypeCalving(jsonParserNotification.getInt("id_message_type_calving"));
        setDeviceId(jsonParserNotification.getInt("device_id"));
        setDescriptionCode(jsonParserNotification.getInt("description"));
        setCalvingTimer(Utils.calculateTime(jsonParserNotification.getString("calving_timer"), "yyyy-MM-dd HH:mm"));
        setClientId(jsonParserNotification.getInt("client_id"));
        setMessageType(jsonParserNotification.getInt("message_type"));
        setMessageGroup(jsonParserNotification.getInt("message_group"));
        setBatteryMessage(jsonParserNotification.getInt("battery_message"));
        setMessageTypeBull(jsonParserNotification.getInt("message_type_bull"));
        setCowId(jsonParserNotification.getString("cow_id"));
        setTimeWithBull(jsonParserNotification.getString("time_with_bull"));
        setFromTime(jsonParserNotification.getString("from_time"));
        setToTime(jsonParserNotification.getString("to_time"));
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSensitivity() {
        return this.sensitivity;
    }

    public void setSensitivity(Integer sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getBattery() {
        return this.battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCharging() {
        return this.charging;
    }

    public void setCharging(Integer charging) {
        this.charging = charging;
    }

    public String getGsm() {
        return this.gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLastBeat() {
        return this.lastBeat;
    }

    public void setLastBeat(String lastBeat) {
        this.lastBeat = lastBeat;
    }

    public Integer getIdMessageTypeCalving() {
        return this.idMessageTypeCalving;
    }

    public void setIdMessageTypeCalving(Integer idMessageTypeCalving) {
        this.idMessageTypeCalving = idMessageTypeCalving;
    }

    public Integer getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getDescriptionCode() {
        return this.descriptionCode;
    }

    public void setDescriptionCode(Integer descriptionCode) {
        this.descriptionCode = descriptionCode;
    }

    public String getCalvingTimer() {
        return this.calvingTimer;
    }

    public void setCalvingTimer(String calvingTimer) {
        this.calvingTimer = calvingTimer;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getMessageType() {
        return this.messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getMessageGroup() {
        return this.messageGroup;
    }

    public void setMessageGroup(Integer messageGroup) {
        this.messageGroup = messageGroup;
    }

    public Integer getBatteryMessage() {
        return this.batteryMessage;
    }

    public void setBatteryMessage(Integer batteryMessage) {
        this.batteryMessage = batteryMessage;
    }

    public Integer getMessageTypeBull() {
        return this.messageTypeBull;
    }

    public void setMessageTypeBull(Integer messageTypeBull) {
        this.messageTypeBull = messageTypeBull;
    }

    public String getCowId() {
        return this.cowId;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public String getTimeWithBull() {
        return this.timeWithBull;
    }

    public void setTimeWithBull(String timeWithBull) {
        this.timeWithBull = timeWithBull;
    }

    public String getFromTime() {
        return this.fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return this.toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String toString() {
        return "Notification{id='" + this.id + '\'' + ", sensitivity=" + this.sensitivity + ", battery='" + this.battery + '\'' + ", name='" + this.name + '\'' + ", charging=" + this.charging + ", gsm='" + this.gsm + '\'' + ", code='" + this.code + '\'' + ", lastBeat='" + this.lastBeat + '\'' + ", idMessageTypeCalving=" + this.idMessageTypeCalving + ", deviceId=" + this.deviceId + ", descriptionCode=" + this.descriptionCode + ", calvingTimer='" + this.calvingTimer + '\'' + ", clientId=" + this.clientId + ", messageType=" + this.messageType + '}';
    }
}
