package com.example.quickshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickshare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/7/16.
 */
public class AppsAdapter  extends BaseAdapter {

    private List<ApplicationInfo> applicationInfos = new ArrayList<>();

    private Context mContext;

    private String TAG = "AppsAdapter";

    private PackageManager packageManager;

    public AppsAdapter(List<ApplicationInfo> applicationInfos, Context context) {
        this.applicationInfos = applicationInfos;
        this.mContext = context;
        packageManager = mContext.getPackageManager();
    }

    @Override
    public int getCount() {
        return applicationInfos.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return applicationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.apps_item_list, parent, false);
            holder = new ViewHolder();
            holder.ivAppIcon = (ImageView) convertView.findViewById(R.id.ivAppIcon);
            holder.tvAppName = (TextView) convertView.findViewById(R.id.tvAppName);
            holder.tvAppSize = (TextView) convertView.findViewById(R.id.tvAppSize);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ApplicationInfo apps = getItem(position);
        holder.ivAppIcon.setImageDrawable(apps.loadIcon(packageManager));
        holder.tvAppName.setText(apps.loadLabel(packageManager));
        return convertView;
    }

    class ViewHolder {
        ImageView ivAppIcon;

        TextView tvAppName;

        TextView tvAppSize;
    }
}
