package it.uniba.di.sms1920.giochiapp;

import android.app.Application;
import android.content.Context;

public class GlobalApplicationContext extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return appContext;
    }
}
