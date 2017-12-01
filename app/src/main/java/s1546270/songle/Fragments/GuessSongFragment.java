package s1546270.songle.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

import s1546270.songle.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GuessSongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GuessSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuessSongFragment extends DialogFragment {

    //private OnFragmentInteractionListener mListener;
    int guess = -1;

    public GuessSongFragment() {
        // Required empty public constructor
    }


    private static final String TAG = GuessSongFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d(TAG, "     |SANTI|      Dialog onCreateDialog Accessed");
        builder.setTitle(R.string.dialog_guess_song);
        builder.setSingleChoiceItems(R.array.guess_song_options_demo, 2,
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
