package com.example.quickshare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.DeviceListAdapter;
import com.example.quickshare.database.models.DeviceModel;
import com.example.quickshare.helpers.CustomDialogFragment;

import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvShowDevices;

    private TextView tvNoResults;

    private Button btnRetry;

    private IntentFilter deviceIntentFilter;

    private String DEVICE_BROADCAST_KEY = "device_broadcast_key";

    private Context mContext;

    private DeviceListAdapter adapter;

    private WifiP2pManager mManager;

    private WifiP2pManager.Channel mChannel;



    private ArrayList<DeviceModel> deviceModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        mContext = this;
        deviceIntentFilter = new IntentFilter(DEVICE_BROADCAST_KEY);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(deviceBroadcastReceiver,deviceIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(deviceBroadcastReceiver);
    }

    public void initComponent () {
        lvShowDevices = (ListView) findViewById(R.id.lvShowDevices);

        tvNoResults = (TextView) findViewById(R.id.tvNoDevice);

        btnRetry = (Button) findViewById(R.id.btnRetry);
        if (btnRetry != null) {
            btnRetry.setOnClickListener(this);
        }
        adapter = new DeviceListAdapter(mContext,R.layout.device_list,deviceModelArrayList);
        lvShowDevices.setAdapter(adapter);
        //show loader Dialog
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("DIALOG_TYPE",1);
        bundle.putString("DIALOG_MESSAGE","Searching for devices. Please wait...");
        //bundle.putString("DIALOG_MESSAGE","This is sample Dialog.");
        customDialogFragment.setCancelable(false);
        customDialogFragment.setArguments(bundle);
        customDialogFragment.show(getSupportFragmentManager(),"Dialog");

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(FileShareActivity.TAG_NAME,"Discovery initiated : ");

            }

            @Override
            public void onFailure(int reason) {
                Log.d(FileShareActivity.TAG_NAME,"Discovery Failed : ");
            }
        });

    }

    BroadcastReceiver deviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(FileShareActivity.TAG_NAME,"inside onReceive in DeviceListActivity : ");
            CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("Dialog");
            if (fragment != null) {
                fragment.dismissProgressDialog();
            }
            deviceModelArrayList = (ArrayList<DeviceModel>) intent.getSerializableExtra("DEVICE_LIST");
            if (deviceModelArrayList.size() == 0) {
                tvNoResults.setVisibility(View.VISIBLE);
            } else {
                tvNoResults.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
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
