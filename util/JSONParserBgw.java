package com.moocall.moocall.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParserBgw {
    private JSONObject jb;

    public JSONParserBgw(JSONObject jb) {
        this.jb = jb;
    }

    public String getString(String propertyName) {
        try {
            if (this.jb.has(propertyName)) {
                return this.jb.getString(propertyName);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public Integer getInt(String propertyName) {
        try {
            if (this.jb.has(propertyName)) {
                return Integer.valueOf(this.jb.getInt(propertyName));
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public Double getDouble(String propertyName) {
        try {
            if (this.jb.has(propertyName)) {
                return Double.valueOf(this.jb.getDouble(propertyName));
            }
        } catch (JSONException e) {
        }
        return Double.valueOf(0.0d);
    }

    public Boolean getBoolean(String propertyName) {
        try {
            if (this.jb.has(propertyName)) {
                return Boolean.valueOf(this.jb.getBoolean(propertyName));
            }
        } catch (JSONException e) {
        }
        return Boolean.valueOf(false);
    }

    public JSONArray getJsonArray(String propertyName) {
        try {
            if (this.jb.has(propertyName)) {
                return this.jb.getJSONArray(propertyName);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public JSONObject getJsonObject(String propertyName) {
        try {
            if (this.jb.has(propertyName)) {
                return this.jb.getJSONObject(propertyName);
            }
        } catch (JSONException e) {
        }
        return null;
    }
}
