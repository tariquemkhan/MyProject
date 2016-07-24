package com.example.quickreminder.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.example.quickreminder.R;

/**
 * Created by Dell on 7/23/2016.
 */
public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Simple Dialog");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_date_picker,null);
        builder.setView(view);


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

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        Log.d("Alpha","dividerId : "+dividerId);
        View divider = d.findViewById(dividerId);
        Log.d("Alpha","Divider : "+divider);
        //divider.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        return builder.create();
    }


}
