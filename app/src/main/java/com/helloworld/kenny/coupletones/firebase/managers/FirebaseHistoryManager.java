package com.helloworld.kenny.coupletones.firebase.managers;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.Entry;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.JSONEntry;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.firebase.FirebaseFactoryService;
import com.helloworld.kenny.coupletones.firebase.registration.FirebaseRegistrationInformation;
import com.helloworld.kenny.coupletones.firebase.registration.exceptions.SelfNotRegisteredException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseHistoryManager {

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
    private FirebaseRegistrationInformation firebaseRegistrationInformation;
    private ArrayList<PartnerFavoriteEntry> partnerHistory;
    private JSONEntry lastVisitedLocation;

    private Firebase root;

    public FirebaseHistoryManager(FirebaseRegistrationInformation firebaseRegistrationInformation) {
        this.firebaseRegistrationInformation = firebaseRegistrationInformation;
        lastVisitedLocation = new JSONEntry();

    }


    public void visitLocation(FavoriteEntry entry) throws SelfNotRegisteredException {
        String email = firebaseRegistrationInformation.getEmail();

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
                        .concat(FirebaseFactoryService.HISTORY_RESET_TIME));

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        for( int i = 0; i < partnerHistory.size(); i++ ) {
            if(partnerHistory.get(i).getTimestamp().before(tsDeletePoint) && currentTime.after(tsDeletePoint)) {
                partnerHistory.remove(i--);

                //TODO: Update Firebase after deletion.
            }
        }

    }
}
