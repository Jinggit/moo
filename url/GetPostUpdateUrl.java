package com.moocall.moocall.url;

public class GetPostUpdateUrl extends UrlAbstract {
    private String lastFetchTime;

    public GetPostUpdateUrl(String lastFetchTime) {
        this.lastFetchTime = lastFetchTime;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/get-post-update/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.lastFetchTime != null) {
            return response + "/last-fetch-time/" + this.lastFetchTime;
        }
        return response;
    }
}
