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

    private HashMap<String, FavoriteEntry> favorites;

    public Favorites() {
        favorites = new HashMap<String, FavoriteEntry>();
    }

    public void addEntry(String name, LatLng location) {
        FavoriteEntry newFav = new FavoriteEntry(name, location);
        favorites.put(name, newFav);
    }

    public void deleteEntry(String name) throws NoSuchElementException{
        FavoriteEntry temp = favorites.remove(name);
        if(temp == null) {
            throw new NoSuchElementException("Favorite Entry Does Not Exist");
        }
    }

    public FavoriteEntry getEntry(String name) {
        FavoriteEntry temp = favorites.get(name);
        if(temp == null) {
            throw new NoSuchElementException("Favorite Entry Does Not Exist");
        } else {
            return temp;
        }
    }

    public ArrayList<String> getAllEntries() {
        ArrayList<String> allFavs = new ArrayList<String>();

        for(Map.Entry<String, FavoriteEntry> e : favorites.entrySet()) {
            String key = e.getKey();
            FavoriteEntry val = e.getValue();

            allFavs.add(val.toString());
        }

        return allFavs;
    }

}
