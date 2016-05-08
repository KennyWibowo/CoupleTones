package com.helloworld.kenny.coupletones.gcm;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Kenny on 5/7/2016.
 */
public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public void onHandleIntent(Intent intent) {
        //TODO: copy GCM_UP's GcmIntentService.sendMessage
    }
}
