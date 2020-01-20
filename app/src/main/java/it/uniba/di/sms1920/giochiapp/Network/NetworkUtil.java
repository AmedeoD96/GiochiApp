package it.uniba.di.sms1920.giochiapp.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class NetworkUtil {
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;

    static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    private static final int NETWORK_STATUS_WIFI = 1;
    private static final int NETWORK_STATUS_MOBILE = 2;

    private static int getConnectivityStatus(Context context) {
        //Classe utile per ottenere informazioni sullo stato della connessione da parte del dispositivo
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Ritorna dettagli sulla rete di connessione attiva in quel momento
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //se fosse connesso
        if (null != activeNetwork) {
            //differenzia il tipo di connesione
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    //si ottiene l'intero legato allo stato della connessione
    static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
}