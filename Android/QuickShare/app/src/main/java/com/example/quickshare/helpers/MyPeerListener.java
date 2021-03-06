package com.example.quickshare.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;

import com.example.quickshare.activity.FileShareActivity;
import com.example.quickshare.activity.FileTransferActivity;
import com.example.quickshare.database.models.DeviceModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/7/16.
 */
public class MyPeerListener implements PeerListListener {

    private Context mContext;

    private String TAG_NAME = "MyPeerListener";

    private String DEVICE_BROADCAST_KEY = "device_broadcast_key";

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private List<DeviceModel> deviceList = new ArrayList<>();

    public MyPeerListener(Context context) {
        mContext = context;
        Log.d(TAG_NAME, "Inside MyPeerListener : ");
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        Log.d(FileShareActivity.TAG_NAME, "inside onPeersAvailable : ");
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        if (peers != null) {
            for (WifiP2pDevice device : peers) {
                DeviceModel deviceModel =  new DeviceModel();
                deviceModel.setDeviceName(device.deviceName);
                deviceModel.setGroupOwner(device.isGroupOwner());
                deviceModel.setDeviceAddress(device.deviceAddress);
                deviceModel.setStatus(device.status);
                deviceList.add(deviceModel);
            }
        }
        sendBroadcast();
        Log.d(FileShareActivity.TAG_NAME, "Device size : " + peers.size());
        Log.d(FileShareActivity.TAG_NAME,"Status : "+peers.get(0).status);
    }


    private void sendBroadcast() {
        Intent intent = new Intent(DEVICE_BROADCAST_KEY);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DEVICE_LIST", (Serializable) deviceList);
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);
    }
}
