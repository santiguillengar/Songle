package s1546270.songle;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import s1546270.songle.Objects.IconStyle;
import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Style;

/**
 * Created by SantiGuillenGar on 29/10/2017.
 */

public class StackOverflowXmlParser {

    private static final String ns = null;
    // For logging purposes
    private static final String TAG = StackOverflowXmlParser.class.getSimpleName();


    // Instantiating the parser
    List parsePlacemarks(InputStream in) throws XmlPullParserException, IOException {

        try {
            Log.d(TAG, "     |SANTI|     Accessed StackOverflowXmlParser parsePlacemarks method");
            Log.d(TAG, "     |SANTI|     Stream: "+in.toString());
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeedPlacemarks(parser);
        } finally {
            in.close();
        }
    }

    // Instantiating the parser
    List parseStyles(InputStream in) throws XmlPullParserException, IOException {

        try {
            Log.d(TAG, "     |SANTI|     Accessed StackOverflowXmlParser parseStyles method");
            Log.d(TAG, "     |SANTI|     Stream: "+in.toString());
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeedStyles(parser);
        } finally {
            in.close();
        }
    }


    // Reading the XML feed
    private List readFeedPlacemarks(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     Accessed StackOverflowXmlParser readFeedPlacemarks method");

        List placemarks = new ArrayList();

        List styles = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();
            Log.d(TAG, "     |SANTI|     readFeed Parsing a: "+pName);

            // Looking for the placemark tag
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



    // Reading the XML feed
    private List readFeedStyles(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     Accessed StackOverflowXmlParser readFeedStyles method");

        List styles = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();
            Log.d(TAG, "     |SANTI|     readFeed Parsing a: "+pName);

            // Looks for Style tags
            if (pName.equals("Style")) {
                String id = parser.getAttributeValue(null, "id");
                styles.add(readStyle(parser, id));
            }
            else if (pName.equals("Document")) {
                readDocument();

            }
            else {
                skip(parser);
            }
        }

        return styles;
    }






    private void readDocument() {
        Log.d(TAG, "     |SANTI|     StackOverflowXmlParser.readDocument accessed");
    }


    private Style readStyle(XmlPullParser parser, String id) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     StackOverflowXmlParser.readStyle accessed");

        parser.require(XmlPullParser.START_TAG, ns, "Style");
        Log.d(TAG, "     ||     PARAMETER ID: "+id);
        // id is passed as parameter from calling method.

        IconStyle iconStyle = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();

            if (pName.equals("IconStyle")) {
                iconStyle = readIconStyle(parser);
            } else {
                skip(parser);
            }
        }

        Style s = new Style(id, iconStyle);
        Log.d(TAG, "     |SANTI|     New Style: "+s);
        return s;

    }



    private IconStyle readIconStyle(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "     |SANTI|     parser.readIconStyle accessed");
        parser.require(XmlPullParser.START_TAG, ns, "IconStyle");
        String scale = null;
        String icon = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String theName = parser.getName();
            if (theName.equals("scale")) {
                scale = readISScale(parser);
                Log.d(TAG, "     |SANTI|     parser.readPoint scale: "+scale);
            }
            else if (theName.equals("Icon")) {
                icon = readISIcon(parser);
                Log.d(TAG, "     |SANTI|     parser.readPoint icon: "+icon);
            }
            else {
                skip(parser);
            }

        }
        IconStyle i = new IconStyle(scale, icon);
        return i;
    }


    private String readISScale(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "     |SANTI|     parser.readISScale accessed");

        parser.require(XmlPullParser.START_TAG, ns, "scale");
        String scale = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "scale");

        Log.d(TAG, "     |SANTI|     parser.readScale scale is: "+scale);
        return scale;
    }

    private String readISIcon(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "     |SANTI|     parser.readISIcon accessed");
        parser.require(XmlPullParser.START_TAG, ns, "Icon");
        String href = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String theName = parser.getName();
            if (theName.equals("href")) {
                href = readText(parser);
                Log.d(TAG, "     |SANTI|     parser.readText href: "+href);
            }
        }

        return href;
    }



    private Placemark readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "     |SANTI|     StackOverflowXmlParser.readPlacemark accessed");

        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        String name = null;
        String description = null;
        String styleUrl = null;
        String point = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();

            if (pName.equals("name")) {
                name = readName(parser);
            } else if (pName.equals("description")) {
                description = readDescription(parser);
            } else if (pName.equals("styleUrl")) {
                styleUrl = readStyleUrl(parser);
            } else if (pName.equals("Point")) {
                point = readPoint(parser);
            } else {
                skip(parser);
            }
        }

        Placemark p = new Placemark(name, description, styleUrl, point);
        String pValues = "NAME: "+p.getName()+" DESCRIPTION: "+p.getDescription()+" STYLEURL: "+p.getStyleUrl()+" POINT: "+p.getPoint();
        Log.d(TAG, "     |SANTI|     New Placemark: "+pValues);
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
        Log.d(TAG, "     |SANTI|     parser.readPoint accessed");
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        String coords = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String theName = parser.getName();

            if (theName.equals("coordinates")) {
                coords = readCoordinates(parser);
                Log.d(TAG, "     |SANTI|     parser.readPoint coordinates: "+coords);
            }
            else {
                skip(parser);
            }
        }

        return coords;
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
