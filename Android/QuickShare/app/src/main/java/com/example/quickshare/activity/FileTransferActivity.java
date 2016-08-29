package com.example.quickshare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.ViewPagerAdapter;
import com.example.quickshare.background.WifiBroadcastReceiver;
import com.example.quickshare.database.models.DeviceModel;
import com.example.quickshare.fragment.AppsFragment;
import com.example.quickshare.fragment.FileFragment;
import com.example.quickshare.fragment.PhotoFragment;

import java.util.ArrayList;

public class FileTransferActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ActionBar actionBar;

    private IntentFilter deviceIntentFilter;

    private String DEVICE_BROADCAST_KEY = "device_broadcast_key";

    private BroadcastReceiver mReceiver;

    private IntentFilter wifiIntentFilter;

    private WifiP2pManager mManager;

    private WifiP2pManager.Channel mChannel;

    private ArrayList<DeviceModel> deviceModelArrayList = new ArrayList<>();

    private boolean isConnected = false;

    private String receiverAddress;

    public static String TAG_NAME = "FileTransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_transfer);
        deviceIntentFilter = new IntentFilter(DEVICE_BROADCAST_KEY);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("RECEIVER_ADDRESS")) {
                receiverAddress = bundle.getString("RECEIVER_ADDRESS");
            }
        }
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, wifiIntentFilter);
        registerReceiver(deviceBroadcastReceiver,deviceIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
        unregisterReceiver(deviceBroadcastReceiver);
    }

    private void initComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FileFragment(), "Files");
        adapter.addFrag(new AppsFragment(), "Apps");
        adapter.addFrag(new PhotoFragment(), "Photos");
        adapter.addFrag(new FileFragment(), "Videos");
        adapter.addFrag(new FileFragment(), "Musics");
        adapter.addFrag(new FileFragment(), "History");
        viewPager.setAdapter(adapter);
    }

    BroadcastReceiver deviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            deviceModelArrayList = (ArrayList<DeviceModel>) intent.getSerializableExtra("DEVICE_LIST");
            Log.d(FileShareActivity.TAG_NAME,"Status in File TransferActiviy : "+deviceModelArrayList.get(0).getStatus());
        }
    };
}
