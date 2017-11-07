package s1546270.songle;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.URL;
import java.util.List;

import s1546270.songle.Objects.MapLevelDialogFragment;
import s1546270.songle.Objects.Placemark;

import static android.app.PendingIntent.getActivity;

public class Home extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = Home.class.getSimpleName();

    private String mapDifficulty = "" + R.integer.default_difficulty;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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



        //Originally LyricMapUI, currently DrawerActivity
        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
        intent.putExtra("difficulty", mapDifficulty);
        startActivity(intent);


        // user should not be allowed to go back to home once the game has started
        finish();
    }

}


