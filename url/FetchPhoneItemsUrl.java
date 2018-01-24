package com.moocall.moocall.url;

public class FetchPhoneItemsUrl extends UrlAbstract {
    public FetchPhoneItemsUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/phone-number/method/fetch-phone-items/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
