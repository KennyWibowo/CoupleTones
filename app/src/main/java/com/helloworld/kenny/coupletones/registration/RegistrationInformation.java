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
public class RegistrationInformation {

    // TODO: some sort of string verification on the email/id?
    private static String email;
    private static String partnerEmail;
    private boolean registered;


    private ArrayList<PartnerFavoriteEntry> partnerFavorites;
    private ArrayList<PartnerFavoriteEntry> partnerHistory;

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

    private static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM

    public static final String endpoint = "https://coupletonesteam6.firebaseio.com/";


    public RegistrationInformation() {
        this.email = null;
        this.registered = false;

        partnerHistory = new ArrayList<PartnerFavoriteEntry>();
        partnerFavorites = new ArrayList<PartnerFavoriteEntry>();
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
        this.partnerEmail = null;
        this.registered = false;
    }
}
