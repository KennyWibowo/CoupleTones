package com.helloworld.kenny.coupletones.favorites.self;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.helloworld.kenny.coupletones.favorites.Entry;

/**
 * Created by Kenny on 5/3/2016.
 */
public class SelfFavoriteEntry extends Entry {

    private Marker marker;

    public SelfFavoriteEntry(String name, LatLng location) {
        super(name, location);
    }

    public String toString() {
        return this.getName();
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public void visit() {
        setTimestamp(System.currentTimeMillis());
    }


}
