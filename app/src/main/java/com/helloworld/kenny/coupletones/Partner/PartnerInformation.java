package com.helloworld.kenny.coupletones.partner;

import com.helloworld.kenny.coupletones.partner.exceptions.PartnerAlreadyRegisteredException;

/**
 * Created by Kenny on 5/5/2016.
 */
public class PartnerInformation {

    // TODO: some sort of string verification on the email/id?
    public static String partnerRegId;
    public static String myRegId;
    private String email;
    private boolean registered;

    /**
     *  DEBUG - AUTO REGISTRATION
     *
     *  Replace both IDs to auto-register on startup!
     *  It does not matter which order you have the IDs.
     *  Don't have to replace emails, they're just there for dummy purposes.
     *
     *  Having trouble deploying two emulators at the same time?
     *  Check this out:
     *      http://stackoverflow.com/questions/18592296/deploy-on-several-emulators-in-android-studio
     *
     *  Full steps:
     *      1. Run app on two emulators
     *      2. Copy and paste the two regIds from Android Monitor (console) onto here
     *      3. Re-run the two apps
     *
     *  This should auto-register both apps to each other!
     * */
    public static final String REG_ID_1 = "APA91bFAWoA4o0HM_WPVSlTWJwSlXHHvEvccSxi5gH12p7_cU4ebSKLBwWlylfwyg8gwzW8yNm3hdj3JvO1MtXLNJPfxoqqfCOS8uDkKzPVEMGzPFbebygQqdU3vIjQylHwffz8O7_uoHnouW6UtnRv2JGKtV1FmsQ";
    public static final String REG_ID_2 = "APA91bEkDEW1eTorFjabIRsjustxw_Zs2VNObxhbOn2kVwfkV1F6D_Edt_Vg37fdUobhwuiA1WzLFIXtEyWI65icQEMRUHHiKP_xzbzL3Lam-AkgP7fOYTAKzARZXCuONauCBKBCZrelZXAD_iAWU9lEHVjce8WGsw";

    private static final String EMAIL_1 = "dummy1@dummymail.com";
    private static final String EMAIL_2 = "dummy2@dummymail.com";

    public PartnerInformation() {
        this.partnerRegId = null;
        this.myRegId = null;
        this.email = null;
        this.registered = false;
    }

    public boolean registerOwnRegId(String myRegId) {
        boolean autoRegistered = false;

        try {
            if (myRegId.equals(REG_ID_1)) {
                registerPartner(REG_ID_2, EMAIL_2);
                System.out.println("Partner auto-registered!");
                autoRegistered = true;
            } else if (myRegId.equals(REG_ID_2)) {
                registerPartner(REG_ID_1, EMAIL_1);
                System.out.println("Partner auto-registered!");
                autoRegistered = true;
            }
        } catch (PartnerAlreadyRegisteredException e) {
            System.out.println("Oops!");
        } finally {
            this.myRegId = myRegId;
        }

        return autoRegistered;

    }

    public void registerPartner(String partnerRegId, String email) throws PartnerAlreadyRegisteredException {
        if(registered)
            throw new PartnerAlreadyRegisteredException("Partner already registered");

        this.partnerRegId = partnerRegId;
        this.email = email;
    }

    public static String getOwnRegId() {
        return myRegId;
    }

    public static String getRegId() {
        return partnerRegId;
    }

    public String getEmail() {
        return email;
    }

    public void clear() {
        this.partnerRegId = null;
        this.email = null;
        this.registered = false;
    }
}
