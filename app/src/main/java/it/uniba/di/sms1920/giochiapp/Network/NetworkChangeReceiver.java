package it.uniba.di.sms1920.giochiapp.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.uniba.di.sms1920.giochiapp.Database.DatabaseManager;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final int status = NetworkUtil.getConnectivityStatusString(context);
        //Log.i("NetworkDebug", "Connection status: " + status);

        if (CONNECTIVITY_CHANGE.equals(intent.getAction())) {
            boolean useLocalVsRemoteDB = status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED;

            DatabaseManager.getInstance().setUseLocalVsRemote(useLocalVsRemoteDB);
        }
    }
}