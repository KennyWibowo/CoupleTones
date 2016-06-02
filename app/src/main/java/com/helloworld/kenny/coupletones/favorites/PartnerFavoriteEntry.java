package com.helloworld.kenny.coupletones.favorites;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.favorites.Entry;
import com.helloworld.kenny.coupletones.notification.DefaultNotifications;
import com.helloworld.kenny.coupletones.notification.ToneNotification;
import com.helloworld.kenny.coupletones.notification.VibrationNotification;

/**
 * Created by Kenny on 5/24/2016.
 */
public class PartnerFavoriteEntry extends Entry {
    private ToneNotification tone;
    private VibrationNotification vibration;

    public PartnerFavoriteEntry(String name, LatLng location) {
        super(name, location);
        vibration = DefaultNotifications.getDefaultVibration();
    }

    //TODO: add functionality to bind tones/vibrations
}
