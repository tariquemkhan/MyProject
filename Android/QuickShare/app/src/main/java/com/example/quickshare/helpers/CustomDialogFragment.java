package com.example.quickshare.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.DeviceListAdapter;
import com.example.quickshare.database.models.DeviceModel;

import java.util.ArrayList;

/**
 * Created by root on 28/7/16.
 */
public class CustomDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    private String TAG = "CustomDialogFragment";

    private ListView lvDevices;

    private DeviceListAdapter adapter;

    private int dialogType;

    private String dialogTitle;

    private String dialogMessage;

    private Dialog dialog = null;

    private ProgressDialog progressDialog;

    private AlertDialog.Builder builder;

    private LayoutInflater inflater;

    private IntentFilter deviceIntentFilter;

    private String DEVICE_BROADCAST_KEY = "device_broadcast_key";

    private ArrayList<DeviceModel> deviceModelArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"inside onCreate of CustomDialog : ");
        inflater = getActivity().getLayoutInflater();
        Bundle bundle = getArguments();
        dialogType = bundle.getInt("DIALOG_TYPE");
        dialogTitle = bundle.getString("DIALOG_TITLE");
        dialogMessage = bundle.getString("DIALOG_MESSAGE");
        deviceModelArrayList = (ArrayList<DeviceModel>) bundle.getSerializable("DEVICES_LIST");
        //Log.d(TAG,"TYPE : "+dialogType);
        if (deviceModelArrayList != null) {
            Log.d(TAG,"device list : "+deviceModelArrayList.size());
        }
        deviceIntentFilter = new IntentFilter(DEVICE_BROADCAST_KEY);
        deviceModelArrayList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Log.d(TAG,"inside onCreateDialog : ");
        switch (dialogType) {
            case 0 : //Dialog for confirmation
                //Log.d(TAG,"inside case 0 : ");
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
                //Log.d(TAG,"inside case 1 : ");
                progressDialog = new ProgressDialog(getActivity(), getTheme());
                if (dialogTitle != null) {
                    progressDialog.setTitle(dialogTitle);
                }
                progressDialog.setMessage(dialogMessage);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                return progressDialog;

            case 2: //Dialog to show List of available devices
                //Log.d(TAG,"inside case 2 : ");
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

                lvDevices = (ListView) view.findViewById(R.id.lvDevice);
                lvDevices.setOnItemClickListener(this);
                if (deviceModelArrayList != null) {
                    Log.d(TAG,"inside if : ");
                    Log.d(TAG,"Device : "+deviceModelArrayList.size());
                    adapter = new DeviceListAdapter(getActivity(),R.layout.device_list,deviceModelArrayList);
                    lvDevices.setAdapter(adapter);
                }

                break;
        }
        return dialog;
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
