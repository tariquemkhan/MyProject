package com.example.dropboximageuploader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by root on 10/2/17.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    private DatabaseHelper databaseHelper;

    private static final String TAG = "Alpha";
    @Override
    public void onReceive(Context context, Intent intent) {
        UploaderActivity activity = null;
        Log.d(TAG, "onReceive: inside NetworkStateReceiver : "+context);
        if (context instanceof UploaderActivity) {
            Log.d(TAG, "onReceive: Inside if : ");
            activity = (UploaderActivity)context;
        } else {
            Log.d(TAG, "onReceive: Inside else : ");
        }
        databaseHelper = new DatabaseHelper(context);
        NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        Log.d(TAG, "onReceive: Status : "+ni.getState());
        if (activity == null) {
            if (ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
                try {
                    Cursor cursor = databaseHelper.getAllData();
                    Log.d(TAG, "Cursor count inside onReceive() : " + cursor.getCount());
                    ArrayList<Map> imageList = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        Map<String,String> map = new LinkedHashMap<>();
                        int status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.STATUS));
                        String uniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_ID));
                        String path = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_PATH));
                        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_NAME));
                        Log.d(TAG, "Status : " + status);
                        if (status == 0 && !path.contains("/Sparkchat")) {
                            map.put("id", uniqueId);
                            map.put("name", name);
                            map.put("path",path);
                            imageList.add(map);
                            //boolean isExist = fileExistance(name);
                        }
                    }
                    Log.d(TAG,"Array Size : "+imageList.size());
                    Intent uploadIntent = new Intent(Intent.ACTION_SYNC, null, context, UploadService.class);
                    uploadIntent.putExtra("IMAGE_LIST",imageList);
                    context.startService(uploadIntent);
                } catch (Exception e) {
                    Log.d(TAG, "onReceive: Exception in NetworkStateReceiver : "+e.toString());
                }
            }
        }
    }
}
