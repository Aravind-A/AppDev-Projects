package com.example.aravind.audiorecorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by aravind on 23/4/16.
 */
public class EditDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        super.onCreateDialog(savedInstanceState);
        final String editName = getArguments().getString("editName");

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.editname, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.name);
        editText.setText(editName);

        final Intent intent = new Intent();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
                .setTitle("Change Recording name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("editChoice",1);
                        intent.putExtra("newName",editText.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(),1,intent);
                        dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("editChoice",0);
                        getTargetFragment().onActivityResult(getTargetRequestCode(),1,intent);
                        dismiss();
                    }
                });
        return builder.create();
    }
}
