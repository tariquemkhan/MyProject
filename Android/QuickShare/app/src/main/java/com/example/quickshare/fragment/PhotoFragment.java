package com.example.quickshare.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickshare.R;

public class PhotoFragment extends Fragment {

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }


}
