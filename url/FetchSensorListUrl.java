package com.moocall.moocall.url;

public class FetchSensorListUrl extends UrlAbstract {
    public FetchSensorListUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/fetch-sensor-list/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
