package com.adrian.redpackageassitant;

import android.app.Application;

import com.adrian.redpackageassitant.tools.LogUtil;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

public class MyApplication extends Application {

    private static MyApplication instance;

    private String imei = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        LogUtil.setDebug(true);
//        NoHttp.initialize(this);

        InitializationConfig config = InitializationConfig.newBuilder(this)
                // 全局连接服务器超时时间，单位毫秒，默认10s。
                .connectionTimeout(30 * 1000)
                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
                .readTimeout(30 * 1000)
                .retry(3) // 全局重试次数，配置后每个请求失败都会重试x次。
                .build();
        NoHttp.initialize(config);
//        Logger.setDebug(LogUtil.isDebug());
//        Logger.setTag("NoHttp");
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
