package com.moocall.moocall.url;

public class FetchHerdOverviewUrl extends UrlAbstract {
    public FetchHerdOverviewUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/herd-overview/method/fetch-herd-overview/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
