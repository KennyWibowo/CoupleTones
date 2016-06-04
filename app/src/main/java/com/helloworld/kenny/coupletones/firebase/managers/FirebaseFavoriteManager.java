package com.helloworld.kenny.coupletones.firebase.managers;

import android.content.Context;
import android.content.Intent;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.FavoriteSwipeAdapter;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.favorites.JSONEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.firebase.intents.FirebaseNotificationIntentService;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserNotRegisteredException;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseFavoriteManager extends FirebaseManager {

    //TODO: handle when user/partner delete favorites.

    private ChildEventListener favoriteListner;
    //TODO: move adapter here and call "notifyDataSetChanged" on it when modifying arraylist
    private ValueEventListener partnerFavoriteEventListner;
    private FavoriteSwipeAdapter<Favorites> favoriteSwipeAdapter;
    //private Favorites partnerFavorites;
    private final static ArrayList<PartnerFavoriteEntry> partnerFavorites = new ArrayList<>();
    private JSONEntry lastAddedFavorite;
    private JSONEntry lastDeletedFavorite;
    private FirebaseRegistrationManager firebaseRegistrationManager;
    private Firebase root;

    public FirebaseFavoriteManager(FirebaseRegistrationManager firebaseRegistrationManager, final Context context) {
        this.root = new Firebase(FirebaseService.ENDPOINT);
        lastAddedFavorite = new JSONEntry();
        this.firebaseRegistrationManager = firebaseRegistrationManager;
        favoriteListner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
                PartnerFavoriteEntry favoriteEntry =
                        new PartnerFavoriteEntry(child.getName(),
                                new LatLng(child.getLatitude(),child.getLongitude()));

                partnerFavorites.add(favoriteEntry);


                // No need to notify user when partner added a new favorite location
                /*Intent notifyUser = new Intent(context, FirebaseNotificationIntentService.class);
                notifyUser.putExtra("title", "Partner added a new favorite location!");
                notifyUser.putExtra("content", "Partner added: "+ favoriteEntry.getName());

                context.startService(notifyUser);*/

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
                PartnerFavoriteEntry partnerFavoriteEntry = new PartnerFavoriteEntry(child.getName(),
                        new LatLng(child.getLatitude(),child.getLongitude()));
                int i;
                for(i=0;i<partnerFavorites.size();i++)
                {

                    if(partnerFavorites.get(i).getName().equals(partnerFavoriteEntry.getName()))
                    {
                        System.out.println(partnerFavoriteEntry.getName());
                        System.out.println(partnerFavorites.get(i).getName());
                        partnerFavorites.remove(i);
                    }
                }
                Intent notifyUser =  new Intent(context, FirebaseNotificationIntentService.class);
                notifyUser.putExtra("title", "Partner deleted a favorite location");
                notifyUser.putExtra("content","Partner deleted: "+ partnerFavoriteEntry.getName());
                context.startService(notifyUser);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        partnerFavoriteEventListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PartnerFavoriteEntry> copy = new ArrayList<PartnerFavoriteEntry>();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {
                    JSONEntry child = childSnapshot.getValue(JSONEntry.class);
                    PartnerFavoriteEntry favoriteEntry = new PartnerFavoriteEntry(child.getName(),
                                new LatLng(child.getLatitude(),child.getLongitude()));
                    copy.add(favoriteEntry);
                }

                partnerFavorites.clear();

                for(int i=0;i<copy.size();i++)
                {
                    partnerFavorites.add(copy.get(i));
                }
                favoriteSwipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    public static PartnerFavoriteEntry getPartnerFavoriteEntry(String name) {
        for( int i = 0; i < partnerFavorites.size(); i++ ) {
            if(partnerFavorites.get(i).getName().equals(name)) {
                return partnerFavorites.get(i);
            }
        }

        return null;
    }

    public void onUserRegistered(String userKey) {
        Firebase userRef = root.child(userKey);
    }

    public void onPartnerRegistered(String partnerKey) {

        Firebase partnerRef = root.child(partnerKey);
        partnerRef.child("favorite").addChildEventListener(favoriteListner);
    }

    public void onUserCleared(String userKey) {

        Firebase favoriteEntryRef = root.child(userKey).child("favorite");
        favoriteEntryRef.removeValue();
    }

    public void onPartnerCleared(String partnerKey) {
        partnerFavorites.clear();
        Firebase favoriteRef = root.child(partnerKey).child("favorite");
        favoriteRef.removeEventListener(favoriteListner);
    }

    public void onFavoriteAdded(FavoriteEntry entry) {
        try {
            String key = firebaseRegistrationManager.getUserKey();
            lastAddedFavorite = new JSONEntry(entry);
            Firebase favoriteEntryRef = root.child(key).child("favorite").child(lastAddedFavorite.getName());
            System.out.println("Favorite uploaded: "+lastAddedFavorite.getName());
            favoriteEntryRef.setValue(new JSONEntry(entry));

        } catch (UserNotRegisteredException e) {
            //do nothing
        }
    }

    public void onFavoriteDeleted(FavoriteEntry entry) {
        try {
            String key = firebaseRegistrationManager.getUserKey();
            lastDeletedFavorite = new JSONEntry(entry);
            Firebase favoriteEntryRef = root.child(key).child("favorite").child(lastDeletedFavorite.getName());
            favoriteEntryRef.removeValue();
            System.out.println("Favorite Deleted: "+lastDeletedFavorite.getName());

        }catch (UserNotRegisteredException e) {

        }
    }

    public void onLocationVisited(FavoriteEntry entry) {

    }

    public void onLocationDeparted() {

    }

    public ArrayList<PartnerFavoriteEntry> getPartnerFavorite()
    {
        return partnerFavorites;
    }
}
