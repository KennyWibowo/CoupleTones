package com.helloworld.kenny.coupletones.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.IBinder;

import com.helloworld.kenny.coupletones.MapsActivity;
import com.helloworld.kenny.coupletones.settings.Settings;

/**
 * Created by Kenny on 6/1/2016.
 */
public class ToneNotification extends Notification {

    private String name;
    Ringtone tone;
    Context context;
    Uri uri;



    public ToneNotification(String name, Ringtone tone, Context context) {
        this.name = name;
        this.tone = tone;
        this.context = context;
    }

    public void setTone(Ringtone tone) {
        this.tone = tone;
    }


    public void play() {
        if(Settings.tonesEnabled()) {
            tone.play();
        }
    }

    public String toString() {

        return name;
    }

    public Ringtone getTone() {
        return tone;
    }



}
