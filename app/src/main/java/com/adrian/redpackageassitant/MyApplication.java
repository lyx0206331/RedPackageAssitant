package com.adrian.redpackageassitant;

import android.app.Application;

import com.adrian.redpackageassitant.tools.LogUtil;

public class MyApplication extends Application {

    private static MyApplication instance;

    private String imei = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        LogUtil.setDebug(true);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
