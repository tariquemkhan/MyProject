package com.example.letstalk.gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.letstalk.R;
import com.example.letstalk.database.models.MediaCategorizingModel;
import com.example.letstalk.helpers.CustomLogger;
import com.example.letstalk.helpers.SquareImageView;
import com.example.letstalk.helpers.UniversalLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by root on 11/8/17.
 */

class MediaCategorizingAdapter extends BaseAdapter {

    private HashMap<Integer, MediaCategorizingModel> mediaList;

    private Context mContext;

    private int mediaType;

    private UniversalLoaderHelper universalLoaderHelper;

    private ImageLoader imageLoader;

    private UniversalLoaderHelper.AnimateFirstDisplayListener animateFirstDisplayListener;

    private static final String TAG = "MediaCategorizingAdapter";

    MediaCategorizingAdapter(Context context, HashMap<Integer, MediaCategorizingModel> list,
                             int type) {
        CustomLogger.log(TAG, "Mediadapter()", "inside Constructor");
        mContext = context;
        mediaList = list;

        mediaType = type;
        universalLoaderHelper = UniversalLoaderHelper.getInstance(mContext);
        imageLoader = universalLoaderHelper.getImageLoaderInstance();
        animateFirstDisplayListener = new UniversalLoaderHelper.AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        CustomLogger.log(TAG, "getCount()", "Count : " + mediaList.size());
        if (mediaList == null) {
            CustomLogger.log(TAG, "getCount()", "Count in if statement: " + mediaList.size());
            return 0;
        } else {
            CustomLogger.log(TAG, "getCount()", "Count in else statement: " + mediaList.size());
            return mediaList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mediaList == null) {
            Log.d(TAG, "in getItem if");
            return null;
        } else {
            Log.d(TAG, "in getItem else " + position);

            return mediaList.get(position);
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomLogger.log(TAG, "getView()", "inside beggining of method : ");
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_categorizing_media_folder, parent, false);
            holder = new ViewHolder();
            holder.ivImageDisplay = (SquareImageView) convertView.findViewById(R.id.ivImageDisplay);
            holder.tvFolderName = (TextView) convertView.findViewById(R.id.tvFolderName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MediaCategorizingModel mediaCategorizingModel = (MediaCategorizingModel) getItem(position);
        String imagePath = mediaCategorizingModel.getPath();
        String folderName = mediaCategorizingModel.getFolderName();
        int imageCount = mediaCategorizingModel.getMediaCount();
        String displayName = folderName + " (" + imageCount + ")";
        holder.tvFolderName.setText(displayName);
        /*Bitmap bitmap = Utils.UriToBitmap(imagePath);
        holder.ivImageDisplay.setImageBitmap(bitmap);*/
        imageLoader.displayImage("file://" + imagePath, holder.ivImageDisplay,
                universalLoaderHelper.getOptions(), animateFirstDisplayListener);
        return convertView;
    }


    private class ViewHolder {
        SquareImageView ivImageDisplay;

        TextView tvFolderName;
    }
}
