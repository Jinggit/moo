package com.moocall.moocall.url;

public class UserLoginUrl extends UrlAbstract {
    private String version;

    public UserLoginUrl(String version) {
        this.version = version;
        setStringUnsignedPart("/mobile-api/index/index/model/account/method/mobile-login/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.version != null) {
            return response + "/version/" + this.version;
        }
        return response;
    }
}
