package com.helloworld.kenny.coupletones.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.partner.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.registration.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.registration.exceptions.SelfAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.registration.exceptions.SelfNotRegisteredException;

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
    private SharedPreferences sharedPreferences;
    private Firebase firebase;

    private static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM
    public static final String endpoint = "https://coupletonesteam6.firebaseio.com/";


    public RegistrationInformation(Context context) {
        this.email = null;
        this.partnerEmail = null;
        this.selfRegistered = false;
        this.partnerRegistered = false;
        this.sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        partnerHistory = new ArrayList<PartnerFavoriteEntry>();
        partnerFavorites = new ArrayList<PartnerFavoriteEntry>();

        firebase = new Firebase(endpoint);

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

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        this.email = email;
        this.selfRegistered = true;
    }

    public String getEmail() {
        return email;
    }

    public void visitLocation(FavoriteEntry entry) throws SelfNotRegisteredException {
        if(!selfRegistered) {
            throw new SelfNotRegisteredException("Self not yet registered");
        }

        //TODO: Append FavoriteEntry details to history.
        firebase.child(email);
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

    public boolean isSelfRegistered() {
        return selfRegistered;
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
