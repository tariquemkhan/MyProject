package com.example.quickshare.database.models;

import java.io.Serializable;

/**
 * Created by root on 28/7/16.
 */
public class DeviceModel implements Serializable {

    private String deviceName;

    private boolean isGroupOwner;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isGroupOwner() {
        return isGroupOwner;
    }

    public void setGroupOwner(boolean groupOwner) {
        isGroupOwner = groupOwner;
    }
}
