package com.moocall.moocall.url;

public class FetchCalvingItemsUrl extends UrlAbstract {
    private String p1;
    private String p2;
    private String p3;
    private String p4;

    public FetchCalvingItemsUrl(String p1, String p2, String p3, String p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        setStringUnsignedPart("/mobile-api/index/index/model/device/method/fetch-calving-items/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.p1 != null) {
            response = response + "/p1/" + this.p1;
        }
        if (this.p2 != null) {
            response = response + "/p2/" + this.p2;
        }
        if (this.p3 != null) {
            response = response + "/p3/" + this.p3;
        }
        if (this.p4 != null) {
            return response + "/p4/" + this.p4;
        }
        return response;
    }
}
