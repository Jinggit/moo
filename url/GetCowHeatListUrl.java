package com.moocall.moocall.url;

public class GetCowHeatListUrl extends UrlAbstract {
    public GetCowHeatListUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/animal-history/method/get-cow-heat-list/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
