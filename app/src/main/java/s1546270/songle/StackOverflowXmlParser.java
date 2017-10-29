package s1546270.songle;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SantiGuillenGar on 29/10/2017.
 */

public class StackOverflowXmlParser {

    private static final String ns = null;
    // For logging purposes
    private static final String TAG = DownloadXmlTask.class.getSimpleName();


    // Instantiating the parser
    List parse(InputStream in) throws XmlPullParserException, IOException {

        try {
            Log.d(TAG, "     |SANTI|     Accessed StackOverflowXmlParser parse method");
            Log.d(TAG, "     |SANTI|     Stream: "+in.toString());
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    // Reading the XML feed
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     Accessed StackOverflowXmlParser readFeed method");

        List placemarks = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();
            Log.d(TAG, "     |SANTI|     readFeed Parsing a: "+pName);

            // Starts by looking for the placemark tag
            if (pName.equals("Placemark")) {
                placemarks.add(readPlacemark(parser));
            }
            else if (pName.equals("Document")) {
                readDocument();

            }
            else {
                skip(parser);
            }
        }
        
        return placemarks;
    }

    private void readDocument() {
        Log.d(TAG, "     |SANTI|     StackOverflowXmlParser.readDocument accessed");
    }


    private Placemark readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     StackOverflowXmlParser.readPlacemark accessed");
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");
        String name = null;
        String description = null;
        String styleUrl = null;
        String point = null;
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String thename = parser.getName();

            if (thename.equals("name")) {
                name = readName(parser);
            } else if (thename.equals("description")) {
                description = readDescription(parser);
            } else if (thename.equals("styleUrl")) {
                styleUrl = readStyleUrl(parser);
            } else if (thename.equals("Point")) {
                point = readPoint(parser);
            } else {
                skip(parser);
            }
        }

        Placemark p = new Placemark(name, description, styleUrl, point);
        Log.d(TAG, "     |SANTI|     New Placemark:"+p);
        return p;
    }


    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        Log.d(TAG, "     |SANTI|     parser.readName name is: "+name);
        return name;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        Log.d(TAG, "     |SANTI|     parser.readDescription description is: "+description);
        return description;
    }

    private String readStyleUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "styleUrl");
        String styleUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "styleUrl");
        Log.d(TAG, "     |SANTI|     parser.readStyleUrl url is: "+styleUrl);

        return styleUrl;
    }


    private String readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "     |SANTI|     parser.readPoint2 accessed");
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String theName = parser.getName();

            if (theName.equals("coordinates")) {
                Log.d(TAG, "     |SANTI|     parser.readPoint2 coordinates: "+readCoordinates(parser));
            }
            else {
                skip(parser);
            }
        }

        return "";
    }

    private String readCoordinates(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "     |SANTI|     parser.readCoordinates accessed");

        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinates = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");

        Log.d(TAG, "     |SANTI|     parser.readCoordinates coords are: "+coordinates);
        return coordinates;
    }


    private String readText (XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "     |SANTI|     parser.readText accessed");

        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }


    // Skipping uninteresting tags
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     parser.skip accessed");
        if(parser.getEventType() != XmlPullParser.START_TAG) {
            Log.d(TAG, "StackOverflowParser.skip: IllegalStateException");
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
