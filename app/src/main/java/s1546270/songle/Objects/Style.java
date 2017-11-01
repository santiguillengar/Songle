package s1546270.songle.Objects;

import android.icu.util.ICUUncheckedIOException;

/**
 * Created by SantiGuillenGar on 29/10/2017.
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
