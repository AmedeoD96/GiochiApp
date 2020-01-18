package it.uniba.di.sms1920.giochiapp.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;


public class NetworkChangeReceiver extends BroadcastReceiver {

    //Lista contenente le Callback 
    List<INetworkCallback> networkCallbacks = new ArrayList<>();

    private static NetworkChangeReceiver _instance;

    //assegna la classe
    public NetworkChangeReceiver() {
        super();
        _instance = this;
    }

    public static NetworkChangeReceiver getInstance() {
        return _instance;
    }


    //aggiunge una callback
    public void registerCallback(INetworkCallback callback) {
        networkCallbacks.add(callback);
    }

    //mai usato
    public boolean removeCallback(INetworkCallback callback) {
        return networkCallbacks.remove(callback);
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {

        //si ottiene il tipo di connessione
        final int status = NetworkUtil.getConnectivityStatusString(context);
        //Log.i("NetworkDebug", "Connection status: " + status);

        String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
        if (CONNECTIVITY_CHANGE.equals(intent.getAction())) {
            //se il tipo di connessione dovesse cambiare in corso d'opera, allora si assegna allo stato della connessione
            //la variabile che indica il "non connesso"
            boolean isNetworkPresent = status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED;

            //DatabaseManager.getInstance().setUseLocalVsRemote(isNetworkPresent);

            //in caso di cambiamenti della connessione verrebbero aggiornate tutte le callback
            for (INetworkCallback networkCallback : networkCallbacks) {
                networkCallback.OnNetworkChange(isNetworkPresent);
            }
        }
    }

    public interface INetworkCallback {

        void OnNetworkChange(boolean isNetworkPresent);

    }

}