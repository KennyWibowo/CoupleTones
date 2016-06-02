package com.helloworld.kenny.coupletones.notification;

import android.content.Context;
import android.os.Vibrator;

import com.helloworld.kenny.coupletones.settings.Settings;

/**
 * Created by Kenny on 6/1/2016.
 */
public class VibrationNotification extends Notification {

    private String name;
    long[] pattern;
    Context context;
    Vibrator v;

    public VibrationNotification(String name, long[] pattern, Context context) {
        this.name = name;
        this.pattern = pattern;
        this.context = context;

        this.v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void play() {
        if(Settings.vibrationsEnabled()) {
            v.vibrate(pattern, -1);
        }
    }

    public String toString() {
        return name;
    }
}
