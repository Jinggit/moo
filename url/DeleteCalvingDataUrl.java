package com.moocall.moocall.url;

public class DeleteCalvingDataUrl extends UrlAbstract {
    private String animalType;
    private String calvesNumber;
    private String idAnimal;
    private String idCalving;
    private String idHistory;

    public DeleteCalvingDataUrl(String idCalving, String idHistory, String idAnimal, String animalType, String calvesNumber) {
        this.idCalving = idCalving;
        this.idHistory = idHistory;
        this.idAnimal = idAnimal;
        this.animalType = animalType;
        this.calvesNumber = calvesNumber;
        setStringUnsignedPart("/mobile-api/index/index/model/calving-history/method/delete-calving-data2/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.idCalving != null) {
            response = response + "/id-calving/" + this.idCalving;
        }
        if (this.idHistory != null) {
            response = response + "/id-history/" + this.idHistory;
        }
        if (this.idAnimal != null) {
            response = response + "/id-animal/" + this.idAnimal;
        }
        if (this.animalType != null) {
            response = response + "/animal-type/" + this.animalType;
        }
        if (this.calvesNumber != null) {
            return response + "/calves-number/" + this.calvesNumber;
        }
        return response;
    }
}
