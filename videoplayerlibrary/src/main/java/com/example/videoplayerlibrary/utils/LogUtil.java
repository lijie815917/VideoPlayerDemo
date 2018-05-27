package com.example.videoplayerlibrary.utils;

import android.util.Log;

/**
 * Created by 眼神 on 2018/5/19.
 */

public class LogUtil {
    private static final String TAG = "NiceVideoPlayer";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }
}
