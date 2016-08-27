package com.example.quickreminder.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.quickreminder.R;

public class ReminderActivity extends AppCompatActivity {

    private ListView lvReminderList;

    private FloatingActionButton fab;

    public static String TAG = "ReminderActivity";

    private Cursor cursor;

    private Context mContext;

    private ReminderDatabase reminderDatabase;

    private ReminderListAdapter reminderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        mContext = this;
        reminderDatabase = new ReminderDatabase(mContext);
        cursor = reminderDatabase.getReminderList();
        Log.d(TAG,"Cursor Count : "+cursor.getCount());
        initComponent();
    }

    private void initComponent() {
        lvReminderList = (ListView)findViewById(R.id.lvReminderList);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick of fab : ");
                Intent intent = new Intent(ReminderActivity.this,CreateReminderActivity.class);
                startActivity(intent);
            }
        });

        reminderListAdapter = new ReminderListAdapter(mContext,cursor);
        lvReminderList.setAdapter(reminderListAdapter);
    }
}

