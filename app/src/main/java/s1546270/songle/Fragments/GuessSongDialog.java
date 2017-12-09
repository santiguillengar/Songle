package s1546270.songle.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import s1546270.songle.Activities.DrawerActivity;
import s1546270.songle.Objects.Song;
import s1546270.songle.R;

/**
 *
 */
public class GuessSongDialog extends DialogFragment {

    //private OnFragmentInteractionListener mListener;
    int guess = -1;

    public GuessSongDialog() {
        // Required empty public constructor
    }

    private Song gameSong;

    private static final String TAG = GuessSongDialog.class.getSimpleName();

    private ArrayList<String> guess_song_options;

    private int guessedSong = 2;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        gameSong = ((DrawerActivity) getActivity()).getGameSong();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d(TAG, "     |SANTI|      Dialog onCreateDialog Accessed");
        builder.setTitle(R.string.dialog_guess_song);

        String check_song_options = check_song_options_determined();
        if (check_song_options == "") {
            guess_song_options = ((DrawerActivity)getActivity()).makeSongList();;
        }
        else {
            guess_song_options = new ArrayList<>(Arrays.asList(check_song_options.split(",")));
        }

        String[] guess_song_array = guess_song_options.toArray(new String[guess_song_options.size()]);
        builder.setSingleChoiceItems(guess_song_array, 2,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int guess) {

                        guessedSong = guess;
                        Log.d(TAG, "     |SANTI|      Guess Dialog Option Clicked "+guess);

                        System.out.print(guess);

                    }
                })

                .setPositiveButton(R.string.dialog_confirm_guess, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(TAG, "     |SANTI|      Guess Dialog Ok Clicked "+id);

                        DrawerActivity callingActivity = (DrawerActivity) getActivity();
                        callingActivity.onUserGuessSong(guess_song_options.get(guessedSong));
                        //Log.d(TAG,"FINDME GUESS: "+guessedSong);
                        //Log.d(TAG,"FINDME What I Passed: "+guess_song_options.get(guessedSong));
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "     |SANTI|      Guess Dialog Cancel Clicked ");
                    }
                });

        return builder.create();

    }


    public String check_song_options_determined() {


        // Initialization
        SharedPreferences pref = this.getActivity().getSharedPreferences("SonglePref", 0);
        SharedPreferences.Editor editor = pref.edit();

        String guess_song_options = pref.getString("guessSongOptions", null);
        return guess_song_options;
    }

}
