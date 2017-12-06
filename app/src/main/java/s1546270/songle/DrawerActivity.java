package s1546270.songle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import s1546270.songle.Fragments.AboutFragment;
import s1546270.songle.Fragments.CorrectGuessFragment;
import s1546270.songle.Fragments.HintsFragment;
import s1546270.songle.Fragments.InstructionsFragment;
import s1546270.songle.Fragments.MapLevelDialogFragment;
import s1546270.songle.Fragments.WordsFound;
import s1546270.songle.Fragments.GuessSongFragment;
import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Song;
import s1546270.songle.Objects.Style;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    // For logging purposes
    private static final String TAG = DrawerActivity.class.getSimpleName();

    //For fragment refresh purposes
    private String currentFragment = null;

    private Song gameSong;
    private List<Song> songs;
    private List<Placemark> placemarks;
    private List<Style> styles;

    private MapFragment mapFragment;

    private String mapDifficulty;

    private List<String> wordsFound;


    //tut
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Songle");

        setSupportActionBar(toolbar);

        wordsFound = Arrays.asList("WordFound1","WordFound2","WordFound3","WordFound4","WordFound5","WordFound6","WordFound7","WordFound8");

        try {
            gameSong = (Song) getIntent().getSerializableExtra("song");
            songs = (List<Song>) getIntent().getSerializableExtra("songsList");
            Log.d(TAG, "     |SANTI|     Song that will be played: "+gameSong.getTitle()+" "+gameSong.getArtist());
        } catch (Exception e){
            Log.e(TAG, "Exception raised in DrawerActivity onCreate");
        }

        // Retrieving Map Difficulty
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();
        mapDifficulty = pref.getString("mapDifficulty", null);
        Log.d(TAG, "     |SANTI|     Shared Pref Retrieved");

        downloadPlacemarksAndStyles();

        //DEFAULT FAB
        FloatingActionButton map_fab = (FloatingActionButton) findViewById(R.id.fab);
        map_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // FAB has been pressed, user wants to guess what song it is.
                // Show song guess options fragment.
                android.app.FragmentManager manager = getFragmentManager();

                GuessSongFragment guessSongFragment = new GuessSongFragment();
                guessSongFragment.show(manager, "");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //When Draweractivity first built, show map as default.
        MapFragment mapFragment = new MapFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();
        currentFragment = "mapFragment";

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Log.d(TAG, "     |SANTI|     Options Item Selected");
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FragmentManager manager = getSupportFragmentManager();
            InstructionsFragment instructionsFragment = new InstructionsFragment();
            instructionsFragment.show(manager, "");
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap mMap) {

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: map page selected");

            if (currentFragment != "mapFragment") {
                currentFragment = "mapFragment";
                //Display Map Fragment
                mapFragment = new MapFragment();

                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();


            }

        } else if (id == R.id.nav_words_found) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: words_found page selected");

            if (currentFragment != "wordsFoundFragment") {
                currentFragment = "wordsFoundFragment";

                WordsFound wordsFound = new WordsFound();

                DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, wordsFound).commit();
            }

        } else if (id == R.id.nav_hints) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: hints page selected");

            if (currentFragment != "hintsFragment") {
                currentFragment = "hintsFragment";

                HintsFragment hintsFragment = new HintsFragment();
                DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, hintsFragment).commit();

            }

        } else if (id == R.id.nav_about) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: about page selected");

            if (currentFragment != "aboutFragment") {
                currentFragment = "aboutFragment";

                AboutFragment aboutFragment = new AboutFragment();
                DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, aboutFragment).commit();

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void onUserGuessSong(String guessedSongTitle) {

        Log.d(TAG, "BACK TO DRAWER ACTIVITY. USER GUESSED: "+guessedSongTitle);

        if (guessedSongTitle.equals(gameSong.getTitle())){

            Snackbar.make(findViewById(R.id.drawer_layout), "correct guess!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.fragment_correct_guess, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
            builder.setView(dialogView);
            builder.show();

        } else {

            Snackbar.make(findViewById(R.id.drawer_layout), "U WRONG!", Snackbar.LENGTH_LONG);

            LayoutInflater inflater2 = getLayoutInflater();
            View dialogView2 = inflater2.inflate(R.layout.fragment_wrong_guess, null);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(DrawerActivity.this);
            builder2.setView(dialogView2);
            builder2.show();

        }
    }

    public void downloadPlacemarksAndStyles() {
        // HANDLE PLACEMARKS FOR MAP
        String url = determineMapUrl();
        DownloadXmlTask dtPlacemarks = new DownloadXmlTask();
        DownloadStylesTask dtStyles = new DownloadStylesTask();


        try {
            dtPlacemarks.execute(url);
            placemarks = dtPlacemarks.get();
            dtStyles.execute(url);
            styles = dtStyles.get();


        } catch (Exception e) {
            Log.e(TAG, "ERROR: Couldn't download placemarks for map");
        }

        for (Style s : styles) {
            Log.d(TAG, "STYLE: ID: "+s.getId()+" ICONSTYLE: "+s.getIconStyle());
        }

    }



    private String determineMapUrl() {

        // Example: "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map1.kml"

        String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/";
        String songNumber = null;

        if (gameSong != null) {
            Log.d(TAG, "Determining Map URL: gameSong number: "+gameSong.getNumber());
            if (gameSong.getNumber() < 10) {
                songNumber = "0"+gameSong.getNumber();
            } else {
                songNumber = ""+gameSong.getNumber();
            }
            Log.d(TAG, "Determining Map URL: songNumber string: "+songNumber);
        } else {
            Log.e(TAG, "gameSong was null in determineMapUrl");
            songNumber = "" + ((int) Math.random() * 10 + 1);
        }

        String url = baseUrl + songNumber + "/map" + mapDifficulty + ".kml";

        Log.d(TAG, "     |SANTI|     URL determined: "+url);
        Log.d(TAG, "SONG CHOSEN: "+songNumber);
        return url;
    }





    public ArrayList<String> makeSongList() {

        ArrayList<String> guess_song_options = new ArrayList<>();
        Log.d(TAG, "PossibleSongs in GuessSongFragment: "+songs.toString());
        Random random = new Random();
        String guessSongOptionsStr = "";
        int index;


        // Initialization
        SharedPreferences pref = getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();

        if (songs == null) {
            guess_song_options.add(gameSong.getTitle());
            guess_song_options.addAll(Arrays.asList(getResources().getStringArray(R.array.guess_song_options_demo)));
        }
        else {

            for (int i = 0; i < 4; i++) {

                // Check that random option chosen hasn't been chosen already or is the correct guess.
                index = random.nextInt(songs.size());
                while((songs.get(index).getTitle()).equals(gameSong.getTitle()) || guess_song_options.contains(songs.get(index).getTitle())) {
                    index = random.nextInt(songs.size());
                }

                guess_song_options.add(songs.get(index).getTitle());
            }
            guess_song_options.add(gameSong.getTitle());
            Collections.shuffle(guess_song_options);
        }


        for (String guessableSong : guess_song_options) {
            if (guessSongOptionsStr.equals("")) {
                guessSongOptionsStr = guessSongOptionsStr + guessableSong;
            } else {
                guessSongOptionsStr = guessSongOptionsStr + "," + guessableSong;
            }
        }


        editor.putString("guessSongOptions",guessSongOptionsStr);
        editor.commit();

        return guess_song_options;
    }



    public Song getGameSong() {
        return gameSong;
    }

    public List<Song> getSongsList() {
        return songs;
    }

    public List<Placemark> getPlacemarks() {
        return placemarks;
    }

    public List<Style> getStyles() {
        return styles;
    }

    public List<String> getWordsFound() {
        return wordsFound;
    }
}
