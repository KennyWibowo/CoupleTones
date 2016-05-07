package com.helloworld.kenny.coupletones.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by tengzhou on 5/6/16.
 */
public class GCM_MessageHandler extends IntentService {

    String mes;
    private Handler handler;

    public GCM_MessageHandler() {
        super("Gcm_MessageHandler");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showToast();

        GcmReceiver.completeWakefulIntent(intent);
    }

    public void showToast() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),mes, Toast.LENGTH_LONG).show();
            }
        });
    }
}
