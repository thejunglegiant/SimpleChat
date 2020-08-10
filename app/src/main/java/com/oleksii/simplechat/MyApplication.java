package com.oleksii.simplechat;

import android.app.Application;

import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.create();
    }
}
