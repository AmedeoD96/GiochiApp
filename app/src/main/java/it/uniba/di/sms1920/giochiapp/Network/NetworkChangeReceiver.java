package it.uniba.di.sms1920.giochiapp.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    List<INetworkCallback> networkCallbacks = new ArrayList<>();

    private static NetworkChangeReceiver _instance;

    public NetworkChangeReceiver() {
        super();
        _instance = this;
    }

    public static NetworkChangeReceiver getInstance() {
        return _instance;
    }


    public void registerCallback(INetworkCallback callback) {
        networkCallbacks.add(callback);
    }

    public boolean removeCallback(INetworkCallback callback) {
        return networkCallbacks.remove(callback);
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {

        final int status = NetworkUtil.getConnectivityStatusString(context);
        //Log.i("NetworkDebug", "Connection status: " + status);

        if (CONNECTIVITY_CHANGE.equals(intent.getAction())) {
            boolean isNetworkPresent = status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED;

            //DatabaseManager.getInstance().setUseLocalVsRemote(isNetworkPresent);

            for (INetworkCallback networkCallback : networkCallbacks) {
                networkCallback.OnNetworkChange(isNetworkPresent);
            }
        }
    }

    public interface INetworkCallback {

        void OnNetworkChange(boolean isNetworkPresent);

    }

}