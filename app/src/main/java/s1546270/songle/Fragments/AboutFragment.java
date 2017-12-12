package s1546270.songle.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import s1546270.songle.Activities.DrawerActivity;
import s1546270.songle.R;

/**
 * Simple dialog showing information about the app.
 */
public class AboutFragment extends Fragment {

    // For logging purposes
    private static final String TAG = AboutFragment.class.getSimpleName();

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);

        view.findViewById(R.id.reveal_song_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Hints Fragment: show lyrics line button pressed");

                ((DrawerActivity)getActivity()).revealSongTitle();
            }
        });
        return view;
    }

}
