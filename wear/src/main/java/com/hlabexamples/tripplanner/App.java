package com.hlabexamples.tripplanner;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by H.T. on 30/11/17.
 */

public class App extends Application {
    private static App androidApp;

    public static App getInstance() {
        return androidApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        androidApp = this;
        FirebaseApp.initializeApp(this);
    }
}
