package com.adrian.redpackageassitant;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class RedPackageService extends AccessibilityService {

    private static final String VIEW_ID_RECEIVE_BTN_OPEN = "";
    private static final String TEXT_GET_REDPACKAGE = "领取红包";

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
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {  //通知栏事件

        } else {    //非通知栏事件
            //获取根节点
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            //根据ID获取控件节点集
            List<AccessibilityNodeInfo> idNodes = nodeInfo.findAccessibilityNodeInfosByViewId(VIEW_ID_RECEIVE_BTN_OPEN);
            //根据内容获取控件节点集
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(TEXT_GET_REDPACKAGE);
            if (!idNodes.isEmpty()) {
                idNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
