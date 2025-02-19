package s1546270.songle.DownloadTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import s1546270.songle.Objects.Placemark;

/**
 * AsyncTask to download placemarks for map.
 */

public class DownloadXmlTask extends AsyncTask<String, Void, List<Placemark>> {

    // For logging purposes.
    private static final String TAG = DownloadXmlTask.class.getSimpleName();
    StackOverflowXmlParser parser = new StackOverflowXmlParser();

    @Override
    protected void onPreExecute() {
    }

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
    }


    private List<Placemark> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        Log.d(TAG, "DownloadXmlTask loadXmlFromNetwork accessed");

        InputStream stream = null;
        List<Placemark> placemarks = null;

        try {

            stream = downloadUrl(urlString);
            placemarks = parser.parsePlacemarks(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return placemarks;
    }

    // Given a string representation of a URL, sets up a connection ang gets an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setReadTimeout(10000 /*milliseconds*/);
        conn.setConnectTimeout(15000 /*milliseconds*/);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Start the query
        conn.connect();
        return conn.getInputStream();
    }
}
