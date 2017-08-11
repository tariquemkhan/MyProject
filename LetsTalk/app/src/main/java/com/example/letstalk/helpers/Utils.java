package com.example.letstalk.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.letstalk.database.models.MediaCategorizingModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by root on 10/8/17.
 */

public class Utils {

    public static boolean checkStoragePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                StaticConstants.Utils.REQUEST_READ_EXTERNAL_STORAGE);
    }

    public static HashMap<Integer,MediaCategorizingModel> getMediaFolderNames (Cursor cursor) {
        HashMap<Integer,MediaCategorizingModel> folderList = new LinkedHashMap<>();
        int position = 0;
        int refPosition = 0;
        boolean isExist = false;
        cursor.moveToFirst();
        MediaCategorizingModel imageCategorizingModel = new MediaCategorizingModel();
        String path  = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String folderName = path.substring(path.substring(0,path.lastIndexOf("/")).lastIndexOf("/")+1,path.lastIndexOf("/"));
        //Log.d(FileTransferActivity.TAG_NAME,"folderName : "+folderName);
        imageCategorizingModel.set_id(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        imageCategorizingModel.setPath(path);
        imageCategorizingModel.setMediaCount(cursor.getCount());
        imageCategorizingModel.setFolderName("ALL");
        folderList.put(0,imageCategorizingModel);
        for (int i = 1; i< cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            folderName = path.substring(path.substring(0,path.lastIndexOf("/")).lastIndexOf("/")+1,path.lastIndexOf("/"));
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            //Checking folderName existance
            for (Map.Entry map : folderList.entrySet()) {
                imageCategorizingModel = (MediaCategorizingModel) map.getValue();
                String existingFolderName = imageCategorizingModel.getFolderName();
                isExist = false;
                if (folderName.equals(existingFolderName)) {
                    isExist = true;
                    int count = imageCategorizingModel.getMediaCount()+1;
                    imageCategorizingModel.setMediaCount(count);
                    break;
                }
                refPosition += 1;
            }
            if (!isExist) {
                position += 1;
                imageCategorizingModel = new MediaCategorizingModel();
                imageCategorizingModel.setFolderName(folderName);
                imageCategorizingModel.set_id(id);
                imageCategorizingModel.setMediaCount(1);
                imageCategorizingModel.setPath(path);
                folderList.put(position,imageCategorizingModel);
            }

        }
        //folderList.put()
        return folderList;
    }
}
