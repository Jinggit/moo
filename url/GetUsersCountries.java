package com.moocall.moocall.url;

public class GetUsersCountries extends UrlAbstract {
    public GetUsersCountries() {
        setStringUnsignedPart("/mobile-api/index/index/model/social-users/method/get-all-countries/ts/");
    }

    protected String createDynamicPart() {
        return "";
    }
}
