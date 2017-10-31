package s1546270.songle;

import android.os.AsyncTask;
import android.util.Log;

import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import s1546270.songle.Objects.Placemark;

/**
 * Created by SantiGuillenGar on 14/10/2017.
 */

public class DownloadXmlTask extends AsyncTask<String, Void, List<Placemark>> {

    private static final String TAG = DownloadXmlTask.class.getSimpleName();
    StackOverflowXmlParser parser = new StackOverflowXmlParser();

    @Override
    protected List<Placemark> doInBackground(String... urls) {

        List<Placemark> placemarks = null;
        try {
            placemarks = loadXmlFromNetwork(urls[0]);
            return placemarks;
        } catch (IOException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadXmlTask when retrieving placemarks"+e);
            return placemarks;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadXmlTask when retrieving placemarks"+e);
            return placemarks;
        }
    }

    @Override
    protected void onPostExecute(List<Placemark> result) {
        /*setContentView(R.layout.activity_lyric_map_ui);

        // Attempt at showing KML points on map.
        try {
            layer = new KmlLayer(mMap,placemarks, getApplicationContext());
            layer.addLayerToMap();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private List<Placemark> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     DownloadXmlTask loadXmlFromNetwork accessed");

        InputStream stream = null;
        List<Placemark> placemarks = null;

        try {

            stream = downloadUrl(urlString);
            placemarks = parser.parse(stream);

            /*
            System.out.print("STREAM: "+stream);
            Log.d(TAG, "     |SANTI|     Stream: "+stream.toString());

            placemarks = parser.parse(stream);
            */
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        //return placemarks.toString();
        return placemarks;
    }

    // Given a string representation of a URL, sets up a connection ang gets an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Also available: HttpsUrlConnection

        conn.setReadTimeout(10000 /*milliseconds*/);
        conn.setConnectTimeout(15000 /*milliseconds*/);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Start the query
        conn.connect();
        return conn.getInputStream();
    }
}
