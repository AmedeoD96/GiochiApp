package it.uniba.di.sms1920.giochiapp;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import it.uniba.di.sms1920.giochiapp.Database.DatabaseManager;
import it.uniba.di.sms1920.giochiapp.Network.NetworkChangeReceiver;


public class GlobalApplicationContext extends Application {
    private static Context appContext;
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        addNetworkReceiver();
        initSystem();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        removeNetworkReceiver();
    }

    public static Context getAppContext(){
        return appContext;
    }



    private void addNetworkReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void removeNetworkReceiver() {
        unregisterReceiver(networkChangeReceiver);
    }


    private void initSystem() {
        DatabaseManager.getInstance().init();
        UsersManager.getInstance().init();
    }


}
