package com.helloworld.kenny.coupletones.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by Kenny on 5/7/2016.
 */
public class GcmSendIntentService extends IntentService {

    private static int msgId = 0;

    public GcmSendIntentService() {
        super("GcmSendIntentService");
    }

    public void onHandleIntent(Intent intent) {
        //TODO: copy GCM_UP's GcmSendIntentService.sendMessage
        // also figure out how to send regId in the Bundle

        System.out.println("Intent - sending message");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String mSenderId = intent.getStringExtra("Sender RegID");
        String locationName = intent.getStringExtra("Location Name");
        try {
            String partnerId = intent.getStringExtra("Partner RegId");
            Bundle data = new Bundle();
            data.putString("action", "com.helloworld.kenny.coupletones.MESSAGE");
            data.putString("message", "Your partner reached: "+ locationName);
            data.putString("Partner ID", partnerId);
            //String id = Integer.toString(getNextMsgId());
            gcm.send(mSenderId + "@gcm.googleapis.com",""+msgId, data);
            msgId++;
            //Log.v("grokkingandroid", "sent message: " + msg);
        } catch (IOException e) {
            System.out.println("uh oh");
        }
    }
}
