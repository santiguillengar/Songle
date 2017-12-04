package s1546270.songle.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import s1546270.songle.DrawerActivity;
import s1546270.songle.Home;
import s1546270.songle.Objects.Song;
import s1546270.songle.R;

import static java.util.Arrays.asList;

/**
 *
 */
public class GuessSongFragment extends DialogFragment {

    //private OnFragmentInteractionListener mListener;
    int guess = -1;

    public GuessSongFragment() {
        // Required empty public constructor
    }

    private Song gameSong;

    private static final String TAG = GuessSongFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        gameSong = ((DrawerActivity) getActivity()).getGameSong();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d(TAG, "     |SANTI|      Dialog onCreateDialog Accessed");
        builder.setTitle(R.string.dialog_guess_song);

        ArrayList<String> guess_song_options = makeSongList();

        String[] guess_song_array = guess_song_options.toArray(new String[guess_song_options.size()]);
        builder.setSingleChoiceItems(guess_song_array, 2,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int guess) {

                        Log.d(TAG, "     |SANTI|      Guess Dialog Option Clicked "+guess);

                        System.out.print(guess);

                    }
                })

                .setPositiveButton(R.string.dialog_confirm_guess, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(TAG, "     |SANTI|      Guess Dialog Ok Clicked "+guess);

                        //Home callingActivity = (Home) getActivity();
                        //callingActivity.onUserSelectDifficulty(difficulty);
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

    public  ArrayList<String> makeSongList() {

        ArrayList<String> guess_song_options = new ArrayList<>();
        List<Song> possibleSongs = ((DrawerActivity) getActivity()).getSongsList();
        Log.d(TAG, "PossibleSongs in GuessSongFragment: "+possibleSongs.toString());
        Random random = new Random();
        int index;


        if (possibleSongs == null) {
            String[] a = {"a","b","c"};
            guess_song_options.add(gameSong.getTitle());
            guess_song_options.addAll(Arrays.asList(getResources().getStringArray(R.array.guess_song_options_demo)));
        }
        else {

            for (int i = 0; i < 4; i++) {
                index = random.nextInt(possibleSongs.size());
                guess_song_options.add(possibleSongs.get(index).getTitle());
            }
            guess_song_options.add(gameSong.getTitle());
            Collections.shuffle(guess_song_options);
        }
        return guess_song_options;
    }







    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuessSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static GuessSongFragment newInstance(String param1, String param2) {
        GuessSongFragment fragment = new GuessSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guess_song, container, false);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
