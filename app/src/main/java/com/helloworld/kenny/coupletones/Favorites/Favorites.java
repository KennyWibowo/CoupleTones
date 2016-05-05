package com.helloworld.kenny.coupletones.Favorites;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Kenny on 5/3/2016.
 */
public class Favorites {

    private ArrayList<FavoriteEntry> favorites;
    private ArrayList<String> favoriteNames;

    public Favorites() {
        favorites = new ArrayList<FavoriteEntry>();
    }

    public void addEntry(String name, LatLng location) {
        FavoriteEntry newFav = new FavoriteEntry(name, location);
        favorites.add(newFav);
        favoriteNames.add(name);
    }

    public void deleteEntry(int position) throws NoSuchElementException{
        favorites.remove(position);
        favoriteNames.remove(position);
    }

    public FavoriteEntry getEntry(int position) {
        FavoriteEntry temp = favorites.get(position);
        return temp;
    }

    public ArrayList<String> getAllEntries() {
        return favoriteNames;
    }

}
