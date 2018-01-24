package com.moocall.moocall.url;

public class AddPostCommentUrl extends UrlAbstract {
    private String postId;

    public AddPostCommentUrl(String postId) {
        this.postId = postId;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/add-new-comment/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.postId != null) {
            return response + "/post-id/" + this.postId;
        }
        return response;
    }
}
