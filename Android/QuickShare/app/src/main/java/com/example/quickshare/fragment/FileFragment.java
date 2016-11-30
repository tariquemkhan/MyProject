package com.example.quickshare.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.quickshare.R;
import com.example.quickshare.activity.FileTransferActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FileFragment extends Fragment {

    private RecyclerView rvShow;

    public int sectionCount = 1;

    private String TAG = "FileFragment";


    public FileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance(String param1, String param2) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        ListView myList = (ListView)view. findViewById(R.id.lvShow);
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_ADDED};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        Cursor imagecursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");
        Log.d(FileTransferActivity.TAG_NAME,"Cursor size : "+imagecursor.getCount());

        for (int i = 0; i < imagecursor.getCount(); i++) {
            Date currentImageDate;
            Date nextImageDate = new Date();
            imagecursor.moveToPosition(i);
            int realImageTime = imagecursor.getInt(imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            currentImageDate = new Date(realImageTime*1000);
            Log.d(TAG,"Current : "+currentImageDate);
            if (i != (imagecursor.getCount()-1)) {
                imagecursor.moveToPosition(i+1);
                int nextImageTime = imagecursor.getInt(imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                nextImageDate = new Date(nextImageTime*1000);
                Log.d(TAG,"Next : "+nextImageDate);
            }

            if (nextImageDate != null) {
                if (! fmt.format(currentImageDate).equals(fmt.format(nextImageDate))) {
                    Log.d(TAG,"Match Not Found  : ");
                }
            }
        }

        return view;
    }


}
