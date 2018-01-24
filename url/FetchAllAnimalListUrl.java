package com.moocall.moocall.url;

public class FetchAllAnimalListUrl extends UrlAbstract {
    public FetchAllAnimalListUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/get-all-animals/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
