package com.helloworld.kenny.coupletones.notification;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;

import com.helloworld.kenny.coupletones.settings.Settings;

/**
 * Created by Kenny on 6/1/2016.
 */
public class ToneNotification extends Notification {

    private String name;
    Ringtone tone;



    public ToneNotification(String name, Ringtone tone) {
        this.name = name;
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
}
