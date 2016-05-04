package com.helloworld.kenny.coupletones.Favorites;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kenny on 5/3/2016.
 */
public class FavoriteEntry extends Entry {

    public FavoriteEntry(String name, LatLng location) {
        super(name, location);
    }

    public String toString() {
        return this.getName();
    }


}
