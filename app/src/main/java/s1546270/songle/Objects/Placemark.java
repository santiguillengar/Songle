package s1546270.songle.Objects;


/**
 * Created by SantiGuillenGar on 28/10/2017.
 */

public class Placemark {

    public final String name;
    public final String description;
    public final String styleUrl;
    public final String point;


    public Placemark(String name, String description, String styleUrl, String point) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
    }


    public String getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStyleUrl() {
        return styleUrl;
    }


}
