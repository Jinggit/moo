package com.moocall.moocall.url;

public class AddNewPostUrl extends UrlAbstract {
    String category;

    public AddNewPostUrl(String category) {
        this.category = category;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/add-new-post/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.category != null) {
            return response + "/category/" + this.category;
        }
        return response;
    }
}
