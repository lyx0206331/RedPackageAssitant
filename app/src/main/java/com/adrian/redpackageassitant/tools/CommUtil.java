package com.adrian.redpackageassitant.tools;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;

public class CommUtil {

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String base64Encode(String content) {
        String encode = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
        return encode;
    }
}
