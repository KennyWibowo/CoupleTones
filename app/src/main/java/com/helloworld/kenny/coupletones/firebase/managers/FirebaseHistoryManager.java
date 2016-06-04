package com.helloworld.kenny.coupletones.firebase.managers;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.sql.Array;
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
    //private final FavoriteSwipeAdapter<PartnerFavoriteEntry> partnerHistoryAdapter;
    private final HistoryAdapter partnerHistoryAdapter;
    private JSONEntry lastVisitedLocation;

    private final Firebase root;

    public FirebaseHistoryManager(FirebaseRegistrationManager firebaseRegistrationManager, final Context context) {
        root = new Firebase(FirebaseService.ENDPOINT);
        partnerHistory = new ArrayList<>();

        this.firebaseRegistrationManager = firebaseRegistrationManager;
        this.lastVisitedLocation = new JSONEntry();
        this.lastVisitedLocation.setDeparted(true);
        this.partnerHistoryAdapter = new HistoryAdapter(context, partnerHistory);

        historyListener= new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                final JSONEntry child = dataSnapshot.getValue(JSONEntry.class);
                PartnerFavoriteEntry historyEntry = new PartnerFavoriteEntry(child.getName(), new LatLng(child.getLatitude(), child.getLongitude()));
                PartnerFavoriteEntry partnerEntry = FirebaseFavoriteManager.getPartnerFavoriteEntry(child.getName());
                historyEntry.setTimestamp(child.getTimestamp());

                System.out.println("Partner visited: " + historyEntry.getName());

                Intent notifyUser = new Intent(context, FirebaseNotificationIntentService.class);
                notifyUser.putExtra("title", "Partner reached a favorite location!");
                notifyUser.putExtra("content", "Partner reached: " + historyEntry.getName());

                Time timeRange = new Time(System.currentTimeMillis() - AlarmManager.INTERVAL_HALF_HOUR);

                if(timeRange.before(historyEntry.getTimestamp())) {
                    context.startService(notifyUser);

                    if( partnerEntry != null ) {
                        //TODO: call this when ready
                        partnerEntry.onPartnerArrived();
                        System.out.println("Partner has arrived - playing arrived tone/vibration");
                    }
                }

                if(!child.getDeparted()) {
                    dataSnapshot.getRef().child("departed").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot departedSnapshot) {
                            if(departedSnapshot.getValue() != null && departedSnapshot.getValue().equals(true)) {
                                PartnerFavoriteEntry departedEntry = FirebaseFavoriteManager.getPartnerFavoriteEntry(child.getName());

                                if( departedEntry != null ) {
                                    //TODO: call this when ready:
                                    departedEntry.onPartnerDeparted();
                                    System.out.println("Partner has departed - playing departed tone/vibration");
                                }

                                dataSnapshot.getRef().child("departed").removeEventListener(this);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
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
                Firebase historyEntryRef = root.child(key).child("history").child(entry.getTimestamp().getTime() + "");

                lastVisitedLocation = new JSONEntry(entry);
                historyEntryRef.setValue(new JSONEntry(entry));
            }
        } catch (UserNotRegisteredException e) {
            //do nothing
        }

    }

    public void onLocationDeparted() {
        try {
            String key = firebaseRegistrationManager.getUserKey();

            if (lastVisitedLocation.getName() != null && !lastVisitedLocation.getDeparted()) {
                //depart last location
                root.child(key).child("history").child(lastVisitedLocation.getTimestamp() + "").child("departed").setValue(true);
                lastVisitedLocation.setDeparted(true);
            }
        } catch (UserNotRegisteredException e) {
            //do nothing
        }
    }


    public ArrayAdapter<PartnerFavoriteEntry> getPartnerHistoryAdapter() {
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

    public class HistoryAdapter extends ArrayAdapter<PartnerFavoriteEntry> {
        Context context;

        public HistoryAdapter(Context context, ArrayList<PartnerFavoriteEntry> historyItems) {
            super(context, 0, historyItems);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PartnerFavoriteEntry entry = getItem(position);

            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.history_view, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.history_name_text);
            TextView time = (TextView) convertView.findViewById(R.id.history_time_text);

            String t = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(entry.getTimestamp());

            name.setText(entry.getName());
            time.setText(t);

            return convertView;
        }
    }

}
