package com.moocall.moocall.db;

import com.moocall.moocall.util.JSONParserBgw;

public class SensorDb {
    private String device_code;
    private Long id;
    private int type;

    public SensorDb(Long id) {
        this.id = id;
    }

    public SensorDb(JSONParserBgw jsonParserSensor) {
        this.device_code = jsonParserSensor.getString("device_code");
        this.type = jsonParserSensor.getInt("type").intValue();
    }

    public SensorDb(Long id, String device_code, int type) {
        this.id = id;
        this.device_code = device_code;
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice_code() {
        return this.device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
