package com.malfaang.e_culture_tool_a.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

/*
 *
 *  @author adrianodelre
 */
public class NetworkConnectivity {

    public NetworkConnectivity() {
    }

    /**
     * Controlla se è presente connessione ad Internet.
     * @param context contesto dell'app android.
     * @return true se la connessione è presente, false altrimenti.
     */
    public static boolean check(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return ni != null && ni.isAvailable() && ni.isConnected();
    }

}
