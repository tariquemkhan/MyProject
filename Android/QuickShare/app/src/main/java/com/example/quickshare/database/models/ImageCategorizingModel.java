package com.example.quickshare.database.models;

/**
 * Created by root on 30/8/16.
 */
public class ImageCategorizingModel {

    private String _id;

    private String path;

    private int imageCount;

    private boolean isInMemory;

    public boolean isInMemory() {
        return isInMemory;
    }

    public void setInMemory(boolean inMemory) {
        isInMemory = inMemory;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }
}
