package com.moocall.moocall.url;

public class ReportPostUrl extends UrlAbstract {
    private String postId;
    private String reason;

    public ReportPostUrl(String postId, String reason) {
        this.postId = postId;
        this.reason = reason;
        setStringUnsignedPart("/mobile-api/index/index/model/social-posts/method/report-post/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.postId != null) {
            response = response + "/post-id/" + this.postId;
        }
        if (this.reason != null) {
            return response + "/reason/" + this.reason;
        }
        return response;
    }
}
