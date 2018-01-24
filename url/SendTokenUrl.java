package com.moocall.moocall.url;

public class SendTokenUrl extends UrlAbstract {
    private String phoneUid;
    private String token;

    public SendTokenUrl(String token, String phoneUid) {
        this.token = token;
        this.phoneUid = phoneUid;
        setStringUnsignedPart("/mobile-api/index/index/model/gcm/method/save-token/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.token != null) {
            response = response + "/token/" + this.token;
        }
        if (this.phoneUid != null) {
            return response + "/phone-uid/" + this.phoneUid;
        }
        return response;
    }
}
