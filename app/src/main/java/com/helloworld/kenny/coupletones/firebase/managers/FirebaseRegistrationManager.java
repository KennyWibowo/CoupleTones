package com.helloworld.kenny.coupletones.firebase.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerNotRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserNotRegisteredException;

/**
 * Created by Kenny on 5/5/2016.
 */
public class FirebaseRegistrationManager extends FirebaseManager {

    // TODO: some sort of string verification on the email/id?
    private String email;
    private String partnerEmail;
    private boolean selfRegistered;
    private boolean partnerRegistered;

    private SharedPreferences sharedPreferences;
    private Firebase root;

    public FirebaseRegistrationManager(Context context) {
        this.email = null;
        this.partnerEmail = null;
        this.selfRegistered = false;
        this.partnerRegistered = false;

        this.sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        this.root = new Firebase(FirebaseService.ENDPOINT);
    }

    public void registerUser(String email) throws UserAlreadyRegisteredException {
        if(selfRegistered) {
            throw new UserAlreadyRegisteredException("Self already registered");
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        this.email = email;
        this.selfRegistered = true;
    }

    public void registerPartner(String partnerEmail) throws PartnerAlreadyRegisteredException {
        if(partnerRegistered) {
            throw new PartnerAlreadyRegisteredException("Partner already registered");
        }

        Firebase partnerRef = root.child("" + partnerEmail.hashCode());

        this.partnerEmail = partnerEmail;
        this.partnerRegistered = true;
    }


    //TODO: deprecate
    public void changeEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        this.email = email;
    }

    public String getEmail() throws UserNotRegisteredException {
        if(!selfRegistered) {
            throw new UserNotRegisteredException("Self not yet registered");
        }

        return email;
    }

    public String getPartnerEmail() throws PartnerNotRegisteredException {
        if(!partnerRegistered) {
            throw new PartnerNotRegisteredException("Partner not yet registered");
        }

        return partnerEmail;
    }

    public boolean isUserRegistered() {
        return selfRegistered;
    }

    public void onUserRegistered() {

    }

    public void onPartnerRegistered() {

    }

    public void onUserCleared() {
        this.email = null;
        this.selfRegistered = false;
    }

    public void onPartnerCleared() {
        this.partnerEmail = null;
        this.partnerRegistered = false;
    }

    public void onLocationVisited(FavoriteEntry entry) {

    }

}

