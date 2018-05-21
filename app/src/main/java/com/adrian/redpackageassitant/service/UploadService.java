package com.adrian.redpackageassitant.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.adrian.redpackageassitant.nohttp.HttpListener;
import com.adrian.redpackageassitant.nohttp.HttpResponseListener;
import com.adrian.redpackageassitant.tools.AppConstants;
import com.adrian.redpackageassitant.tools.CommUtil;
import com.adrian.redpackageassitant.tools.LogUtil;
import com.adrian.redpackageassitant.tools.Toast;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class UploadService extends Service {

    private static final String TAG = "UploadService";

    /**
     * 用来标记取消。
     */
    private Object object = new Object();

    /**
     * 请求队列。
     */
    private RequestQueue mQueue;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadService() {
        mQueue = NoHttp.newRequestQueue(1);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String json = intent.getStringExtra("json");
        LogUtil.LogE(TAG, "data1:" + json);
        pushData(CommUtil.base64Encode(json));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // 和声明周期绑定，退出时取消这个队列中的所有请求，当然可以在你想取消的时候取消也可以，不一定和声明周期绑定。
        mQueue.cancelBySign(object);

        // 因为回调函数持有了activity，所以退出activity时请停止队列。
        mQueue.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void pushData(String jsonStr) {
//        LogUtil.LogE(TAG, "base64:" + jsonStr);
        Request<String> request = NoHttp.createStringRequest(AppConstants.SERVER_HOST, RequestMethod.POST);
        request.add("v", jsonStr);
        request(0, request, httpListener, false, false);
    }

    /**
     * 发起请求。
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
    public <T> void request(int what, Request<T> request, HttpListener<T> callback,
                            boolean canCancel, boolean isLoading) {
        request.setCancelSign(object);
        mQueue.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));
    }

    private HttpListener<String> httpListener = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            LogUtil.LogE(TAG, "upload success response:" + response.get());
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            LogUtil.LogE(TAG, "upload failed response:" + response.getException().getMessage());
        }
    };
}
