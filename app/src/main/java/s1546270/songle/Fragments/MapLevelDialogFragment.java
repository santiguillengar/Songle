package s1546270.songle.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import s1546270.songle.Home;
import s1546270.songle.R;

/**
 * Created by SantiGuillenGar on 02/11/2017.
 *
 * Class implements dialog that pops up when user cicks FAB.
 * User inputs difficulty for map and fragment returns value to load appropriate map.
 */

public class MapLevelDialogFragment extends DialogFragment {


    public String difficulty = "2";

    private static final String TAG = "MapLevelDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d(TAG, "     |SANTI|      Dialog onCreateDialog Accessed");
        builder.setTitle(R.string.dialog_choose_difficulty);
        builder.setSingleChoiceItems(R.array.map_difficulty, 2,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int inDifficulty) {

                        Log.d(TAG, "     |SANTI|      Dialog Option Clicked "+inDifficulty);

                        difficulty = ""+(inDifficulty+1);


                    }
        })

                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Log.d(TAG, "     |SANTI|      Dialog Option Clicked "+difficulty);

                Home callingActivity = (Home) getActivity();
                callingActivity.onUserSelectDifficulty(difficulty);
            }
        });

        return builder.create();

    }
}
