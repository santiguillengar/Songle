package s1546270.songle;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SantiGuillenGar on 28/10/2017.
 */

public class Placemark {
    public final String name;
    public final String description;
    public final String styleUrl;
    public final String point;
    public final String coordinates;

    private static final String ns = null;


    public Placemark(String name, String description, String styleUrl, String point, String coordinates) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
        this.coordinates = coordinates;
    }


}
