package com.moocall.moocall.url;

import com.moocall.moocall.db.AnimalDb;

public class SaveMoocallTagUrl extends UrlAbstract {
    private AnimalDb animalDb;
    private String offline;
    private String type;

    public SaveMoocallTagUrl(AnimalDb animalDb, String type, String offline) {
        this.animalDb = animalDb;
        this.type = type;
        this.offline = offline;
        setStringUnsignedPart("/mobile-api/index/index/model/animal/method/save-moocall-tag-number/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.animalDb != null) {
            response = (response + "/id-animal/" + this.animalDb.getAnimal_id().toString()) + "/animal-type/" + this.animalDb.getType().toString();
        } else {
            response = (response + "/id-animal/0") + "/animal-type/" + this.type;
        }
        if (this.offline != null) {
            return response + "/offline/" + this.offline;
        }
        return response;
    }
}
