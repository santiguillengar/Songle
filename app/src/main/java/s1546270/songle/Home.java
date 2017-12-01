package s1546270.songle;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import s1546270.songle.Fragments.MapLevelDialogFragment;
import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Song;
import s1546270.songle.Objects.Style;

import static android.app.PendingIntent.getActivity;

public class Home extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = Home.class.getSimpleName();

    private String mapDifficulty = "" + R.integer.default_difficulty;

    // Store list of songs parsed
    List<Song> songs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Song gameSong = selectGameplaySong();

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


        return null;
    }
}


