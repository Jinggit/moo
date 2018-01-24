package com.moocall.moocall.url;

public class GetCalvingDataUrl extends UrlAbstract {
    private String calvingId;

    public GetCalvingDataUrl(String calvingId) {
        this.calvingId = calvingId;
        setStringUnsignedPart("/mobile-api/index/index/model/calving-history/method/get-calving-data/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.calvingId != null) {
            return response + "/calving-id/" + this.calvingId;
        }
        return response;
    }
}
