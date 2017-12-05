package s1546270.songle;

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

import java.util.List;

import s1546270.songle.Fragments.AboutFragment;
import s1546270.songle.Fragments.CorrectGuessFragment;
import s1546270.songle.Fragments.WordsFound;
import s1546270.songle.Fragments.GuessSongFragment;
import s1546270.songle.Objects.Song;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    // For logging purposes
    private static final String TAG = DrawerActivity.class.getSimpleName();

    //For fragment refresh purposes
    private String currentFragment = null;

    private Song gameSong;
    private List<Song> songs;

    private MapFragment mapFragment;


    //tut
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Songle");

        setSupportActionBar(toolbar);

        gameSong = (Song) getIntent().getSerializableExtra("song");
        songs = (List<Song>) getIntent().getSerializableExtra("songsList");
        Log.d(TAG, "     |SANTI|     Song that will be played: "+gameSong.getTitle()+" "+gameSong.getArtist());


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
            Snackbar.make(findViewById(R.id.drawer_layout), "words found pressed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            if (currentFragment != "wordsFoundFragment") {
                currentFragment = "wordsFoundFragment";

                WordsFound wordsFound = new WordsFound();

                DrawerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, wordsFound).commit();
            }

        } else if (id == R.id.nav_hints) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: hints page selected");
            Snackbar.make(findViewById(R.id.drawer_layout), "hints pressed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();


            if (currentFragment != "hintsFragment") {
                currentFragment = "hintsFragment";

                //DO STUFF HERE
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



        // TODO: NOW CHECK IF GUESS WAS CORRECT OR WRONG!

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

    public Song getGameSong() {
        return gameSong;
    }

    public List<Song> getSongsList() {
        return songs;
    }
}
