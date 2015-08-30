package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Akshay on 8/28/15.
 */
public class Util {
    /*
    * check the network connection state
    *
    *
    * @return boolean if no network connection return false
    * */
    public static boolean isConnectedToInternet(Context context) {

        boolean isConnected = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;
    }
}
