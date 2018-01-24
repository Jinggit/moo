package com.moocall.moocall.url;

public class DeletePostUrl extends UrlAbstract {
    private String postId;

    public DeletePostUrl(String postId) {
        this.postId = postId;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/delete-post/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.postId != null) {
            return response + "/post-id/" + this.postId;
        }
        return response;
    }
}
