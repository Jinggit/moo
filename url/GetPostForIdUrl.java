package com.moocall.moocall.url;

public class GetPostForIdUrl extends UrlAbstract {
    private String postId;

    public GetPostForIdUrl(String postId) {
        this.postId = postId;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/get-post-for-id/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.postId != null) {
            return response + "/post-id/" + this.postId;
        }
        return response;
    }
}
