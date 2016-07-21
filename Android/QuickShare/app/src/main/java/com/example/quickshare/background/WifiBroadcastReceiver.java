package com.example.quickshare.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import com.example.quickshare.activity.FileShareActivity;
import com.example.quickshare.helpers.MyPeerListener;

/**
 * Created by root on 11/7/16.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;

    private Channel mChannel;

    private FileShareActivity mActivity;

    private String TAG_NAME = "WifiBroadcastReceiver";


    public WifiBroadcastReceiver(WifiP2pManager mManager, Channel channel, FileShareActivity activity) {
        super();
        this.mManager = mManager;
        mChannel = channel;
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            Log.d(TAG_NAME," if Wi-Fi is enabled and notify appropriate activity : ");
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            Log.d(TAG_NAME," Call WifiP2pManager.requestPeers() to get a list of current peers : ");
            if (mManager != null && mActivity.getSource().equals("join")) {
                Log.d(TAG_NAME,"before requesting peers : ");
                mManager.requestPeers(mChannel, new MyPeerListener());
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            Log.d(TAG_NAME," Respond to new connection or disconnections : ");
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            Log.d(TAG_NAME," Respond to this device's wifi state changing : ");
        }
    }
}
