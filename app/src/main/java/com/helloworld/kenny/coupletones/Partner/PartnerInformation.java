package com.helloworld.kenny.coupletones.Partner;

import com.helloworld.kenny.coupletones.Partner.Exceptions.PartnerAlreadyRegisteredException;

/**
 * Created by Kenny on 5/5/2016.
 */
public class PartnerInformation {

    // TODO: some sort of string verification on the email/id?
    private String partnerRegId;
    private String myRegId;
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
    private static final String REG_ID_1 = "APA91bEoj3IHckIM-KcmiCf2DVoq4ttOslY_1sby9GZp5V9xS6qTBQACqTyiys1iNkC15ZLMfbl_T1zk4-VuKxHf9frfwufFpCrcTts0uO62rBbXSgO9fl8H0eR-7YZYQmoYtHYgo_45rVewrMtKE93IDL9kOfUepw";
    private static final String REG_ID_2 = "APA91bEuenavnRzubaJkgk3N9uW2IIz6-fA8iX27n4WfqOtX0BMw4HyKElkDJx_0x6NbhZb2NQttn797b4X0gYydoJVsBoAaz0yG4II-ycogbLsQvIjoHQ0busuRPtPy6SpduMNMEUokohX671Xd2zht9QGaq0Jy2w";

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

    public String getOwnRegId() {
        return myRegId;
    }

    public String getRegId() {
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
