package com.example.quickshare.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.quickshare.R;

/**
 * Created by root on 28/7/16.
 */
public class CustomDialogFragment extends DialogFragment {

    private String TAG = "CustomDialogFragment";

    private int dialogType;

    private String dialogTitle;

    private String dialogMessage;

    private Dialog dialog = null;

    private AlertDialog.Builder builder;

    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"inside onCreate of CustomDialog : ");
        inflater = getActivity().getLayoutInflater();
        Bundle bundle = getArguments();
        dialogType = bundle.getInt("DIALOG_TYPE");
        dialogTitle = bundle.getString("DIALOG_TITLE");
        dialogMessage = bundle.getString("DIALOG_MESSAGE");
        Log.d(TAG,"TYPE : "+dialogType);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG,"inside onCreateDialog : ");
        switch (dialogType) {
            case 0 : //Dialog for confirmation
                Log.d(TAG,"inside case 0 : ");
                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(dialogTitle);
                builder.setMessage(dialogMessage);
                builder.setCancelable(false);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                dialog = builder.create();
                break;
            case 1 : //Dialog to show loader
                Log.d(TAG,"inside case 1 : ");
                ProgressDialog progressDialog = new ProgressDialog(getActivity(), getTheme());
                progressDialog.setTitle(dialogTitle);
                progressDialog.setMessage(dialogMessage);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                return progressDialog;

            case 2: //Dialog to show List of available devices
                Log.d(TAG,"inside case 2 : ");
                View view = inflater.inflate(R.layout.device_list,null);
                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(dialogTitle);
                builder.setView(view);
                builder.setCancelable(false);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                dialog = builder.create();
                break;
        }
        return dialog;
    }
}
