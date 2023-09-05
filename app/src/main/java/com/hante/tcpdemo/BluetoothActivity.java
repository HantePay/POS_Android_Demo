package com.hante.tcpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hante.tcp.bean.v2.POSBase;
import com.hante.tcp.bean.v2.POSCapture;
import com.hante.tcp.bean.v2.POSOrderQuery;
import com.hante.tcp.bean.v2.POSRefund;
import com.hante.tcp.bean.v2.POSTransaction;
import com.hante.tcp.bean.v2.POSVoid;
import com.hante.tcp.util.EncryptUtils;
import com.hante.tcpdemo.event.UpdateMsgEvent;
import com.hante.tcpdemo.utils.IPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG="TcpV2Activity";
    private EditText ip_et;
    private TextView param_tv;
    private TextView result_tv;
    private EditText merchant_et;

    private EditText reconnection_time_et;

    private EditText reconnection_order_et;



    private TextView status_tv;

    /**
     * 金额
     */
    private EditText et_amount;
    private EditText et_tax_amount;

    private EditText et_tip_amount;

    private EditText et_remark;


    private EditText refund_et;

    private EditText capture_et;

    private EditText capture_tip_et;

    private EditText order_tr_et;

    /**
     * pos 创建 服务连接设备
     * 商户后台->设备管理->设备列表->设置->创建终端密钥
     */
    private EditText device_id_et;
    private EditText device_key_et;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        device_id_et=findViewById(R.id.device_id_et);
        device_key_et=findViewById(R.id.device_key_et);
        ip_et = findViewById(R.id.ip_et);
        param_tv = findViewById(R.id.param_tv);
        result_tv = findViewById(R.id.result_tv);
        merchant_et = findViewById(R.id.merchant_et);
        status_tv=findViewById(R.id.status_tv);
        reconnection_time_et=findViewById(R.id.reconnection_time_et);
        reconnection_order_et=findViewById(R.id.reconnection_order_et);
        et_amount=findViewById(R.id.et_amount);
        et_tax_amount=findViewById(R.id.et_tax_amount);
        et_tip_amount=findViewById(R.id.et_tip_amount);
        et_remark=findViewById(R.id.et_remark);
        order_tr_et=findViewById(R.id.order_tr_et);

        refund_et=findViewById(R.id.refund_et);
        capture_et=findViewById(R.id.capture_et);
        capture_tip_et=findViewById(R.id.capture_tip_et);

        findViewById(R.id.checkstand_btn).setOnClickListener(this);
        findViewById(R.id.qrcode_collection_btn).setOnClickListener(this);
        findViewById(R.id.qrcode_scan_btn).setOnClickListener(this);
        findViewById(R.id.checkout_counter_btn).setOnClickListener(this);

        findViewById(R.id.checkout_cancel_btn).setOnClickListener(this);

        findViewById(R.id.jump_to_choose_bluetooth).setOnClickListener(this);

        findViewById(R.id.upload_order_btn).setOnClickListener(this);

        findViewById(R.id.refresh_order_btn).setOnClickListener(this);

        findViewById(R.id.cancel_order_btn).setOnClickListener(this);

        findViewById(R.id.refund_btn).setOnClickListener(this);

        findViewById(R.id.auth_btn).setOnClickListener(this);

        findViewById(R.id.capture_btn).setOnClickListener(this);

        findViewById(R.id.void_btn).setOnClickListener(this);

        findViewById(R.id.query_order_btn).setOnClickListener(this);

        findViewById(R.id.checkout_ebt_btn).setOnClickListener(this);
        EventBus.getDefault().register(this);
        customDialog=new CustomDialog(this);
    }

    @Override
    public void onClick(View view) {
        POSTransaction posSendMessage = new POSTransaction();
        posSendMessage.setVersion("V2");
        posSendMessage.setIp(IPUtil.getInNetIp(BluetoothActivity.this));
        switch (view.getId()) {
            case R.id.jump_to_choose_bluetooth://选择蓝牙
                startActivity(new Intent(BluetoothActivity.this,BluetoothChooseActivity.class));
                break;
            case R.id.checkstand_btn://信用卡
            case R.id.auth_btn:
                posSendMessage.setType("transaction");
                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    posSendMessage.setAmount(Integer.parseInt(et_amount.getText().toString()));
                }
                posSendMessage.setCurrency("USD");
                posSendMessage.setTitle("大厅1001桌");
                posSendMessage.setDesc(et_remark.getText().toString());
                posSendMessage.setOrderNo(randomOrderNo());
                posSendMessage.setPaymentScenario("POS_PAYMENT");
                posSendMessage.setTransType(R.id.checkstand_btn==view.getId()?"SALE":"AUTH");
                //"{\"taxAmount\":1,\"tipCustom\":false,\"tipAmount\":0}"
                JSONObject charges0=new JSONObject();
                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    charges0.put("taxAmount",et_tax_amount.getText().toString());

                }
                //tipCustom 小费类型
                //true : 商户进行小费传入
                //false : 用户机器上选择小费
                if(!TextUtils.isEmpty(et_tip_amount.getText().toString())){
                    charges0.put("tipCustom",true);
                    charges0.put("tipAmount",et_tip_amount.getText().toString());
                }else {
                    charges0.put("tipCustom",false);
                }
                posSendMessage.setAdditionalCharges(charges0.toJSONString());
                posSendMessage.setDesc(et_remark.getText().toString());
                posSendMessage.setItems("[{\"itemId\":1001,\"name\":\"衣服\",\"description\":\"x125\",\"quantity\":1,\"unitPrice\":1},{\"itemId\":1001,\"name\":\"裤子\",\"description\":\"x125\",\"quantity\":2,\"unitPrice\":1}]");
                posSendMessage.setNote("pay=1215284515");
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());
                sendMsg(posSendMessage);
                break;
            case R.id.qrcode_collection_btn://二维码收款
                posSendMessage.setType("transaction");
                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    posSendMessage.setAmount(Integer.parseInt(et_amount.getText().toString()));
                }
                posSendMessage.setCurrency("USD");
                posSendMessage.setTitle("大厅1001桌");
                posSendMessage.setDesc(et_remark.getText().toString());
                posSendMessage.setOrderNo(randomOrderNo());
                //"{\"taxAmount\":1,\"tipCustom\":false,\"tipAmount\":0}"
                JSONObject charges=new JSONObject();
                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    charges.put("taxAmount",et_tax_amount.getText().toString());

                }
                //tipCustom 小费类型
                //true : 商户进行小费传入
                //false : 用户机器上选择小费
                if(!TextUtils.isEmpty(et_tip_amount.getText().toString())){
                    charges.put("tipCustom",true);
                    charges.put("tipAmount",et_tip_amount.getText().toString());
                }else {
                    charges.put("tipCustom",false);
                }

                posSendMessage.setItems("[{\"itemId\":1001,\"name\":\"衣服\",\"description\":\"x125\",\"quantity\":2,\"unitPrice\":1},{\"itemId\":1001,\"name\":\"裤子\",\"description\":\"x125\",\"quantity\":2,\"unitPrice\":1}]");
                posSendMessage.setAdditionalCharges(charges.toJSONString());
                posSendMessage.setPaymentScenario("QR_CODE_PAYMENT");
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());
                sendMsg(posSendMessage);

                break;
            case R.id.qrcode_scan_btn://扫码收款

                posSendMessage.setType("transaction");
                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    posSendMessage.setAmount(Integer.parseInt(et_amount.getText().toString()));
                }
                posSendMessage.setCurrency("USD");
                posSendMessage.setTitle("大厅1001桌");
                posSendMessage.setDesc(et_remark.getText().toString());
                posSendMessage.setOrderNo(randomOrderNo());
                posSendMessage.setPaymentScenario("SCAN_CODE_PAYMENT");
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());

                //"{\"taxAmount\":1,\"tipCustom\":false,\"tipAmount\":0}"
                JSONObject charge=new JSONObject();
                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    charge.put("taxAmount",et_tax_amount.getText().toString());

                }
                //tipCustom 小费类型
                //true : 商户进行小费传入
                //false : 用户机器上选择小费
                if(!TextUtils.isEmpty(et_tip_amount.getText().toString())){
                    charge.put("tipCustom",true);
                    charge.put("tipAmount",et_tip_amount.getText().toString());
                }else {
                    charge.put("tipCustom",false);
                }
                posSendMessage.setAdditionalCharges(charge.toJSONString());
                sendMsg(posSendMessage);

                break;
            case R.id.checkout_counter_btn://收银台
                posSendMessage.setType("transaction");
                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    posSendMessage.setAmount(Integer.parseInt(et_amount.getText().toString()));
                }
                posSendMessage.setCurrency("USD");
                posSendMessage.setTitle("大厅1001桌");
                posSendMessage.setDesc(et_remark.getText().toString());
                JSONObject charges2=new JSONObject();
                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    charges2.put("taxAmount",et_tax_amount.getText().toString());

                }
                //tipCustom 小费类型
                //true : 商户进行小费传入
                //false : 用户机器上选择小费
                if(!TextUtils.isEmpty(et_tip_amount.getText().toString())){
                    charges2.put("tipCustom",true);
                    charges2.put("tipAmount",et_tip_amount.getText().toString());
                }else {
                    charges2.put("tipCustom",false);
                }
                posSendMessage.setAdditionalCharges(charges2.toJSONString());
                posSendMessage.setOrderNo(randomOrderNo());
                posSendMessage.setPaymentScenario("HANTE_CASHIER");
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());
                sendMsg(posSendMessage);
                break;
            case R.id.checkout_cancel_btn://取消
                posSendMessage.setType("transactionCancel");
                posSendMessage.setOrderNo(reconnection_order_et.getText().toString());
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());
                sendMsg(posSendMessage);
                break;
            case R.id.upload_order_btn://上报订单
                break;
            case R.id.refresh_order_btn:
                break;
            case R.id.cancel_order_btn://cancelorder
                posSendMessage.setType("cancelorder");
                posSendMessage.setOrderNo(reconnection_order_et.getText().toString());
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());
                sendMsg(posSendMessage);
                break;

            case R.id.refund_btn://退款
                POSRefund tcpRefundMessage=new POSRefund();
                tcpRefundMessage.setIp(IPUtil.getInNetIp(BluetoothActivity.this));
                tcpRefundMessage.setType("refund");
                tcpRefundMessage.setVersion("V2");
                tcpRefundMessage.setMerchantNo(merchant_et.getText().toString());
                tcpRefundMessage.setDeviceId(device_id_et.getText().toString());
                if(!TextUtils.isEmpty(refund_et.getText().toString())){
                    tcpRefundMessage.setAmount(Integer.parseInt(refund_et.getText().toString()));
                }

                if(!TextUtils.isEmpty(order_tr_et.getText().toString())){
                    tcpRefundMessage.setTransactionId(order_tr_et.getText().toString());
                }
                if(!TextUtils.isEmpty(reconnection_order_et.getText().toString())){
                    tcpRefundMessage.setOrderNo(reconnection_order_et.getText().toString());
                }
                sendMsg(tcpRefundMessage);
                break;
            case R.id.capture_btn://capture
                POSCapture tcpCaptureMessage=new POSCapture();
                tcpCaptureMessage.setIp(IPUtil.getInNetIp(BluetoothActivity.this));
                tcpCaptureMessage.setType("capture");
                tcpCaptureMessage.setVersion("V2");
                tcpCaptureMessage.setMerchantNo(merchant_et.getText().toString());
                tcpCaptureMessage.setDeviceId(device_id_et.getText().toString());
                if(!TextUtils.isEmpty(capture_et.getText().toString())){
                    tcpCaptureMessage.setAmount(Integer.parseInt(capture_et.getText().toString()));
                }
                if(!TextUtils.isEmpty(capture_tip_et.getText().toString())){
                    tcpCaptureMessage.setTipAmount(Integer.parseInt(capture_tip_et.getText().toString()));
                }
                if(!TextUtils.isEmpty(order_tr_et.getText().toString())){
                    tcpCaptureMessage.setTransactionId(order_tr_et.getText().toString());
                }
                if(!TextUtils.isEmpty(reconnection_order_et.getText().toString())){
                    tcpCaptureMessage.setOrderNo(reconnection_order_et.getText().toString());
                }
                sendMsg(tcpCaptureMessage);
                break;

            case R.id.void_btn:
                POSVoid tcpVoidMessage=new POSVoid();
                tcpVoidMessage.setIp(IPUtil.getInNetIp(BluetoothActivity.this));
                tcpVoidMessage.setType("void");
                tcpVoidMessage.setVersion("V2");
                tcpVoidMessage.setMerchantNo(merchant_et.getText().toString());
                tcpVoidMessage.setDeviceId(device_id_et.getText().toString());

                if(!TextUtils.isEmpty(order_tr_et.getText().toString())){
                    tcpVoidMessage.setTransactionId(order_tr_et.getText().toString());
                }

                if(!TextUtils.isEmpty(reconnection_order_et.getText().toString())){
                    tcpVoidMessage.setOrderNo(reconnection_order_et.getText().toString());
                }
                sendMsg(tcpVoidMessage);

                break;
            case R.id.query_order_btn:
                POSOrderQuery orderqueryMessage=new POSOrderQuery();
                orderqueryMessage.setIp(IPUtil.getInNetIp(BluetoothActivity.this));
                orderqueryMessage.setVersion("V2");
                orderqueryMessage.setType("orderquery");
                orderqueryMessage.setMerchantNo(merchant_et.getText().toString());
                orderqueryMessage.setDeviceId(device_id_et.getText().toString());
                if(!TextUtils.isEmpty(reconnection_order_et.getText().toString())){
                    orderqueryMessage.setOrderNo(reconnection_order_et.getText().toString());
                }

                if(!TextUtils.isEmpty(order_tr_et.getText().toString())){
                    orderqueryMessage.setTransactionId(order_tr_et.getText().toString());
                }

                sendMsg(orderqueryMessage);
                break;
            case R.id.checkout_ebt_btn:
                posSendMessage.setType("transaction");
                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    posSendMessage.setAmount(Integer.parseInt(et_amount.getText().toString()));
                }
                posSendMessage.setCurrency("USD");
                posSendMessage.setTitle("大厅1001桌");
                posSendMessage.setDesc(et_remark.getText().toString());
                posSendMessage.setOrderNo(randomOrderNo());
                posSendMessage.setPaymentScenario("POS_PAYMENT");
                posSendMessage.setTransType("EBT");
                //"{\"taxAmount\":1,\"tipCustom\":false,\"tipAmount\":0}"
                JSONObject charges00=new JSONObject();
                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    charges00.put("taxAmount",et_tax_amount.getText().toString());

                }
                //tipCustom 小费类型
                //true : 商户进行小费传入
                //false : 用户机器上选择小费
                if(!TextUtils.isEmpty(et_tip_amount.getText().toString())){
                    charges00.put("tipCustom",true);
                    charges00.put("tipAmount",et_tip_amount.getText().toString());
                }else {
                    charges00.put("tipCustom",false);
                }
                posSendMessage.setAdditionalCharges(charges00.toJSONString());
                posSendMessage.setDesc(et_remark.getText().toString());
                posSendMessage.setItems("[{\"itemId\":1001,\"name\":\"衣服\",\"description\":\"x125\",\"quantity\":1,\"unitPrice\":1},{\"itemId\":1001,\"name\":\"裤子\",\"description\":\"x125\",\"quantity\":2,\"unitPrice\":1}]");
                posSendMessage.setNote("pay=1215284515");
                posSendMessage.setMerchantNo(merchant_et.getText().toString());
                posSendMessage.setDeviceId(device_id_et.getText().toString());
                sendMsg(posSendMessage);
                break;

        }
    }

    private void sendMsg(POSBase msg) {
        //签名
        JSONObject json = JSON.parseObject(msg.toString());
        SortedMap<String, Object> signMap = new TreeMap();
        Iterator var4 = json.entrySet().iterator();
        while(var4.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var4.next();
            if (!"signature".equals(entry.getKey())
                    && entry.getValue() != null
                    && entry.getValue().toString().length() != 0) {
                signMap.put(entry.getKey(), entry.getValue());
            }
        }
        try {
            msg.setSignature(EncryptUtils.GetObjectSign(signMap, device_key_et.getText().toString()));
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        param_tv.setText("入参:" + msg.toString());
        result_tv.setText("响应:");
        HantePayApplication.application.bluetoothClient.sendMsg(JSON.toJSONString(msg));
    }




    private String randomOrderNo(){
        String orderNo="T"+System.currentTimeMillis();
        reconnection_order_et.setText(orderNo);
        return orderNo ;
    }

    /**
     * 接受事件  刷新页面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateMsgEvent event) {
        result_tv.setText("响应:"+event.msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}