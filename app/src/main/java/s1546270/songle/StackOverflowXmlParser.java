package s1546270.songle;

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



    // Instantiating the parser
    List<Placemark> parse(InputStream in) throws XmlPullParserException, IOException {

        try {
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
    private List<Placemark> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<Placemark> placemarks = new ArrayList<Placemark>();
        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            // Starts by looking for the placemark tag
            if (name.equals("placemark")) {
                placemarks.add(readPlacemark(parser));
            }
            else {
                skip(parser);
            }
        }
        return placemarks;
    }




    private Placemark readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "placemark");
        String name = null;
        String description = null;
        String styleUrl = null;
        String point = null;
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;

            String thename = parser.getName();
            if (thename.equals("name")) {
                name = readName(parser);
            } else if (thename.equals("description")) {
                description = readDescription(parser);
            } else if (thename.equals("styleUrl")) {
                description = readStyleUrl(parser);
            } else if (thename.equals("point")) {
                description = readPoint(parser);
            } else if (thename.equals("coordinates")) {
                description = readCoordinates(parser);
            }
            else {
                skip(parser);
            }
        }

        return new Placemark(name, description, styleUrl, point, coordinates);
    }


    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readStyleUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "styleUrl");
        String styleUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "styleUrl");
        return styleUrl;
    }

    private String readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "point");
        String point = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "point");
        return point;
    }


    private String readCoordinates(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "point");
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinates = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        parser.require(XmlPullParser.END_TAG, ns, "point");
        return coordinates;
    }

    private String readText (XmlPullParser parser) throws IOException, XmlPullParserException {

        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skipping uninteresting tags
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if(parser.getEventType() != XmlPullParser.START_TAG) {
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
