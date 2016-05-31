package com.helloworld.kenny.coupletones.firebase.managers;

import android.content.Context;
import android.content.Intent;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.favorites.JSONEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.firebase.FirebaseNotificationIntentService;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserNotRegisteredException;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseFavoriteManager extends FirebaseManager {

    private ChildEventListener favoriteListner;
    private Favorites partnerFavorites;
    private JSONEntry lastAddedFavorite;
    private FirebaseRegistrationManager firebaseRegistrationManager;
    private Firebase root;

    public FirebaseFavoriteManager(FirebaseRegistrationManager firebaseRegistrationManager, final Context context) {
        this.root = new Firebase(FirebaseService.ENDPOINT);
        partnerFavorites = new Favorites();
        lastAddedFavorite = new JSONEntry();
        this.firebaseRegistrationManager = firebaseRegistrationManager;
        favoriteListner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
                PartnerFavoriteEntry favoriteEntry =
                        new PartnerFavoriteEntry(child.getName(),
                                new LatLng(child.getLatitude(),child.getLongitude()));
                try {
                    partnerFavorites.addEntry(favoriteEntry.getName(),new LatLng(child.getLatitude(),child.getLongitude()));
                }catch (InvalidNameException e)
                {
                    //do nothing
                }catch (NameInUseException e)
                {
                    //do nothing
                }

                Intent notifyUser = new Intent(context, FirebaseNotificationIntentService.class);
                notifyUser.putExtra("title", "Partner added a new favortie location!");
                notifyUser.putExtra("content", "Partner added: "+ favoriteEntry.getName());

                context.startService(notifyUser);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

    }

    public void onUserRegistered(String userKey) {
        Firebase userRef = root.child(userKey);
    }

    public void onPartnerRegistered(String partnerKey) {
        Firebase partnerRef = root.child(partnerKey);
        partnerRef.child("favorite").addChildEventListener(favoriteListner);
    }

    public void onUserCleared() {
        this.lastAddedFavorite = new JSONEntry();
    }

    public void onPartnerCleared() {
        root.child(""+firebaseRegistrationManager.
                getPartnerKey()).child("favorite").removeEventListener(favoriteListner);
    }

    public void onFavoriteAdded(FavoriteEntry entry) {
        try {
            String email = firebaseRegistrationManager.getEmail();
            System.out.println("Adding favorite to Firebase");
            if(lastAddedFavorite.getName() == null ||
                    !lastAddedFavorite.getName().equals(entry.getName()))
            {
                Firebase favoriteEntryRef = root.child(""+email.hashCode()).child("favorite").push();
                lastAddedFavorite = new JSONEntry(entry);
                System.out.println("Favorite uploaded: "+lastAddedFavorite.getName());
                favoriteEntryRef.setValue(new JSONEntry(entry));
            }

        } catch (UserNotRegisteredException e) {
            //do nothing
        }
    }
    public void onLocationVisited(FavoriteEntry entry) {

    }

    public ArrayList<FavoriteEntry> getPartnerFavorite()
    {
        return partnerFavorites.getAllEntries();
    }
}
