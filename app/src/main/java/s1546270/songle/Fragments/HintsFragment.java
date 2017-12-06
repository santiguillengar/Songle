package s1546270.songle.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import s1546270.songle.DownloadLyricsTask;
import s1546270.songle.DrawerActivity;
import s1546270.songle.Objects.Song;
import s1546270.songle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HintsFragment extends Fragment {

    private String TAG = HintsFragment.class.getSimpleName();

    private List<String> lyricsLines;

    public HintsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_hints, container, false);


        View view =  inflater.inflate(R.layout.fragment_hints, container, false);

        view.findViewById(R.id.hints_show_lyrics_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "     |SANTI|     Hints Fragment: show lyrics line button pressed");
                showLyricsLine();
            }
        });

        view.findViewById(R.id.hints_reduce_guesses_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "     |SANTI|     Hints Fragment: reduce guesses button pressed");
                reducePossibleGuesses();
            }
        });

        return view;
    }


    public void showLyricsLine() {

        String url = determineLyricsUrl();
        DownloadLyricsTask dtLyrics = new DownloadLyricsTask();

        String chosenLine = null;
        int chosenIndex;
        Random generator = new Random();


        try {
            dtLyrics.execute(url);
            lyricsLines = dtLyrics.get();

            chosenIndex = generator.nextInt(lyricsLines.size());
            while( lyricsLines.get(chosenIndex).substring(9)=="" || lyricsLines.get(chosenIndex).substring(9)==null ){
                chosenIndex = generator.nextInt(lyricsLines.size());
            }

            Toast.makeText(getActivity(), lyricsLines.get(chosenIndex).substring(7), Toast.LENGTH_LONG ).show();

        } catch (Exception e) {
            Log.e(TAG, "ERROR: Couldn't download lyrics for song");
        }
    }

    public void reducePossibleGuesses() {

        // Initialization
        SharedPreferences pref = this.getActivity().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();

        int numGuessableSongs = pref.getInt("numGuessableSongs",2);

        if (numGuessableSongs <= 2) {
            Toast.makeText(getActivity(), "You can't reduce the number of options anymore!", Toast.LENGTH_LONG ).show();
        }
        else {

            editor.putInt("numGuessableSongs",numGuessableSongs-1);

            ArrayList<String> guess_song_options;
            String check_song_options = check_song_options_determined();
            Song gameSong = ((DrawerActivity)getActivity()).getGameSong();

            if (check_song_options == "") {
                guess_song_options = ((DrawerActivity)getActivity()).makeSongList();
            }
            else {
                guess_song_options = new ArrayList<>(Arrays.asList(check_song_options.split(",")));
            }

            Random generator = new Random();
            int chosenIndex = generator.nextInt(guess_song_options.size());
            while (guess_song_options.get(chosenIndex).equals(gameSong.getTitle())) {
                chosenIndex = generator.nextInt(guess_song_options.size());
            }
            guess_song_options.remove(chosenIndex);

            String guessSongOptionsStr = "";

            for (String guessableSong : guess_song_options) {
                if (guessSongOptionsStr.equals("")) {
                    guessSongOptionsStr = guessSongOptionsStr + guessableSong;
                } else {
                    guessSongOptionsStr = guessSongOptionsStr + "," + guessableSong;
                }
            }


            editor.putString("guessSongOptions",guessSongOptionsStr);
            editor.commit();

            Toast.makeText(getActivity(), "The list of possible answers has been reduced!", Toast.LENGTH_LONG ).show();

        }

    }



    public String determineLyricsUrl() {

        Song gameSong = ((DrawerActivity)getActivity()).getGameSong();
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
        Log.d(TAG, "     |SANTI|     URL determined: "+url);

        return url;
    }


    public String check_song_options_determined() {

        // Initialization
        SharedPreferences pref = this.getActivity().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();

        String guess_song_options = pref.getString("guessSongOptions", null);
        return guess_song_options;
    }

}
