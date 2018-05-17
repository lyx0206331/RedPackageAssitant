package com.adrian.redpackageassitant;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.adrian.redpackageassitant.tools.LogUtil;

import java.util.List;

import static com.adrian.redpackageassitant.config.WxConfig.*;
import static com.adrian.redpackageassitant.config.AliConfig.*;

public class RedPackageService extends AccessibilityService {

    private static final String TAG = "REDPCKAGE_SERVICE";

    private AccessibilityNodeInfo rootNodeInfo;

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
        rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return;
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            LogUtil.LogE(TAG, "通知栏事件");
            getNotification(accessibilityEvent);
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            LogUtil.LogE(TAG, "窗口内容变化,判断是否有红包视图出现");

            //-----------------------微信首页--------------------------
            jumpFromWxFirstPage();
            //----------------------微信红包----------------------------
            getWxRedpackage();


            //----------------------微信转账-------------------------
            getWxTransfer();


            openWxChangePage();

            //----------------------微信二维码收款---------------------------
            getWxQrSum();

            //---------------------支付宝二维码收款-----------------------
            getAliBillDetail();
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            LogUtil.LogE(TAG, "窗口状态变化");

            //---------------------微信红包-----------------------
            getWxRedpackageInfo();

            //-----------------------微信转账---------------------
            getWxTransferInfo();
            openWxChangeList();

            //--------------------返回--------------------------
            answerWxBackKey();

        }
    }

    /**
     * 打开零钱页面
     */
    private void openWxChangePage() {
        List<AccessibilityNodeInfo> checkList = rootNodeInfo.findAccessibilityNodeInfosByText(TXT_CHECK_CHANGE);
        if (checkList != null && checkList.size() > 0) {
            LogUtil.LogE(TAG, "点击查看零钱");
            AccessibilityNodeInfo checkChange = checkList.get(0);
            checkChange.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 获取账信息
     */
    private void getAliBillDetail() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> dataList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_ALI_DETAIL_ROOT);
        for (AccessibilityNodeInfo nodeInfo : dataList) {
            LogUtil.LogE(TAG, "data:" + nodeInfo.getContentDescription());
            recursiveNode(nodeInfo);
        }
//        int size = dataList.get(0).getChild(0).getChild(0).getChild(0).getChildCount();
//        LogUtil.LogE(TAG, "data size:" + size);
//        if (dataList != null && dataList.size() > 0) {
//            LogUtil.LogE(TAG, "data size:" + dataList.size());
//            if (dataList.get(0).getChildCount() > 0) {
//
//            }
//        }
    }

    /**
     * 递归获取数据
     *
     * @param nodeInfo
     */
    private void recursiveNode(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null || nodeInfo.getChildCount() == 0) {
            return;
        }
        int size = nodeInfo.getChildCount();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo info = nodeInfo.getChild(i);
            LogUtil.LogE(TAG, "desc:" + info.getContentDescription());
            recursiveNode(info);
        }
    }

    /**
     * 响应微信返回键
     */
    private void answerWxBackKey() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> backList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_BACK_REDPACKAGE);
        List<AccessibilityNodeInfo> titleList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TITLE_REDPACKAGE);
        List<AccessibilityNodeInfo> recordList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_RECORD_REDPACKAGE);
        if (backList.size() > 0 && titleList.size() > 0) {
            LogUtil.LogE(TAG, "title:" + titleList.get(0).getText()/* + "--" + recordList.get(0).getText()*/);
            if (titleList.size() > 0 && (titleList.get(0).getText().toString().equals(TEXT_TITLE_REDPACKAGE) || titleList.get(0).getText().toString().equals(TEXT_TITLE_TRANSFER))) {
                backList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 打开零钱明细列表页
     */
    private void openWxChangeList() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> changeList = rootNodeInfo.findAccessibilityNodeInfosByText(TXT_CHANGE_DETAIL);
        if (changeList != null && changeList.size() > 0) {
            LogUtil.LogE(TAG, "点击零钱明细");
            AccessibilityNodeInfo change = changeList.get(0);
            change.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 获取微信转账信息
     */
    private void getWxTransferInfo() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> confirmList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_CONFIRM_RECEIPT);
        if (confirmList.size() > 0) {
            confirmList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        List<AccessibilityNodeInfo> transferSum = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_TRANSFER);
        if (transferSum.size() > 0) {
            Double sum = Double.parseDouble(transferSum.get(0).getText().toString().substring(1));
            LogUtil.LogE(TAG, "转账金额:¥" + sum);
        }
        List<AccessibilityNodeInfo> timeTransfer = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TIME_TRANSFER);
        if (timeTransfer.size() > 0) {
            String time = timeTransfer.get(0).getText().toString().substring(5);
            LogUtil.LogE(TAG, "转账时间:" + time);
        }
        List<AccessibilityNodeInfo> timeReceipt = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TIME_RECEIPT);
        if (timeReceipt.size() > 0) {
            String time = timeReceipt.get(0).getText().toString().substring(5);
            LogUtil.LogE(TAG, "收钱时间:" + time);
        }
    }

    /**
     * 获取微信红包信息
     */
    private void getWxRedpackageInfo() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> clickedWindowList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_OPEN_REDPACKAGE);
        if (clickedWindowList.size() > 0) {
            clickedWindowList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        List<AccessibilityNodeInfo> nameList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_NAME_REDPACKAGE);
        if (nameList.size() > 0) {
            String name = nameList.get(0).getText().toString();
            LogUtil.LogE(TAG, name + "发送的红包");
        }
        List<AccessibilityNodeInfo> redpackageSum = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_REDPACKAGE);
        if (redpackageSum.size() > 0) {
            Double sum = Double.parseDouble(redpackageSum.get(0).getText().toString());
            LogUtil.LogE(TAG, "红包金额:¥" + sum);
        }
        List<AccessibilityNodeInfo> redPackageMsg = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_WISH_REDPACKAGE);
        if (redPackageMsg.size() > 0) {
            String wish = redPackageMsg.get(0).getText().toString();
            LogUtil.LogE(TAG, "红包祝福:" + wish);
        }
    }

    /**
     * 获取二码收款金额
     */
    private void getWxQrSum() {
        if (rootNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> accountTimeList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TIME_ACCOUNT);
//        for (AccessibilityNodeInfo nodeInfo : accountTimeList) {
//            LogUtil.LogE(TAG, "到账时间:" + nodeInfo.getText().toString());
//        }
        if (accountTimeList != null && accountTimeList.size() > 0) {
            AccessibilityNodeInfo lastInfo = accountTimeList.get(accountTimeList.size() - 1);
            LogUtil.LogE(TAG, "到账时间:" + lastInfo.getText());
            List<AccessibilityNodeInfo> sumList = lastInfo.getParent().findAccessibilityNodeInfosByViewId(ID_SUM_ACCOUNT);
            if (sumList.size() > 0) {
                String sum = sumList.get(0).getText().toString();
                LogUtil.LogE(TAG, "收款金额:" + Double.parseDouble(sum.substring(1)));
            }
            List<AccessibilityNodeInfo> msgList = lastInfo.getParent().findAccessibilityNodeInfosByViewId(ID_OTHER_ACCOUNT);
            for (AccessibilityNodeInfo msg : msgList) {
                LogUtil.LogE(TAG, "其它信息:" + msg.getText().toString());
            }
        }

//        List<AccessibilityNodeInfo> titleList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TITLE_REDPACKAGE);
//        List<AccessibilityNodeInfo> qrNames = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_NAME_QRSCAN);
//        if (qrNames.size() > 0 && titleList.size() > 0 && titleList.get(0).getText().equals(TEXT_TITLE_QRSCAN)) {
//            String name = TextUtils.isEmpty(qrNames.get(0).getText()) ? "" : qrNames.get(0).getText().toString();
//            LogUtil.LogE(TAG, "二维码付款人:" + name);
//        }
//        List<AccessibilityNodeInfo> qrSum = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_SUM_QRSCAN);
//        if (qrSum.size() > 0) {
//            try {
//                Double sum = Double.parseDouble(qrSum.get(0).getText().toString().substring(1));
//                LogUtil.LogE(TAG, "二维码收款金额:¥" + sum);
//            } catch (NumberFormatException e) {
//                LogUtil.LogE(TAG, qrSum.get(0).getText().toString());
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 收取微信转账
     */
    private void getWxTransfer() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> transferList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_TRANSFER);
        for (AccessibilityNodeInfo node : transferList) {
//                LogUtil.LogE(TAG, "模拟点击--转账收款:" + node.getText());
            if (!(node.getText().equals(TEXT_RECEIPTED) || node.getText().toString().contains(TEXT_GOT))) {
                node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                LogUtil.LogE(TAG, "转账收款:" + node.getText());
            }
        }
    }

    /**
     * 领取微信红包
     */
    private void getWxRedpackage() {
        if (rootNodeInfo == null) {
            return;
        }
        //根据ID获取控件节点集
        List<AccessibilityNodeInfo> redpackageList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_GET_REDPACKAGE);
        //根据内容获取控件节点集
        List<AccessibilityNodeInfo> txtNodes = rootNodeInfo.findAccessibilityNodeInfosByText(TEXT_WX_REDPACKAGE);
        for (AccessibilityNodeInfo node :
                redpackageList) {
            if (node.getText().equals(TEXT_GET_REDPACKAGE)) {
                node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                LogUtil.LogE(TAG, "模拟点击--领取红包");
            }
        }
    }

    /**
     * 从微信首页跳转到聊天界面
     */
    private void jumpFromWxFirstPage() {
        if (rootNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> homeList = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_WX_REDPACKAGE);
        List<AccessibilityNodeInfo> redIcons = rootNodeInfo.findAccessibilityNodeInfosByViewId(ID_WX_REDICON);
        for (AccessibilityNodeInfo iconNode : redIcons) {
            List<AccessibilityNodeInfo> redpackageNodes = iconNode.getParent().findAccessibilityNodeInfosByViewId(ID_WX_REDPACKAGE);
            if (redpackageNodes.size() > 0) {
                for (AccessibilityNodeInfo redpackage : redpackageNodes) {
                    String tips = redpackage.getText().toString();
                    if (tips.startsWith(TEXT_WX_REDPACKAGE) || tips.endsWith(TEXT_WX_TRANSFER) || tips.contains(TEXT_MONEY_MSG)) {
                        LogUtil.LogE(TAG, "首页自动跳转--" + tips);
                        redpackage.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }
    }

    /**
     * 获取微信通知栏数据
     *
     * @param accessibilityEvent
     */
    private void getNotification(AccessibilityEvent accessibilityEvent) {
        List<CharSequence> texts = accessibilityEvent.getText();
        if (!texts.isEmpty()) {
            for (CharSequence txt :
                    texts) {
                String msg = txt.toString();
                LogUtil.LogE(TAG, "notification:" + msg);
                if (msg.contains(TEXT_WX_REDPACKAGE) || msg.contains(TEXT_WX_TRANSFER) || msg.contains(TEXT_MONEY_MSG) || msg.contains(TXT_ALI_QRSCAN_MSG)) {
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

    @Override
    public void onInterrupt() {

    }

    private boolean isWxHomePage(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> listItemNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/c48");   //com.tencent.mm:id/apr
        if (listItemNodes.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
