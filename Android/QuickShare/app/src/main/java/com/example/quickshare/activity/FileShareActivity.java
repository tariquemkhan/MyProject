package com.example.quickshare.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.example.quickshare.adapter.DeviceListAdapter.DeviceActionListener;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;

public class FileShareActivity extends AppCompatActivity implements ChannelListener, View.OnClickListener,DeviceActionListener {

    private WifiP2pManager mManager;

    private Channel mChannel;

    private BroadcastReceiver mReceiver;

    private IntentFilter wifiIntentFilter;

    private Button btnCreateSession,btnJoin;

    public static String TAG_NAME = "FileShareActivity";

    private String source = "";

    private Context mContext;

    public boolean isWifiEnabled = false;

    public final int REQUEST_READ_EXTERNAL_STORAGE = 0;

    private ArrayList<DeviceModel> deviceModelArrayList;

    private DeviceListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_share);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mContext = this;

        //Initialize the layout component
        initComponent();
        handlePermission();

    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, wifiIntentFilter);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_create :
                Log.d(TAG_NAME,"on click of button create : ");
                source = "create";
                /*if(!isWifiEnabled) {
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
                });*/
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
                Intent deviceListIntent = new Intent(FileShareActivity.this, DeviceListActivity.class);
                startActivity(deviceListIntent);
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

    public void handlePermission () {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
            Log.d(TAG_NAME,"inside if of handle permission : ");
        } else {
            Log.d(TAG_NAME,"inside else of handle permission : ");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG_NAME,"on storage permission granted : ");
                } else {
                    Log.d(TAG_NAME,"on Storage permission denied : ");
                }
                return;
        }
    }
}
