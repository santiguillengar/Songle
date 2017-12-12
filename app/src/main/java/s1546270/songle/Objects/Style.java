package s1546270.songle.Objects;

/**
 * Class represents a placemark style.
 */

public class Style {

    public final String id;
    public final IconStyle iconStyle;

    public Style(String id, IconStyle iconStyle) {
        this.id = id;
        this.iconStyle = iconStyle;
    }

    public String getId() {
        return id;
    }
    public String getIconStyle() {
        return ""+iconStyle.getScale()+","+ iconStyle.getIcon();

    }
}
