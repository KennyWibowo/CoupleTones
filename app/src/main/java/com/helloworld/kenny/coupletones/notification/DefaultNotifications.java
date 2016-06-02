package com.helloworld.kenny.coupletones.notification;

import android.content.Context;

/**
 * Created by Kenny on 6/1/2016.
 */
public class DefaultNotifications {
    private static final long[] DEFAULT_VIBRATION_PATTERN = {0L, 500L};

    private static VibrationNotification defaultVibration;

    public DefaultNotifications(Context context) {
        defaultVibration = new VibrationNotification("Default Vibration", DEFAULT_VIBRATION_PATTERN, context);
    }

    public static VibrationNotification getDefaultVibration() {
        return defaultVibration;
    }
}
