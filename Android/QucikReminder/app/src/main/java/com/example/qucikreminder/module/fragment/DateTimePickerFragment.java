package com.example.qucikreminder.module.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.qucikreminder.R;
import com.example.qucikreminder.module.adapter.PagerAdapter;


public class DateTimePickerFragment extends DialogFragment implements TabLayout.OnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    public DateTimePickerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DateTimePickerFragment newInstance() {
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogClickListener dialgClickListener = new DialogClickListener();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Date and Time : ");
        mBuilder.setNegativeButton("Cancel",dialgClickListener);
        mBuilder.setPositiveButton("Ok",dialgClickListener);
        AlertDialog mDialog = mBuilder.create();
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        View dialogView = inflater.inflate(R.layout.fragment_date_time_picker,null,false);
        /*tabLayout  = (TabLayout) dialogView.findViewById(R.id.tabLayout);
        Log.d("Alpha","viewPager : "+tabLayout);
        viewPager = (ViewPager) dialogView.findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager)*/;

        TabHost tabHost = (TabHost) dialogView.findViewById(R.id.TabHost01);
        Log.d("Alpha","Tab Host : "+tabHost);

        mDialog.setView(dialogView);
        return mDialog;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private class DialogClickListener implements DialogInterface.OnClickListener {


        @Override
        public void onClick(DialogInterface dialog, int result) {
            Log.d("Alpha","Result : "+result);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DateFragment(), "DATE");
        adapter.addFragment(new TimeFragment(), "TIME");

        viewPager.setAdapter(adapter);
    }
}
