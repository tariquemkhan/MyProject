package com.example.quickshare.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.AppsAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AppsFragment extends Fragment {

    private GridView gvApps;

    private List<ApplicationInfo> packages;

    private AppsAdapter adapter;

    private String TAG_NAME = "AppsFragment";

    private Context mContext;

    public AppsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps, container, false);
        mContext = getActivity();
        gvApps = (GridView) view.findViewById(R.id.gvApps);
        final PackageManager pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        adapter = new AppsAdapter(packages,mContext);
        gvApps.setAdapter(adapter);
        /*for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG_NAME, "Installed package :" + packageInfo.packageName);
            Log.d(TAG_NAME, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG_NAME, "App Name :" + packageInfo.loadLabel(pm));
            Log.d(TAG_NAME,"-------------------------------------------------------------------------");
        }*/
        return view;
    }
}
