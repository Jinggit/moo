package com.moocall.moocall.url;

public class AddNewUserUrl extends UrlAbstract {
    private String city;
    private String country;
    private String latitude;
    private String longitude;
    private String userNickname;
    private String version;

    public AddNewUserUrl(String userNickname, String latitude, String longitude, String city, String country, String version) {
        this.userNickname = userNickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
        this.version = version;
        setStringUnsignedPart("/mobile-api/index/index/model/social-users/method/add-new-user/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.userNickname != null) {
            response = response + "/user-nickname/" + this.userNickname;
        }
        if (this.latitude != null) {
            response = response + "/latitude/" + this.latitude;
        }
        if (this.longitude != null) {
            response = response + "/longitude/" + this.longitude;
        }
        if (this.city != null) {
            response = response + "/city/" + this.city;
        }
        if (this.country != null) {
            response = response + "/country/" + this.country;
        }
        if (this.version != null) {
            return response + "/version/" + this.version;
        }
        return response;
    }
}
