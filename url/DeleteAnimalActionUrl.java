package com.moocall.moocall.url;

public class DeleteAnimalActionUrl extends UrlAbstract {
    private String actionType;
    private String animalType;
    private String historyId;
    private String idAnimal;

    public DeleteAnimalActionUrl(String historyId, String actionType, String animalType, String idAnimal) {
        this.historyId = historyId;
        this.actionType = actionType;
        this.animalType = animalType;
        this.idAnimal = idAnimal;
        setStringUnsignedPart("/mobile-api/index/index/model/animal-edit/method/delete-animal-action/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.historyId != null) {
            response = response + "/id-history/" + this.historyId;
        }
        if (this.actionType != null) {
            response = response + "/action-type/" + this.actionType;
        }
        if (this.animalType != null) {
            response = response + "/animal-type/" + this.animalType;
        }
        if (this.idAnimal != null) {
            return response + "/id-animal/" + this.idAnimal;
        }
        return response;
    }
}
