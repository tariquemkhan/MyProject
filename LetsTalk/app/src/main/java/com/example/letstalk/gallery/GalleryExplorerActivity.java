package com.example.letstalk.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.letstalk.R;
import com.example.letstalk.helpers.CustomLogger;
import com.example.letstalk.helpers.Utils;

import static com.example.letstalk.helpers.StaticConstants.Utils.REQUEST_READ_EXTERNAL_STORAGE;

public class GalleryExplorerActivity extends AppCompatActivity {

    private static final String TAG = "GalleryExplorerActivity";

    private FragmentManager fragmentManager;

    private Context mContext;

    private Toolbar toolbar;

    private PhotoExplorerFragment photoExplorerFragment;

    private OnStorageGrantedListener onStorageGrantedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_explorer);
        initHelpers();
        initComponents();
    }

    /**
     * Initialize all helper classes and variables
     */
    private void initHelpers() {
        mContext = this;
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * Initialize all UI components
     */
    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar()
                .setTitle(getResources().getString(R.string.gallery_activity_title));
        photoExplorerFragment = PhotoExplorerFragment.newInstance();
        //Adding photo fragment to frame layout initially
        fragmentManager.beginTransaction()
                .add(photoExplorerFragment,
                        getResources().getString(R.string.photo_fragment_tag))
                .commit();
        if (Utils.checkStoragePermission((Activity) mContext)) {
        } else {
            Utils.requestStoragePermission((Activity) mContext);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photoExplorerFragment = (PhotoExplorerFragment) fragmentManager
                            .findFragmentByTag(
                                    getResources().getString(R.string.photo_fragment_tag));
                    CustomLogger.log(TAG, "onRequestPermissionResult()", "inside if : "+photoExplorerFragment);
                    onStorageGrantedListener = photoExplorerFragment;
                    onStorageGrantedListener.onStorageGranted();
                } else {
                }
                return;
        }
    }
}
