package com.example.letstalk.helpers;

import android.util.Log;

/**
 * Created by root on 4/8/17.
 */

public class CustomLogger {

    /**
     *
     * @param tagName by which log can be filter out
     * @param methodName method name in which log is showing
     * @param message to show in the log
     */
    public static void log(String tagName, String methodName, String message) {
        Log.d(tagName, " : " + methodName + " : " + message );
    }

}
