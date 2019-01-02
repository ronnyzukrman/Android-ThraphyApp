package com.tenserflow.therapist.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Admin on 7/17/2018.
 */

public class InternetConnectivity {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else {
            return false;
        }

    }

}
