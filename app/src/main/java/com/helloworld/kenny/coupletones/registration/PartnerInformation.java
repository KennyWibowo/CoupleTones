package com.helloworld.kenny.coupletones.registration;

import com.helloworld.kenny.coupletones.registration.exceptions.PartnerAlreadyRegisteredException;

/**
 * Created by Kenny on 5/5/2016.
 */
public class PartnerInformation {

    // TODO: some sort of string verification on the email/id?
    public static String partnerRegId;
    public static String myRegId;
    private static String email;
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
    public static final String REG_ID_1 = "INSERT_REG_ID";
    public static final String REG_ID_2 = "INSERT_REG_ID";

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

    public static String getEmail() {
        return email;
    }

    public void clear() {
        this.partnerRegId = null;
        this.email = null;
        this.registered = false;
    }
}
