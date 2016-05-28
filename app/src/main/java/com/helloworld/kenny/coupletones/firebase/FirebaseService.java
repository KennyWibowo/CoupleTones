package com.helloworld.kenny.coupletones.firebase;

import android.content.Context;

import com.firebase.client.Firebase;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseManager;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseRegistrationManager;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseService {

    public static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM
    public static final String ENDPOINT = "https://coupletonesteam6.firebaseio.com/";

    private FirebaseRegistrationManager registrationManager;
    private ArrayList<FirebaseManager> extraManagers;
    private Firebase root;

    public FirebaseService(Context context) {
        root = new Firebase(ENDPOINT);
        registrationManager = new FirebaseRegistrationManager(context);
        extraManagers = new ArrayList<>();

        addManager(registrationManager);
    }

    public FirebaseRegistrationManager getRegistrationManager() {
        return registrationManager;
    }

    public void addManager(FirebaseManager manager) {
        extraManagers.add(manager);
    }

    public boolean registerUser(String email) {
        try {
            registrationManager.registerUser(email);

            for (int i = 0; i < extraManagers.size(); i++) {
                extraManagers.get(i).onUserRegistered();
            }

            return true;
        } catch (UserAlreadyRegisteredException e) {
            return false;
        }
    }

    public boolean registerPartner(String email) {
        try {
            registrationManager.registerPartner(email);

            for (int i = 0; i < extraManagers.size(); i++) {
                extraManagers.get(i).onPartnerRegistered();
            }

            return true;
        } catch (PartnerAlreadyRegisteredException e) {
            return false;
        }
    }

    public void clearUser() {
        for (int i = 0; i < extraManagers.size(); i++) {
            extraManagers.get(i).onUserCleared();
        }
    }

    public void clearPartner() {
        for (int i = 0; i < extraManagers.size(); i++) {
            extraManagers.get(i).onPartnerCleared();
        }
    }

    public void visitLocation(FavoriteEntry entry) {
        for (int i = 0; i < extraManagers.size(); i++) {
            extraManagers.get(i).onLocationVisited(entry);
        }
    }
}
