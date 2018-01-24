package com.moocall.moocall.url;

public class GetSocialImageUrl extends UrlAbstract {
    private String url;

    public GetSocialImageUrl(String url) {
        this.url = url;
        setStringUnsignedPart("/mobile-api/index/index/model/social-users/method/get-image/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.url != null) {
            return response + "/url/" + this.url;
        }
        return response;
    }
}
