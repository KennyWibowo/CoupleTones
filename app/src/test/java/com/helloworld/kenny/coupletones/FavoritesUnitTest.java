package com.helloworld.kenny.coupletones;

import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;


import com.helloworld.kenny.coupletones.favorites.*;


import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.exceptions.*;

import org.junit.Test;

import java.util.Map;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

public class FavoritesUnitTest extends ActivityInstrumentationTestCase2<MapsActivity>
{

    public FavoritesUnitTest()
    {
        super(MapsActivity.class);
    }

    @Test
    public void check_repeat_add() throws Exception
    {
        Favorites favorites = new Favorites();
        boolean check = false;
        favorites.addEntry("Library", new LatLng(45, 45));
        try {

            favorites.addEntry("Library", new LatLng(45, 45));
        } catch (InvalidNameException ex) {
            check = true;
            assertTrue(check);
        }
        assertTrue(check);

    }

    @Test
    public void check_add_size() throws Exception
    {
        Favorites favorites = new Favorites();
        favorites.addEntry("Library", new LatLng(45, 45));
        favorites.addEntry("Church", new LatLng(25, 65));
        favorites.addEntry("Supermarket", new LatLng(83, 29));
        favorites.addEntry("Class", new LatLng(32, 24));
        favorites.addEntry("Home", new LatLng(335, 554));
        favorites.addEntry("Restaurant", new LatLng(934, 753));
        favorites.addEntry("Fair", new LatLng(675, 875));
        assertEquals(7, favorites.size());
    }

    @Test
    public void check_lost_entry() throws Exception
    {
        Favorites favorites = new Favorites();
        favorites.addEntry("Library", new LatLng(45, 45));
        favorites.addEntry("Church", new LatLng(25, 65));
        favorites.addEntry("Supermarket", new LatLng(83, 29));
        favorites.addEntry("Class", new LatLng(32, 24));
        favorites.addEntry("Home", new LatLng(335, 554));
        favorites.addEntry("Restaurant", new LatLng(934, 753));
        favorites.addEntry("Fair", new LatLng(675, 875));
        favorites.deleteEntry(0);
        assertEquals("Church", favorites.getEntry(0).getName());
    }

    @Test
    public void check_entry_exists() throws Exception
    {
        Favorites favorites = new Favorites();
        FavoriteEntry list = new FavoriteEntry("Taco Bell", new LatLng(68, 68));
        favorites.addEntry("Taco Bell", new LatLng(68, 68));
        assertEquals(list.toString(), favorites.getEntry(0).toString());
    }
}