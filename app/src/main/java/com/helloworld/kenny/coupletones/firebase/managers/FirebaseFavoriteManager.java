package com.helloworld.kenny.coupletones.firebase.managers;

import com.firebase.client.Firebase;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseFavoriteManager extends FirebaseManager {

    private ArrayList<PartnerFavoriteEntry> partnerFavorites;
    private Firebase root;

    public FirebaseFavoriteManager() {
        this.root = new Firebase(FirebaseService.ENDPOINT);
    }

    public void onUserRegistered() {

    }

    public void onPartnerRegistered() {

    }

    public void onUserCleared() {

    }

    public void onPartnerCleared() {

    }

    public void onLocationVisited(FavoriteEntry entry) {

    }
}
