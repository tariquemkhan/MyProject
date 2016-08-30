package com.example.quickshare.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.quickshare.R;
import com.example.quickshare.activity.FileTransferActivity;
import com.example.quickshare.adapter.AllPhotoAdapter;
import com.example.quickshare.helpers.Utils;

import java.util.ArrayList;

public class PhotoFragment extends Fragment {

    private AllPhotoAdapter allPhotoAdapter;

    private GridView gvShowImage;

    private ArrayList<String> imagePathList;

    private Context mContext;

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
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        mContext = getActivity();
        gvShowImage = (GridView) view.findViewById(R.id.gvPhoto);
        imagePathList = new ArrayList<>();
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        Cursor imagecursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");
        Log.d(FileTransferActivity.TAG_NAME,"Cursor count : "+imagecursor.getCount());
        if (imagecursor.getCount() != 0) {
            Utils.getImageFolderName(imagecursor);
        }
        if (imagecursor != null) {
            for (int i = 0; i < imagecursor.getCount(); i++) {

                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

                Log.d(FileTransferActivity.TAG_NAME,"Path : "+imagecursor.getString(dataColumnIndex));
                //if (ImageUtils.checkFileExistance(imagecursor.getString(dataColumnIndex))) {*/
                    imagePathList.add(imagecursor.getString(dataColumnIndex));
                //}

            }
        }
        allPhotoAdapter = new AllPhotoAdapter(mContext,imagePathList);
        gvShowImage.setAdapter(allPhotoAdapter);

        return view;
    }


}
