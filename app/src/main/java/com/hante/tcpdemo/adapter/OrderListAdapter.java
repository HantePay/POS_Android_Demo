package com.hante.tcpdemo.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hante.tcpdemo.R;
import com.hante.tcpdemo.TransactionRecordActivity;
import com.hante.tcpdemo.bean.OrderEnum;
import com.hante.tcpdemo.bean.OrderInfo;
import com.hante.tcpdemo.utils.MyDateUtil;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<OrderInfo> records=new ArrayList<>();



    //获取当前的时区
    String timeZone="America/Los_Angeles";
    public OrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list_info_content, parent, false);
        return new ViewHolder(v);
    }


    /**
     * 刷新 或加载更多数据
     * @param isRefresh
     * @param datas
     */
    public void refreshOrLoadMore(boolean isRefresh,List<OrderInfo> datas){
        if(isRefresh){
            records.clear();
        }
        if(null!=datas){
            records.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderInfo item=records.get(position);
        if(null!=item) {
            if (holder instanceof ViewHolder) {
                //转换类型
                ViewHolder viewHolder= (ViewHolder) holder;
//总金额
                double amount = item.getAmount();
                //人民币总金额
                double rmb_amount = item.getRmbAmount();
                //货币
                String currency = item.getCurrency();
                String payTime = item.getPaytime();
                String createTime = item.getCreatetime();
                String description = item.getTransactionId();
                String vendor = item.getVendor();
                String type = item.getType();
                String payStatus = item.getPayStatus();
                if (!TextUtils.isEmpty(description)) {
                    viewHolder.tv_item_transaction_record_description.setText(description);
                } else if (OrderEnum.OrderTypeEnum.TABLE_SIGN.name().equals
                        (item.getOrderType()) && !TextUtils.isEmpty(item.getQrCode())) {
                    viewHolder.tv_item_transaction_record_description.setText("#" + item.getQrCode());
                }else {
                    viewHolder.tv_item_transaction_record_description.setText("");
                }
                //时间
                String showDate = "";
                if (payStatus.equals(OrderEnum.MerchantOrderpayStatusEnum.success.name())) {
                    showDate = payTime;
                } else if (payStatus.equals(OrderEnum.MerchantOrderpayStatusEnum.pending.name())) {
                    //待支付
                    showDate = createTime;
                }
                //UTC 转指定时区
                String dateStr = MyDateUtil.Date2TimeZone(showDate, timeZone);
                if (!TextUtils.isEmpty(dateStr)) {
                    viewHolder.tv_item_transaction_record_paytime.setText(MyDateUtil.showDateInterval(dateStr, timeZone, true));
                }
                //收款方式
                viewHolder.iv_item_transaction_record_pay_type.setVisibility(!TextUtils.isEmpty(vendor)&&vendor.length()>1?View.VISIBLE:View.GONE);
                if (vendor.equals(OrderEnum.PaymentMethodEnum.wechatpay.name())) {
                    //微信支付
//                    viewHolder.iv_item_transaction_record_payicon.setBackgroundResource(R.mipmap.icon_wechatpay);
                    viewHolder.iv_item_transaction_record_pay_type.setText("WechatPay");
//            viewHolder.iv_order_change, R.drawable.wechatpay);
                } else if (vendor.equals(OrderEnum.PaymentMethodEnum.alipay.name())) {
                    //支付宝
//                    viewHolder.iv_item_transaction_record_payicon.setBackgroundResource(R.mipmap.icon_alipay);
                    viewHolder.iv_item_transaction_record_pay_type.setText("aliPay");
//            viewHolder.iv_order_change, R.drawable.alipay);
                } else if (vendor.equals(OrderEnum.PaymentMethodEnum.creditcard.name())) {
                    String cardType = "Credit Card";
                    //信用卡
                    if(item.getVendor().equals("creditcard") && !TextUtils.isEmpty(item.getExtendInfo())){

                        com.alibaba.fastjson.JSONObject extendInfo = com.alibaba.fastjson.JSONObject.parseObject(item.getExtendInfo());
                        String cardTypeTemp = extendInfo.getString("cardType");

                        if(TextUtils.isEmpty(cardTypeTemp)){
                            cardType = extendInfo.getString("accountType");
                        }
                        if (!TextUtils.isEmpty(cardTypeTemp)){

                            cardType = cardTypeTemp.toUpperCase();
//                            if (upCardUp.equals("VISA")){
//                                imageId = R.drawable.visa_ico;
//                            }else if (cardType.equals("MASTERCARD")){
//                                imageId = R.drawable.mastercard_ico;
//                            }else if (cardType.equals("DISCOVER")){
//                                imageId = R.drawable.discover_ico;
//                            }else if (cardType.equals("AMERICAN EXPRESS")){
//                                imageId = R.drawable.american_ico;
//                            }
                        }
                    }
                    if("V".equals(cardType)){
                        cardType="VISA";
                    }
                    viewHolder.iv_item_transaction_record_pay_type.setText(cardType);
//                    viewHolder.iv_item_transaction_record_payicon.setBackgroundResource(imageId);
                }else if (vendor.equals(OrderEnum.PaymentMethodEnum.unionpay.name())) {
//                    viewHolder.iv_item_transaction_record_payicon.setBackgroundResource(R.drawable.unionpay);
                    viewHolder.iv_item_transaction_record_pay_type.setText("Union Pay");

                }

                String currency_amount = mContext.getResources().getString(R.string.activity_transaction_record_amount_remote_order);

                if (rmb_amount==0) {
                    //美元
                    currency = "$";
                    viewHolder.tv_item_transaction_record_amount.setText(String.format(currency_amount, "$", amount));
                } else if (rmb_amount!=0) {
                    //人民币
                    currency = "¥";
                    if (payStatus.equals(OrderEnum.MerchantOrderpayStatusEnum.success)) {
                        viewHolder.tv_item_transaction_record_amount.setText(String.format(currency_amount, "¥", rmb_amount));
                    } else {
                        //支付成功的消费和退款类型,需要显示整体金额
                        viewHolder.tv_item_transaction_record_amount.setText(String.format(mContext.getResources().getString(R.string.activity_transaction_record_amount_rbm), "¥", rmb_amount, "$", amount));
                    }
                }

                viewHolder.tv_item_transaction_record_ordertype.setVisibility(View.VISIBLE);
                //订单类型
                switch (type) {
                    case "charge":
                        viewHolder.iv_order_change.setVisibility(View.GONE);
                        if (payStatus.equals(OrderEnum.MerchantOrderpayStatusEnum.pending)) {
                            //待支付
                            viewHolder.tv_refunded.setVisibility(View.GONE);
                            viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_wait_pay));
                        } else if (payStatus.equals("4")) {
                            //已关闭
                            viewHolder.tv_refunded.setVisibility(View.GONE);
                            viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_closed));
                        } else {
                            //收款
                            viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_receipt));
                            //如果有退款,显示退款信息
                            if (item.getSuccessRefundAmount() != 0) {
                                viewHolder.tv_refunded.setVisibility(View.VISIBLE);
                                viewHolder.tv_item_transaction_record_ordertype.setVisibility(View.GONE);
                                viewHolder.tv_refunded.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_refunded, currency, item.getSuccessRefundAmount()));
                            }else{
                                viewHolder.tv_refunded.setVisibility(View.GONE);
                            }

                            if(item.getPreAuthStatus() != null){
                                if(item.getPreAuthStatus() == 0){
                                    viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_pending_authorization));
                                }else if(item.getPreAuthStatus() == 1){
                                    viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_authorized));
                                }else if(item.getPreAuthStatus() == 2){
                                    viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_authorization_failed));
                                }
                            }
                        }
                        viewHolder.tv_item_transaction_record_amount.setTextColor(Color.parseColor("#00acff"));
                        break;
                    case "cancel":
                        viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_consumption_withdrawal));
                        viewHolder.tv_refunded.setVisibility(View.GONE);
                        viewHolder.iv_order_change.setVisibility(View.GONE);
                        break;
                    case "refund":
                        viewHolder.tv_item_transaction_record_ordertype.setText(mContext.getString(R.string.activity_transaction_record_item_order_type_refund));
                        viewHolder.tv_item_transaction_record_amount.setText("-" + viewHolder.tv_item_transaction_record_amount.getText());
                        viewHolder.tv_item_transaction_record_amount.setTextColor(Color.parseColor("#F8585E"));
                        viewHolder.tv_refunded.setVisibility(View.GONE);
                        viewHolder.iv_order_change.setVisibility(View.VISIBLE);
                        //退款icon
                        viewHolder.iv_order_change.setBackgroundResource(R.drawable.ic_refunded);
                        break;

                }
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        copyText(mContext,item.getTransactionId());
                        Intent data=new Intent();
                        data.putExtra("transactionId",item.getTransactionId());
                        ((TransactionRecordActivity)mContext).setResult(102,data);
                        ((TransactionRecordActivity)mContext).finish();

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public OrderInfo getItem(int i) {
        return records.get(i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tv_item_transaction_record_description;
        public TextView tv_item_transaction_record_paytime;
        //        public ImageView iv_item_transaction_record_payicon;
        public TextView tv_item_transaction_record_amount;
        public TextView tv_item_transaction_record_ordertype;
        public ImageView iv_order_change;
        public TextView tv_refunded;
        public TextView iv_item_transaction_record_pay_type;


        public ViewHolder(View v) {
            super(v);
            rootView=v;
            tv_item_transaction_record_description = v.findViewById(R.id.tv_item_transaction_record_description);
            tv_item_transaction_record_paytime =  v.findViewById(R.id.tv_item_transaction_record_paytime);
//            iv_item_transaction_record_payicon = v.findViewById(R.id.iv_item_transaction_record_payicon);
            tv_item_transaction_record_amount =  v.findViewById(R.id.tv_item_transaction_record_amount);
            tv_item_transaction_record_ordertype=v.findViewById(R.id.tv_item_transaction_record_ordertype);
            iv_item_transaction_record_pay_type=v.findViewById(R.id.iv_item_transaction_record_pay_type);
            iv_order_change=  v.findViewById(R.id.iv_order_change);
            tv_refunded = v.findViewById(R.id.tv_refunded);
        }
    }

    public void copyText(Context context, String text){
        ClipboardManager clipboardManager = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("ru", text);
        clipboardManager.setPrimaryClip(clipData);
        ToastUtils.show("Copy Transaction number Success");
    }
}
