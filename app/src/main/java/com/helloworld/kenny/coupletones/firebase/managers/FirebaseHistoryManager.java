package com.helloworld.kenny.coupletones.firebase.managers;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.FavoriteSwipeAdapter;
import com.helloworld.kenny.coupletones.R;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.JSONEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerNotRegisteredException;
import com.helloworld.kenny.coupletones.firebase.intents.FirebaseNotificationIntentService;
import com.helloworld.kenny.coupletones.firebase.FirebaseService;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserNotRegisteredException;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseHistoryManager extends FirebaseManager {

    private ChildEventListener historyListener;
    private ValueEventListener historyMultipleEventListner;
    private FirebaseRegistrationManager firebaseRegistrationManager;
    private final ArrayList<PartnerFavoriteEntry> partnerHistory;
    private final FavoriteSwipeAdapter<PartnerFavoriteEntry> partnerHistoryAdapter;
    private JSONEntry lastVisitedLocation;

    private Firebase root;

    public FirebaseHistoryManager(FirebaseRegistrationManager firebaseRegistrationManager, final Context context) {
        root = new Firebase(FirebaseService.ENDPOINT);
        partnerHistory = new ArrayList<>();

        this.firebaseRegistrationManager = firebaseRegistrationManager;
        this.lastVisitedLocation = new JSONEntry();
        this.partnerHistoryAdapter = new FavoriteSwipeAdapter<>(context, R.layout.listview_item, R.id.listview_item_text, partnerHistory);

        historyListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
                PartnerFavoriteEntry historyEntry = new PartnerFavoriteEntry(child.getName(), new LatLng(child.getLatitude(), child.getLongitude()));
                historyEntry.setTimestamp(child.getTimestamp());

                System.out.println("Partner visited: " + historyEntry.getName());

                System.out.println(partnerHistory.toString());

                Intent notifyUser = new Intent(context, FirebaseNotificationIntentService.class);
                notifyUser.putExtra("title", "Partner reached a favorite location!");
                notifyUser.putExtra("content", "Partner reached: " + historyEntry.getName());

                Time timeRange = new Time(System.currentTimeMillis() - AlarmManager.INTERVAL_HALF_HOUR);

                if(timeRange.before(historyEntry.getTimestamp())) { //TODO: fix this
                    context.startService(notifyUser);
                }

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

        historyMultipleEventListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PartnerFavoriteEntry> copy = new ArrayList<PartnerFavoriteEntry>();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {
                    JSONEntry child = childSnapshot.getValue(JSONEntry.class);
                    PartnerFavoriteEntry historyEntry = new PartnerFavoriteEntry(child.getName(), new LatLng(child.getLatitude(), child.getLongitude()));
                    historyEntry.setTimestamp(child.getTimestamp());
                    copy.add(historyEntry);
                }
                partnerHistory.clear();

                for(int i = 0; i < copy.size(); i++ ) {
                    partnerHistory.add(copy.get(i));
                }

                partnerHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    public void onLocationVisited(FavoriteEntry entry) {

        try {
            String key = firebaseRegistrationManager.getUserKey();

            if (lastVisitedLocation.getName() == null || !lastVisitedLocation.getName().equals(entry.getName())) {
                Firebase historyEntryRef = root.child(key).child("history").push();

                lastVisitedLocation = new JSONEntry(entry);
                historyEntryRef.setValue(new JSONEntry(entry));
            }
        } catch (UserNotRegisteredException e) {
            //do nothing
        }

    }

    public FavoriteSwipeAdapter<PartnerFavoriteEntry> getPartnerHistoryAdapter() {
        return partnerHistoryAdapter;
    }

    public ArrayList<PartnerFavoriteEntry> getPartnerHistory() {
        return partnerHistory;
    }


    public void onUserRegistered(String userKey) {
        Firebase userRef = root.child(userKey);

    }

    public void onPartnerRegistered(String partnerKey) {

        Firebase historyRef = root.child(partnerKey).child("history");
        historyRef.addChildEventListener(historyListener);
        historyRef.addValueEventListener(historyMultipleEventListner);


    }

    public void onUserCleared(String userKey) {
        this.lastVisitedLocation = new JSONEntry();
    }

    public void onPartnerCleared(String partnerKey) {
        partnerHistory.clear();
        Firebase historyRef = root.child(partnerKey).child("history");
        historyRef.removeEventListener(historyListener);
        historyRef.removeEventListener(historyMultipleEventListner);
    }

    public void onFavoriteAdded(FavoriteEntry entry){}

    public void onFavoriteDeleted(FavoriteEntry entry){}

}
