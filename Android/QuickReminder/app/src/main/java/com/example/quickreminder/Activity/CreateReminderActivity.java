package com.example.quickreminder.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.quickreminder.R;

public class CreateReminderActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivTimepicker;

    private Button btnOk,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        initComponent();
    }

    private void initComponent() {
        ivTimepicker = (ImageView)findViewById(R.id.ivTimePicker);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnOk);
        ivTimepicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ivTimePicker :
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentManager fm = getSupportFragmentManager();
                datePickerFragment.show(fm,"Picker Dialog");
                break;
            case R.id.btnOk :
                break;
            case R.id.btnCancel :
                break;
        }
    }
}
