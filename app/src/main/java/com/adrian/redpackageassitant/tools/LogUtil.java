package com.adrian.redpackageassitant.tools;

import android.support.annotation.NonNull;
import android.util.Log;

public class LogUtil {

    private static boolean isDebug = false;

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    public static void LogE(@NonNull String tag, @NonNull String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void LogV(@NonNull String tag, @NonNull String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void LogD(@NonNull String tag, @NonNull String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void LogI(@NonNull String tag, @NonNull String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void LogW(@NonNull String tag, @NonNull String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }
}
