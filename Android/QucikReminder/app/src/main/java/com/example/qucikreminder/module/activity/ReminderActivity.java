package com.example.qucikreminder.module.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.qucikreminder.R;
import com.example.qucikreminder.module.fragment.CreateReminderFragment;

public class ReminderActivity extends AppCompatActivity {

    private ListView lvShowReminderList;

    private FloatingActionButton fabCreateReminder;

    private Fragment frCreateReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        lvShowReminderList = (ListView) findViewById(R.id.lvShowReminderList);
        fabCreateReminder = (FloatingActionButton) findViewById(R.id.fab);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frCreateReminder);
        FragmentTransaction ft = fm.beginTransaction();
        //ft.hide(fragment);
        ft.commit();
    }
}
