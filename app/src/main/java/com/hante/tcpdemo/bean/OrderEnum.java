package com.hante.tcpdemo.bean;

public class OrderEnum {

    /**
     * type
     */
    public enum MerchantOrderTypeEnum {
        /**
         * 消费
         */
        charge,

        /**
         * 撤销，已作废
         */
        cancel,
        /**
         * 退款
         */
        refund;

    }

    /**
     * payStatus
     */
    public enum MerchantOrderpayStatusEnum {
        /**
         * 待支付
         */
        pending,
        /**
         * 成功
         */
        success,
        /**
         * 失败
         */
        failure,
        /**
         * 取消
         */
        cancel,
        /**
         * 关闭
         */
        closed
    }

    /**
     * 支付方式
     */
    public enum PaymentMethodEnum {

        /**
         *
         */
        alipay("支付宝"),
        wechatpay("微信支付"),
        paypal("PayPal支付"),
        applepay("Apple Pay支付"),
        googlepay("Google Pay支付"),
        creditcard("信用卡支付"),
        unionpay("银联");


        /**
         * 描述
         */
        private String desc;

        public String getDesc() {
            return desc;
        }

        private PaymentMethodEnum(String desc) {
            this.desc = desc;
        }


        public static PaymentMethodEnum getEnum(String name) {
            for (PaymentMethodEnum item : PaymentMethodEnum.values()) {
                if (item.name().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }
    }


    /**
     * 支付来源枚举
     *
     * @author wt
     */
    public enum OrderTypeEnum {
        /**
         *
         */
        APP("APP收款"),

        SHARE("远程分享订单"),

        TABLE_SIGN("台卡订单"),

        API("商户使用token调用订单"),

        MERCHANT_QR_CODE("商户二维码订单"),

        MACHINE("机器订单"),

        SCAN_GOODS_ORDER("扫码点单"),

        MICRO_MALL_ORDER("微商城");

        /**
         * 描述
         */
        private String desc;

        public String getDesc() {
            return desc;
        }

        private OrderTypeEnum(String desc) {
            this.desc = desc;
        }

        public static OrderTypeEnum getEnum(String name) {
            for (OrderTypeEnum item : OrderTypeEnum.values()) {
                if (item.name().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }

    }
    /**
     * 描述 货币
     *
     * @author :         wt
     * @version :        2.0
     * @date :     2019/6/19 17:50
     */
    public enum CurrencyEnum {

        /**
         * 货币单位
         */
        USD("美元"),

        CNY("人民币");

        /**
         * 描述
         */

        private String desc;

        CurrencyEnum(String desc) {
            this.desc = desc;
        }


        public static CurrencyEnum getEnum(String name) {
            for (CurrencyEnum item : CurrencyEnum.values()) {
                if (item.name().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }

    }

}
