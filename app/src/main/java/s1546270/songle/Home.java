package s1546270.songle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.net.URL;
import java.util.List;

import s1546270.songle.Objects.Placemark;

public class Home extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = Home.class.getSimpleName();
    //StackOverflowXmlParser parser = new StackOverflowXmlParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LyricMapUI.class);
                startActivity(i);
            }
        });
    }
}


