package com.moocall.moocall.url;

public class SaveCalvingDataUrl extends UrlAbstract {
    private String bullId;
    private String calvesNumber;
    private String cowId;
    private String datetime;

    public SaveCalvingDataUrl(String cowId, String bullId, String datetime, String calvesNumber) {
        this.cowId = cowId;
        this.bullId = bullId;
        this.datetime = datetime;
        this.calvesNumber = calvesNumber;
        setStringUnsignedPart("/mobile-api/index/index/model/calving-history/method/add-calving-data2/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.cowId != null) {
            response = response + "/cow-id/" + this.cowId;
        }
        if (this.bullId != null) {
            response = response + "/id-bull/" + this.bullId;
        } else {
            response = response + "/id-bull/";
        }
        if (this.datetime != null) {
            response = response + "/datetime/" + this.datetime;
        }
        if (this.calvesNumber != null) {
            return response + "/calves-number/" + this.calvesNumber;
        }
        return response;
    }
}
