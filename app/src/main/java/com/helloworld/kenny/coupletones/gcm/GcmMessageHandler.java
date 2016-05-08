package com.helloworld.kenny.coupletones.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.helloworld.kenny.coupletones.R;

/**
 * Created by Kenny on 5/7/2016.
 */
public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;

    public GcmMessageHandler(){
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO make sure that this is all good
        System.out.println("MessageHandler - Message received!");
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("message");
        showMessage(mes);

        Log.i("GCM", "Received: (" + messageType + ") " + extras.getString("title"));

        GcmReceiver.completeWakefulIntent(intent);
    }

    public void showMessage(String msg) {
        // TODO: refactor this into an actual notification
        // Helpful : http://developer.android.com/training/notify-user/build-notification.html
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.);
                .setContentTitle("Partner reached favorite location!")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}