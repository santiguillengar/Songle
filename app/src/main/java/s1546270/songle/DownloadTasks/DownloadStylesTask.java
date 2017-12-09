package s1546270.songle.DownloadTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import s1546270.songle.Objects.Style;

/**
 * Created by SantiGuillenGar on 14/10/2017.
 */

public class DownloadStylesTask extends AsyncTask<String, Void, List<Style>> {

    private static final String TAG = DownloadStylesTask.class.getSimpleName();
    StackOverflowXmlParser parser = new StackOverflowXmlParser();

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected List<Style> doInBackground(String... urls) {



        List<Style> styles = null;
        try {
            styles = loadXmlFromNetwork(urls[0]);
            return styles;
        } catch (IOException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadStylesTask when retrieving styles"+e);
            return styles;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadstylesTask when retrieving styles"+e);
            return styles;
        }
    }

    @Override
    protected void onPostExecute(List<Style> result) {

    }

    private List<Style> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        Log.d(TAG, "DownloadStylesTask loadXmlFromNetwork accessed");

        InputStream stream = null;
        List<Style> styles = null;

        try {

            stream = downloadUrl(urlString);
            styles = parser.parseStyles(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return styles;
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
