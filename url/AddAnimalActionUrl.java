package com.moocall.moocall.url;

public class AddAnimalActionUrl extends UrlAbstract {
    private String actionType;
    private String animalType;
    private String idAnimal;
    private String response2;

    public AddAnimalActionUrl(String actionType, String animalType, String idAnimal, String response2) {
        this.actionType = actionType;
        this.animalType = animalType;
        this.idAnimal = idAnimal;
        this.response2 = response2;
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/add-animal-action/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.actionType != null) {
            response = response + "/action-type/" + this.actionType;
        }
        if (this.animalType != null) {
            response = response + "/animal-type/" + this.animalType;
        }
        if (this.idAnimal != null) {
            response = response + "/id-animal/" + this.idAnimal;
        }
        if (this.response2 != null) {
            return response + "/response/" + this.response2;
        }
        return response;
    }
}
