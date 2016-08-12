package com.example.quickshare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickshare.R;
import com.example.quickshare.database.models.DeviceModel;
import com.example.quickshare.helpers.CustomDialogFragment;

import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvShowDevices;

    private TextView tvNoResults;

    private Button btnRetry;

    private ArrayList<DeviceModel> deviceModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        initComponent();
    }

    public void initComponent () {
        lvShowDevices = (ListView) findViewById(R.id.lvShowDevices);

        tvNoResults = (TextView) findViewById(R.id.tvNoDevice);

        btnRetry = (Button) findViewById(R.id.btnRetry);
        if (btnRetry != null) {
            btnRetry.setOnClickListener(this);
        }
    }

    BroadcastReceiver deviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(FileShareActivity.TAG_NAME,"inside onReceive in FileShareActivity : ");
            CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("Dialog");
            if (fragment != null) {
                fragment.dismissProgressDialog();
            }
            deviceModelArrayList = (ArrayList<DeviceModel>) intent.getSerializableExtra("DEVICE_LIST");
            Log.d(FileShareActivity.TAG_NAME,"Devices Size : "+deviceModelArrayList.size());

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRetry :
                break;
        }
    }
}
