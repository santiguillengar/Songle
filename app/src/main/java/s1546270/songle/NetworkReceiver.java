package s1546270.songle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by SantiGuillenGar on 13/10/2017.
 */

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        /*if(networkPref.equals(WIFI) && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // Wifi is connected, so use wifi.
        }
        else if (networkPref.equals(ANY) && networkInfo != null) {
            // Have a network connection and permission, so use data
        }
        else {
            // No wifi and no permission, or no network connection
        }*/

    }
}
