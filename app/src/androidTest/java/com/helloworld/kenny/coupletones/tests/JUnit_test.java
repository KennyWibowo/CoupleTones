package com.helloworld.kenny.coupletones.tests;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.MapsActivity;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserNotRegisteredException;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseRegistrationManager;
import com.helloworld.kenny.coupletones.notification.DefaultNotifications;
import com.helloworld.kenny.coupletones.notification.ToneNotification;
import com.helloworld.kenny.coupletones.notification.VibrationNotification;

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
     * Tests User data from FirebaseRegistrationManager
     * @throws Exception
     */
    public void test_userEMail() throws Exception {
        FirebaseRegistrationManager registrationInformation = new FirebaseRegistrationManager(getActivity().getApplicationContext());
        boolean check = false;
        String email = "someemail@email.com";

            registrationInformation.registerUser(email);
        try {
            registrationInformation.registerUser(email);
        }
        catch (UserAlreadyRegisteredException ex)
        {
            check = true;
        }
            assertTrue(check);
            assertEquals(registrationInformation.getEmail(), email);
            assertEquals(registrationInformation.isUserRegistered(), true);
    }


    /**
     * Tests Partner data from FirebaseRegistrationManager
     * @throws Exception
     */
    public void test_partnerEMail() throws Exception {
        FirebaseRegistrationManager registrationInformation = new FirebaseRegistrationManager(getActivity().getApplicationContext());
        String email = "someemail@email.com";

        boolean check = false;


        registrationInformation.registerPartner(email);
        try {
            registrationInformation.registerPartner(email);
        }
        catch (PartnerAlreadyRegisteredException ex)
        {
            check = true;
        }

        assertTrue(check);
        assertEquals(registrationInformation.getPartnerEmail(), email);
    }


    /**
     * Tests ToneNotification construction
     * @throws Exception
     */
    public void test_Tone() throws Exception {
        Uri base = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone common = RingtoneManager.getRingtone(getActivity().getApplicationContext(),base);
        ToneNotification testTone = new ToneNotification("Test", common);
        assertEquals(testTone.toString(), "Test");
        assertTrue(testTone.getTone().equals(common));
    }

    /**
     * Tests VibrationNotification construction
     * @throws Exception
     */
    public void test_Vibration() throws Exception {
        final long[] VIBRATION_PATTERN = {0L, 999L};
        VibrationNotification testVib = new VibrationNotification("Test", VIBRATION_PATTERN, getActivity().getApplicationContext());
        assertEquals(testVib.toString(), "Test");
        assertTrue(testVib.getPattern().equals(VIBRATION_PATTERN));
    }



    /**
     * Tests Default ToneNotification construction
     * @throws Exception
     */
    public void test_DefaultTone() throws Exception{
        DefaultNotifications defaults = new DefaultNotifications(getActivity().getApplicationContext());
        assertEquals(defaults.getDefaultArrivalTone().toString(), "Default Tone");
        assertEquals(defaults.getDefaultDepartureTone().toString(), "Default Tone");
    }

    /**
     * Tests Default VibrationNotification construction
     * @throws Exception
     */
    public void test_DefaultVibration() throws Exception {
        DefaultNotifications defaults = new DefaultNotifications(getActivity().getApplicationContext());
        assertEquals(defaults.getDefaultArrivalVibration().toString(), "Default Vibration");
        assertEquals(defaults.getDefaultDepartureVibration().toString(), "Default Vibration");
    }



}
