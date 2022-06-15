package com.freeme.smartscan.utils;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "SmartScan";
    private static final boolean isEnable = true;

    //DEBUG
    public static void d(String msg) {
        if (isEnable) {
            Log.d(TAG, getCallerInfo() + msg);
        }
    }

    //INFO
    public static void i(String msg) {
        if (isEnable) {
            Log.i(TAG, getCallerInfo() + msg);
        }
    }

    //WARN
    public static void w(String msg) {
        if (isEnable) {
            Log.w(TAG, getCallerInfo() + msg);
        }
    }

    //ERROR
    public static void e(String msg) {
        if (isEnable) {
            Log.e(TAG, getCallerInfo() + msg);
        }
    }

    private static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[4];
        return caller.getClassName() + " " + caller.getMethodName() + " " + caller.getLineNumber() + " ";
    }
}
