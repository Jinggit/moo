package com.moocall.moocall.url;

public class FetchSmartListUrl extends UrlAbstract {
    public FetchSmartListUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/fetch-smart-list/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
