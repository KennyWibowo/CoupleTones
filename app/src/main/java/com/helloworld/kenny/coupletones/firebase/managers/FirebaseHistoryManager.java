package com.helloworld.kenny.coupletones.firebase.managers;

import android.content.Context;
import android.content.Intent;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.JSONEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.FirebaseNotificationIntentService;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerNotRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserNotRegisteredException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseHistoryManager extends FirebaseManager {

    private ChildEventListener historyListener;
    private FirebaseRegistrationManager firebaseRegistrationManager;
    private ArrayList<PartnerFavoriteEntry> partnerHistory;
    private JSONEntry lastVisitedLocation;

    private Firebase root;

    public FirebaseHistoryManager(FirebaseRegistrationManager firebaseRegistrationManager, final Context context) {
        root = new Firebase(FirebaseService.ENDPOINT);
        partnerHistory = new ArrayList<>();

        this.firebaseRegistrationManager = firebaseRegistrationManager;
        lastVisitedLocation = new JSONEntry();

        historyListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
                PartnerFavoriteEntry historyEntry = new PartnerFavoriteEntry(child.getName(), new LatLng(child.getLatitude(), child.getLongitude()));
                historyEntry.setTimestamp(child.getTimestamp());
                partnerHistory.add(historyEntry);

                System.out.println("Partner visited: " + historyEntry.getName());

                System.out.println(partnerHistory.toString());

                Intent notifyUser = new Intent(context, FirebaseNotificationIntentService.class);
                notifyUser.putExtra("title", "Partner reached a favorite location!");
                notifyUser.putExtra("content", "Partner reached: " + historyEntry.getName());

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

    public void onLocationVisited(FavoriteEntry entry) {

        try {
            String email = firebaseRegistrationManager.getEmail();

            if (lastVisitedLocation.getName() == null || !lastVisitedLocation.getName().equals(entry.getName())) {
                Firebase historyEntryRef = root.child("" + email.hashCode()).child("history").push();

                lastVisitedLocation = new JSONEntry(entry);
                historyEntryRef.setValue(new JSONEntry(entry));
            }
        } catch (UserNotRegisteredException e) {
            //do nothing
        }

    }

    public ArrayList<PartnerFavoriteEntry> getPartnerHistory() {
        return partnerHistory;
    }

    // TODO: deprecate this, only use this as reference?
    private void updatePartnerHistory() {
        Timestamp tsDeletePoint = Timestamp.valueOf(
                new SimpleDateFormat("yyyy-MM-dd ")
                        .format(new Date())
                        .concat(FirebaseService.HISTORY_RESET_TIME));

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < partnerHistory.size(); i++) {
            if (partnerHistory.get(i).getTimestamp().before(tsDeletePoint) && currentTime.after(tsDeletePoint)) {
                partnerHistory.remove(i--);
            }
        }

    }

    public void onUserRegistered() {

    }

    public void onPartnerRegistered() {
        //TODO: add listener to clear partner's history at 3 AM (clear both Firebase and local)
        //TODO: partnerRef.child("history").addChildEventListener(historyListener);

        String partnerKey = firebaseRegistrationManager.getPartnerKey();
        Firebase partnerRef = root.child(partnerKey);
        partnerRef.child("history").addChildEventListener(historyListener);
    }

    public void onUserCleared() {
        this.lastVisitedLocation = new JSONEntry();
    }

    public void onPartnerCleared() {
        root.child("" + firebaseRegistrationManager.getPartnerKey()).child("history").removeEventListener(historyListener);
    }
}
