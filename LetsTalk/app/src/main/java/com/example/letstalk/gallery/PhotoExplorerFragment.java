package com.example.letstalk.gallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.letstalk.R;
import com.example.letstalk.database.models.MediaCategorizingModel;
import com.example.letstalk.helpers.CustomLogger;
import com.example.letstalk.gallery.OnStorageGrantedListener;
import com.example.letstalk.helpers.StaticConstants;
import com.example.letstalk.helpers.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PhotoExplorerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoExplorerFragment extends Fragment implements OnStorageGrantedListener {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gvShowMediaList;

    private Activity activity;

    private HashMap<Integer, MediaCategorizingModel> imageList;

    private MediaCategorizingAdapter adapter;

    private static final String TAG = "PhotoExplorerFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhotoExplorerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoExplorerFragment newInstance() {
        PhotoExplorerFragment fragment = new PhotoExplorerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_explorer, container, false);
        activity = getActivity();
        initComponents(view);
        return view;
    }

    /**
     * Initialize all UI components for this fragment
     *
     * @param view parent view layout
     */
    private void initComponents(View view) {
        imageList = new LinkedHashMap<>();
        gvShowMediaList = (GridView) view.findViewById(R.id.gvPhoto);
        if (Utils.checkStoragePermission(activity)) {
            new loadPhotoAsync().execute();
        }
    }

    @Override
    public void onStorageGranted() {
        CustomLogger.log(TAG, "onStorageGranted()", "inside method : ");
        new loadPhotoAsync().execute();
    }

    /**
     * this class is used to load images from android device and
     * responsible for providing list of images
     */
    private class loadPhotoAsync extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "loadPhotoAsync";

        @Override
        protected Void doInBackground(Void... params) {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED;
            Cursor imagecursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null,
                    null,
                    orderBy + " DESC");
            if (imagecursor != null) {
                CustomLogger.log(TAG, "doInBackGround()", "Cursor Count : " + imagecursor.getCount());
                imageList = Utils.getMediaFolderNames(imagecursor);
                imagecursor.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomLogger.log(TAG, "onPostExecut()", "List Size : "+imageList.size());
            adapter = new MediaCategorizingAdapter(activity, imageList,
                    StaticConstants.MessageType.IMAGE_MESSAGE);
            CustomLogger.log(TAG, "onPostExecute()", "adapter count : "+adapter.getCount());

            gvShowMediaList.setAdapter(adapter);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CustomLogger.log(TAG, "onDestroyView()", "inside beggining of method :");
    }
}
