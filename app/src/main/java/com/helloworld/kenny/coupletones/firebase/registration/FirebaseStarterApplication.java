package com.helloworld.kenny.coupletones.firebase.registration;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Kenny on 5/26/2016.
 */
public class FirebaseStarterApplication extends Application {
    @Override public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);     // other setup code
    }
}
