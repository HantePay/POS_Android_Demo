package com.hante.tcpdemo.bean;

import java.io.Serializable;

public class OrderInfoResponse implements Serializable {
    /**
     * 业务结果状态码
     * SUCCESS 成功
     * FAIL 失败
     */
    public String resultCode;

    /**
     * 返回消息信息
     */
    public String resultMsg;



    /**
     * 返回消息信息
     */
    public String returnCode;

    /**
     * 金额
     */
    private int amount;


    /**
     * 支付方式
     * wechatpay
     * alipay
     * unionpay
     * creditcard
     */
    private String paymentMethod;

    /**
     * 交易类型:
     * SALE 直接收款
     * AUTH 预授权(刷卡支付生效)
     */
    private String transType;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 卡类型
     */
    private String cardType;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
