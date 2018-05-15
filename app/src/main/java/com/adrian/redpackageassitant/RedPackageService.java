package com.adrian.redpackageassitant;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class RedPackageService extends AccessibilityService {

    private static final String TAG = "REDPCKAGE_SERVICE";

    //领取红包ID
    private static final String ID_GET_REDPACKAGE = "com.tencent.mm:id/ae_";
    private static final String TEXT_GET_REDPACKAGE = "领取红包";
    private static final String TEXT_WX_REDPACKAGE = "微信红包";
    //打开红包图片ID
    private static final String ID_OPEN_REDPACKAGE = "com.tencent.mm:id/c31";
    //领取红包后返回键ID
    private static final String ID_BACK_REDPACKAGE = "com.tencent.mm:id/i1";
    //发送红包人名ID
    private static final String ID_NAME_REDPACKAGE = "com.tencent.mm:id/bz_";
    //红包金额ID
    private static final String ID_SUM_REDPACKAGE = "com.tencent.mm:id/bzd";

    //转账给你ID
    private static final String ID_TRANSFER = "com.tencent.mm:id/aei";
    private static final String TEXT_TRANSFER = "转账给你";
    //确认收款ID
    private static final String ID_CONFIRM_RECEIPT = "com.tencent.mm:id/cwk";
    //转账金额ID.文本格式:¥0.01
    private static final String ID_SUM_TRANSFER = "com.tencent.mm:id/cwj";
    //转账时间ID.文本格式:转账时间：2018-05-15 16:14
    private static final String ID_TIME_TRANSFER = "com.tencent.mm:id/cwm";
    //收钱时间ID.文本格式:收钱时间：2018-05-15 18:25
    private static final String ID_TIME_RECEIPT = "com.tencent.mm:id/cwn";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //获取根节点
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return;
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Log.e(TAG, "通知栏事件");
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Log.e(TAG, "窗口内容变化,判断是否有红包视图出现");

            //----------------------红包----------------------------
            //根据ID获取控件节点集
            List<AccessibilityNodeInfo> redpackageList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_GET_REDPACKAGE);
            //根据内容获取控件节点集
            List<AccessibilityNodeInfo> txtNodes = rootNodeInfo.findAccessibilityNodeInfosByText(TEXT_WX_REDPACKAGE);
            for (AccessibilityNodeInfo node :
                    redpackageList) {
                if (node.getText().equals(TEXT_GET_REDPACKAGE)) {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

            //----------------------转账-------------------------
            List<AccessibilityNodeInfo> transferList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TRANSFER);
            for (AccessibilityNodeInfo node : transferList) {
                if (node.getText().equals(TEXT_TRANSFER)) {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.e(TAG, "窗口状态变化");

            //---------------------红包-----------------------
            List<AccessibilityNodeInfo> clickedWindowList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_OPEN_REDPACKAGE);
            if (clickedWindowList.size() > 0) {
                clickedWindowList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            List<AccessibilityNodeInfo> nameList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_NAME_REDPACKAGE);
            if (nameList.size() > 0) {
                String name = nameList.get(0).getText().toString();
                Log.e(TAG, name + "发送的红包");
            }
            List<AccessibilityNodeInfo> sumList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_REDPACKAGE);
            if (sumList.size() > 0) {
                Double sum = Double.parseDouble(sumList.get(0).getText().toString());
                Log.e(TAG, "金额:" + sum + "元");
            }

            //-----------------------转账---------------------
            List<AccessibilityNodeInfo> confirmList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_CONFIRM_RECEIPT);
            if (confirmList.size() > 0) {
                confirmList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

//            List<AccessibilityNodeInfo> backList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_BACK_REDPACKAGE);
//            if (backList.size() > 0) {
//                backList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            }

        }
    }

    @Override
    public void onInterrupt() {

    }

    private boolean isHomePage(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> listItemNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/c48");   //com.tencent.mm:id/apr
        if (listItemNodes.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
