package com.moocall.moocall.url;

public class FetchStatisticsUrl extends UrlAbstract {
    public FetchStatisticsUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/smart-statistics/method/fetch-statistics/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
