package s1546270.songle;

import android.app.Activity;

/**
 * Created by SantiGuillenGar on 28/10/2017.
 */

public class NetworkActivity extends Activity {
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";

    // Whether there is a wifi connection
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshdisplay = true;
    public static String sPref = null;

    public void loadPage() {

        if ((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) {
            new DownloadXmlTask().execute(URL);
        }
        else if ((sPref.equals(WIFI)) && (wifiConnected)) {
            new DownloadXmlTask().execute(URL);
        } else {
            // show error
        }
    }
}
