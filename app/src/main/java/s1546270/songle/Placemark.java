package s1546270.songle;


/**
 * Created by SantiGuillenGar on 28/10/2017.
 */

public class Placemark {
    public final String name;
    public final String description;
    public final String styleUrl;
    public final String point;
    /*public final String coordinates;*/



    public Placemark(String name, String description, String styleUrl, String point/*, String coordinates*/) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
        /*this.coordinates = coordinates;*/
    }


}
