package com.moocall.moocall.url;

public class FetchPostListUrl extends UrlAbstract {
    private String offset;

    public FetchPostListUrl(String offset) {
        this.offset = offset;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/fetch-post-list/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.offset != null) {
            return response + "/offset/" + this.offset;
        }
        return response;
    }
}
