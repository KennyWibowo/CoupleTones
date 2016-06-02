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
    private ToneNotification arriveTone;
    private ToneNotification departTone;
    private VibrationNotification arriveVibration;
    private VibrationNotification departVibration;

    public PartnerFavoriteEntry(String name, LatLng location) {
        super(name, location);
        arriveVibration = DefaultNotifications.getDefaultArrivalVibration();
        departVibration = DefaultNotifications.getDefaultDepartureVibration();

        //TODO: assign default tones
    }

    public void onPartnerArrived() {
        arriveVibration.play();
        arriveTone.play();
    }

    public void onPartnerDeparted() {
        departVibration.play();
        departTone.play();
    }

}
