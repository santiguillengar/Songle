package s1546270.songle.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import s1546270.songle.DownloadTasks.DownloadLyricsTask;
import s1546270.songle.DownloadTasks.DownloadStylesTask;
import s1546270.songle.DownloadTasks.DownloadXmlTask;
import s1546270.songle.Fragments.AboutFragment;
import s1546270.songle.Fragments.GuessSongDialog;
import s1546270.songle.Fragments.HintsFragment;
import s1546270.songle.Fragments.InstructionsDialog;
import s1546270.songle.Fragments.MapFragment;
import s1546270.songle.Fragments.WordsFoundFragment;
import s1546270.songle.Objects.NetworkReceiver;
import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Song;
import s1546270.songle.Objects.Style;
import s1546270.songle.R;

import static android.provider.CalendarContract.CalendarCache.URI;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, MapFragment.WordCollectedListener {

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

    private HashMap<Integer, String> lyricsMap2 = new HashMap<>();
    List<String> lyricsLines;

    NetworkReceiver networkReceiver;
    static IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Songle");


        setSupportActionBar(toolbar);

        wordsFound = new ArrayList<>();

        this.networkReceiver = new NetworkReceiver();
        registerReceiver(this.networkReceiver, filter);

        try {
            gameSong = (Song) getIntent().getSerializableExtra("song");
            songs = (List<Song>) getIntent().getSerializableExtra("songsList");
            Log.d(TAG, "Song that will be played: "+gameSong.getTitle()+" "+gameSong.getArtist());
        } catch (Exception e){
            Log.e(TAG, "Exception raised in DrawerActivity onCreate");
        }

        // Retrieving Map Difficulty
        SharedPreferences pref = getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();
        mapDifficulty = pref.getString("mapDifficulty", null);
        Log.d(TAG, "Shared Pref Retrieved");


        processLyrics();
        downloadPlacemarksAndStyles();

        //DEFAULT FAB
        FloatingActionButton map_fab = (FloatingActionButton) findViewById(R.id.fab);
        map_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // FAB has been pressed, user wants to guess what song it is.
                // Show song guess options fragment.
                android.app.FragmentManager manager = getFragmentManager();

                GuessSongDialog guessSongFragment = new GuessSongDialog();
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkReceiver, filter);
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
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Log.d(TAG, "Options Item Selected");
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FragmentManager manager = getSupportFragmentManager();
            InstructionsDialog instructionsFragment = new InstructionsDialog();
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
            Log.d(TAG, "DrawerActivity: map page selected");

            if (currentFragment != "mapFragment") {
                currentFragment = "mapFragment";
                //Display Map Fragment
                mapFragment = new MapFragment();

                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();

            }

        } else if (id == R.id.nav_words_found) {
            Log.d(TAG, "DrawerActivity: words_found page selected");

            if (currentFragment != "wordsFoundFragment") {
                currentFragment = "wordsFoundFragment";

                WordsFoundFragment wordsFound = new WordsFoundFragment();

                DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, wordsFound).commit();
            }

        } else if (id == R.id.nav_hints) {
            Log.d(TAG, "DrawerActivity: hints page selected");

            if (currentFragment != "hintsFragment") {
                currentFragment = "hintsFragment";

                HintsFragment hintsFragment = new HintsFragment();
                DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, hintsFragment).commit();

            }

        } else if (id == R.id.nav_about) {
            Log.d(TAG, "DrawerActivity: about page selected");

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

        AlertDialog.Builder dialog = new AlertDialog.Builder(DrawerActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        SharedPreferences pref = getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();

        int score = pref.getInt("score",1000000);
        if (score < 0) {
            score = 0;
        }
        Log.d(TAG, "User guessed the song with current score: "+score);

        if (guessedSongTitle.equals(gameSong.getTitle())){


            View dialogView = inflater.inflate(R.layout.fragment_correct_guess, null);

            Button ok_Button = (Button) dialogView.findViewById(R.id.correct_guess_ok_button);
            Button youtube_Button = (Button) dialogView.findViewById(R.id.correct_guess_youtube_button);

            dialog.setView(dialogView);

            final AlertDialog alertDialog = dialog.create();

            ok_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            TextView textView = (TextView) dialogView.findViewById(R.id.correct_guess_score);
            textView.setText("Score: "+score);

            // If link isn't working don't show youtube button to user.
            if (gameSong.getLink() == null || gameSong.getLink().equals("")) {
                youtube_Button.setVisibility(View.INVISIBLE);
            }
            youtube_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(URI.parse(gameSong.getLink()));
                    startActivity(intent);

                    alertDialog.dismiss();
                }
            });

            alertDialog.show();

        } else {

            editor.putInt("score",score-250000);
            editor.commit();
            View dialogView = inflater.inflate(R.layout.fragment_wrong_guess, null);

            Button ok_Button = (Button) dialogView.findViewById(R.id.wrong_guess_ok_button);
            Button hints_Button = (Button) dialogView.findViewById(R.id.wrong_guess_hint_button);

            dialog.setView(dialogView);

            final AlertDialog alertDialog = dialog.create();

            ok_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            hints_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentFragment != "hintsFragment") {
                        currentFragment = "hintsFragment";

                        HintsFragment hintsFragment = new HintsFragment();
                        DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, hintsFragment).commit();
                    }
                    alertDialog.dismiss();

                }
            });

            alertDialog.show();

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

        if (styles != null) {
            for (Style s : styles) {
                Log.d(TAG, "STYLE: ID: "+s.getId()+" ICONSTYLE: "+s.getIconStyle());
            }
        } else {
            Log.e(TAG, "ERROR: Styles is null in donwloadPlacemarksAndStyles() ");
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
            } // I don't know why but when the number is 1 the above if doesn't catch it.
            else {
                songNumber = ""+gameSong.getNumber();
            }
            Log.d(TAG, "Determining Map URL: songNumber string: "+songNumber);
        } else {

            Log.e(TAG, "gameSong was null in determineMapUrl");

        }

        String url = baseUrl + songNumber + "/map" + mapDifficulty + ".kml";

        // The app has a weird bug where this specific url keeps breaking.
        if (url.equals("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/1/map2.kml")) {
            url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map2.kml";
        }

        Log.d(TAG, "URL determined: "+url);
        Log.d(TAG, "SONG CHOSEN: "+songNumber);
        return url;
    }





    public ArrayList<String> makeSongList() {

        ArrayList<String> guess_song_options = new ArrayList<>();
        Log.d(TAG, "PossibleSongs in GuessSongDialog: "+songs.toString());
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


    public void processLyrics() {

        Log.d(TAG, "processLyrics() accessed");

        String url = determineLyricsUrl();
        DownloadLyricsTask dtLyrics = new DownloadLyricsTask();
        String lineIndex;
        String lineText;

        try {
            dtLyrics.execute(url);
            lyricsLines = dtLyrics.get();

        } catch (Exception e) {
            Log.e(TAG, "ERROR: Couldn't download lyrics for song "+url);
        }

        try {
            for (String line: lyricsLines) {

                lineIndex = line.substring(0,7);
                int testIndex = Integer.parseInt(lineIndex.replaceAll("\\D+",""));
                Log.d(TAG, "testIndex: "+testIndex);
                lineText = "";

                if (line.length() > 7) {
                    lineText = line.substring(7);
                }

                lyricsMap2.put(testIndex, lineText);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing lyrics into lyricsMap");
        }
    }


    public String determineLyricsUrl() {

        String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/";
        String songNumber;

        if (gameSong != null) {

            Log.d(TAG, "Determining Lyrics URL: gameSong number: "+gameSong.getNumber());

            if (gameSong.getNumber() < 10) {
                songNumber = "0"+gameSong.getNumber();
            } else {
                songNumber = ""+gameSong.getNumber();
            }

            Log.d(TAG, "Determining Lyrics URL: songNumber string: "+songNumber);

        } else {

            Log.e(TAG, "gameSong was null in determineLyricsUrl");
            songNumber = "" + ((int) Math.random() * 10 + 1);
        }

        String url = baseUrl + songNumber + "/words.txt";
        Log.d(TAG, "URL determined: "+url);

        return url;
    }


    public String getLyricsLine() {
        Random generator = new Random();
        int chosenIndex = generator.nextInt(lyricsLines.size());
        String line = lyricsLines.get(chosenIndex).substring(7);
        while( line=="" || line==null ){
            chosenIndex = generator.nextInt(lyricsLines.size());
        }
        return line;

    }


    @Override
    public void newWordFound(String wordIndex) {
        Log.d(TAG, "New word collected in DrawerActivity: "+wordIndex);

        String[] wordPos = wordIndex.split(":");
        String newWordLine = lyricsMap2.get(Integer.parseInt(wordPos[0]));
        String[] words = newWordLine.split(" ");
        String wordFound = words[Integer.parseInt(wordPos[1])-1];

        wordsFound.add(wordFound);

        Toast.makeText(this, "New Word Collected: "+wordFound, Toast.LENGTH_SHORT ).show();
        Log.d(TAG, "Word received:  Pos: " + wordIndex+"Text: "+wordFound);


    }

    public void revealSongTitle() {
        Log.d(TAG, "someone called secret method to reveal answer!");
        Toast.makeText(this, gameSong.getTitle(), Toast.LENGTH_SHORT ).show();

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
