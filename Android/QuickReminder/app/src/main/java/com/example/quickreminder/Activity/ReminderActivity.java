package com.example.quickreminder.Activity;

import android.content.Intent;
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

    private String TAG = "ReminderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
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
    }
}

