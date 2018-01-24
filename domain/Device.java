package com.moocall.moocall.domain;

import java.io.Serializable;
import java.util.List;

public class Device implements Serializable {
    private Integer assignedPhones;
    private Integer battery;
    private Integer charging;
    private String code;
    private DeviceMessages deviceMessages;
    private Integer fwstatus;
    private String fwversion;
    private Integer gsm;
    private Integer id;
    private Boolean inactive;
    private String lastBeat;
    private String lastTime;
    private String name;
    private List<Phone> phones;
    private String renewal;
    private Integer sensitivity;
    private String warranty;

    public Device(Integer id, Integer sensitivity, Integer battery, String name, Integer charging, Integer gsm, String code, String lastBeat, String lastTime, List<Phone> phones, String fwversion, Integer fwstatus, DeviceMessages deviceMessages, String renewal, String warranty, Boolean inactive) {
        setId(id);
        setSensitivity(sensitivity);
        setBattery(battery);
        setName(name);
        setCharging(charging);
        setGsm(gsm);
        setCode(code);
        setLastBeat(lastBeat);
        setLastTime(lastTime);
        setPhones(phones);
        setAssignedPhones(Integer.valueOf(phones.size()));
        setFwversion(fwversion);
        setFwstatus(fwstatus);
        setDeviceMessages(deviceMessages);
        setRenewal(renewal);
        setWarranty(warranty);
        setInactive(inactive);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSensitivity() {
        return this.sensitivity;
    }

    public void setSensitivity(Integer sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Integer getBattery() {
        return this.battery;
    }

    public void setBattery(Integer battery) {
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

    public Integer getGsm() {
        return this.gsm;
    }

    public void setGsm(Integer gsm) {
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

    public List<Phone> getPhones() {
        return this.phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getAssignedPhones() {
        return this.assignedPhones;
    }

    public void setAssignedPhones(Integer assignedPhones) {
        this.assignedPhones = assignedPhones;
    }

    public void assignPhone() {
        Integer num = this.assignedPhones;
        this.assignedPhones = Integer.valueOf(this.assignedPhones.intValue() + 1);
    }

    public void unassignPhone() {
        Integer num = this.assignedPhones;
        this.assignedPhones = Integer.valueOf(this.assignedPhones.intValue() - 1);
    }

    public String getFwversion() {
        return this.fwversion;
    }

    public void setFwversion(String fwversion) {
        this.fwversion = fwversion;
    }

    public Integer getFwstatus() {
        return this.fwstatus;
    }

    public void setFwstatus(Integer fwstatus) {
        this.fwstatus = fwstatus;
    }

    public DeviceMessages getDeviceMessages() {
        return this.deviceMessages;
    }

    public void setDeviceMessages(DeviceMessages deviceMessages) {
        this.deviceMessages = deviceMessages;
    }

    public String getRenewal() {
        return this.renewal;
    }

    public void setRenewal(String renewal) {
        if (renewal.isEmpty()) {
            this.renewal = "N/A";
        } else {
            this.renewal = renewal;
        }
    }

    public String getWarranty() {
        return this.warranty;
    }

    public void setWarranty(String warranty) {
        if (warranty.isEmpty()) {
            this.warranty = "N/A";
        } else {
            this.warranty = warranty;
        }
    }

    public Boolean getInactive() {
        return this.inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public String toString() {
        return "Device{id=" + this.id + ", sensitivity=" + this.sensitivity + ", battery=" + this.battery + ", name='" + this.name + '\'' + ", charging=" + this.charging + ", gsm=" + this.gsm + ", code='" + this.code + '\'' + ", lastBeat='" + this.lastBeat + '\'' + ", phones=" + this.phones + ", lastTime='" + this.lastTime + '\'' + ", assignedPhones=" + this.assignedPhones + '}';
    }
}
