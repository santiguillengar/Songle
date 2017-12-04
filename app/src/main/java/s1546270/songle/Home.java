package s1546270.songle;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import s1546270.songle.Fragments.MapLevelDialogFragment;
import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.PropertyReader;
import s1546270.songle.Objects.Song;
import s1546270.songle.Objects.Style;


import static android.app.PendingIntent.getActivity;

public class Home extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = Home.class.getSimpleName();

    private String mapDifficulty = "" + R.integer.default_difficulty;

    // Store list of songs parsed
    List<Song> songs = null;

    // Used to open properties file
    private PropertyReader propertyReader;
    private Context context;
    private Properties properties;
    private Song gameSong = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameSong = selectGameplaySong();

        Log.d(TAG, "     |SANTI|      HOME_FAB");
        FloatingActionButton home_fab = (FloatingActionButton) findViewById(R.id.home_fab);
        home_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = getFragmentManager();

                MapLevelDialogFragment mldf = new MapLevelDialogFragment();
                mldf.show(manager, "");

            }
        });

    }

    public void onUserSelectDifficulty(String inputDifficulty) {
        Log.d(TAG, "     |SANTI|     RECEIVED CHALLENGE LEVEL: "+inputDifficulty);
        mapDifficulty = inputDifficulty;



        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
        intent.putExtra("difficulty", mapDifficulty);
        intent.putExtra("song", gameSong);
        startActivity(intent);


        // user should not be allowed to go back to home once the game has started
        finish();
    }

    public Song selectGameplaySong() {

        DownloadSongsTask dtSongs = new DownloadSongsTask();
        String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";

        try {
            dtSongs.execute(url);
            songs = dtSongs.get();

        } catch (Exception e) {
            Log.e(TAG, "ERROR: Couldn't download list of songs");
        }

        for (Song s : songs) {
            Log.d(TAG, "SONG: NUMBER: "+s.getNumber()+" ARTIST: "+s.getArtist()+" TITLE: "+s.getTitle()+" LINK: "+s.getLink());
        }



        // read properties to find
        context = this;
        propertyReader = new PropertyReader(context);
        properties = propertyReader.getProperties("config.properties");

        Log.d(TAG, "     |SANTI|     Properties found: "+properties);

        // Make list of song numbers from which to chose song the user will guess
        String[] recentGameplaySongs = properties.getProperty("recentGameplaySongs").split(",");
        ArrayList<String> availableSongs = new ArrayList<>();
        for (int i=1; i <= songs.size(); i++){
            if (!Arrays.asList(recentGameplaySongs).contains(""+i)) {
                availableSongs.add(""+i);
            }
        }

        if (availableSongs.isEmpty()){
            for (int i=1; i <= songs.size(); i++){
                availableSongs.add(""+i);
            }
        }

        Random generator = new Random();
        int randomIndex = generator.nextInt(availableSongs.size());


        //update properties to include chosen song



        for (Song song : songs) {
            if (song.getNumber() == randomIndex){
                return song;
            }
        }
        return null;
    }
}


