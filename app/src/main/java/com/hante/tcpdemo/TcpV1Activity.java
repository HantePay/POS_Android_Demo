package com.hante.tcpdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hante.tcp.bean.SendMessage;
import com.hante.tcp.callback.SocketCallback;
import com.hante.tcp.util.HantePOSAPI;
import com.hante.tcpdemo.bean.Constant;
import com.hante.tcpdemo.dialog.PaymentTipDialog;
import com.hante.tcpdemo.dialog.TipDialog;
import com.hante.tcpdemo.net.BaseResponse;
import com.hante.tcpdemo.net.PosInfoResponse;
import com.hante.tcpdemo.net.RetrofitFactory;
import com.hante.tcpdemo.net.SimpleObserver;
import com.hante.tcpdemo.utils.SpUtils;
import com.hjq.toast.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * POS tcp测试
 */
public class TcpV1Activity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG="POS_V1";

    public static final String DEVICE_ID="v1_device_id";
    //设备token
    public static final String DEVICE_TOKEN="v1_device_token";


    CommonTitleBar title_bar;

    private EditText ip_et;
    private TextView param_tv;
    private TextView result_tv;
    private EditText merchant_et;

    private EditText reconnection_time_et;

    private EditText reconnection_order_et;

    private TextView status_tv;

    private EditText transaction_order_et;


    private EditText device_sn_et;

    private EditText token_verify_code_et;


    private EditText et_amount;

    private EditText et_tax_amount;

    private EditText et_tip_amount;

    private EditText refund_et;


    private EditText capture_et;


    private EditText capture_tip_et;


    /**
     * pos 创建 服务连接设备
     * 操作见：商米机POS功能配置指引.docx文档
     */
    private EditText device_id_et;
    private EditText device_key_et;


    CustomDialog customDialog;

    PaymentTipDialog paymentTipDialog;

    TipDialog tipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title_bar=findViewById(R.id.act_tcp_v2_title_bar);
        ip_et = findViewById(R.id.ip_et);
        param_tv = findViewById(R.id.param_tv);
        result_tv = findViewById(R.id.result_tv);
        merchant_et = findViewById(R.id.merchant_et);
        status_tv=findViewById(R.id.status_tv);
        reconnection_time_et=findViewById(R.id.reconnection_time_et);

        et_amount=findViewById(R.id.et_amount);
        et_tax_amount=findViewById(R.id.et_tax_amount);

        et_tip_amount=findViewById(R.id.et_tip_amount);

        reconnection_order_et=findViewById(R.id.reconnection_order_et);
        transaction_order_et=findViewById(R.id.transaction_order_et);

        token_verify_code_et=findViewById(R.id.token_verify_code_et);

        device_id_et=findViewById(R.id.device_id_et);
        device_key_et=findViewById(R.id.device_key_et);


        device_sn_et=findViewById(R.id.device_sn_et);

        refund_et=findViewById(R.id.refund_et);

        capture_et=findViewById(R.id.capture_et);

        capture_tip_et=findViewById(R.id.capture_tip_et);

        title_bar.setListener(new CommonTitleBar.OnTitleBarListener() {

            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_BUTTON:
                        finish();
                        break;
                }
            }
        });

        tipDialog=new TipDialog(this).builder().setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        findViewById(R.id.connect_btn).setOnClickListener(this);
        findViewById(R.id.checkstand_btn).setOnClickListener(this);
        findViewById(R.id.qrcode_collection_btn).setOnClickListener(this);
        findViewById(R.id.qrcode_scan_btn).setOnClickListener(this);
        findViewById(R.id.checkout_counter_btn).setOnClickListener(this);
        findViewById(R.id.stop_connect_btn).setOnClickListener(this);

        findViewById(R.id.reconnection_time_btn).setOnClickListener(this);
        findViewById(R.id.checkout_cancel_btn).setOnClickListener(this);
        findViewById(R.id.checkout_ebt_btn).setOnClickListener(this);

//        findViewById(R.id.jump_to_cloud).setOnClickListener(this);


        findViewById(R.id.checkout_create_token_btn).setOnClickListener(this);
        findViewById(R.id.close_server_btn).setOnClickListener(this);

        findViewById(R.id.tip_server_btn).setOnClickListener(this);

        findViewById(R.id.tip_close_server_btn).setOnClickListener(this);

        findViewById(R.id.transaction_countdown_btn).setOnClickListener(this);

        findViewById(R.id.credit_card_sign_in_btn).setOnClickListener(this);

        findViewById(R.id.transaction_voice_btn).setOnClickListener(this);

        findViewById(R.id.query_order_btn).setOnClickListener(this);

        findViewById(R.id.checkstand_bath_btn).setOnClickListener(this);


        findViewById(R.id.device_search_btn).setOnClickListener(this);

        findViewById(R.id.merchant_search_token_btn).setOnClickListener(this);


        findViewById(R.id.refund_btn).setOnClickListener(this);

        findViewById(R.id.check_sale_btn).setOnClickListener(this);


        findViewById(R.id.void_btn).setOnClickListener(this);


        findViewById(R.id.capture_btn).setOnClickListener(this);


        device_id_et.setText(SpUtils.getInstance().getString(DEVICE_ID,""));
        device_key_et.setText(SpUtils.getInstance().getString(DEVICE_TOKEN,""));
        device_sn_et.setText(SpUtils.getInstance().getString(Constant.CONFIG_DEVICE_SN,""));


       customDialog=new CustomDialog(this);
       refreshPosIp(SpUtils.getInstance().getString(Constant.CONFIG_DEVICE_SN,""));
    }

    @Override
    public void onClick(View view) {
        SendMessage posSendMessage = new SendMessage();
        int amount=0;
        switch (view.getId()) {
            case R.id.connect_btn://连接socket
                //检查配置
                if(TextUtils.isEmpty(ip_et.getText().toString())){
                    ToastUtils.show("请输入POS服务 IP 地址");
                    return;
                }
                if(TextUtils.isEmpty(merchant_et.getText().toString())){
                    ToastUtils.show("请输入商户号");
                    return;
                }

                if(TextUtils.isEmpty(device_id_et.getText().toString())){
                    ToastUtils.show("请输入POS服务 DeviceId");
                    return;
                }

                if(TextUtils.isEmpty(device_key_et.getText().toString())){
                    ToastUtils.show("请输入POS服务 Token");
                    return;
                }


                if (!HantePOSAPI.isConnected()) {
                    customDialog.show();
                    customDialog.setProgressText("连接Socket");
                    HantePOSAPI.setReconnectionTime(180);
                    HantePOSAPI.connectPOSService(TcpV1Activity.this, ip_et.getText().toString(), device_id_et.getText().toString(), device_key_et.getText().toString(),merchant_et.getText().toString(), new SocketCallback() {
                        @Override
                        public void connected() {
                            setStatus("连接成功");
                            SpUtils.getInstance().save(DEVICE_ID,device_id_et.getText().toString());
                            SpUtils.getInstance().save(DEVICE_TOKEN,device_key_et.getText().toString());
                            refreshTransBtn(true);
                        }

                        @Override
                        public void connectionFails(String s) {
                            Log.e(TAG,"连接失败:"+s);
                            setStatus(s);
                            refreshTransBtn(false);
                        }

                        @Override
                        public void error(int code,String s) {
                            setStatus("异常:"+s);

                        }

                        @Override
                        public void heartbeat(String s) {
                            setStatus("Successful connection");
//                            runOnUiThread(()->{
//                                result_tv.setText("心跳包:"+s);
//                            });
                        }

                        @Override
                        public void disConnected() {
                            setStatus("断开连接");
                            refreshTransBtn(false);
                        }

                        @Override
                        public void reconnection() {
                            setStatus("重新连接");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TcpV1Activity.this,"重新连接",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void receiveMessage(int length,String msg) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    result_tv.setText("响应:" + msg);
                                    SendMessage orderBean = JSON.parseObject(msg, SendMessage.class);
                                    if("tableListRequest".equals(orderBean.type)){
                                        List<SendMessage> tableBeans = new ArrayList<SendMessage>();

                                        for (int i = 1 ; i <= 1 ; i ++){
                                            SendMessage tableBean = new SendMessage();
                                            tableBean.amount = 1;
                                            tableBean.orderNo = "0008";
                                            tableBean.des = "test";
                                            tableBeans.add(tableBean);
                                        }
                                        orderBean.deviceSN = null;
//                                    orderBean.tableListStr = "[{\"amount\":\"943\",\"name\":\"T8\",\"orderNo\":\"0008\"}]";
                                        orderBean.tableListStr = tableBeans.toString();
                                        orderBean.deviceId=device_id_et.getText().toString();
                                        orderBean.merchantNo = merchant_et.getText().toString();
                                        orderBean.des = "test";
                                        orderBean.type = "tableListResponse";
                                        sendMsg(orderBean);
                                    }else if("devicePair".equals(orderBean.type)){
                                        try {
                                            JSONObject token=JSONObject.parseObject(msg);
                                            if(null!=token){
                                                String deviceId=token.getString("deviceId");
                                                String tok=token.getString("token");
                                                if(!TextUtils.isEmpty(deviceId)){
                                                    if(!deviceId.equals(device_id_et.getText().toString())){
                                                        device_id_et.setText(deviceId);
                                                        device_key_et.setText(tok);
                                                        //关闭连接，重新连接
                                                        HantePOSAPI.refreshToken(deviceId,tok,merchant_et.getText().toString());
                                                    }
                                                }

                                            }
                                        }catch (RuntimeException e){
                                            e.printStackTrace();
                                        }
                                    }
//                                    else if("searchToken".equals(orderBean.type)){
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        JSONArray tokenList= jsonObject.getJSONArray("tokenList");
//                                        if(null!=tokenList && !tokenList.isEmpty()){
//                                            JSONObject token=tokenList.getJSONObject(0);
//                                            if(null!=token){
//                                                String deviceId=token.getString("deviceId");
//                                                String tok=token.getString("token");
//                                                if(!TextUtils.isEmpty(deviceId)){
//                                                    if(!deviceId.equals(device_id_et.getText().toString())){
//                                                        device_id_et.setText(deviceId);
//                                                        device_key_et.setText(tok);
//                                                        //关闭连接，重新连接
//                                                        HantePOSAPI.refreshToken(deviceId,tok);
//                                                    }
//                                                }
//
//                                            }
//                                        }
//                                    }else if("transaction".equals(orderBean.type)){
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        if(null!=jsonObject){
//                                            String resultCode=jsonObject.getString("resultCode");
//                                            String resultMsg=jsonObject.getString("resultMsg");
//                                            String paymentMethod=jsonObject.getString("paymentMethod");
//                                            if("PENDING".equals(resultCode)){
//                                                if(null!=paymentTipDialog && paymentTipDialog.isShowing()){
//                                                    paymentTipDialog.refreshMsg(resultMsg);
//                                                }
//                                            }else if("SUCCESS".equals(resultCode)){
//                                                paymentTipDialog.setPositiveButton(null);
//                                                paymentTipDialog.refreshSuccess(paymentMethod);
//                                            }else if("FAIL".equals(resultCode)){
//                                                paymentTipDialog.setPositiveButton(null);
//                                                paymentTipDialog.refreshFail(resultMsg);
//                                            }
//                                        }
//                                    }else if("search".equals(orderBean.type)){
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        if(null!=jsonObject){
//                                            String resultCode=jsonObject.getString("resultCode");
//                                            String returnCode=jsonObject.getString("returnCode");
//                                            String resultMsg=jsonObject.getString("resultMsg");
//                                            if("SUCCESS".equals(resultCode)){
//                                                double amount=0;
//                                                String totalAmount=jsonObject.getString("totalAmount");
//                                                String transactionId=jsonObject.getString("transactionId");
//                                                String paytime=jsonObject.getString("paytime");
//                                                String payStatus=jsonObject.getString("paystatus");
//                                                String paymentMethod=jsonObject.getString("paymentMethod");
//
//
//                                                if(!TextUtils.isEmpty(totalAmount)){
//                                                    amount+=Double.parseDouble(totalAmount)*0.01;
//                                                }
//                                                new OrderInfoDialog(TcpV1Activity.this).builder().setPositiveButton(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//
//                                                    }
//                                                }).show(paymentMethod,
//                                                        "$"+amount,transactionId,paytime,payStatus);
//                                            }else{
//                                                if(tipDialog.isShowing()){
//                                                    tipDialog.dismiss();
//                                                }
//                                                tipDialog.show(resultCode+":"+resultMsg);
//                                            }
//                                        }
//
//                                    }else  if("refund".equals(orderBean.type)){
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        if(null!=jsonObject){
//                                            String resultCode=jsonObject.getString("resultCode");
//                                            String resultMsg=jsonObject.getString("resultMsg");
//                                            String paymentMethod=jsonObject.getString("paymentMethod");
//                                            paymentTipDialog.setPositiveButton(null);
//                                             if("SUCCESS".equals(resultCode)){
//                                                paymentTipDialog.refreshSuccess(paymentMethod);
//                                            }else if("FAIL".equals(resultCode)){
//                                                paymentTipDialog.refreshFail(resultMsg);
//                                            }
//                                        }
//                                    }else  if("capture".equals(orderBean.type)){
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        if(null!=jsonObject){
//                                            paymentTipDialog.setPositiveButton(null);
//                                            String resultCode=jsonObject.getString("resultCode");
//                                            String resultMsg=jsonObject.getString("resultMsg");
//                                            String paymentMethod=jsonObject.getString("paymentMethod");
//                                            if("SUCCESS".equals(resultCode)){
//                                                paymentTipDialog.refreshSuccess(paymentMethod);
//                                            }else if("FAIL".equals(resultCode)){
//                                                paymentTipDialog.refreshFail(resultMsg);
//                                            }
//                                        }
//                                    }else  if("void".equals(orderBean.type)){
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        if(null!=jsonObject){
//                                            paymentTipDialog.setPositiveButton(null);
//                                            String resultCode=jsonObject.getString("resultCode");
//                                            String resultMsg=jsonObject.getString("resultMsg");
//                                            String paymentMethod=jsonObject.getString("paymentMethod");
//                                           if("SUCCESS".equals(resultCode)){
//                                                paymentTipDialog.refreshSuccess(paymentMethod);
//                                            }else if("FAIL".equals(resultCode)){
//                                                paymentTipDialog.refreshFail(resultMsg);
//                                            }
//                                        }
//                                    }else{
//                                        JSONObject jsonObject=JSONObject.parseObject(msg);
//                                        String resultCode=jsonObject.getString("resultCode");
//                                        String resultMsg=jsonObject.getString("resultMsg");
//                                        if("SUCCESS".equals(resultCode)){
//
//                                        }else{
//                                            if(null!=resultCode){
//                                                if(tipDialog.isShowing()){
//                                                    tipDialog.dismiss();
//                                                }
//                                                tipDialog.show(resultCode+":"+resultMsg);
//                                            }
//                                        }
//                                    }
                                }
                            });

                        }
                    });
                }

                break;
            case R.id.stop_connect_btn:
                    HantePOSAPI.stopPOSConnect();
                break;
            case R.id.reconnection_time_btn:
                    if(TextUtils.isEmpty(reconnection_time_et.getText().toString())){
                        Toast.makeText(this,"请输入断开重连时长(单位:秒)",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int time=Integer.parseInt(reconnection_time_et.getText().toString());
                    HantePOSAPI.setReconnectionTime(time);
                break;
            case R.id.checkstand_btn://信用卡
                posSendMessage.type = "transaction";
                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    amount +=Integer.parseInt(et_amount.getText().toString());
                }

                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    amount +=Integer.parseInt(et_tax_amount.getText().toString());
                }
                posSendMessage.amount =amount;
                posSendMessage.orderNo = randomOrderNo();
                posSendMessage.payment = "creditcard";
                posSendMessage.transType = "Auth";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.check_sale_btn:
                posSendMessage.type = "transaction";

                if(!TextUtils.isEmpty(et_amount.getText().toString())){
                    amount +=Integer.parseInt(et_amount.getText().toString());
                }

                if(!TextUtils.isEmpty(et_tax_amount.getText().toString())){
                    amount +=Integer.parseInt(et_tax_amount.getText().toString());
                }

                if(!TextUtils.isEmpty(et_tip_amount.getText().toString())){
                    amount +=Integer.parseInt(et_tip_amount.getText().toString());
                }


                posSendMessage.amount =amount;
                posSendMessage.orderNo = randomOrderNo();
                posSendMessage.payment = "creditcard";
                posSendMessage.transType = "Sale";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.checkstand_bath_btn://信用卡批量测试
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
                            for(int i=0;i<5;i++){
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                posSendMessage.type = "transaction";
                                posSendMessage.amount = 1;
                                posSendMessage.orderNo = randomOrderNo();
                                posSendMessage.payment = "creditcard";
                                posSendMessage.transType = "Auth";
                                posSendMessage.merchantNo = merchant_et.getText().toString();
                                posSendMessage.deviceId = device_id_et.getText().toString();
                                sendMsg(posSendMessage);
                            }
//                        }
//                    }).start();

                break;
            case R.id.qrcode_collection_btn://二维码收款
                posSendMessage.type = "transaction";
                posSendMessage.amount = 1;
                posSendMessage.taxAmount = 0;
                posSendMessage.payment = "qrcode";
                posSendMessage.orderNo = randomOrderNo();
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);

                break;
            case R.id.qrcode_scan_btn://扫码收款
                posSendMessage.type = "transaction";
                posSendMessage.amount = 1;
                posSendMessage.taxAmount = 0;
                posSendMessage.payment = "scancode";
                posSendMessage.orderNo = randomOrderNo();
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.checkout_counter_btn://收银台
                posSendMessage.type = "transaction";
                posSendMessage.amount = 1;
                posSendMessage.taxAmount = 0;
                posSendMessage.payment = "custom";
                posSendMessage.orderNo = randomOrderNo();
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.checkout_cancel_btn://取消
                posSendMessage.type = "transactionCancel";
                posSendMessage.orderNo = reconnection_order_et.getText().toString();
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.checkout_ebt_btn:
                posSendMessage.type = "transaction";
                posSendMessage.amount = 1;
                posSendMessage.orderNo = randomOrderNo();
                posSendMessage.payment = "creditcard";
                posSendMessage.transType = "EBT";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.checkout_create_token_btn:
                if(!TextUtils.isEmpty(token_verify_code_et.getText().toString())){
                    posSendMessage.type="devicePair";
                    posSendMessage.verifyCode=token_verify_code_et.getText().toString();
                    posSendMessage.merchantNo=merchant_et.getText().toString();
                    sendMsg(posSendMessage);
                }else {
                    ToastUtils.show("请输入 verify code");
                }

                break;
            case R.id.close_server_btn:
                posSendMessage.type = "closeService";
                sendMsg(posSendMessage);
                break;
            case R.id.tip_server_btn:
                    posSendMessage.type = "tipMessage";
                    posSendMessage.merchantNo=merchant_et.getText().toString();
                    posSendMessage.deviceId = device_id_et.getText().toString();
                    posSendMessage.text="please wait ...";
                    posSendMessage.imgUrl="https://tupian.qqw21.com/article/UploadPic/2023-1/20231722375544866.jpg";
                    sendMsg(posSendMessage);
                break;
            case R.id.tip_close_server_btn:
                posSendMessage.type = "closeTipMessage";
                posSendMessage.merchantNo=merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.transaction_countdown_btn://交易倒计时
                posSendMessage.type = "transactionCountdown";
                posSendMessage.merchantNo=merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                //1开启 0 关闭
                posSendMessage.opened="1";
                posSendMessage.time=60;
                sendMsg(posSendMessage);
                break;
            case R.id.credit_card_sign_in_btn://签到
                posSendMessage.type = "creditCardSignIn";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                //密码
                posSendMessage.password="Hante2023!!";
                sendMsg(posSendMessage);
                break;
            case R.id.transaction_voice_btn://交易语音播报
                posSendMessage.type = "transactionVoice";
                posSendMessage.opened="1";
                sendMsg(posSendMessage);
                break;
            case R.id.query_order_btn://查询订单

                    posSendMessage.type = "search";
                    posSendMessage.merchantNo = merchant_et.getText().toString();
                    posSendMessage.deviceId = device_id_et.getText().toString();
                    if(!TextUtils.isEmpty(reconnection_order_et.getText())){
                        posSendMessage.orderNo=reconnection_order_et.getText().toString();
                    }
                    if(!TextUtils.isEmpty(transaction_order_et.getText())){
                        posSendMessage.transactionId=transaction_order_et.getText().toString();
                    }
                    sendMsg(posSendMessage);
                break;
            case R.id.device_search_btn:
                if(!TextUtils.isEmpty(device_sn_et.getText().toString())){
                    refreshPosIp(device_sn_et.getText().toString());
                }else{
                    ToastUtils.show("请输入 Device SN");
                }
                break;
            case R.id.merchant_search_token_btn:
                posSendMessage.type="searchToken";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                sendMsg(posSendMessage);
                break;
            case R.id.refund_btn:
                posSendMessage.type="refund";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();
                if(!TextUtils.isEmpty(refund_et.getText().toString())){
                    posSendMessage.amount=Integer.parseInt(refund_et.getText().toString());
                }

                if(!TextUtils.isEmpty(transaction_order_et.getText().toString())){
                    posSendMessage.transactionId=transaction_order_et.getText().toString();
                }
                if(!TextUtils.isEmpty(reconnection_order_et.getText().toString())){
                    posSendMessage.orderNo=reconnection_order_et.getText().toString();
                }
                sendMsg(posSendMessage);
                break;
            case R.id.void_btn:
                posSendMessage.type="void";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();

                //交易流水号
                if(!TextUtils.isEmpty(transaction_order_et.getText().toString())){
                    posSendMessage.transactionId=(transaction_order_et.getText().toString());
                }

                sendMsg(posSendMessage);

                break;
            case R.id.capture_btn:

                posSendMessage.type="capture";
                posSendMessage.merchantNo = merchant_et.getText().toString();
                posSendMessage.deviceId = device_id_et.getText().toString();

                if(!TextUtils.isEmpty(capture_et.getText().toString())){
                    posSendMessage.amount=Integer.parseInt(capture_et.getText().toString());
                }

                if(!TextUtils.isEmpty(capture_tip_et.getText().toString())){
                    posSendMessage.tipAmount=Integer.parseInt(capture_tip_et.getText().toString());
                }

                //交易流水号
                if(!TextUtils.isEmpty(transaction_order_et.getText().toString())){
                    posSendMessage.transactionId=(transaction_order_et.getText().toString());
                }
                sendMsg(posSendMessage);

                break;

        }
    }


    private void sendMsg(SendMessage msg) {
        Log.e("sendMsg",msg.toString());
        if(HantePOSAPI.isConnected()){
            param_tv.setText("Request:" + msg.toString());
            result_tv.setText("Response:");
            HantePOSAPI.sentMessageV1(msg);
        }else {
            Toast.makeText(this,"Please connect to POS first",Toast.LENGTH_SHORT).show();
        }
    }



    private void setStatus(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(customDialog.isShowing()){
                    customDialog.dismiss();
                }
                status_tv.setText(msg);
            }
        });
    }

    private String randomOrderNo(){
        String orderNo="T"+System.currentTimeMillis();
        reconnection_order_et.setText(orderNo);
        return orderNo ;
    }

    private void refreshPosIp(String sn){
        if(!TextUtils.isEmpty(sn)){
            RetrofitFactory.getInstance().queryPosIP(sn)
                    .subscribe(new SimpleObserver<BaseResponse<PosInfoResponse>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            super.onSubscribe(d);
                        }


                        @Override
                        public void onNext(BaseResponse<PosInfoResponse> response) {
                            //请求成功
                            PosInfoResponse posInfo=response.getData();
                            if(null!=posInfo){
                                String url=posInfo.getConnectUrl();
                                if(!TextUtils.isEmpty(url)){
                                    String[] arr= url.split(":");
                                    if(arr.length==2){
                                        ip_et.setText(arr[0]);
                                    }
                                }
                                String merchantNo=posInfo.getUserNo();
                                SpUtils.getInstance().save(Constant.DATE_USER_NO,merchantNo);
                                merchant_et.setText(merchantNo);
                                SpUtils.getInstance().save(Constant.CONFIG_DEVICE_SN,sn);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
    }


    private void refreshTransBtn(boolean isConnected) {
        runOnUiThread(()->{
            findViewById(R.id.connect_btn).setEnabled(!isConnected);
            findViewById(R.id.stop_connect_btn).setEnabled(isConnected);
            findViewById(R.id.check_sale_btn).setEnabled(isConnected);
            findViewById(R.id.checkstand_btn).setEnabled(isConnected);
            findViewById(R.id.qrcode_collection_btn).setEnabled(isConnected);
            findViewById(R.id.qrcode_scan_btn).setEnabled(isConnected);
            findViewById(R.id.checkout_counter_btn).setEnabled(isConnected);
            findViewById(R.id.checkout_ebt_btn).setEnabled(isConnected);
            findViewById(R.id.checkout_cancel_btn).setEnabled(isConnected);
            findViewById(R.id.query_order_btn).setEnabled(isConnected);

            findViewById(R.id.void_btn).setEnabled(isConnected);
            findViewById(R.id.refund_btn).setEnabled(isConnected);
            findViewById(R.id.capture_btn).setEnabled(isConnected);
        });

    }

    @Override
    public void finish() {
        HantePOSAPI.stopPOSConnect();
        super.finish();
    }
}