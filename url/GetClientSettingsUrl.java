package com.moocall.moocall.url;

public class GetClientSettingsUrl extends UrlAbstract {
    public GetClientSettingsUrl() {
        setStringUnsignedPart("/mobile-api/index/index/model/client/method/get-client-settings/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
