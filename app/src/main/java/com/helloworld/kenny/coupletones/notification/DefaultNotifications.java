package com.helloworld.kenny.coupletones.notification;

import android.content.Context;

/**
 * Created by Kenny on 6/1/2016.
 */
public class DefaultNotifications {
    private static final long[] DEFAULT_ARRIVAL_VIBRATION_PATTERN = {0L, 500L};
    private static final long[] DEFAULT_DEPARTURE_VIBRATION_PATTERN = {0L, 750L};

    private static VibrationNotification defaultArrivalVibration;
    private static VibrationNotification defaultDepartureVibration;

    public DefaultNotifications(Context context) {
        defaultArrivalVibration = new VibrationNotification("Default Vibration", DEFAULT_ARRIVAL_VIBRATION_PATTERN, context);
        defaultArrivalVibration = new VibrationNotification("Default Vibration", DEFAULT_DEPARTURE_VIBRATION_PATTERN, context);
    }

    public static VibrationNotification getDefaultArrivalVibration() {
        return defaultArrivalVibration;
    }

    public static VibrationNotification getDefaultDepartureVibration() {
        return defaultDepartureVibration;
    }
}
