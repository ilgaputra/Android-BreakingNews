package com.gyosanila.e_power.breakingnews.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.gyosanila.e_power.breakingnews.Home.View.HomeActivity;

public class InternetUtils {
    /**
     * Checking internet connection
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo[] info = conn.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
        }
        Toast.makeText(context,"Not Internet Access",Toast.LENGTH_SHORT).show();
        return false;
    }
}
