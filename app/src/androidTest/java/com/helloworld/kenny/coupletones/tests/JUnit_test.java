package com.helloworld.kenny.coupletones.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.MapsActivity;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.registration.PartnerInformation;

/**
 * Created by Karen on 5/8/2016.
 */
public class JUnit_test extends ActivityInstrumentationTestCase2<MapsActivity> {

    MapsActivity maps;

    public JUnit_test() {
        super(MapsActivity.class);
    }

    /**
     * Tests add functionality of Favorites data structure
     * @throws Exception
     */

    public void test_add() throws Exception
    {
        Favorites favorites = new Favorites();
        boolean check = false;
        favorites.addEntry("Library", new LatLng(45, 45));
        try {

            favorites.addEntry("Library", new LatLng(45, 45));
        } catch (NameInUseException ex) {
            check = true;
            assertTrue(check);
        }

        if(favorites.size() != 1)
        {
            check = false;
        }
        assertTrue(check);
    }

    /**
     * Tests size functionality of Favorites data structure
     * @throws Exception
     */
    public void test_size() throws Exception
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


    /**
     * Tests deleteEntry functionality of Favorites data structure
     * @throws Exception
     */
    public void test_entry() throws Exception
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


    /**
     * Tests existence method of Favorites data structure
     * @throws Exception
     */
    public void test_exist() throws Exception
    {
        Favorites favorites = new Favorites();
        FavoriteEntry list = new FavoriteEntry("Taco Bell", new LatLng(68, 68));
        favorites.addEntry("Taco Bell", new LatLng(68, 68));
        assertEquals(list.toString(), favorites.getEntry(0).toString());
    }

    /**
     * Tests existence method of PartnerInformation data structure
     * @throws Exception
     */
    public void test_partnerInfo() throws Exception {
        PartnerInformation partnerInformation = new PartnerInformation();
        String id = "someID";
        String partnerId = "somePartner";
        String email = "someemail@email.com";

        partnerInformation.registerOwnRegId(id);
        partnerInformation.registerPartner(partnerId, email);

        assertEquals(PartnerInformation.getOwnRegId(), id);
        assertEquals(PartnerInformation.getRegId(), partnerId);
        assertEquals(PartnerInformation.getEmail(), email);
    }



}
