package com.example.quickreminder.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.quickreminder.R;

public class CreateReminderActivity extends AppCompatActivity implements View.OnClickListener,OnDateClickListener {

    private ImageView ivTimepicker;

    private Button btnOk,btnCancel;

    private EditText etDescription, etTitle, etTime;

    private TextInputLayout tilTitle, tilDescription, tilTime;

    private RelativeLayout rlParentLayout;

    private long timestamp = 0;

    private Context mContext;

    private ReminderDatabase reminderDatabase;

    private int errorIndex = 0;

    public static String TAG = "CreateReminderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        mContext = this;
        reminderDatabase = new ReminderDatabase(mContext);
        initComponent();
        boolean isEdit = getIntent().getBooleanExtra("IS_EDIT",false);
        if (isEdit) {
            String task_id = getIntent().getStringExtra("TASK_ID");
            setValue(task_id);
        }
    }

    /**
     * Initialize the layout component
     */
    private void initComponent() {
        rlParentLayout = (RelativeLayout) findViewById(R.id.rlParentLayout);
        ivTimepicker = (ImageView)findViewById(R.id.ivTimePicker);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(watcher);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etTime = (EditText) findViewById(R.id.etTime);
        etTime.addTextChangedListener(watcher);
        tilTitle = (TextInputLayout) findViewById(R.id.inputlayoutTilte);
        tilDescription = (TextInputLayout) findViewById(R.id.inputlayoutDescription);
        tilTime = (TextInputLayout) findViewById(R.id.inputLayoutTime);
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
                InputMethodManager inputMethodManager =
                        (InputMethodManager) this.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        this.getCurrentFocus().getWindowToken(), 0);
                rlParentLayout.setFocusable(true);
                rlParentLayout.setFocusableInTouchMode(true);
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentManager fm = getSupportFragmentManager();
                datePickerFragment.show(fm,"Picker Dialog");
                break;
            case R.id.btnOk :
                if (isValidated()) {
                    addReminderToDb();
                    Intent okIntent = new Intent(CreateReminderActivity.this,ReminderActivity.class);
                    startActivity(okIntent);
                }
                break;
            case R.id.btnCancel :
                Intent cancelIntent = new Intent(CreateReminderActivity.this,ReminderActivity.class);
                startActivity(cancelIntent);
                break;
        }
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (errorIndex != 0) {
                if (errorIndex == 1) {
                    Log.d(TAG,"inside if : ");
                    tilTitle.setErrorEnabled(false);
                    errorIndex = 0;
                } else {
                    tilTime.setErrorEnabled(false);
                    errorIndex = 0;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void addDate(long timeStamp) {
        this.timestamp = timeStamp;
        etTime.setText(ReminderHelper.getFromattedDate(this.timestamp));
        Log.d(TAG,"date in addDate() : "+timeStamp);
    }

    public void setValue(String task_id) {
        Cursor cursor = reminderDatabase.getTask(task_id);
        cursor.moveToFirst();
        etTitle.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.TASK_TITLE)));
        etDescription.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.TASK_DESCRIPTION)));
        long time = cursor.getInt(cursor.getColumnIndex(ReminderDatabase.TASK_TIMESTAMP));
        String formattedTime = ReminderHelper.getFromattedDate(time * 1000);
        etTime.setText(formattedTime);
    }

    /**
     * Add reminder task to database
     */
    public void addReminderToDb() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String taskId = ReminderHelper.getRandomString()+"-"+ System.currentTimeMillis();
        ReminderModel reminderModel = new ReminderModel();
        reminderModel.setTaskId(taskId);
        reminderModel.setTaskTitle(title);
        reminderModel.setTaskDescription(description);
        reminderModel.setTaskTimestamp(timestamp);
        reminderDatabase.addReminder(reminderModel);

    }

    private boolean isValidated() {
        if (etTitle.getText().toString().equals("")) {
            tilTitle.setErrorEnabled(true);
            tilTitle.setError("Title Required !");
            errorIndex = 1;
            return false;
        } else{
            if (etTime.getText().toString().equals("")) {
                tilTime.setErrorEnabled(true);
                tilTime.setError("Date Required !");
                errorIndex = 2;
                return false;
            }
        }
        return true;
    }
}
