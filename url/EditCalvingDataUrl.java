package com.moocall.moocall.url;

public class EditCalvingDataUrl extends UrlAbstract {
    private String animalId;
    private String bullId;
    private String calvesNumber;
    private String calvingId;
    private String datetime;
    private String idHistory;

    public EditCalvingDataUrl(String idHistory, String animalId, String calvingId, String bullId, String datetime, String calvesNumber) {
        this.idHistory = idHistory;
        this.animalId = animalId;
        this.calvingId = calvingId;
        this.bullId = bullId;
        this.datetime = datetime;
        this.calvesNumber = calvesNumber;
        setStringUnsignedPart("/mobile-api/index/index/model/calving-history/method/edit-calving-data2/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.idHistory != null) {
            response = response + "/id-history/" + this.idHistory;
        }
        if (this.animalId != null) {
            response = response + "/id-animal/" + this.animalId;
        }
        if (this.calvingId != null) {
            response = response + "/id-calving/" + this.calvingId;
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
