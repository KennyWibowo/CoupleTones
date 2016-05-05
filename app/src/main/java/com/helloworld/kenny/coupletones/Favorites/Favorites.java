package com.helloworld.kenny.coupletones.Favorites;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.helloworld.kenny.coupletones.Favorites.Exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.Favorites.Exceptions.NameInUseException;

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

    //TODO change these exceptions to specific ones
    public void addEntry(String name, LatLng location) throws InvalidNameException, NameInUseException {
        if(lookupPosition(name) != -1)
            throw new NameInUseException("Name already in use");
        if(name.equals(""))
            throw new InvalidNameException("Invalid name");

        FavoriteEntry newFav = new FavoriteEntry(name, location);
        favorites.add(newFav);
    }

    public void deleteEntry(int position) {
        favorites.remove(position);
    }

    public int lookupPosition(String name) {
        for(int i = 0; i <favorites.size(); i++) {
            if(favorites.get(i).toString().equals(name))
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

    public ArrayList<FavoriteEntry> getAllEntries() {
        return favorites;
    }

}