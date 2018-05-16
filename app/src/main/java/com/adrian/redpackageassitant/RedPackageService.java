package com.adrian.redpackageassitant;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class RedPackageService extends AccessibilityService {

    private static final String TAG = "REDPCKAGE_SERVICE";

    //首页微信红包ID
    private static final String ID_WX_REDPACKAGE = "com.tencent.mm:id/apx";
    private static final String TEXT_WX_REDPACKAGE = "[微信红包]";
    private static final String TEXT_QRSCAN_PAY = "收款到账通知";
    private static final String TEXT_WX_TRANSFER = "请你确认收钱";
    //首页红色角标ID
    private static final String ID_WX_REDICON = "com.tencent.mm:id/jj";

    //领取红包ID
    private static final String ID_GET_REDPACKAGE = "com.tencent.mm:id/ae_";
    private static final String TEXT_GET_REDPACKAGE = "领取红包";
    //打开红包图片ID
    private static final String ID_OPEN_REDPACKAGE = "com.tencent.mm:id/c31";
    //领取红包后返回键ID
    private static final String ID_BACK_REDPACKAGE = "com.tencent.mm:id/i1";
    //发送红包人名ID
    private static final String ID_NAME_REDPACKAGE = "com.tencent.mm:id/bz_";
    //红包金额ID
    private static final String ID_SUM_REDPACKAGE = "com.tencent.mm:id/bzd";
    //红包详情title ID
    private static final String ID_TITLE_REDPACKAGE = "android:id/text1";
    private static final String TEXT_TITLE_REDPACKAGE = "红包详情";
    private static final String TEXT_TITLE_TRANSFER = "交易详情";
    private static final String TEXT_TITLE_QRSCAN = "二维码收款";
    //红包记录ID
    private static final String ID_RECORD_REDPACKAGE = "com.tencent.mm:id/hg";
    private static final String TEXT_RECORD_REDPACKAGE = "红包记录";
    //红包祝福ID
    private static final String ID_WISH_REDPACKAGE = "com.tencent.mm:id/bzb";

    //转账给你ID
    private static final String ID_TRANSFER = "com.tencent.mm:id/aei";
    private static final String TEXT_RECEIPTED = "已收钱";
    private static final String TEXT_GOT = "已被领取";
    //确认收款ID
    private static final String ID_CONFIRM_RECEIPT = "com.tencent.mm:id/cwk";
    //转账金额ID.文本格式:¥0.01
    private static final String ID_SUM_TRANSFER = "com.tencent.mm:id/cwj";
    //转账时间ID.文本格式:转账时间：2018-05-15 16:14
    private static final String ID_TIME_TRANSFER = "com.tencent.mm:id/cwm";
    //收钱时间ID.文本格式:收钱时间：2018-05-15 18:25
    private static final String ID_TIME_RECEIPT = "com.tencent.mm:id/cwn";

    //扫码支付ID
    private static final String ID_NAME_QRSCAN = "android:id/title";
    //扫码付款金额
    private static final String ID_SUM_QRSCAN = "android:id/summary";

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
            List<CharSequence> texts = accessibilityEvent.getText();
            if (!texts.isEmpty()) {
                for (CharSequence txt :
                        texts) {
                    String msg = txt.toString();
                    if (msg.contains(TEXT_WX_REDPACKAGE) || msg.contains(TEXT_WX_TRANSFER)) {
                        if (accessibilityEvent.getParcelableData() != null && accessibilityEvent.getParcelableData() instanceof Notification) {
                            Notification notification = (Notification) accessibilityEvent.getParcelableData();
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Log.e(TAG, "窗口内容变化,判断是否有红包视图出现");

            //-----------------------微信首页--------------------------
            List<AccessibilityNodeInfo> homeList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_WX_REDPACKAGE);
            List<AccessibilityNodeInfo> redIcons = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_WX_REDICON);
            for (AccessibilityNodeInfo nodeInfo : homeList) {
                String tips = nodeInfo.getText().toString().trim();
//                Log.e(TAG, "首页信息:" + tips);
                if (tips.startsWith(TEXT_WX_REDPACKAGE) || tips.endsWith(TEXT_WX_TRANSFER)) {
                    Log.e(TAG, "自动跳转");
                    nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

            //----------------------红包----------------------------
            //根据ID获取控件节点集
            List<AccessibilityNodeInfo> redpackageList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_GET_REDPACKAGE);
            //根据内容获取控件节点集
            List<AccessibilityNodeInfo> txtNodes = rootNodeInfo.findAccessibilityNodeInfosByText(TEXT_WX_REDPACKAGE);
            for (AccessibilityNodeInfo node :
                    redpackageList) {
                if (node.getText().equals(TEXT_GET_REDPACKAGE)) {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e(TAG, "模拟点击--领取红包");
                }
            }

            //----------------------转账-------------------------
            List<AccessibilityNodeInfo> transferList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TRANSFER);
            for (AccessibilityNodeInfo node : transferList) {
//                Log.e(TAG, "模拟点击--转账收款:" + node.getText());
                if (!(node.getText().equals(TEXT_RECEIPTED) || node.getText().toString().contains(TEXT_GOT))) {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e(TAG, "转账收款:" + node.getText());
                }
            }

            //----------------------二维码收款---------------------------
            List<AccessibilityNodeInfo> titleList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TITLE_REDPACKAGE);
            List<AccessibilityNodeInfo> qrNames = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_NAME_QRSCAN);
            if (qrNames.size() > 0 && titleList.size() > 0 && titleList.get(0).getText().equals(TEXT_TITLE_QRSCAN)) {
                String name = TextUtils.isEmpty(qrNames.get(0).getText()) ? "" : qrNames.get(0).getText().toString();
                Log.e(TAG, "二维码付款人:" + name);
            }
            List<AccessibilityNodeInfo> qrSum = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_QRSCAN);
            if (qrSum.size() > 0) {
                try {
                    Double sum = Double.parseDouble(qrSum.get(0).getText().toString().substring(1));
                    Log.e(TAG, "二维码收款金额:¥" + sum);
                } catch (NumberFormatException e) {
                    Log.e(TAG, qrSum.get(0).getText().toString());
                    e.printStackTrace();
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
            List<AccessibilityNodeInfo> redpackageSum = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_REDPACKAGE);
            if (redpackageSum.size() > 0) {
                Double sum = Double.parseDouble(redpackageSum.get(0).getText().toString());
                Log.e(TAG, "红包金额:¥" + sum);
            }
            List<AccessibilityNodeInfo> redPackageMsg = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_WISH_REDPACKAGE);
            if (redPackageMsg.size() > 0) {
                String wish = redPackageMsg.get(0).getText().toString();
                Log.e(TAG, "红包祝福:" + wish);
            }

            //-----------------------转账---------------------
            List<AccessibilityNodeInfo> confirmList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_CONFIRM_RECEIPT);
            if (confirmList.size() > 0) {
                confirmList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            List<AccessibilityNodeInfo> transferSum = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_TRANSFER);
            if (transferSum.size() > 0) {
                Double sum = Double.parseDouble(transferSum.get(0).getText().toString().substring(1));
                Log.e(TAG, "转账金额:¥" + sum);
            }
            List<AccessibilityNodeInfo> timeTransfer = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TIME_TRANSFER);
            if (timeTransfer.size() > 0) {
                String time = timeTransfer.get(0).getText().toString().substring(5);
                Log.e(TAG, "转账时间:" + time);
            }
            List<AccessibilityNodeInfo> timeReceipt = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TIME_RECEIPT);
            if (timeReceipt.size() > 0) {
                String time = timeReceipt.get(0).getText().toString().substring(5);
                Log.e(TAG, "收钱时间:" + time);
            }

            //--------------------返回--------------------------
            List<AccessibilityNodeInfo> backList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_BACK_REDPACKAGE);
            List<AccessibilityNodeInfo> titleList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TITLE_REDPACKAGE);
            List<AccessibilityNodeInfo> recordList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_RECORD_REDPACKAGE);
            if (backList.size() > 0 && titleList.size() > 0) {
                Log.e(TAG, "title:" + titleList.get(0).getText()/* + "--" + recordList.get(0).getText()*/);
                if (titleList.size() > 0 && (titleList.get(0).getText().toString().equals(TEXT_TITLE_REDPACKAGE) || titleList.get(0).getText().toString().equals(TEXT_TITLE_TRANSFER))) {
                    backList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

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
