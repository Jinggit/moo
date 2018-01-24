package com.moocall.moocall.domain;

import java.io.Serializable;
import java.util.List;

public class Phone implements Serializable {
    private List<Integer> deviceIds;
    private Integer id;
    private Integer idClient;
    private String name;
    private String phoneNumber;

    public Phone(Integer id, Integer idClient, String name, String phoneNumber, List<Integer> deviceIds) {
        setId(id);
        setIdClient(idClient);
        setName(name);
        setPhoneNumber(phoneNumber);
        setDeviceIds(deviceIds);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdClient() {
        return this.idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Integer> getDeviceIds() {
        return this.deviceIds;
    }

    public void setDeviceIds(List<Integer> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String toString() {
        return "Phone{id=" + this.id + ", idClient=" + this.idClient + ", name='" + this.name + '\'' + ", phoneNumber='" + this.phoneNumber + '\'' + ", deviceIds=" + this.deviceIds + '}';
    }
}
