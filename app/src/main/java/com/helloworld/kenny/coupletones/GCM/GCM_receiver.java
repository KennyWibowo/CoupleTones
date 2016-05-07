package com.helloworld.kenny.coupletones.GCM;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by tengzhou on 5/6/16.
 */
public class GCM_receiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ComponentName comp = new ComponentName(context.getPackageName(),GCM_MessageHandler.class.getName());

        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}
