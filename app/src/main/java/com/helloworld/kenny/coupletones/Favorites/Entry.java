package com.helloworld.kenny.coupletones.Favorites;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Kenny on 5/3/2016.
 */
public abstract class Entry {

    private String name;
    private LatLng location;
    private Timestamp timestamp;

    public Entry(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public void setTimestamp(long millis) {
        this.timestamp = new Timestamp(millis);
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

}
