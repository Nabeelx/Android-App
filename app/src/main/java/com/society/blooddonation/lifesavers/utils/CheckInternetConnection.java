package com.society.blooddonation.lifesavers.utils;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckInternetConnection {

    Context context;

    public CheckInternetConnection(Context context) {

        this.context = context;
    }

    public boolean checkConnection() {

//        Log.d(TAG, "checkConnection: is called");
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }


}
