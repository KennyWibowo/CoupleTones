package com.helloworld.kenny.coupletones.firebase.intents;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.helloworld.kenny.coupletones.R;

/**
 * Created by Kenny on 5/7/2016.
 */
public class FirebaseNotificationIntentService extends IntentService {

    private Handler handler;
    private static int notificationId = 0;

    public FirebaseNotificationIntentService() {
        super("FirebaseNotificationIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        String title = extras.getString("title");
        String content = extras.getString("content");
        showMessage(title, content);
    }

    public void showMessage(String title, String content) {
        // Helpful : http://developer.android.com/training/notify-user/build-notification.html
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //.setSmallIcon(R.drawable.);
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(R.drawable.ic_launcher);
        int mNotificationId = notificationId++;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
