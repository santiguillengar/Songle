package s1546270.songle;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by SantiGuillenGar on 14/10/2017.
 */

public class DownloadXmlTask extends AsyncTask<String, Void, String> {

    private static final String TAG = DownloadXmlTask.class.getSimpleName();
    private Placemark placemark;
    private List<Placemark> placemarks;
    KmlLayer layer; //KML object

    @Override
    protected String doInBackground(String... urls) {

        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            return "Unable to load content. Check your network connection";
        } catch (XmlPullParserException e) {
            return "Error parsing XML";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        /*setContentView(R.layout.activity_home);

        // Attempt at showing KML points on map.
        try {
            layer = new KmlLayer(mMap,/*KML file path - kmlInputStream, getApplicationContext());
            layer.addLayerToMap();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

        StringBuilder result = new StringBuilder();

        InputStream stream = null;
        StackOverflowXmlParser parser = new StackOverflowXmlParser();
        List<Placemark> placemarks = null;
        String name;
        String description;
        String styleUrl;
        String point;
        String coordinates;

        try {

            stream = downloadUrl(urlString);
            System.out.print("STREAM: "+stream);
            Log.d(TAG, "     |SANTI|     Stream: "+stream.toString());

            placemarks = parser.parse(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.d(TAG, "     |SANTI|     Post parsing placemarks contents: "+placemarks.toString());

        return placemarks.toString();
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
