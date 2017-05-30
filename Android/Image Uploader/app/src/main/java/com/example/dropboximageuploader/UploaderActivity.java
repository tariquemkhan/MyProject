package com.example.dropboximageuploader;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class UploaderActivity extends AppCompatActivity implements OnChangeListener {

    public static final String APP_KEY = "ayv9ikjskd4ugh0";

    public static final String SECRET_KEY = "888i1qsmd21ahw8";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;

    private final static String DROPBOX_NAME = "dropbox_prefs";

    public final static String DROPBOX_FILE_DIR = "/My Photos";

    private DropboxAPI<AndroidAuthSession> dropbox;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 101;

    private String TAG = "Alpha";

    private OnChangeListener onChangeListener;

    private GridView gvImageList;

    private Cursor mCursor;

    private DatabaseHelper databaseHelper;

    private ImageAdapter adapter;

    private Context mContext;

    public static String BROADCAST_KEY = "broadcast_key";

    public static String STATUS_UPDATE_RECEIVER = "status_update_receiver";

    public static final int REQUEST_CODE_PICK_CONTACT = 1;

    public static final int MAX_PICK_CONTACT= 10;

    private ArrayList<JSONObject> imageList;

    public static boolean isActive = false;

    private NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);
        mContext = this;
        Log.d(TAG, "onCreate: inside onCreate() : ");
        // And later in some initialization function:
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, SECRET_KEY);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);
        //dropbox.getSession().startOAuth2Authentication(UploaderActivity.this);
        initComponents();
    }

    @Override
    protected void onStart() {
        getLayoutInflater();
        isActive = true;
        Log.d(TAG, "onStart: inside onStart() : ");
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_KEY));
        registerReceiver(statusReceiver, new IntentFilter(STATUS_UPDATE_RECEIVER));
        if (checkForPermission()) {
            mCursor = getImages();
            onChangeListener.onChange();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkStateReceiver, filter);
        Log.d(TAG, "Row Count : " + databaseHelper.getImageCount());
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: inside onResume() : ");
        AndroidAuthSession session = dropbox.getSession();
        //Log.d(TAG, "Is Authentication : " + session.authenticationSuccessful());
        if (dropbox.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                dropbox.getSession().finishAuthentication();
                String accessToken = dropbox.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.d(TAG, "Error authenticating", e);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: inside onPause() : ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: inside onStop() : ");
        isActive = false;
        dropbox.getSession().unlink();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "inside onDestroy : ");
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(statusReceiver);
    }

    /**
     * initialize component
     */
    public void initComponents() {
        Log.d(TAG, "inside initComponent: ");
        gvImageList = (GridView) findViewById(R.id.gvPhotos);
        onChangeListener = (OnChangeListener) mContext;
        databaseHelper = new DatabaseHelper(mContext);
        networkStateReceiver = new NetworkStateReceiver();
        imageList = new ArrayList<>();
        adapter = new ImageAdapter(mContext,mCursor);
        gvImageList.setAdapter(adapter);
    }

    /**
     * get all images from device
     */
    public Cursor getImages() {
        Cursor cursor = null;
        Uri uri;
        try {
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED};

            cursor = getContentResolver().query(uri, projection, " _data NOT LIKE '%Sparkchat%'",
                    null, "DESC "+MediaStore.Images.Media.DATE_ADDED);
            Log.d(TAG, "Cursor count : " + cursor.getCount());

        } catch (Exception e) {
            Log.d(TAG, "Exception in getImage: " + e.toString());
        } finally {
            return cursor;
        }
    }

    /**
     * build dropbox session for user
     */
    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, SECRET_KEY);
        AndroidAuthSession session;
        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(APP_KEY, null);
        String secret = prefs.getString(SECRET_KEY, null);
        Log.d(TAG, "Key : " + key + " Secret : " + secret);
        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(appKeyPair, token);
        } else {
            session = new AndroidAuthSession(appKeyPair);
        }
        return session;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "inside onReceive() : ");
            try {
                Cursor cursor = databaseHelper.getAllData();
                Log.d(TAG, "Cursor count inside onReceive() : " + cursor.getCount());
                ArrayList<JSONObject> imageList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    int status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.STATUS));
                    String uniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_ID));
                    String path = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_PATH));
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_NAME));
                    Log.d(TAG, "Status : " + status);
                    if (status == 0 && !path.contains("/Sparkchat")) {
                        jsonObject.put("id", uniqueId);
                        jsonObject.put("name", name);
                        jsonObject.put("path",path);
                        imageList.add(jsonObject);
                        //boolean isExist = fileExistance(name);


                    }
                }
                Log.d(TAG,"Array Size : "+imageList.size());
                DropboxFileAsync dropboxFileAsync = new DropboxFileAsync(mContext,dropbox);
                dropboxFileAsync.execute(imageList);
            } catch (Exception e) {
                Log.d(TAG, "Error inside onReceive : " + e.toString());
            }

        }
    };

    BroadcastReceiver statusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Inside onReceive() of statusReceiver : ");
            int status = intent.getIntExtra("STATUS",0);
            String id = intent.getStringExtra("ID");
            Log.d(TAG, "onReceive: STATUS : "+status+" ID : "+id);
            if (status == 1) {
                Log.d(TAG, "onReceive: Uploaded count : "+databaseHelper.getUpladedImageCount());
                int updateStatus = databaseHelper.updateStatus(id);
                Log.d(TAG, "onReceive: Update Status : "+updateStatus);
                Cursor cursor = databaseHelper.getAllData();
                adapter.refereshCursor(cursor);
            }
        }
    };

    public boolean fileExistance(String fileName) {
        boolean isExist = false;
        try {

        } catch (Exception e) {
            Log.d(TAG, "Error inside fileExistance() : " + e.toString());
        }
        return isExist;
    }

    /**
     * Check for runtime permission for marshmallow or above version
     */
    public boolean checkForPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

               /* ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);*/

            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.d(TAG,"Resutlt code : "+grantResults[0]+" permission length : "+permissions.length+ " permission type : "+permissions[0]);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (permissions.length == 1 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCursor = getImages();
                if (onChangeListener != null) {
                    onChangeListener.onChange();
                }
            } else {
                if (permissions.length == 1 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                Log.d(TAG, "onClick of action_upload : ");
                try {
                } catch (Exception e) {
                    Log.d(TAG, "Exception inside action_upload : " + e.toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange() {
        Log.d(TAG, "inside onChange() : ");
        databaseHelper.addImages(mCursor);
        Cursor cursor = databaseHelper.getAllData();
        adapter.refereshCursor(cursor);
    }
}
