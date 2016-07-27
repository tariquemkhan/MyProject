package com.example.quickshare.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.AppsAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AppsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView gvApps;

    private List<ApplicationInfo> packages;

    private AppsAdapter adapter;

    private String TAG_NAME = "AppsFragment";

    private Context mContext;

    private PackageManager pm;

    private List<ApplicationInfo> userInstalledApp;

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
        userInstalledApp = new ArrayList<>();
        pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                // System application
            } else {
                // Installed by user
                userInstalledApp.add(packageInfo);
            }
            /*Log.d(TAG_NAME, "Installed package :" + packageInfo.packageName);
            Log.d(TAG_NAME, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG_NAME, "App Name :" + packageInfo.loadLabel(pm));
            Log.d(TAG_NAME,"-------------------------------------------------------------------------");*/
        }
        adapter = new AppsAdapter(userInstalledApp,mContext);
        gvApps.setAdapter(adapter);

        gvApps.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG_NAME,"You clicked position : "+position);
        try {
            ApplicationInfo applicationInfo = adapter.getItem(position);
            File apkfile = new File(applicationInfo.publicSourceDir);
            /*String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+applicationInfo.loadLabel(pm)+".apk";
            File destFile = new File(path);
            if (!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();

            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            InputStream in = new FileInputStream(apkfile);
            OutputStream out = new FileOutputStream(path);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();*/
        }catch (Exception e) {
            Log.d(TAG_NAME,"Exception in onItemClick : "+e.toString());
        }
    }
}
