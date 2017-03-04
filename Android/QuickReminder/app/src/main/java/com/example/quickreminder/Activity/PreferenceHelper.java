package com.example.quickreminder.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;
import java.util.Set;

/**
 * Created by Dell on 7/31/2016.
 */
public class PreferenceHelper implements SharedPreferences {

    public static final String REMINDERPREFERENCES = "ReminderPrefs";

    private static SharedPreferences sharedpreferences;

    private static SharedPreferences.Editor editor;

    private String TAG = "PreferenceHelper";

    public static PreferenceHelper getInstance(Context context) {
        sharedpreferences = context.getSharedPreferences(REMINDERPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        return new PreferenceHelper();
    }

    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return null;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        return sharedpreferences.getInt(key, defValue);
    }

    public void setInt(String key, int value) {
        Log.d(TAG, "inside setInt : ");
        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }


    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }
}
