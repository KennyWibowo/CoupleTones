package com.helloworld.kenny.coupletones.settings;

/**
 * Created by Karen on 5/26/2016.
 */
public class Settings {
    private static boolean tones = true;
    private static boolean vibrations = true;

    public Settings() {
        this.tones = true;
        this.vibrations = true;
    }

    public static boolean tonesEnabled() {
        return tones;
    }

    public static boolean vibrationsEnabled() {
        return vibrations;
    }

    public void enableTones() {
        this.tones = true;
    }

    public void disableTones() {
        this.tones = false;
    }

    public void enableVibrations() {
        this.vibrations = true;
    }

    public void disableVibrations() {
        this.vibrations = false;
    }
}
