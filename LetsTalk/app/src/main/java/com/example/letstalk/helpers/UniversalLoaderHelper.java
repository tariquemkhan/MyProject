package com.example.letstalk.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 11/8/17.
 */

public class UniversalLoaderHelper {

    private static UniversalLoaderHelper instance = null;

    private static Context mContext;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private UniversalLoaderHelper(Context context) {
        mContext = context;
    }

    public static UniversalLoaderHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UniversalLoaderHelper(context);
        }
        return instance;
    }

    public ImageLoader getImageLoaderInstance() {
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration configuration = getConfiguration();
        imageLoader.init(configuration);
        return imageLoader;
    }

    private ImageLoaderConfiguration getConfiguration() {
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(mContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(10)
                .build();
        return imageLoaderConfiguration;
    }

    public DisplayImageOptions getOptions() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .build();
        return options;
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
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
