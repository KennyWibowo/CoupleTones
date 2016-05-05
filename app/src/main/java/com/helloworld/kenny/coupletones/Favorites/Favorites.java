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
        favoriteNames = new ArrayList<String>();
    }

    public void addEntry(String name, LatLng location) throws Exception{
        if(lookupPosition(name) != -1)
            throw new Exception("Name already in use");

        FavoriteEntry newFav = new FavoriteEntry(name, location);
        favorites.add(newFav);
        favoriteNames.add(name);
    }

    public void deleteEntry(int position) throws NoSuchElementException{
        favorites.remove(position);
        favoriteNames.remove(position);
    }

    public int lookupPosition(String name) {
        for(int i = 0; i <favoriteNames.size(); i++) {
            if(favoriteNames.get(i).equals(name))
                return i;
        }
        return -1;
    }

    public int size() {
        return favorites.size();
    }

    public FavoriteEntry getEntry(int position) {
        FavoriteEntry temp = favorites.get(position);
        return temp;
    }

    public ArrayList<String> getAllEntries() {
        return favoriteNames;
    }

}
