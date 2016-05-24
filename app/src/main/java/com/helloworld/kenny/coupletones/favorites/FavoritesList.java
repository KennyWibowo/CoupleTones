package com.helloworld.kenny.coupletones.favorites;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.favorites.self.SelfFavoriteEntry;

import java.util.ArrayList;

/**
 * Created by Kenny on 5/3/2016.
 */

//TODO: refactor to extend ArrayList
public class FavoritesList<E> extends ArrayList<E>{

    public FavoritesList() {
        super();
    }

    public void addEntry(String name, LatLng location) throws InvalidNameException, NameInUseException {
        if(lookupPosition(name) != -1)
            throw new NameInUseException("Name already in use");
        if(name.equals(""))
            throw new InvalidNameException("Invalid name");

        SelfFavoriteEntry newFav = new SelfFavoriteEntry(name, location);

        this.add((E) newFav);
    }

    public void deleteEntry(int position) {
        this.remove(position);
    }

    public int lookupPosition(String name) {
        for(int i = 0; i <this.size(); i++) {
            if(this.get(i).toString().equals(name))
                return i;
        }
        return -1;
    }

    public int size() {
        return super.size();
    }

    public Entry getEntry(int position) {
        E temp = this.get(position);
        return (Entry) temp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Entries: ");

        for(int i = 0; i < this.size()-1; i++) {
            sb.append(this.get(i).toString() + ", ");
        }

        if(this.size()>0) {
            sb.append(this.get(this.size()-1));
        } else {
            sb.append("(empty)");
        }

        return sb.toString();
    }

}