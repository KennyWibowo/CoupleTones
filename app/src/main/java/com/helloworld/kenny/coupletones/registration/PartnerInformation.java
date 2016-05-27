package com.helloworld.kenny.coupletones.registration;

import com.helloworld.kenny.coupletones.favorites.partner.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.registration.exceptions.PartnerAlreadyRegisteredException;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Kenny on 5/5/2016.
 */
public class PartnerInformation {

    // TODO: some sort of string verification on the email/id?
    public static String partnerRegId;
    public static String myRegId;
    private static String email;
    private static String partnerEmail;
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

    private static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM

    private static final String EMAIL_1 = "dummy1@dummymail.com";
    private static final String EMAIL_2 = "dummy2@dummymail.com";

    public static final String endpoint = "https://coupletonesteam6.firebaseio.com/";

    private ArrayList<PartnerFavoriteEntry> partnerFavorites;

    private ArrayList<PartnerFavoriteEntry> partnerHistory;

    public PartnerInformation() {
        this.partnerRegId = null;
        this.myRegId = null;
        this.email = null;
        this.registered = false;

        partnerHistory = new ArrayList<PartnerFavoriteEntry>();
        partnerFavorites = new ArrayList<PartnerFavoriteEntry>();
    }

    // DEPRECATED
    public void registerPartner(String partnerRegId, String email) throws PartnerAlreadyRegisteredException {
        if(registered)
            throw new PartnerAlreadyRegisteredException("Partner already registered");

        this.partnerRegId = partnerRegId;
        this.email = email;
    }


    public void registerPartner(String partnerEmail) throws PartnerAlreadyRegisteredException {
        if(registered) {
            throw new PartnerAlreadyRegisteredException("Partner already registered");
        }

        this.partnerEmail = partnerEmail;
        this.registered = true;
    }

    public static String getEmail() {
        return email;
    }

    public ArrayList<PartnerFavoriteEntry> getPartnerHistory() {
        updatePartnerHistory();

        return partnerHistory;
    }

    public void updatePartnerHistory() {
        Timestamp tsDeletePoint = Timestamp.valueOf(
                        new SimpleDateFormat("yyyy-MM-dd ")
                        .format(new Date())
                        .concat(HISTORY_RESET_TIME));

        for( int i = 0; i < partnerHistory.size(); i++ ) {
            if(partnerHistory.get(i).getTimestamp().after(tsDeletePoint)) {
                partnerHistory.remove(i--);

                //TODO: Update Firebase after deletion.
            }
        }
    }

    public void clear() {
        this.partnerRegId = null;
        this.email = null;
        this.registered = false;
    }
}
