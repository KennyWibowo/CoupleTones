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
        arriveTone = DefaultNotifications.getDefaultArrivalTone();
        departTone = DefaultNotifications.getDefaultDepartureTone();

        //TODO: assign new tones based on user input
    }

    public void setPartnerArrivedTone(ToneNotification arrivedTone) {
        this.arriveTone = arrivedTone;
    }

    public void setPartnerDepartedTone(ToneNotification departTone) {
        this.departTone = departTone;
    }

    public void setPartnerArrivedVibration(VibrationNotification arriveVibration) {
        this.arriveVibration = arriveVibration;
    }

    public void setPartnerDepartedVibration(VibrationNotification departVibration) {
        this.departVibration = departVibration;
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
