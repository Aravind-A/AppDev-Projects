package com.example.aravind.quicknotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by aravind on 29/3/16.
 */
public class DialogShowNote extends DialogFragment {
    private Note mNote = new Note();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceSate){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_note_show, null);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);
        Button button = (Button) dialogView.findViewById(R.id.btnOK);
        ImageView ivImportant = (ImageView) dialogView.findViewById(R.id.imageViewImportant);
        //ImageView ivTodo = (ImageView) dialogView.findViewById(R.id.imageViewTodo);
        ImageView ivIdea = (ImageView) dialogView.findViewById(R.id.imageViewIdea);

        txtTitle.setText(mNote.getTitle());
        txtDescription.setText(mNote.getDescription());

        if(!mNote.isImportant())
            ivImportant.setVisibility(View.GONE);
        if(!mNote.isIdea())
            ivIdea.setVisibility(View.GONE);

        builder.setView(dialogView).setMessage("");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void sendNoteSelected(Note noteSelected){
        mNote = noteSelected;
    }
}
