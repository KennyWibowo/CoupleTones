package com.helloworld.kenny.coupletones.firebase.managers;

import com.firebase.client.Firebase;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserAlreadyRegisteredException;

/**
 * Created by Kenny on 5/27/2016.
 */
public abstract class FirebaseManager {

    public abstract void onUserCleared();

    public abstract void onPartnerCleared();

    public abstract void onUserRegistered(String userKey);

    public abstract void onPartnerRegistered(String partnerKey);

    public abstract void onLocationVisited(FavoriteEntry entry);

    public abstract void onFavoriteAdded(FavoriteEntry entry);
}
