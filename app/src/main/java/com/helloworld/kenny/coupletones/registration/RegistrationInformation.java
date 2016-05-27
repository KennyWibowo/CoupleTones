package com.helloworld.kenny.coupletones.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.helloworld.kenny.coupletones.favorites.partner.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.registration.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.registration.exceptions.SelfAlreadyRegisteredException;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Kenny on 5/5/2016.
 */
public class RegistrationInformation {

    // TODO: some sort of string verification on the email/id?
    private String email;
    private String partnerEmail;
    private boolean selfRegistered;
    private boolean partnerRegistered;

    private ArrayList<PartnerFavoriteEntry> partnerFavorites;
    private ArrayList<PartnerFavoriteEntry> partnerHistory;

    private static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM
    public static final String endpoint = "https://coupletonesteam6.firebaseio.com/";


    public RegistrationInformation() {
        this.email = null;
        this.partnerEmail = null;
        this.selfRegistered = false;
        this.partnerRegistered = false;

        partnerHistory = new ArrayList<PartnerFavoriteEntry>();
        partnerFavorites = new ArrayList<PartnerFavoriteEntry>();

    }

    public void registerPartner(String partnerEmail) throws PartnerAlreadyRegisteredException {
        if(partnerRegistered) {
            throw new PartnerAlreadyRegisteredException("Partner already registered");
        }

        this.partnerEmail = partnerEmail;
        this.partnerRegistered = true;
    }

    public void registerSelf(String email) throws SelfAlreadyRegisteredException {
        if(selfRegistered) {
            throw new SelfAlreadyRegisteredException("Self already registered");
        }

        this.email = email;
        this.selfRegistered = true;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<PartnerFavoriteEntry> getPartnerHistory() {
        updatePartnerHistory();

        return partnerHistory;
    }

    private void updatePartnerHistory() {
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

    public void clearPartner() {
        this.partnerEmail = null;
        this.partnerRegistered = false;
    }

    public void clearSelf() {
        this.email = null;
        this.selfRegistered = false;
    }
}
