package com.example.dropboximageuploader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by root on 31/1/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "image_uploader";

    private static final int VERSION = 1;

    public static final String TABLE_NAME = "image_record";

    public static final String _ID = "_id";

    public static final String IMAGE_ID = "image_id";

    public static final String IMAGE_NAME = "image_name";

    public static final String IMAGE_PATH = "image_path";

    public static final String STATUS = "status";

    private String TAG = "Alpha";

    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "inside onCreate of Database : ");
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + IMAGE_ID + " TEXT UNIQUE," + IMAGE_NAME + " TEXT,"
                + IMAGE_PATH + " TEXT," + STATUS + " INTEGER DEFAULT 0);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * add image data to table
     * @param cursor contains row data
     */
    public void addImages(Cursor cursor) {
        Log.d(TAG, "inside addImages() : ");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            while (cursor.moveToNext()) {
                contentValues.put(IMAGE_ID, cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
                contentValues.put(IMAGE_NAME, cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                contentValues.put(IMAGE_PATH, cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
            mContext.sendBroadcast(new Intent(UploaderActivity.BROADCAST_KEY));
        } catch (Exception e) {
            Log.d(TAG, "Exception in adding images : " + e.toString());
        } finally {
            db.endTransaction();
            cursor.close();
        }
    }

    /**
     * get raw count of table
     * @return number of rows
     */
    public int getImageCount() {
        int count  = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
        } catch (Exception e) {
            Log.d(TAG,"Exception inside getImageCount : "+e.toString());
        }
        return count;
    }

    /**
     * reterive all Data from table
     * @return cursor containing all records
     */
    public Cursor getAllData() {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_NAME;
            cursor = db.rawQuery(countQuery, null);
        } catch (Exception e) {
            Log.d(TAG,"Exception inside getImageCount : "+e.toString());
        }
        return cursor;
    }

    /**
     * update status of image
     * @param id whose status has to change
     */
    public int updateStatus(String id) {
        Log.d(TAG, "updateStatus: inside updateStatus() : ");
        int status = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String filter  = IMAGE_ID+" = "+id;
            ContentValues contentValues = new ContentValues();
            contentValues.put(STATUS, 1);
            db.update(TABLE_NAME, contentValues, filter, null);
            status = 1;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG,"Exception inside updateStatus() : "+e.toString());
        }
        finally {
            db.endTransaction();
        }
        return status;
    }

    /**
     * Count the number of images that has been uploaded
     * @return number of rows
     */
    public long getUpladedImageCount () {
        long count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+ " WHERE "+STATUS+" = "+1+" ;";

        try {
            Cursor cursor = db.rawQuery(query,null);
            count = cursor.getCount();
        } catch (Exception e) {
            Log.d(TAG, "getUpladedImageCount: Error : "+e.toString());
        }
        return count;
    }


}
