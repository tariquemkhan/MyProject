package com.example.quickreminder.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dell on 7/24/2016.
 */
public class ReminderDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminder.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "reminder";

    public static final String _ID = "_id";

    public static final String TASK_ID = "task_id";

    public static final String TASK_TITLE = "title";

    public static final String TASK_DESCRIPTION = "description";

    public static final String TASK_TIMESTAMP = "timestamp";

    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+" ("+_ID+" integer PRIMARY KEY AUTOINCREMENT,"
                +TASK_ID+" TEXT UNIQUE ,"
                +TASK_TITLE+" TEXT,"
                +TASK_DESCRIPTION+" TEXT,"
                +TASK_TIMESTAMP+" INTEGER)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addReminder (ReminderModel model) {
        try {
            Log.d(CreateReminderActivity.TAG,"Time : "+model.getTaskTimestamp());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TASK_ID, model.getTaskId());
            contentValues.put(TASK_TITLE, model.getTaskTitle());
            contentValues.put(TASK_DESCRIPTION, model.getTaskDescription());
            contentValues.put(TASK_TIMESTAMP, model.getTaskTimestamp()/1000);
            db.insert(TABLE_NAME, null, contentValues);
        }catch (Exception e) {
            Log.d(CreateReminderActivity.TAG,"error inside addReminder() : "+e.toString());
        }
    }

    public Cursor getReminderList() {
        Cursor cursor = null;
        Log.d(ReminderActivity.TAG,"inside getReminderList() : ");
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor =db.rawQuery(query,null);
        } catch (Exception e) {
            Log.d(ReminderActivity.TAG,"Error inside getReminderList() : "+e.toString());
        }
        return cursor;
    }

    public Cursor getTask(String task_id) {
        Cursor cursor = null;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+TASK_ID+"='"+task_id+"';";
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(query,null);
        } catch (Exception e) {
            Log.d(ReminderActivity.TAG,"Error inside getTask() : ");
        }
        return cursor;
    }
}
