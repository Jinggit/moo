package com.moocall.moocall.url;

import com.moocall.moocall.domain.Account;

public class ResetPasswordUrl extends UrlAbstract {
    private String email;

    public ResetPasswordUrl(String email) {
        this.email = email;
        Account.setPassword("f5d92a8014456f0237250ec673a42b0919e3fef5");
        Account.setUsername("basicaccount");
        setStringUnsignedPart("/mobile-api/index/index/model/account/method/reset-password/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.email != null) {
            return response + "/reset-email/" + this.email;
        }
        return response;
    }
}
