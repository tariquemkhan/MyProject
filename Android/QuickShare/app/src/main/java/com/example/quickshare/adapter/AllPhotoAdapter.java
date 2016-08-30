package com.example.quickshare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickshare.R;
import com.example.quickshare.helpers.SquareImageView;
import com.example.quickshare.helpers.Utils;

import java.util.ArrayList;

/**
 * Created by root on 29/8/16.
 */
public class AllPhotoAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<String> mImageList;

    public AllPhotoAdapter (Context mContext, ArrayList<String> imageList) {
        this.mContext = mContext;
        mImageList = imageList;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public String getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photo_list_items, parent, false);
            holder.ivImageDisplay = (SquareImageView) convertView.findViewById(R.id.ivImageDisplay);
            holder.tvImageFolderName = (TextView) convertView.findViewById(R.id.tvImageFolderName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String imagePath = getItem(position);
        Bitmap bitmap = Utils.UriToBitmap(imagePath);
        holder.ivImageDisplay.setImageBitmap(bitmap);
        return convertView;
    }

    public class ViewHolder {
        SquareImageView ivImageDisplay;

        TextView tvImageFolderName;
    }
}
