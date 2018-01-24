package com.moocall.moocall.url;

public class ChangeAnimalImageUrl extends UrlAbstract {
    private String animalId;
    private String animalType;

    public ChangeAnimalImageUrl(String animalId, String animalType) {
        this.animalId = animalId;
        this.animalType = animalType;
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/change-image/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.animalId != null) {
            response = response + "/id-animal/" + this.animalId;
        }
        if (this.animalType != null) {
            return response + "/animal-type/" + this.animalType;
        }
        return response;
    }
}
