package com.hante.tcpdemo.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderInfo implements Serializable {
    /**
     * 实际消费金额
     */
    private double actualAmount;

    public Integer getPreAuthStatus() {
        return preAuthStatus;
    }

    public void setPreAuthStatus(int preAuthStatus) {
        this.preAuthStatus = preAuthStatus;
    }

    /**
     * 0待授权 1已授权 2授权失败
     */
    private Integer preAuthStatus;

    /**
     * 更新时间 yyyy-MM-dd HH:mm:ss
     */
    private String updatetime;

    /**
     * 代理商汇率
     */
    private double agentRate;
    /**
     * 代理商id
     */
    private double agentId;
    /**
     * 代理商净结算金额
     */
    private double agentCommission;
    /**
     * 支付金额
     */
    private double amount;
    /**
     * 银行流水号
     */
    private String bankTrxNo;
    /**
     * 二级商户ID
     */
    private String channelMerchantId;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private String createtime;
    /**
     * 客户ID
     */
    private String customerId;
    /**
     * 货币类型 CNY人民币, USD 美元, JPY 日元, HKD 港币, GBP 英镑, EUR 欧元 CAD 加拿大元
     */
    private String currency;
    /**
     * 描述
     */
    private String description;
    private String userDescription;
    /**
     * 手续费
     */
    private double fee;
    /**
     * 健康管理费
     */
    private double healthAmount;

    /**
     * 是否有刷卡手续费
     */
    private Integer isSwipe = 0;

    public Integer getIsSwipe() {
        return isSwipe;
    }

    public void setIsSwipe(Integer isSwipe) {
        this.isSwipe = isSwipe;
    }

    public Double getSwipeAmount() {
        return swipeAmount;
    }

    public void setSwipeAmount(Double swipeAmount) {
        this.swipeAmount = swipeAmount;
    }

    public Double getSwipeRate() {
        return swipeRate;
    }

    public void setSwipeRate(Double swipeRate) {
        this.swipeRate = swipeRate;
    }

    /**
     * 刷卡手续费金额
     */
    private Double swipeAmount = 0.00;

    /**
     * 刷卡手续费费率
     */
    private Double swipeRate = 0.00;
    /**
     * 健康管理费率
     */
    private double healthRate;
    /**
     * 商户费率
     */
    private double merchantRate;
    /**
     * 是否有手续费
     */
    private int isHandling;
    /**
     * 是否有健康管理费
     */
    private int isHealth;
    /**
     * 是否退款(100:是,101:否,默认值为:101)
     */
    private String isRefund;
    /**
     * 是否有税费
     */
    private int isTax;
    /**
     * 是否有小费
     */
    private int isTip;
    /**
     * 创建时间 yyyy-MM-dd
     */
    private String orderDate;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 渠道编码
     */
    private String payWayCode;
    /**
     * 状态 0待支付 1成功 2失败 3交易全部撤销 4交易关闭
     */
    private String paystatus;
    /**
     * 支付时间
     */
    private String paytime;
    /**
     * 订单号
     */
    private String reference;
    /**
     * 需求备注和补充
     */
    private String remark;
    /**
     * 退款次数(默认值为:0)
     */
    private int refundTimes;
    /**
     * 手续费率
     */
    private double serviceCharge;
    /**
     * 手续费金额
     */
    private double serviceChargeAmount;
    /**
     * 手续费模式 0商户承担 1各自承担50% 3用户承担
     */
    private int serviceChargeMode;
    /**
     * 结算日期
     */
    private String settleDate;
    /**
     * 成功退款总金额
     */
    private double successRefundAmount;
    /**
     * 商户净结算金额
     */
    private double settleNetAmount;
    /**
     * 税率
     */
    private double taxation;
    /**
     * 税费金额
     */
    private double taxationAmount;
    /**
     * 指定支付页面是在PC(ONLINE) 上显示，还是手机浏览器mobile (WAP)上显示
     */
    private String terminal;
    /**
     * 小费
     */
    private double tip;
    /**
     * 订单总金额
     */
    private double totalAmount;
    /**
     * 人民币
     */
    private double rmbAmount;
    /**
     * 流水号
     */
    private String transactionId;
    /**
     * 类型 0消费 1消费撤销 2消费退款
     */
    private String type;
    /**
     * 付款方式
     */
    private String vendor;
    /**
     * 订单id
     */
    private int id;
    /**
     * 是否来源于ISV
     */
    private int isSourceIsv;
    /**
     * 商户ID
     */
    private int merchantId;
    /**
     * 渠道API类型 ONLINE线上,OFFLINE线下
     */
    private String paymentMethod;
    /**
     * 当前支付方式使用  ONLINE 线上费率 OFFLINE线下费率
     */
    private String paymentRateType;
    /**
     * 结算状态 0未结算 1待结算 2已结算
     */
    private int settleStatus;
    /**
     * 门店id
     */
    private int storeId;

    /**
     * 扩展信息
     */
    private String extendInfo;


    private List<OrderInfo> refundedOrderList;

    /**
     * 台卡支付二维码code
     */
    private String qrCode;
    //-------------menusifu order
    private String paymentChannelType;

    private String scenes;

    private String scenesAttach;


    //----------------------

    public CreditCardPreAuthOrder getCreditCardPreAuthOrder() {
        return creditCardPreAuthOrder;
    }

    public List<OrderInfo> getRefundedOrderList() {
        return refundedOrderList;
    }

    public void setRefundedOrderList(List<OrderInfo> refundedOrderList) {
        this.refundedOrderList = refundedOrderList;
    }

    public void setCreditCardPreAuthOrder(CreditCardPreAuthOrder creditCardPreAuthOrder) {
        this.creditCardPreAuthOrder = creditCardPreAuthOrder;
    }

    private CreditCardPreAuthOrder creditCardPreAuthOrder;

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public int getIsPreAuth() {
        return isPreAuth;
    }

    public void setIsPreAuth(int isPreAuth) {
        this.isPreAuth = isPreAuth;
    }

    private  int isPreAuth;

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public double getAgentId() {
        return agentId;
    }

    public void setAgentId(double agentId) {
        this.agentId = agentId;
    }

    public String getBankTrxNo() {
        return bankTrxNo;
    }

    public void setBankTrxNo(String bankTrxNo) {
        this.bankTrxNo = bankTrxNo;
    }

    public String getChannelMerchantId() {
        return channelMerchantId;
    }

    public void setChannelMerchantId(String channelMerchantId) {
        this.channelMerchantId = channelMerchantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsSourceIsv() {
        return isSourceIsv;
    }

    public void setIsSourceIsv(int isSourceIsv) {
        this.isSourceIsv = isSourceIsv;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentRateType() {
        return paymentRateType;
    }

    public void setPaymentRateType(String paymentRateType) {
        this.paymentRateType = paymentRateType;
    }

    public int getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(int settleStatus) {
        this.settleStatus = settleStatus;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public double getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(double agentRate) {
        this.agentRate = agentRate;
    }

    public double getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(double agentCommission) {
        this.agentCommission = agentCommission;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getMerchantRate() {
        return merchantRate;
    }

    public void setMerchantRate(double merchantRate) {
        this.merchantRate = merchantRate;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String  getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public double getSettleNetAmount() {
        return settleNetAmount;
    }

    public void setSettleNetAmount(double settleNetAmount) {
        this.settleNetAmount = settleNetAmount;
    }

    public double getRmbAmount() {
        return rmbAmount;
    }

    public void setRmbAmount(double rmbAmount) {
        this.rmbAmount = rmbAmount;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getHealthAmount() {
        return healthAmount;
    }

    public void setHealthAmount(double healthAmount) {
        this.healthAmount = healthAmount;
    }

    public double getHealthRate() {
        return healthRate;
    }

    public void setHealthRate(double healthRate) {
        this.healthRate = healthRate;
    }

    public int getIsHandling() {
        return isHandling;
    }

    public void setIsHandling(int isHandling) {
        this.isHandling = isHandling;
    }

    public int getIsHealth() {
        return isHealth;
    }

    public void setIsHealth(int isHealth) {
        this.isHealth = isHealth;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public int getIsTax() {
        return isTax;
    }

    public void setIsTax(int isTax) {
        this.isTax = isTax;
    }

    public int getIsTip() {
        return isTip;
    }

    public void setIsTip(int isTip) {
        this.isTip = isTip;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPayWayCode() {
        return payWayCode;
    }

    public void setPayWayCode(String payWayCode) {
        this.payWayCode = payWayCode;
    }

    public String getPayStatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getRefundTimes() {
        return refundTimes;
    }

    public void setRefundTimes(int refundTimes) {
        this.refundTimes = refundTimes;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(double serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public int getServiceChargeMode() {
        return serviceChargeMode;
    }

    public void setServiceChargeMode(int serviceChargeMode) {
        this.serviceChargeMode = serviceChargeMode;
    }

    public double getSuccessRefundAmount() {
        return successRefundAmount;
    }

    public void setSuccessRefundAmount(double successRefundAmount) {
        this.successRefundAmount = successRefundAmount;
    }

    public double getTaxation() {
        return taxation;
    }

    public void setTaxation(double taxation) {
        this.taxation = taxation;
    }

    public double getTaxationAmount() {
        return taxationAmount;
    }

    public void setTaxationAmount(double taxationAmount) {
        this.taxationAmount = taxationAmount;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setPreAuthStatus(Integer preAuthStatus) {
        this.preAuthStatus = preAuthStatus;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public String getPaymentChannelType() {
        return paymentChannelType;
    }

    public void setPaymentChannelType(String paymentChannelType) {
        this.paymentChannelType = paymentChannelType;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
    }

    public String getScenesAttach() {
        return scenesAttach;
    }

    public void setScenesAttach(String scenesAttach) {
        this.scenesAttach = scenesAttach;
    }

    // 充值信息
    private String customerMobile;
    private Double rechargeAmount;
    private Double balance;
    private Integer integral;

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public double getTotalRefundAmount(){
        if(rmbAmount!=0){
            return new BigDecimal(rmbAmount-successRefundAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return new BigDecimal(amount-successRefundAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public boolean isTsys(){
        return "tsys".equals(payWayCode);
    }


    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


    public double getPaymentAmount(){
//        if(tip>0){
//            return MathUtil.sub(amount,tip);
//        }
        return  amount;
    }

    /**
     *  状态 0待支付 1成功 2失败 3交易全部撤销 4交易关闭
     *      * pending 待⽀付
     *      * success 成功
     *      * failure 失败
     *      * closed 关闭
     * @return
     */
    public String getPayStatusFormat() {
        if("0".equals(paystatus)){
            return "pending";
        }else  if("1".equals(paystatus)){
            return "success";
        }else if("2".equals(paystatus)){
            return "failure";
        }else if("4".equals(paystatus)){
            return "closed";
        }
        return paystatus;
    }



}
