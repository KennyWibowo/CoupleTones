package com.helloworld.kenny.coupletones.firebase;

import android.content.Context;

import com.helloworld.kenny.coupletones.firebase.managers.FirebaseHistoryManager;
import com.helloworld.kenny.coupletones.firebase.registration.FirebaseRegistrationInformation;

/**
 * Created by Kenny on 5/27/2016.
 */
public class FirebaseFactoryService {

    public static final String HISTORY_RESET_TIME = "03:00:00"; // 3AM
    public static final String ENDPOINT = "https://coupletonesteam6.firebaseio.com/";

    public static FirebaseRegistrationInformation linkTheFire(Context context) {
        FirebaseRegistrationInformation registrationInformation = new FirebaseRegistrationInformation(context);
        FirebaseHistoryManager historyManager = new FirebaseHistoryManager(registrationInformation);

        return registrationInformation;
    }
}
