package com.moocall.moocall.url;

public class EditAnimalUrl extends UrlAbstract {
    private String animalId;
    private String animalType;
    private String birthDate;
    private String datetime;
    private String gestation;
    private String name;
    private String newAnimalType;
    private Integer state;
    private String temperament;
    private String vendor;

    public EditAnimalUrl(String animalId, String birthDate, String name, String animalType, String newAnimalType, String temperament, String vendor, Integer state, String datetime, String gestation) {
        this.animalId = animalId;
        this.birthDate = birthDate;
        this.name = name;
        this.animalType = animalType;
        this.newAnimalType = newAnimalType;
        this.temperament = temperament;
        this.vendor = vendor;
        this.state = state;
        this.datetime = datetime;
        this.gestation = gestation;
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/edit-animal-details/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.animalId != null) {
            response = response + "/id-animal/" + this.animalId;
        }
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
        if (this.newAnimalType != null) {
            response = response + "/new-animal-type/" + this.newAnimalType;
        }
        if (this.temperament != null) {
            response = response + "/temperament/" + this.temperament;
        }
        if (this.vendor != null) {
            response = response + "/vendor/" + this.vendor;
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
