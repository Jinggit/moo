package com.moocall.moocall.url;

public class FetchCalvingListUrl extends UrlAbstract {
    public FetchCalvingListUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/cow/method/fetch-calving-list/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
