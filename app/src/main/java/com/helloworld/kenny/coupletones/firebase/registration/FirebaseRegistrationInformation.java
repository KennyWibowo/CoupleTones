package com.helloworld.kenny.coupletones.firebase.registration;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;
import com.helloworld.kenny.coupletones.favorites.JSONEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.FirebaseFactoryService;
import com.helloworld.kenny.coupletones.firebase.registration.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.registration.exceptions.PartnerNotRegisteredException;
import com.helloworld.kenny.coupletones.firebase.registration.exceptions.SelfAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.registration.exceptions.SelfNotRegisteredException;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/5/2016.
 */
public class FirebaseRegistrationInformation {

    // TODO: some sort of string verification on the email/id?
    private String email;
    private String partnerEmail;
    private boolean selfRegistered;
    private boolean partnerRegistered;

    private SharedPreferences sharedPreferences;
    private Firebase root;

    public FirebaseRegistrationInformation(Context context) {
        this.email = null;
        this.partnerEmail = null;
        this.selfRegistered = false;
        this.partnerRegistered = false;
        this.sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        root = new Firebase(FirebaseFactoryService.ENDPOINT);
    }

    public void registerPartner(String partnerEmail) throws PartnerAlreadyRegisteredException {
        if(partnerRegistered) {
            throw new PartnerAlreadyRegisteredException("Partner already registered");
        }

        Firebase partnerRef = root.child("" + partnerEmail.hashCode());
        //TODO: add listener to clear partner's history at 3 AM

        //TODO: partnerRef.child("history").addChildEventListener(historyListener);
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


    //TODO: deprecate this
    public void changeEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        this.email = email;
    }

    public String getEmail() throws SelfNotRegisteredException {
        if(!selfRegistered) {
            throw new SelfNotRegisteredException("Self not yet registered");
        }

        return email;
    }

    public String getPartnerEmail() throws PartnerNotRegisteredException {
        if(!partnerRegistered) {
            throw new PartnerNotRegisteredException("Partner not yet registered");
        }

        return partnerEmail;
    }

    public boolean isSelfRegistered() {
        return selfRegistered;
    }

    //TODO: move clears to a service

    public void clearPartner() {
        //TODO: root.child("" + partnerEmail.hashCode()).child("history").removeEventListener(historyListener);

        this.partnerEmail = null;
        this.partnerRegistered = false;
    }

    public void clearSelf() {
        this.email = null;
        this.selfRegistered = false;
        //TODO: this.lastVisitedLocation = new JSONEntry();
    }
}

