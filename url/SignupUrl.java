package com.moocall.moocall.url;

import com.moocall.moocall.domain.Account;

public class SignupUrl extends UrlAbstract {
    private String firstName;
    private String lastName;
    private String password;
    private String username;

    public SignupUrl(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        Account.setPassword("f5d92a8014456f0237250ec673a42b0919e3fef5");
        Account.setUsername("basicaccount");
        setStringUnsignedPart("/mobile-api/index/index/model/user/method/register-user/ts/");
    }

    protected String createDynamicPart() {
        String response = "";
        if (this.firstName != null) {
            response = response + "/first-name/" + this.firstName;
        }
        if (this.lastName != null) {
            response = response + "/last-name/" + this.lastName;
        }
        if (this.username != null) {
            response = response + "/username/" + this.username;
        }
        if (this.password != null) {
            return response + "/password/" + this.password;
        }
        return response;
    }
}
