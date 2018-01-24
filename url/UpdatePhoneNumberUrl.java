package com.moocall.moocall.url;

public class UpdatePhoneNumberUrl extends UrlAbstract {
    private String devices;
    private String id;
    private String name;
    private String phone;

    public UpdatePhoneNumberUrl(String id, String phone, String name, String devices) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.devices = devices;
        setStringUnsignedPart("/mobile-api/index/index/model/phone-number/method/phone-device/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.id != null) {
            response = response + "/id/" + this.id;
        }
        if (this.phone != null) {
            response = response + "/phone/" + this.phone;
        }
        if (this.name != null) {
            response = response + "/name/" + this.name;
        }
        if (this.id != null) {
            return response + "/devices/" + this.devices;
        }
        return response;
    }
}
