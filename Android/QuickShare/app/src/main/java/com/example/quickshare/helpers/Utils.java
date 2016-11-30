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
import java.util.Map;

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

    public static  HashMap<Integer,ImageCategorizingModel> getImageFolderName (Cursor cursor) {
        HashMap<Integer,ImageCategorizingModel> folderList = new LinkedHashMap<>();
        int position = 0;
        int refPosition = 0;
        boolean isExist = false;
        cursor.moveToFirst();
        ImageCategorizingModel imageCategorizingModel = new ImageCategorizingModel();
        String path  = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String folderName = path.substring(path.substring(0,path.lastIndexOf("/")).lastIndexOf("/")+1,path.lastIndexOf("/"));
        //Log.d(FileTransferActivity.TAG_NAME,"folderName : "+folderName);
        imageCategorizingModel.set_id(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        imageCategorizingModel.setPath(path);
        imageCategorizingModel.setImageCount(cursor.getCount());
        imageCategorizingModel.setFolderName("ALL");
        folderList.put(0,imageCategorizingModel);
        for (int i = 1; i< cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            folderName = path.substring(path.substring(0,path.lastIndexOf("/")).lastIndexOf("/")+1,path.lastIndexOf("/"));
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            //Checking folderName existance
            for (Map.Entry map : folderList.entrySet()) {
                imageCategorizingModel = (ImageCategorizingModel) map.getValue();
                String existingFolderName = imageCategorizingModel.getFolderName();
                isExist = false;
                if (folderName.equals(existingFolderName)) {
                    isExist = true;
                    int count = imageCategorizingModel.getImageCount()+1;
                    imageCategorizingModel.setImageCount(count);
                    break;
                }
                refPosition += 1;
            }
            if (!isExist) {
                position += 1;
                imageCategorizingModel = new ImageCategorizingModel();
                imageCategorizingModel.setFolderName(folderName);
                imageCategorizingModel.set_id(id);
                imageCategorizingModel.setImageCount(1);
                imageCategorizingModel.setPath(path);
                folderList.put(position,imageCategorizingModel);
            }

        }
        //folderList.put()
        return folderList;
    }
}
