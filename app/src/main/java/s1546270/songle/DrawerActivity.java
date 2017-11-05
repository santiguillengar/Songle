package s1546270.songle;

//import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    // For logging purposes
    private static final String TAG = DrawerActivity.class.getSimpleName();

    //For fragment refresh purposes
    private String currentFragment = null;

    //tut
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //DEFAULT FAB
        FloatingActionButton map_fab = (FloatingActionButton) findViewById(R.id.fab);
        map_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FAB pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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


        //FragmentManager fragmentManager = getSupportFragmentManager();
        //Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainLayout);
        //Log.d(TAG, "     |SANTI|      CURRENT FRAGMENT: "+currentFragment.getTag());

        if (id == R.id.nav_map) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: map page selected");
            Snackbar.make(findViewById(R.id.drawer_layout), "map pressed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            if (currentFragment != "mapFragment") {
                currentFragment = "mapFragment";
                //Display Map Fragment
                MapFragment mapFragment = new MapFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();


            }

        } else if (id == R.id.nav_words_found) {
            Log.d(TAG, "     |SANTI|     DrawerActivity: words_found page selected");
            Snackbar.make(findViewById(R.id.drawer_layout), "words found pressed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            if (currentFragment != "wordsFoundFragment") {
                currentFragment = "wordsFoundFragment";

                //DO STUFF HERE
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
            Snackbar.make(findViewById(R.id.drawer_layout), "about pressed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();


            if (currentFragment != "aboutFragment") {
                currentFragment = "aboutFragment";

                //DO STUFF HERE
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
