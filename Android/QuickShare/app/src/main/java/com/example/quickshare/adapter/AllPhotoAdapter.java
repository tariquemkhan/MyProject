package com.example.quickshare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickshare.R;
import com.example.quickshare.activity.FileTransferActivity;
import com.example.quickshare.database.models.ImageCategorizingModel;
import com.example.quickshare.helpers.SquareImageView;
import com.example.quickshare.helpers.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 29/8/16.
 */
public class AllPhotoAdapter extends BaseAdapter {

    private Context mContext;

    private HashMap<Integer,ImageCategorizingModel> mImageList;

    ImageCategorizingModel imageCategorizingModel;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener;

    public AllPhotoAdapter (Context mContext, HashMap<Integer,ImageCategorizingModel> imageList) {
        this.mContext = mContext;
        mImageList = imageList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .build();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(mContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(10)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfiguration);
        animateFirstListener = new AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public ImageCategorizingModel getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d(FileTransferActivity.TAG_NAME,"inside getView() : ");
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
        imageCategorizingModel = getItem(position);
        String imagePath = imageCategorizingModel.getPath();
        String folderName = imageCategorizingModel.getFolderName();
        int imageCount = imageCategorizingModel.getImageCount();
        String displayName = folderName+" ("+imageCount+")";
        holder.tvImageFolderName.setText(displayName);
        /*Bitmap bitmap = Utils.UriToBitmap(imagePath);
        holder.ivImageDisplay.setImageBitmap(bitmap);*/
        imageLoader.displayImage("file://" + imagePath, holder.ivImageDisplay, options, animateFirstListener);
        return convertView;
    }

    public class ViewHolder {
        SquareImageView ivImageDisplay;

        TextView tvImageFolderName;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
