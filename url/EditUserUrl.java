package com.moocall.moocall.url;

public class EditUserUrl extends UrlAbstract {
    private String city;
    private String country;
    private String latitude;
    private String longitude;
    private String userId;
    private String userNickname;

    public EditUserUrl(String userId, String userNickname, String latitude, String longitude, String city, String country) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
        setStringUnsignedPart("/mobile-api/index/index/model/social-users/method/edit-user/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.userId != null) {
            response = response + "/user-id/" + this.userId;
        }
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
            return response + "/country/" + this.country;
        }
        return response;
    }
}
