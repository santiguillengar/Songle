package s1546270.songle.Objects;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import s1546270.songle.R;


/**
 * Created by SantiGuillenGar on 09/12/2017.
 */

public class NetworkReceiver extends BroadcastReceiver{

    private static final String TAG = NetworkReceiver.class.getSimpleName();

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private Context context;
    private boolean connection;


    @Override
    public void onReceive(Context context, Intent intent) {

        connection = isConnected(context);
        if (!connection) {
            showAlert(context);
        } else {
            Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT ).show();

        }
    }


    public boolean isConnected(Context context) {

        boolean isConnected = false;

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {

            // Wifi is connected, use Wifi
            Log.d(TAG,"Wifi connection available." );
            isConnected = true;

        }
        else if (networkInfo != null && networkInfo.isConnected()) {

            // Have a network connection and permission, so use data
            Log.d(TAG,"Network connection available." );
            isConnected = true;

        } else {

            // No wifi and no permission, or no nnetwork connection.
            Log.e(TAG, "ERROR: No Network Connection Available.");
            isConnected = false;

        }
        return isConnected;
    }


    public void showAlert(Context theContext) {
        context = theContext;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Log.d(TAG, "No Connectivity Dialog Accessed");
        builder.setTitle(R.string.dialog_no_connection_header)
                .setMessage(R.string.dialog_no_connection_text)
                .setPositiveButton(R.string.dialog_retry_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(TAG, " Connectivity Retry Button Clicked ");

                        dialog.dismiss();

                        connection = isConnected(context);
                        if (!connection) {
                            showAlert(context);
                        }

                    }
                }).setNegativeButton(R.string.dialog_finish_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                Log.d(TAG, " Connectivity Finish Button Clicked ");

                dialog.dismiss();
                System.exit(1);
            }
        });

        AlertDialog alert =  builder.create();
        alert.show();
    }



}
