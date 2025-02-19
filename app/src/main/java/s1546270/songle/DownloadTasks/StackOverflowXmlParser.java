package s1546270.songle.DownloadTasks;

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
import s1546270.songle.Objects.Song;
import s1546270.songle.Objects.Style;

/**
 * Parser includes method to parse placemarks, styles and songs.
 */

public class StackOverflowXmlParser {

    private static final String ns = null;
    // For logging purposes
    private static final String TAG = StackOverflowXmlParser.class.getSimpleName();


    // Instantiating the parser
    List parsePlacemarks(InputStream in) throws XmlPullParserException, IOException {

        try {
            Log.d(TAG, "Accessed StackOverflowXmlParser parsePlacemarks method");
            Log.d(TAG, "Stream: "+in.toString());
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
            Log.d(TAG, "Accessed StackOverflowXmlParser parseStyles method");
            Log.d(TAG, "Stream: "+in.toString());
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeedStyles(parser);
        } finally {
            in.close();
        }
    }

    // Instantiating the parser
    List parseSongs(InputStream in) throws XmlPullParserException, IOException {

        try {
            Log.d(TAG, "Accessed StackOverflowXmlParser parseSongs method");
            Log.d(TAG, "Stream: "+in.toString());
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeedSongs(parser);
        } finally {
            in.close();
        }
    }


    // Reading the XML feed
    private List readFeedPlacemarks(XmlPullParser parser) throws XmlPullParserException, IOException {

        List placemarks = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();
            Log.d(TAG, "readFeed Parsing a: "+pName);

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

        List styles = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();

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

    // Reading the XML feed
    private List readFeedSongs(XmlPullParser parser) throws XmlPullParserException, IOException {

        List songs = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Songs");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();

            // Looking for the placemark tag
            if (pName.equals("Song")) {
                songs.add(readSong(parser));
            }
            else {
                skip(parser);
            }
        }
        return songs;
    }




    private Song readSong(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "Song");

        int number = -1;
        String artist = null;
        String title = null;
        String link = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String pName = parser.getName();

            if (pName.equals("Number")) {
                number = readNumber(parser);
            } else if (pName.equals("Artist")) {
                artist = readArtist(parser);
            } else if (pName.equals("Title")) {
                title = readTitle(parser);
            } else if (pName.equals("Link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }

        Song song = new Song(number, artist, title, link);
        String sValues = "NUMBER: "+song.getNumber()+" ARTIST: "+song.getArtist()+" TITLE: "+song.getTitle()+" LINK: "+song.getLink();
        Log.d(TAG, "New Song: "+sValues);
        return song;
    }


    private int readNumber(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Number");
        String number = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Number");
        return Integer.parseInt(number);
    }

    private String readArtist(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Artist");
        String artist = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Artist");
        return artist;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Title");
        return title;
    }


    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Link");
        return link;
    }


    private void readDocument() {
        // Method simply skips document tag
    }


    private Style readStyle(XmlPullParser parser, String id) throws XmlPullParserException, IOException {

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
        Log.d(TAG, "New Style: "+s);
        return s;
    }


    private IconStyle readIconStyle(XmlPullParser parser) throws IOException, XmlPullParserException {

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
            }
            else if (theName.equals("Icon")) {
                icon = readISIcon(parser);
            }
            else {
                skip(parser);
            }

        }
        IconStyle i = new IconStyle(scale, icon);
        return i;
    }


    private String readISScale(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "scale");
        String scale = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "scale");

        return scale;
    }

    private String readISIcon(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Icon");
        String href = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String theName = parser.getName();
            if (theName.equals("href")) {
                href = readText(parser);

            }
        }

        return href;
    }



    private Placemark readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {

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
        Log.d(TAG, "New Placemark: "+pValues);
        return p;
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
        Log.d(TAG, "parser.readPoint accessed");
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        String coords = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String theName = parser.getName();

            if (theName.equals("coordinates")) {
                coords = readCoordinates(parser);
            }
            else {
                skip(parser);
            }
        }

        return coords;
    }

    private String readCoordinates(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinates = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");

        return coordinates;
    }


    private String readText (XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "parser.readText accessed");

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
            Log.e(TAG, "StackOverflowParser.skip: IllegalStateException");
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