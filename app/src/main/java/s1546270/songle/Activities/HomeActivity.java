package s1546270.songle.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import s1546270.songle.DownloadTasks.DownloadSongsTask;
import s1546270.songle.Fragments.MapDifficultyDialog;
import s1546270.songle.Objects.NetworkReceiver;
import s1546270.songle.Objects.Song;
import s1546270.songle.R;


public class HomeActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = HomeActivity.class.getSimpleName();

    private String mapDifficulty = "" + R.integer.default_difficulty;

    // Store list of songs parsed
    List<Song> songs = null;

    private Song gameSong = null;


    NetworkReceiver networkReceiver;
    CallbackManager callbackManager;

    String fbData;

    private boolean loggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        this.networkReceiver = new NetworkReceiver();
        registerReceiver(this.networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        gameSong = selectGameplaySong();
        if (gameSong == null) {
            Log.e(TAG, "ERROR: Couldn't retrieve song for the game in HomeActivity");
        }


        Log.d(TAG, " HOME_FAB");
        FloatingActionButton home_fab = (FloatingActionButton) findViewById(R.id.home_fab);
        home_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = getFragmentManager();

                MapDifficultyDialog mldf = new MapDifficultyDialog();
                mldf.show(manager, "");


            }
        });


        callbackManager = CallbackManager.Factory.create();
        loggedIn = AccessToken.getCurrentAccessToken() != null;

        LoginButton fbButton = (LoginButton) findViewById(R.id.login_button);
        fbButton.setReadPermissions("email");
        Log.d(TAG, "Reached here 4");

        fbButton.setVisibility(View.VISIBLE);

        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"Reached here Z1");
                loggedIn = true;

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                fbData = object.toString();
                                Log.d(TAG, "penis");
                            }
                        });


                Bundle permission_param = new Bundle();
                permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
                request.setParameters(permission_param);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d(TAG,"Reached here Z2");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"Reached here Z3");
            }
        });


    }


    // Part of FB integration
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onUserSelectDifficulty(String inputDifficulty) {

        Log.d(TAG, "RECEIVED CHALLENGE LEVEL: "+inputDifficulty);
        mapDifficulty = inputDifficulty;

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();
        // Update Preferences to include the difficulty of the map
        editor.putString("mapDifficulty",mapDifficulty);
        editor.commit();


        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
        intent.putExtra("song", gameSong);
        intent.putExtra("songsList",(Serializable) songs);
        intent.putExtra("fbData",fbData);
        startActivity(intent);

        unregisterReceiver(networkReceiver);
        // user should not be allowed to go back to home once the game has started
        finish();
    }

    public Song selectGameplaySong() {

        Log.d(TAG, "selectGameplaySong() accessed");
        Random generator = new Random();

        String[] recentSongsPlayed;
        String rspStr;

        ArrayList<String> availableSongs = new ArrayList<>();


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


        // Initialization
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();


        // Retrieving songs played
        rspStr = pref.getString("recentSongsPlayed", null);
        Log.d(TAG, "Shared Pref Retrieved");
        recentSongsPlayed = rspStr.split(",");
        Log.d(TAG, "recentSongsPlayed list: "+Arrays.toString(recentSongsPlayed));

        for (int i=1; i <= songs.size(); i++){
            if (!Arrays.asList(recentSongsPlayed).contains(""+i)) {
                availableSongs.add(""+i);
            }
        }
        Log.d(TAG, "availableSongs list: "+Arrays.toString(availableSongs.toArray()));

        // All songs are in the recent songs so restart cycle through song list
        if (availableSongs.isEmpty()){
            for (int i=1; i <= songs.size(); i++){
                availableSongs.add(""+i);
            }
            rspStr="";
        }

        int randomIndexAS = generator.nextInt((availableSongs.size()));

        String newRspStr = null;
        if (rspStr!=""){
            newRspStr = rspStr+","+availableSongs.get(randomIndexAS);
        } else {
            newRspStr = rspStr+availableSongs.get(randomIndexAS);
        }

        Log.d(TAG, "recentSongsPlayed updated to: "+newRspStr);

        // Update Preferences to include the newly chosen song
        editor.putString("recentSongsPlayed",newRspStr);
        editor.commit();

        Log.d(TAG, "Random Song Index Chosen: "+availableSongs.get(randomIndexAS));
        for (Song song : songs) {
            Log.d(TAG, "Looking for gameplay Song. Song Number: "+song.getNumber());
            if (song.getNumber() == Integer.parseInt(availableSongs.get(randomIndexAS))){
                return song;
            }
        }
        Log.e(TAG,"Error selecting random song, returned song 1");
        return songs.get(1);

    }

    public List<Song> getSongs() {
        return songs;
    }
}


