package com.hante.tcpdemo.net;

public class CloudCreateOrderRequest {
    /**
     * 商户号
     */
    private String merchant_no;

    /**
     * ⻔店号
     */
    private String store_no;


    /**
     * 商家订单号
     */
    private String out_trade_no;

    /**
     * 金额 ⾦额(单位:分)
     */
    private int amount=1;

    /**
     * 时间戳
     */
    private long time=System.currentTimeMillis();

    /**
     * 随机字符串
     */
    private String nonce_str;


    /**
     * 附加数据
     */
    private String note;


    /**
     * 货币，允许值USD
     */
    private String currency="USD";


    /**
     * 描述类容
     */
    private String body="1025258896";


    /**
     * 取餐号
     */
    private String mealNumber;

    /**
     * 签名信息
     */
    private String signature;


    /**
     * 签名类型,默认值:MD5（不参与签名）
     */
//    private String sign_type="MD5";


    /**
     * 类型默认值:SUNMI_POS
     */
    private String type="SUNMI_POS";

    /**
     * 机器SN编号
     */
    private String sn;


    /**
     * ⼩费(单位:分)
     */
    private int tip;

    /**
     * ⼩费(单位:分)
     */
    private int tax;

    /**
     * 异步通知地址
     */
    private String notify_url="https://www.hantepay.com";


    /**
     *
     */
    private int jump_type=1;


    private String payment_method="creditcard";

    /**
     * 订单名
     */
    private String name;

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getStore_no() {
        return store_no;
    }

    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

//    public String getSign_type() {
//        return sign_type;
//    }
//
//    public void setSign_type(String sign_type) {
//        this.sign_type = sign_type;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJump_type() {
        return jump_type;
    }

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getMealNumber() {
        return mealNumber;
    }

    public void setMealNumber(String mealNumber) {
        this.mealNumber = mealNumber;
    }
}
