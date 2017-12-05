package s1546270.songle.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import s1546270.songle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HintsFragment extends Fragment {

    private String TAG = HintsFragment.class.getSimpleName();

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
        Toast.makeText(getActivity(), "Show Lyrics Hint button pressed!", Toast.LENGTH_LONG ).show();
    }

    public void reducePossibleGuesses() {
        Toast.makeText(getActivity(), "Reduce Guesses Hint button pressed!", Toast.LENGTH_LONG ).show();
    }

}
