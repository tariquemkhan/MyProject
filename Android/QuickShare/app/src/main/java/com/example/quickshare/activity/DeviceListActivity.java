package com.example.quickshare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.DeviceListAdapter;
import com.example.quickshare.background.WifiBroadcastReceiver;
import com.example.quickshare.database.models.DeviceModel;
import com.example.quickshare.helpers.CustomDialogFragment;
import com.example.quickshare.adapter.DeviceListAdapter.DeviceActionListener;
import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener,DeviceActionListener {

    private ListView lvShowDevices;

    private TextView tvNoResults;

    private Button btnRetry;

    private IntentFilter deviceIntentFilter;

    private String DEVICE_BROADCAST_KEY = "device_broadcast_key";

    private BroadcastReceiver mReceiver;

    private IntentFilter wifiIntentFilter;

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
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        initComponent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(deviceBroadcastReceiver,deviceIntentFilter);
        registerReceiver(mReceiver, wifiIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(deviceBroadcastReceiver);
        unregisterReceiver(mReceiver);
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
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(FileShareActivity.TAG_NAME,"inside handler : ");
                        CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("Dialog");
                        if (fragment != null) {
                            fragment.dismissProgressDialog();
                        }
                        if (deviceModelArrayList.size() == 0) {
                            tvNoResults.setVisibility(View.VISIBLE);
                            btnRetry.setVisibility(View.VISIBLE);
                        } else {
                            btnRetry.setVisibility(View.GONE);
                            tvNoResults.setVisibility(View.GONE);
                        }
                    }
                },1000*30);

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
                btnRetry.setVisibility(View.VISIBLE);
            } else {
                btnRetry.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.GONE);
            }
            //adapter.notifyDataSetChanged();
            adapter = new DeviceListAdapter(mContext,R.layout.device_list,deviceModelArrayList);
            lvShowDevices.setAdapter(adapter);
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

    @Override
    public void showDetails(WifiP2pDevice device) {

    }

    @Override
    public void cancelDisconnect() {

    }

    @Override
    public void connect(final WifiP2pConfig config) {
        Log.d(FileShareActivity.TAG_NAME,"inside onConnect : ");
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.d(FileShareActivity.TAG_NAME,"inside onSuccess : ");

                Intent intent = new Intent(DeviceListActivity.this,FileTransferActivity.class);
                intent.putExtra("RECEIVER_ADDRESS",config.deviceAddress);
                startActivity(intent);
            }

            @Override
            public void onFailure(int reason) {
                Log.d(FileShareActivity.TAG_NAME,"inside onFailure : ");
            }
        });
    }

    @Override
    public void disconnect() {

    }
}
