package s1546270.songle.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

import s1546270.songle.R;

/**
 * First screen displayed when app is started.
 */
public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // Refresh important values at start of game state.

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("guessSongOptions","");
        editor.putString("mapDifficulty","");
        editor.putInt("numGuessableSongs",5);
        editor.putInt("score",1000000);
        editor.commit();


        // Once timer is over show the home activity screen.
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // This method will be executed once the timer is over

                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}