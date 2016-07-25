package com.example.quickshare.helpers;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/7/16.
 */
public class MyPeerListener implements PeerListListener {

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private String TAG_NAME = "MyPeerListener";

    public MyPeerListener() {
        Log.d(TAG_NAME,"Inside MyPeerListener : ");
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        Log.d(TAG_NAME,"inside onPeersAvailable : ");
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        Log.d(TAG_NAME,"Device size : "+peers.size());
    }


}
