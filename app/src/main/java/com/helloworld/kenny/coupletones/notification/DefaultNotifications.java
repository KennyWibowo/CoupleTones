package com.helloworld.kenny.coupletones.notification;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by Kenny on 6/1/2016.
 */
public class DefaultNotifications {
    private static final long[] DEFAULT_ARRIVAL_VIBRATION_PATTERN = {0L, 500L};
    private static final long[] DEFAULT_DEPARTURE_VIBRATION_PATTERN = {0L, 750L};

    private static VibrationNotification defaultArrivalVibration;
    private static VibrationNotification defaultDepartureVibration;
    private static ToneNotification defaultArrivalTone;
    private static ToneNotification defaultDepartureTone;

    public DefaultNotifications(Context context) {
        defaultArrivalVibration = new VibrationNotification("Default Vibration", DEFAULT_ARRIVAL_VIBRATION_PATTERN, context);
        defaultDepartureVibration = new VibrationNotification("Default Vibration", DEFAULT_DEPARTURE_VIBRATION_PATTERN, context);
        Uri base = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone common = RingtoneManager.getRingtone(context,base);
        defaultArrivalTone = new ToneNotification("Default Tone", common);
        defaultDepartureTone = new ToneNotification("Default Tone", common);

    }

    public static VibrationNotification getDefaultArrivalVibration() {
        return defaultArrivalVibration;
    }

    public static VibrationNotification getDefaultDepartureVibration() {
        return defaultDepartureVibration;
    }

    public static ToneNotification getDefaultArrivalTone() {
        return defaultArrivalTone;
    }

    public static ToneNotification getDefaultDepartureTone(){
        return defaultDepartureTone;
    }
}
