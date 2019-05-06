package com.medisensehealth.fdccontributor.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hp on 16/02/2017.
 */

public class NetworkUtil {
    public static boolean isConnectingToInternet(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo[] = connectivityManager
                        .getAllNetworkInfo();
                if (networkInfo != null) {
                    for (int i = 0; i < networkInfo.length; i++) {
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        return false;

    }

    public static String getConnectivityStatusString(Context context) {
        boolean conn = NetworkUtil.isConnectingToInternet(context);
        String status = null;
        if (conn == true) {
            status = "enabled";
        } else {
            status = "desabled";
        }
        return status;
    }
}
