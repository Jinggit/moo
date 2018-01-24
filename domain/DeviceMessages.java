package com.moocall.moocall.domain;

public class DeviceMessages {
    private Boolean blocked;
    private Boolean connected;
    private Boolean noproblem;
    private Boolean renewalExp;
    private Boolean renewalPast;
    private Boolean turnedOff;
    private Boolean warrantyExp;
    private Boolean warrantyPast;

    public DeviceMessages(Boolean renewalExp, Boolean warrantyExp, Boolean turnedOff, Boolean blocked, Boolean warrantyPast, Boolean renewalPast, Boolean connected, Boolean noproblem) {
        setRenewalExp(renewalExp);
        setWarrantyExp(warrantyExp);
        setTurnedOff(turnedOff);
        setBlocked(blocked);
        setWarrantyPast(warrantyPast);
        setRenewalPast(renewalPast);
        setConnected(connected);
        setNoproblem(noproblem);
    }

    public Boolean getRenewalExp() {
        return this.renewalExp;
    }

    public void setRenewalExp(Boolean renewalExp) {
        this.renewalExp = renewalExp;
    }

    public Boolean getWarrantyExp() {
        return this.warrantyExp;
    }

    public void setWarrantyExp(Boolean warrantyExp) {
        this.warrantyExp = warrantyExp;
    }

    public Boolean getTurnedOff() {
        return this.turnedOff;
    }

    public void setTurnedOff(Boolean turnedOff) {
        this.turnedOff = turnedOff;
    }

    public Boolean getBlocked() {
        return this.blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getWarrantyPast() {
        return this.warrantyPast;
    }

    public void setWarrantyPast(Boolean warrantyPast) {
        this.warrantyPast = warrantyPast;
    }

    public Boolean getRenewalPast() {
        return this.renewalPast;
    }

    public void setRenewalPast(Boolean renewalPast) {
        this.renewalPast = renewalPast;
    }

    public Boolean getConnected() {
        return this.connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Boolean getNoproblem() {
        return this.noproblem;
    }

    public void setNoproblem(Boolean noproblem) {
        this.noproblem = noproblem;
    }
}
