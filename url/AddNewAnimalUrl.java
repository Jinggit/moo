package com.moocall.moocall.url;

public class AddNewAnimalUrl extends UrlAbstract {
    private String animalType;
    private String birthDate;
    private String datetime;
    private String gestation;
    private String name;
    private Integer state;

    public AddNewAnimalUrl(String birthDate, String name, String animalType, Integer state, String datetime, String gestation) {
        this.birthDate = birthDate;
        this.name = name;
        this.animalType = animalType;
        this.state = state;
        this.datetime = datetime;
        this.gestation = gestation;
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/add-new-animal/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.birthDate != null) {
            response = response + "/birth-date/" + this.birthDate;
        } else {
            response = response + "/birth-date/";
        }
        if (this.name != null) {
            response = response + "/name/" + this.name;
        }
        if (this.animalType != null) {
            response = response + "/animal-type/" + this.animalType;
        }
        if (this.state != null) {
            response = response + "/state/" + this.state.toString();
        } else {
            response = response + "/state/";
        }
        if (this.datetime != null) {
            response = response + "/datetime/" + this.datetime;
        }
        if (this.gestation != null) {
            return response + "/gestation/" + this.gestation;
        }
        return response;
    }
}
