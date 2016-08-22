package com.example.quickshare.adapter;

import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickshare.R;
import com.example.quickshare.activity.DeviceListActivity;
import com.example.quickshare.activity.FileShareActivity;
import com.example.quickshare.database.models.DeviceModel;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by root on 3/8/16.
 */
public class DeviceListAdapter extends ArrayAdapter {

    private Context mContext;

    private ArrayList<DeviceModel> devicesList;

    private String TAG = "DeviceListAdapter";

    private DeviceActionListener deviceActionListener;

    public DeviceListAdapter(Context context, int resource, ArrayList<DeviceModel> devicesList) {
        super(context, resource);
        mContext = context;
        this.devicesList = devicesList;
        deviceActionListener = (DeviceActionListener)mContext;
        Log.d(TAG,"inside DeviceListAdapter : "+devicesList.size());
    }

    @Override
    public DeviceModel getItem(int position) {
        return devicesList.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }

    @Override
    public int getCount() {
        return devicesList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.d(TAG,"inside getView : ");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_list_item,parent,false);
            holder = new ViewHolder();
            holder.tvDeviceName = (TextView) convertView.findViewById(R.id.tvDeviceName);
            holder.tvDeviceAddress = (TextView) convertView.findViewById(R.id.tvDeviceAddress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DeviceModel deviceModel = devicesList.get(position);
        holder.tvDeviceName.setText(deviceModel.getDeviceName());
        holder.tvDeviceAddress.setText(deviceModel.getDeviceAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(FileShareActivity.TAG_NAME,"inside onClick : ");
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = deviceModel.getDeviceAddress();
                config.wps.setup = WpsInfo.PBC;
                deviceActionListener.connect(config);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tvDeviceName;

        TextView tvDeviceAddress;

        ImageView ivDeviceIcon;
    }

    public interface DeviceActionListener {

        void showDetails(WifiP2pDevice device);

        void cancelDisconnect();

        void connect(WifiP2pConfig config);

        void disconnect();
    }

}
