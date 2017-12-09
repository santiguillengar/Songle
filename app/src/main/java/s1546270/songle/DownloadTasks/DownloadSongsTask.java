package s1546270.songle.DownloadTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import s1546270.songle.Objects.Song;

/**
 * Created by SantiGuillenGar on 14/10/2017.
 */

public class DownloadSongsTask extends AsyncTask<String, Void, List<Song>> {

    private static final String TAG = DownloadSongsTask.class.getSimpleName();
    StackOverflowXmlParser parser = new StackOverflowXmlParser();

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected List<Song> doInBackground(String... urls) {


        List<Song> songs = null;
        try {
            songs = loadXmlFromNetwork(urls[0]);
            return songs;
        } catch (IOException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadSongsTask when retrieving songs"+e);
            return songs;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadSongsTask when retrieving songs"+e);
            return songs;
        }
    }

    @Override
    protected void onPostExecute(List<Song> result) {


    }

    private List<Song> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     DownloadSongsTask loadXmlFromNetwork accessed");

        InputStream stream = null;
        List<Song> songs = null;

        try {

            stream = downloadUrl(urlString);
            songs = parser.parseSongs(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return songs;
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
