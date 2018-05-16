package com.adrian.redpackageassitant.model;

public class DataBean {
    private String channel;   //渠道。WX:微信;ALI:支付宝;BANK:银行
    private String mode; //支付模式.QR:扫码;ZZ:转账;HB:红包
    private int money;    //金额(分)
    private String number;  //交易单号 (微信/支付宝的交易单号)
    private String payer; //付款方 (微信或支付宝：昵称/用户名)
    private String payerNotes;    //付款方备注(无备注时，默认为空字符串)
    private String paymentDatetime; //付款时间 (格式：2018-01-01 08:00:00)
    private String receiptDatetime; //收钱时间 (格式：2018-01-01 08:00:00)
    private String wxStats; //微信扫码，统计摘要信息
    private String wxDelay; //微信转账，延时到账信息
    private String deviceName;  // 设备名称 (默认：空字符串)
    private String deviceIMEI;  //设备串号 (IMEI)
    private String deviceTime;  //设备时间 (设备当前时间戳)

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayerNotes() {
        return payerNotes;
    }

    public void setPayerNotes(String payerNotes) {
        this.payerNotes = payerNotes;
    }

    public String getPaymentDatetime() {
        return paymentDatetime;
    }

    public void setPaymentDatetime(String paymentDatetime) {
        this.paymentDatetime = paymentDatetime;
    }

    public String getReceiptDatetime() {
        return receiptDatetime;
    }

    public void setReceiptDatetime(String receiptDatetime) {
        this.receiptDatetime = receiptDatetime;
    }

    public String getWxStats() {
        return wxStats;
    }

    public void setWxStats(String wxStats) {
        this.wxStats = wxStats;
    }

    public String getWxDelay() {
        return wxDelay;
    }

    public void setWxDelay(String wxDelay) {
        this.wxDelay = wxDelay;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }
}
