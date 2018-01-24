package com.moocall.moocall.url;

public class EditPostUrl extends UrlAbstract {
    private String category;
    private String postId;

    public EditPostUrl(String postId, String category) {
        this.postId = postId;
        this.category = category;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/edit-post/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.postId != null) {
            response = response + "/post-id/" + this.postId;
        }
        if (this.category != null) {
            return response + "/category/" + this.category;
        }
        return response;
    }
}
