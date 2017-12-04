package s1546270.songle.Objects;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;
import java.util.PropertyPermission;

/**
 * Class used to read stored properties for the app.
 */

public class PropertyReader {

    private static final String TAG = PropertyReader.class.getSimpleName();


    private Context context;
    private Properties properties;

    public PropertyReader(Context context){
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties(String file){
        Log.d(TAG, "     |SANTI|     PropertyReader getProperties accessed");

        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(file);
            properties.load(inputStream);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return properties;
    }

}
