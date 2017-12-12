package s1546270.songle.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;


import s1546270.songle.R;

/**
 * Dialog to show instructions when triple dot button clicked.
 */
public class InstructionsDialog extends DialogFragment {

    private String TAG = InstructionsDialog.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d(TAG, " Dialog onCreateDialog Accessed");
        builder.setTitle(R.string.dialog_instructions_header)
                .setMessage(R.string.map_help)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(TAG, " Instructions OK Button Clicked ");

                        dialog.dismiss();
                    }
                });

        return builder.create();

    }

}
