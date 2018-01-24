package com.moocall.moocall.url;

public class DeletePhoneNumberUrl extends UrlAbstract {
    private String id;

    public DeletePhoneNumberUrl(String id) {
        this.id = id;
        setStringUnsignedPart("/mobile-api/index/index/model/phone-number/method/delete-phone/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.id != null) {
            return response + "/id/" + this.id;
        }
        return response;
    }
}
