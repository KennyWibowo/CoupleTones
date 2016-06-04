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
    Ringtone standardTone;
    MediaPlayer m = new MediaPlayer();
    Uri uri;



    public ToneNotification(String name, Ringtone tone, Ringtone standardTone, Context context, Uri uri) {
        this.name = name;
        this.tone = tone;
        this.context = context;
        this.standardTone = standardTone;
        this.uri = uri;
    }


    public void play() {

        //Intent intent = new Intent(context, MediaService.class);
        //context.startService(intent);
        if(Settings.tonesEnabled()) {
            //standardTone.play();
            m.create(context,uri);
            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    m.stop();
                    tone.play();
                }
            });
            m.start();
        }
    }

    public String toString() {

        return name;
    }

    public Ringtone getTone() {
        return standardTone;
    }

    /*class MediaService extends Service{

        public MediaService(){

        }

        public int onStartCommand(Intent intent, int flags, int startId){
                try{
                    wait(2000);
                    if (tone != null) {
                        tone.play();
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

    }*/

}
