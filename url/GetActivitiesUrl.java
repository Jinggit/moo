package com.moocall.moocall.url;

public class GetActivitiesUrl extends UrlAbstract {
    public GetActivitiesUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/animal-history/method/get-recent-animal-history/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
