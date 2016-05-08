package com.helloworld.kenny.coupletones.gcm;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Kenny on 5/7/2016.
 */
public class GcmSendIntentService extends IntentService {

    public GcmSendIntentService() {
        super("GcmSendIntentService");
    }

    public void onHandleIntent(Intent intent) {
        //TODO: copy GCM_UP's GcmSendIntentService.sendMessage
        // also figure out how to send regId in the Bundle
    }
}
