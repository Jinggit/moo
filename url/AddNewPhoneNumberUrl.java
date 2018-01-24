package com.moocall.moocall.url;

public class AddNewPhoneNumberUrl extends UrlAbstract {
    private String devices;
    private String name;
    private String phone;

    public AddNewPhoneNumberUrl(String phone, String name, String devices) {
        this.phone = phone;
        this.name = name;
        this.devices = devices;
        setStringUnsignedPart("/mobile-api/index/index/model/phone-number/method/phone-device/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.phone != null) {
            response = response + "/phone/" + this.phone;
        }
        if (this.name != null) {
            response = response + "/name/" + this.name;
        }
        if (this.devices != null) {
            return response + "/devices/" + this.devices;
        }
        return response;
    }
}
