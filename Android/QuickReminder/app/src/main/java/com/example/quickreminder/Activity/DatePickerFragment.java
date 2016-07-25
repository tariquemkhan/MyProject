package com.example.quickreminder.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import com.example.quickreminder.R;

/**
 * Created by Dell on 7/23/2016.
 */
public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;

    private TimePicker timePicker;

    private String TAG = "DatePickerFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Simple Dialog");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_date_picker,null);
        builder.setView(view);
        builder.setCancelable(false);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        timePicker = (TimePicker)view.findViewById(R.id.timePicker);

        TabHost host = (TabHost)view.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("DATE");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("TIME");
        spec.setContent(R.id.tab2);
        spec.setIndicator("TIME");
        host.addTab(spec);

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
        return builder.create();
    }


}
