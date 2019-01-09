package com.tongji.lisa1225.calendartest.util;


public class LogUtil {
    private static final String TAG = "DEBUG_FAN";
    private static final boolean LOG = true;

    public static void i(String msg) {
        if (LOG)
            android.util.Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (LOG)
            android.util.Log.d(TAG, msg);
    }

    public static void w(String msg) {
        if (LOG)
            android.util.Log.w(TAG, msg);
    }

    public static void w(String msg, Throwable throwable) {
        if (LOG)
            android.util.Log.w(TAG, msg, throwable);
    }

    public static void v(String msg) {
        if (LOG)
            android.util.Log.v(TAG, msg);
    }

    public static void e(String msg) {
        if (LOG)
            android.util.Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable throwable) {
        if (LOG)
            android.util.Log.e(TAG, msg, throwable);
    }
}
