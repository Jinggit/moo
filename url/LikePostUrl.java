package com.moocall.moocall.url;

public class LikePostUrl extends UrlAbstract {
    private String postId;
    private String postUserId;

    public LikePostUrl(String postId, String postUserId) {
        this.postId = postId;
        this.postUserId = postUserId;
        setStringUnsignedPart("/mobile-api/index/index/model/social-opinions/method/like-post/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.postId != null) {
            response = response + "/post-id/" + this.postId;
        }
        if (this.postUserId != null) {
            return response + "/post-user-id/" + this.postUserId;
        }
        return response;
    }
}
