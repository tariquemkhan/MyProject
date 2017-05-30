package com.example.dropboximageuploader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 7/2/17.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private Cursor mCursor;

    private LayoutInflater inflater;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener;

    private String TAG = "Alpha";

    public ImageAdapter (Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        inflater = LayoutInflater.from(mContext);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub)
                .showImageForEmptyUri(R.mipmap.ic_empty)
                .showImageOnFail(R.mipmap.ic_error)
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
        if (mCursor == null) {
            return  0;
        }
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_items,parent,false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.ctvUploadStatus = (CheckedTextView) convertView.findViewById(R.id.ctvUploadStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mCursor.moveToPosition(position);
        String thumb_path = "";
        String path = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.IMAGE_PATH));
        int status = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.STATUS));
        Long _id = Long.valueOf(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.IMAGE_ID)));
        File image = new File(path);
        if (image.exists()) {
            /*bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);*/
            //holder.imageView.setImageBitmap(bitmap);
            Uri uri = Uri.fromFile(new File(path));

            Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                    mContext.getContentResolver(), _id,
                    MediaStore.Images.Thumbnails.MICRO_KIND,
                    null );
            if( cursor != null && cursor.getCount() > 0 ) {
                cursor.moveToFirst();//**EDIT**
                thumb_path = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Thumbnails.DATA ) );
                //Log.d(TAG, "getView: thumb path : "+thumb_path+" Position : "+position);
            }

            imageLoader.displayImage("file://" + path, holder.imageView, options, animateFirstListener);
        }
        //Log.d(TAG, "getView: Status : "+status);
        if (status == 1) {
            holder.ctvUploadStatus.setChecked(true);
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_done_black_24dp);
            drawable.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.SRC_ATOP);
            holder.ctvUploadStatus.setCheckMarkDrawable(drawable);
        } else {
            holder.ctvUploadStatus.setChecked(false);
            holder.ctvUploadStatus.setCheckMarkDrawable(null);
        }
        return convertView;
    }

    class ViewHolder {

        private ImageView imageView;

        private CheckedTextView ctvUploadStatus;
    }

    public void refereshCursor(Cursor cursor) {
        this.mCursor = cursor;
        Log.d(TAG, "refereshCursor: inside refereshCursor() : ");
        this.notifyDataSetChanged();
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
