package com.example.quickshare.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.quickshare.activity.FileTransferActivity;

import java.io.File;

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
}
