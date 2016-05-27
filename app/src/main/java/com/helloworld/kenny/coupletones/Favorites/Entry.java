package com.helloworld.kenny.coupletones.favorites;

import com.google.android.gms.maps.model.LatLng;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setTimestamp(long millis) {
        this.timestamp = new Timestamp(millis);
    }

    public String getName() {
        return this.name;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

}
