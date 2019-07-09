package com.example.lostandfoundnew;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {

    public static boolean isNetworkAvailable(Context context)  {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        return mConnectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE ).getState() == NetworkInfo.State.CONNECTED
                || mConnectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI ).getState() == NetworkInfo.State.CONNECTED;

    }
    public static String[] getHobbysFromServer(){
        return null;
    }
}
