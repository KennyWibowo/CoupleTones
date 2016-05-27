package com.helloworld.kenny.coupletones.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.Entry;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.partner.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.registration.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.registration.exceptions.SelfAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.registration.exceptions.SelfNotRegisteredException;

import java.sql.Time;
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
    private JSONEntry lastVisitedLocation;
    private ArrayList<PartnerFavoriteEntry> partnerFavorites;
    private ArrayList<PartnerFavoriteEntry> partnerHistory;

    private SharedPreferences sharedPreferences;
    private Firebase root;

    private static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM
    public static final String endpoint = "https://coupletonesteam6.firebaseio.com/";

    private ChildEventListener historyListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
            PartnerFavoriteEntry historyEntry = new PartnerFavoriteEntry(child.getName(), new LatLng(child.getLatitude(), child.getLongitude()));
            historyEntry.setTimestamp(child.getTimestamp());
            partnerHistory.add(historyEntry);

            System.out.println("Partner visited: " + historyEntry.getName());

            System.out.println(partnerHistory.toString());
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

    public RegistrationInformation(Context context) {
        this.email = null;
        this.partnerEmail = null;
        this.selfRegistered = false;
        this.partnerRegistered = false;
        this.sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        lastVisitedLocation = new JSONEntry();

        partnerHistory = new ArrayList<PartnerFavoriteEntry>();
        partnerFavorites = new ArrayList<PartnerFavoriteEntry>();

        root = new Firebase(endpoint);

    }

    public void registerPartner(String partnerEmail) throws PartnerAlreadyRegisteredException {
        if(partnerRegistered) {
            throw new PartnerAlreadyRegisteredException("Partner already registered");
        }

        Firebase partnerRef = root.child("" + partnerEmail.hashCode());
        //TODO: add listener to clear partner's history at 3 AM

        partnerRef.child("history").addChildEventListener(historyListener);
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

    public void changeEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void visitLocation(FavoriteEntry entry) throws SelfNotRegisteredException {
        if(!selfRegistered) {
            throw new SelfNotRegisteredException("Self not yet registered");
        }

        if(lastVisitedLocation.getName() == null || !lastVisitedLocation.getName().equals(entry.getName())) {
            Firebase historyEntryRef = root.child( "" + email.hashCode()).child("history").push();

            lastVisitedLocation = new JSONEntry(entry);
            historyEntryRef.setValue(new JSONEntry(entry));
        }
    }

    public ArrayList<PartnerFavoriteEntry> getPartnerHistory() {
        //updatePartnerHistory();

        return partnerHistory;
    }

    private void updatePartnerHistory() {
        Timestamp tsDeletePoint = Timestamp.valueOf(
                        new SimpleDateFormat("yyyy-MM-dd ")
                        .format(new Date())
                        .concat(HISTORY_RESET_TIME));

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        for( int i = 0; i < partnerHistory.size(); i++ ) {
            if(partnerHistory.get(i).getTimestamp().before(tsDeletePoint) && currentTime.after(tsDeletePoint)) {
                partnerHistory.remove(i--);

                //TODO: Update Firebase after deletion.
            }
        }


    }

    public boolean isSelfRegistered() {
        return selfRegistered;
    }

    public void clearPartner() {
        root.child("" + partnerEmail.hashCode()).child("history").removeEventListener(historyListener);

        this.partnerEmail = null;
        this.partnerRegistered = false;
    }

    public void clearSelf() {
        this.email = null;
        this.selfRegistered = false;
        this.lastVisitedLocation = new JSONEntry();
    }
}

class JSONEntry {
    private String name;
    private long timestamp;
    private double latitude;
    private double longitude;

    public JSONEntry() {}

    public JSONEntry(Entry entry) {
        this.name = entry.getName();
        this.timestamp = entry.getTimestamp().getTime();
        this.latitude = entry.getLocation().latitude;
        this.longitude = entry.getLocation().longitude;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}