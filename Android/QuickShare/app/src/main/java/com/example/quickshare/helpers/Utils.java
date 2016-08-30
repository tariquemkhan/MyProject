package com.example.quickshare.helpers;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import com.example.quickshare.activity.FileTransferActivity;
import com.example.quickshare.database.models.ImageCategorizingModel;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by root on 29/8/16.
 */
public class Utils {

    public static Bitmap UriToBitmap (String path) {
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        } catch (Exception e) {
            Log.d(FileTransferActivity.TAG_NAME,"Error inside UriToBitmap() : "+e.toString());
        }
        return bitmap;
    }

    public static  HashMap<String,ImageCategorizingModel> getImageFolderName (Cursor cursor) {
        HashMap<String,ImageCategorizingModel> folderList = new LinkedHashMap<>();
        cursor.moveToFirst();
        ImageCategorizingModel imageCategorizingModel = new ImageCategorizingModel();
        String path  = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String folderName = path.substring(path.substring(0,path.lastIndexOf("/")).lastIndexOf("/")+1,path.lastIndexOf("/"));
        Log.d(FileTransferActivity.TAG_NAME,"folderName : "+folderName);
        imageCategorizingModel.set_id(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        imageCategorizingModel.setPath(path);
        imageCategorizingModel.setInMemory(false);
        imageCategorizingModel.setImageCount(cursor.getCount());
        for (int i = 1; i< cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        }
        //folderList.put()
        return folderList;
    }
}
