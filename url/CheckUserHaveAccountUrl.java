package com.moocall.moocall.url;

public class CheckUserHaveAccountUrl extends UrlAbstract {
    private String version;

    public CheckUserHaveAccountUrl(String version) {
        this.version = version;
        setStringUnsignedPart("/mobile-api/index/index/model/social-users/method/check-user-exist/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.version != null) {
            return response + "/version/" + this.version;
        }
        return response;
    }
}
