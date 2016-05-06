package com.helloworld.kenny.coupletones.Parnter;

/**
 * Created by Kenny on 5/5/2016.
 */
public class PartnerInformation {

    private String regId;
    private String email;

    public PartnerInformation(String regId, String email) {
        this.regId = regId;
        this.email = email;
        // TODO: some sort of string verification on the email?
    }

    public String getRegId() {
        return regId;
    }

    public String getEmail() {
        return email;
    }
}
