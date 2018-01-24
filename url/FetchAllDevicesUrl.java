package com.moocall.moocall.url;

public class FetchAllDevicesUrl extends UrlAbstract {
    public FetchAllDevicesUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/device/method/fetch-all-devices/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
