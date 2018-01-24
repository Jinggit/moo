package com.moocall.moocall.url;

public class SetClientSettingsUrl extends UrlAbstract {
    private String clientTimeZone;
    private String email1;
    private String email2;
    private String email3;
    private String emailOption;

    public SetClientSettingsUrl(String clientTimeZone, String emailOption, String email1, String email2, String email3) {
        this.clientTimeZone = clientTimeZone;
        this.emailOption = emailOption;
        this.email1 = email1;
        this.email2 = email2;
        this.email3 = email3;
        setStringUnsignedPart("/mobile-api/index/index/model/client/method/client-settings/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.clientTimeZone != null) {
            response = response + "/client-time-zone/" + this.clientTimeZone;
        }
        if (this.emailOption != null) {
            response = response + "/email-option/" + this.emailOption;
        }
        if (this.email1 != null) {
            response = response + "/email1/" + this.email1;
        }
        if (this.email2 != null) {
            response = response + "/email2/" + this.email2;
        }
        if (this.email3 != null) {
            return response + "/email3/" + this.email3;
        }
        return response;
    }
}
