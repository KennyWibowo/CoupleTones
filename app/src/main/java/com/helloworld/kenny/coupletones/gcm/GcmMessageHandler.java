package com.helloworld.kenny.coupletones.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showMessage();

        Log.i("GCM", "Received: (" + messageType + ") " + extras.getString("title"));

        GcmReceiver.completeWakefulIntent(intent);
    }

    public void showMessage() {
        // TODO: refactor this into an actual notification
        // Helpful : http://developer.android.com/training/notify-user/build-notification.html
    }
}