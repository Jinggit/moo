package com.moocall.moocall.url;

import android.content.Context;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;

public abstract class UrlAbstract {
    private String signature;
    private String stringUnsignedPart;
    private long unixTs = Utils.getUnixTs();
    private String url;
    private String urn;

    protected abstract String createDynamicPart();

    public void createStringUnsignedPart(Context context) {
        if (Account.getUsername() != null) {
            this.stringUnsignedPart += getUnixTs() + createDynamicPart() + "/key/" + Account.getUsername();
        } else {
            StorageContainer.removeCredentialsFromPreferences(context);
        }
    }

    public long getUnixTs() {
        return this.unixTs;
    }

    public void setUnixTs(long unixTs) {
        this.unixTs = unixTs;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStringUnsignedPart() {
        return this.stringUnsignedPart;
    }

    public void setStringUnsignedPart(String stringUnsignedPart) {
        this.stringUnsignedPart = stringUnsignedPart;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUrn() {
        return this.urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String createUrn() {
        return this.stringUnsignedPart + "/signature/" + createSignature();
    }

    public String createSignature() {
        return Utils.calcHmac(this.stringUnsignedPart, Account.getPassword());
    }

    public String toString() {
        return this.url;
    }

    public String createAndReturnUrl(Context context) {
        createStringUnsignedPart(context);
        if (createUrn().toLowerCase().contains("social")) {
            return StorageContainer.socialHost + createUrn();
        }
        return StorageContainer.host + createUrn();
    }
}
