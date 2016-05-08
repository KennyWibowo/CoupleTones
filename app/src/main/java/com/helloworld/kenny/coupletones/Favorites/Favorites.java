package com.helloworld.kenny.coupletones.favorites;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/3/2016.
 */
public class Favorites {

    private ArrayList<FavoriteEntry> favorites;
    private ArrayList<String> favoriteNames;

    public Favorites() {
        favorites = new ArrayList<FavoriteEntry>();
    }

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

    public String toString() {
        StringBuilder sb = new StringBuilder("Entries: ");

        for(int i = 0; i < favorites.size()-1; i++) {
            sb.append(favorites.get(i).toString() + ", ");
        }

        if(favorites.size()>0) {
            sb.append(favorites.get(favorites.size()-1));
        } else {
            sb.append("(empty)");
        }

        return sb.toString();
    }

}