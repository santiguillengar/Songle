package s1546270.songle.DownloadTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SantiGuillenGar on 14/10/2017.
 */

public class DownloadLyricsTask extends AsyncTask<String, Void, List<String>> {

    private static final String TAG = DownloadLyricsTask.class.getSimpleName();
    StackOverflowXmlParser parser = new StackOverflowXmlParser();

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected List<String> doInBackground(String... urls) {


        List<String> lyricsLines = null;
        try {
            lyricsLines = loadLyricsFromNetwork(urls[0]);
            return lyricsLines;
        } catch (IOException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadLyricsTask when retrieving lyrics"+e);
            return lyricsLines;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "ERROR: Failed attempt in DonwloadLyricsTask when retrieving lyrics"+e);
            return lyricsLines;
        }
    }

    @Override
    protected void onPostExecute(List<String> result) {


    }

    private List<String> loadLyricsFromNetwork(String urlString) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     DownloadLyricsTask loadLyricsFromNetwork accessed");

        ArrayList<String> lyricsLines = new ArrayList<>();

        URL url = new URL(urlString);
         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

         String inputLine;
         while ((inputLine = in.readLine()) != null) {
             Log.d(TAG, "Lyrics line: "+inputLine);
             lyricsLines.add(inputLine);
         }
         in.close();

         return lyricsLines;
    }

}
