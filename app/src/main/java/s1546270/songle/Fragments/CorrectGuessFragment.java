package s1546270.songle.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import s1546270.songle.R;


public class CorrectGuessFragment extends DialogFragment {


    private String TAG = CorrectGuessFragment.class.getSimpleName();

    /*public CorrectGuessFragment() {
        // Required empty public constructor
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_correct_guess, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_correct_guess_message)
        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(TAG, "     |SANTI|      Correct Guess Dialog Ok Clicked ");

                        //Home callingActivity = (Home) getActivity();
                        //callingActivity.onUserSelectDifficulty(difficulty);
                    }
                });

        return builder.create();

    }

}
