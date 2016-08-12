package com.example.quickshare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.quickshare.R;
import com.example.quickshare.adapter.DeviceListAdapter;
import com.example.quickshare.background.WifiBroadcastReceiver;
import com.example.quickshare.database.models.DeviceModel;
import com.example.quickshare.helpers.CustomDialogFragment;
import com.example.quickshare.helpers.CustomDialogFragment.DeviceActionListener;

import java.io.Serializable;
import java.util.ArrayList;

public class FileShareActivity extends AppCompatActivity implements ChannelListener, View.OnClickListener,DeviceActionListener {

    private WifiP2pManager mManager;

    private Channel mChannel;

    private BroadcastReceiver mReceiver;

    private IntentFilter wifiIntentFilter,deviceIntentFilter;

    private String DEVICE_BROADCAST_KEY = "device_broadcast_key";

    private Button btnCreateSession,btnJoin;

    public static String TAG_NAME = "FileShareActivity";

    private String source = "";

    private Context mContext;

    public boolean isWifiEnabled = false;

    private ArrayList<DeviceModel> deviceModelArrayList;

    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_share);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);
        deviceIntentFilter = new IntentFilter(DEVICE_BROADCAST_KEY);
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mContext = this;

        //Initialize the layout component
        initComponent();
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, wifiIntentFilter);
        registerReceiver(deviceBroadcastReceiver,deviceIntentFilter);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isWifiEnabled = mWifi.isConnected();
        Log.d(TAG_NAME,"inside onResume " );
        Log.d(TAG_NAME,"Wifi Status : "+isWifiEnabled);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
        unregisterReceiver(deviceBroadcastReceiver);
    }

    public void initComponent() {
        btnCreateSession = (Button) findViewById(R.id.btn_create);
        btnJoin = (Button) findViewById(R.id.btn_join);

        //adding click listener to buttons
        btnCreateSession.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public void onChannelDisconnected() {

    }

    BroadcastReceiver deviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_NAME,"inside onReceive in FileShareActivity : ");
            CustomDialogFragment fragment = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("Dialog");
            if (fragment != null) {
                fragment.dismissProgressDialog();
            }
            deviceModelArrayList = (ArrayList<DeviceModel>) intent.getSerializableExtra("DEVICE_LIST");
            Log.d(TAG_NAME,"Devices Size : "+deviceModelArrayList.size());

            CustomDialogFragment customDialogFragment = new CustomDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("DIALOG_TYPE",2);
            bundle.putString("DIALOG_TITLE","List of available devices");
            bundle.putSerializable("DEVICES_LIST",deviceModelArrayList);
            //bundle.putString("DIALOG_MESSAGE","This is sample Dialog.");
            customDialogFragment.setArguments(bundle);
            customDialogFragment.setCancelable(false);
            customDialogFragment.show(getSupportFragmentManager(),"Dialog");

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_create :
                Log.d(TAG_NAME,"on click of button create : ");
                source = "create";
                if(!isWifiEnabled) {
                    Log.d(TAG_NAME,"inside if : ");
                    WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    Log.d(TAG_NAME,"isWifi : "+wifiManager.isWifiEnabled());
                }
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG_NAME,"Discovery initiated : ");
                        Intent intent = new Intent(FileShareActivity.this,FileTransferActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG_NAME,"Discovery Failed : ");
                    }
                });
                break;

            case R.id.btn_join :
                Log.d(TAG_NAME,"on click of button join : ");
                source = "join";
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG_NAME,"Discovery initiated : ");
                        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("DIALOG_TYPE",1);
                        bundle.putString("DIALOG_MESSAGE","Searching for devices. Please wait...");
                        //bundle.putString("DIALOG_MESSAGE","This is sample Dialog.");
                        customDialogFragment.setCancelable(false);
                        customDialogFragment.setArguments(bundle);
                        customDialogFragment.show(getSupportFragmentManager(),"Dialog");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG_NAME,"Discovery Failed : ");
                    }
                });
                break;
        }
    }

    public String getSource(){
        return source;
    }

    @Override
    public void showDetails(WifiP2pDevice device) {

    }

    @Override
    public void cancelDisconnect() {

    }

    @Override
    public void connect(WifiP2pConfig config) {
        Log.d(TAG_NAME,"inside onConnect : ");
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG_NAME,"inside onSuccess : ");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG_NAME,"inside onFailure : ");
            }
        });
    }

    @Override
    public void disconnect() {

    }
}
