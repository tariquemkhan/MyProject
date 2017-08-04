package com.example.letstalk.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.letstalk.R;

/**
 * Created by Tarique Khan on 19/7/17.
 */

public class PreferenceHelper {

    private Context mContext;

    private SharedPreferences sharedPreferences;

    //object to edit value inside preference file
    SharedPreferences.Editor editor;

    //Static object reference of the same class
    private static PreferenceHelper instance = null;

    /**
     * private constructor to make class singleton
     * @param context current context
     */
    private PreferenceHelper (Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(mContext.getResources().
                getString(R.string.pref_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Provide single instance of the class
     * @param context
     * @return
     */
    public static PreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceHelper(context);
        }
        return instance;
    }

    /**
     * save string to preference
     * @param key of preference
     * @param value for the preference
     */
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * get String value from preference
     * @param key which value is to get
     * @return value of the key in string
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * save Integer to preference
     * @param key of preference
     * @param value for the preference
     */
    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * get Integer value from preference
     * @param key which value is to get
     * @return value of the key in Integer
     */
    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

}
