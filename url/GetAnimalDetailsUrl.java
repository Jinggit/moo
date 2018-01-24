package com.moocall.moocall.url;

public class GetAnimalDetailsUrl extends UrlAbstract {
    private String animalType;
    private String idAnimal;

    public GetAnimalDetailsUrl(String idAnimal, String animalType) {
        this.idAnimal = idAnimal;
        this.animalType = animalType;
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/get-animal-details/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.idAnimal != null) {
            response = response + "/id-animal/" + this.idAnimal;
        }
        if (this.animalType != null) {
            return response + "/animal-type/" + this.animalType;
        }
        return response;
    }
}
