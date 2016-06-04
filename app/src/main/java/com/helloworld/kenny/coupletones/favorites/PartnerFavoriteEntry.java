package com.helloworld.kenny.coupletones.favorites;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.MapsActivity;
import com.helloworld.kenny.coupletones.R;
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
    private ToneNotification arriveStandard;
    private ToneNotification departStandard;
    private VibrationNotification arriveVibration;
    private VibrationNotification departVibration;
    private Context context;

    public PartnerFavoriteEntry(String name, LatLng location, Context context) {
        super(name, location);
        this.context = context;
        arriveVibration = DefaultNotifications.getDefaultArrivalVibration();
        departVibration = DefaultNotifications.getDefaultDepartureVibration();
        arriveTone = DefaultNotifications.getDefaultArrivalTone();
        departTone = DefaultNotifications.getDefaultDepartureTone();
        Uri base = Uri.parse("android.resource://" + "com.helloworld.kenny.coupletones" + "/" + R.raw.wilhelm);
        Uri base1 = Uri.parse("android.resource://" + "com.helloworld.kenny.coupletones" + "/" + R.raw.wilhelm_reversed);
        Ringtone common = RingtoneManager.getRingtone(context ,base);
        Ringtone common1 = RingtoneManager.getRingtone(context ,base1);
        arriveStandard = new ToneNotification("arrive",common, context);
        departStandard = new ToneNotification("depart",common1, context);
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
        arriveStandard.play();
        Intent intent = new Intent(context,ArriveMediaService.class);
        context.startService(intent);
    }

    public void onPartnerDeparted() {
        departVibration.play();
        departStandard.play();
        Intent intent = new Intent(context,DepartMediaService.class);
        context.startService(intent);
    }

    class ArriveMediaService extends Service {

        public ArriveMediaService(){

        }

        public int onStartCommand(Intent intent, int flags, int startId){
            try{
                wait(2000);
                if (arriveTone != null) {
                    arriveTone.play();
                }

            }
            catch (InterruptedException w){
                w.printStackTrace();
            }
            return super.onStartCommand(intent, flags, startId);
        }

        public IBinder onBind(Intent intent){
            throw new UnsupportedOperationException("Not yet");
        }

        public void onDestroy() {
            super.onDestroy();

        }

    }

    class DepartMediaService extends Service {

        public DepartMediaService(){

        }

        public int onStartCommand(Intent intent, int flags, int startId){
            try{
                wait(2000);
                if (departTone != null) {
                    departTone.play();
                }

            }
            catch (InterruptedException w){
                w.printStackTrace();
            }
            return super.onStartCommand(intent, flags, startId);
        }

        public IBinder onBind(Intent intent){
            throw new UnsupportedOperationException("Not yet");
        }

        public void onDestroy() {
            super.onDestroy();

        }

    }

}
