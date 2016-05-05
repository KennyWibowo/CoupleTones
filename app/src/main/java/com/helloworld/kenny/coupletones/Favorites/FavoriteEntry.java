package com.helloworld.kenny.coupletones.Favorites;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Kenny on 5/3/2016.
 */
public class FavoriteEntry extends Entry {

    private Marker marker;

    public FavoriteEntry(String name, LatLng location) {
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


}
