package com.example.aravind.audiorecorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by aravind on 23/4/16.
 */
public class DeleteDialog extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreateDialog(savedInstanceState);
        String name = getArguments().getString("delName");
        final Intent intent = new Intent();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this recording ?")
                .setMessage("Audio Recording '" + name + "' will be deleted.")
                .setPositiveButton("OK",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("choice",1);
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(), 0, intent);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("choice",0);
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(),0,intent);
                        dismiss();
                    }
                });
        return builder.create();
    }


}
