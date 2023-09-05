package com.hante.tcpdemo.tcp;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hante.tcp.bean.v2.POSBase;
import com.hante.tcp.bean.v2.POSTransaction;
import com.hante.tcp.callback.SocketCallback;
import com.hante.tcp.util.HantePOSAPI;
import com.hante.tcpdemo.CustomDialog;
import com.hante.tcpdemo.R;
import com.hante.tcpdemo.bean.Constant;
import com.hante.tcpdemo.dialog.PairingCodeDialog;
import com.hante.tcpdemo.net.BaseCallBack;
import com.hante.tcpdemo.net.BaseResponse;
import com.hante.tcpdemo.net.PosInfoResponse;
import com.hante.tcpdemo.net.RetrofitFactory;
import com.hante.tcpdemo.net.SimpleObserver;
import com.hante.tcpdemo.utils.IPUtil;
import com.hante.tcpdemo.utils.SpUtils;
import com.hjq.toast.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class TcpSettingsActivity extends AppCompatActivity {


    CommonTitleBar act_tcp_settings_title_bar;

    EditText device_sn_et;
    TextView tv_pos_ip;
    ImageView tv_pos_ip_check_net_img;
    TextView tv_pos_connected_sn;


    EditText token_verify_code_et;
    TextView tv_token;
    TextView tv_pos_token_pair_code_tv;
    ImageView tv_pos_token_check_img;

    EditText reconnection_time_et;

    EditText credit_card_sign_in_et;
    TextView tv_credit_card_sign;
    ImageView credit_card_sign_img;


    TextView payment_notify_url_et;


    CustomDialog customDialog;

    String version="V1";
    String merchantNo="";
    String deviceId="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_settings);
        act_tcp_settings_title_bar=findViewById(R.id.act_tcp_settings_title_bar);

        device_sn_et=findViewById(R.id.device_sn_et);
        tv_pos_ip=findViewById(R.id.tv_pos_ip);


        tv_pos_connected_sn=findViewById(R.id.tv_pos_connected_sn);

        tv_pos_ip_check_net_img=findViewById(R.id.tv_pos_ip_check_net_img);


        token_verify_code_et=findViewById(R.id.token_verify_code_et);
        tv_token=findViewById(R.id.tv_token);
        tv_pos_token_check_img=findViewById(R.id.tv_pos_token_check_img);
        tv_pos_token_pair_code_tv=findViewById(R.id.tv_pos_token_pair_code_tv);


        reconnection_time_et=findViewById(R.id.reconnection_time_et);
        credit_card_sign_in_et=findViewById(R.id.credit_card_sign_in_et);
        tv_credit_card_sign=findViewById(R.id.tv_credit_card_sign);
        credit_card_sign_img=findViewById(R.id.credit_card_sign_img);

        payment_notify_url_et=findViewById(R.id.payment_notify_url_et);

        customDialog=new CustomDialog(this);
        HantePOSAPI.stopPOSConnect();
        version=getIntent().getStringExtra("version");
        if(TextUtils.isEmpty(version)){
            version="V1";
        }
        act_tcp_settings_title_bar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_BUTTON:
                        if(TextUtils.isEmpty(SpUtils.getInstance().getString(Constant.CONFIG_DEVICE_SN,""))){
                            ToastUtils.show("Please search Device");
                        }else{
                            finish();
                        }
                    break;
                }
            }
        });

        device_sn_et.setText(SpUtils.getInstance().getString(Constant.CONFIG_DEVICE_SN,""));

        token_verify_code_et.setText(SpUtils.getInstance().getString(Constant.CONFIG_TOKEN_VERIFY_CODE,""));

        reconnection_time_et.setText(SpUtils.getInstance().getString(Constant.CONFIG_HEART_BEAT_TIME,""));

        credit_card_sign_in_et.setText(SpUtils.getInstance().getString(Constant.CONFIG_CREDIT_CARD_SIGN_IN_PASSWORD,""));

        //保存SN
        findViewById(R.id.device_search_btn).setOnClickListener((View v)->{
            if(!TextUtils.isEmpty(device_sn_et.getText())){
                SpUtils.getInstance().save(Constant.CONFIG_DEVICE_SN,device_sn_et.getText().toString().trim().toUpperCase());
                refreshPosIp(SpUtils.getInstance().getString(Constant.CONFIG_DEVICE_SN,""));
            }else{
                ToastUtils.show("Please enter Device SN");
            }
        });

        //保存token 验证码
        findViewById(R.id.merchant_create_token_btn).setOnClickListener((View v)->{
//            POSTransaction posSendMessage = new POSTransaction();
//            posSendMessage.setVersion("V2");
//            posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
//            posSendMessage.setType("checkPOS");
//            sendMsg(posSendMessage);
//
//            if(!TextUtils.isEmpty(token_verify_code_et.getText())){
//                SpUtils.getInstance().save(Constant.CONFIG_TOKEN_VERIFY_CODE,token_verify_code_et.getText().toString().trim());
//                checkToken();
//            }else{
//                ToastUtils.show("Please enter token verify code");
//            }
            //查询token
            POSTransaction posSendMessage = new POSTransaction();
            posSendMessage.setVersion("V2");
            posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
            posSendMessage.setType("searchToken");
            posSendMessage.setMerchantNo(merchantNo);
            sendMsg(posSendMessage);
        });

        //保存心跳包时间
        findViewById(R.id.reconnection_time_btn).setOnClickListener((View v)->{
            if(!TextUtils.isEmpty(reconnection_time_et.getText())){
                SpUtils.getInstance().save(Constant.CONFIG_HEART_BEAT_TIME,reconnection_time_et.getText().toString().trim());
                ToastUtils.show("Save Success");
            }else{
                ToastUtils.show("Please enter Heartbeat time(second)");
            }
        });


        //保存TSYS签到密码
        findViewById(R.id.credit_card_sign_in_btn).setOnClickListener((View v)->{
            if(!TextUtils.isEmpty(credit_card_sign_in_et.getText())){
                SpUtils.getInstance().save(Constant.CONFIG_CREDIT_CARD_SIGN_IN_PASSWORD,credit_card_sign_in_et.getText().toString().trim());

                checkTsysSignIn();
            }else{
                ToastUtils.show("Please enter Credit Sign in Password");
            }
        });

        findViewById(R.id.merchant_reset_pair_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(merchantNo)){
                    POSTransaction posSendMessage = new POSTransaction();
                    posSendMessage.setVersion("V2");
                    posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
                    posSendMessage.setType("reSetPair");
                    posSendMessage.setMerchantNo(merchantNo);
                    posSendMessage.setDeviceId(deviceId);
                    sendMsg(posSendMessage);
                }else {
                    ToastUtils.show("please search device");
                }

            }
        });


        String ipData= SpUtils.getInstance().getString(Constant.DATE_POS_IP, "");
        if(!TextUtils.isEmpty(ipData)){
            try {
                PosInfoResponse posInfo= JSONObject.parseObject(ipData,PosInfoResponse.class);
                if(null!=posInfo){
                    StringBuilder posIP=new StringBuilder();
                    String url=posInfo.getConnectUrl();
                    if(!TextUtils.isEmpty(url)){
                        posIP.append("IP:").append(url);
                    }

                    merchantNo=posInfo.getUserNo();
                    if(!TextUtils.isEmpty(merchantNo)){
                        SpUtils.getInstance().save(Constant.DATE_USER_NO,merchantNo);
                        posIP.append(" ").append(merchantNo);
                    }
                    tv_pos_ip.setText(posIP.toString());
                }
            }catch (RuntimeException e){
                e.printStackTrace();
            }

        }

        String token=SpUtils.getInstance().getString(Constant.DATE_DEVICE_TOKEN,"");
        if(!TextUtils.isEmpty(token)){
            try {
                JSONObject jsonObject=JSONObject.parseObject(token);
                deviceId=jsonObject.getString("deviceId");
                String key=jsonObject.getString("token");
                if(!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(key)){
                    StringBuilder sb=new StringBuilder();
                    sb.append("deviceId:").append(deviceId).append("  token:").append(key);
                    tv_token.setText(sb.toString());
                    tv_pos_token_check_img.setBackgroundResource(R.drawable.self_check_success);
                    tv_pos_token_pair_code_tv.setVisibility(View.VISIBLE);
                }
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }

        boolean hasSign= SpUtils.getInstance().getBoolean(Constant.DATE_DEVICE_SIGN_IN,false);
        if(hasSign){
            credit_card_sign_img.setBackgroundResource(R.drawable.self_check_success);
        }

        payment_notify_url_et.setText("http://"+IPUtil.getInNetIp(TcpSettingsActivity.this)+":4000");

        findViewById(R.id.payment_notify_url_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
                POSTransaction posSendMessage = new POSTransaction();
                posSendMessage.setVersion("V2");
                posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
                posSendMessage.setType("paymentNotifyUrl");
                posSendMessage.setDeviceId(deviceId);
                posSendMessage.setMerchantNo(merchantNo);
                posSendMessage.setNotifyUrl(payment_notify_url_et.getText().toString());
                sendMsg(posSendMessage);
            }
        });

        refreshPosIp(SpUtils.getInstance().getString(Constant.CONFIG_DEVICE_SN,""));

    }

    private void refreshPosIp(String sn){
        if(!TextUtils.isEmpty(sn)){
            customDialog.show();
            RetrofitFactory.getInstance().queryPosIP(sn)
                    .subscribe(new SimpleObserver<BaseResponse<PosInfoResponse>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            super.onSubscribe(d);
                        }


                        @Override
                        public void onNext(@NonNull BaseResponse<PosInfoResponse> response) {
                            customDialog.dismiss();
                            //请求成功
                            if(response.isSuccess()){
                                PosInfoResponse posInfo=response.getData();
                                if(null!=posInfo){
                                    SpUtils.getInstance().save(Constant.DATE_POS_IP, JSONObject.toJSONString(posInfo));
                                    StringBuilder posIP=new StringBuilder();
                                    String url=posInfo.getConnectUrl();
                                    if(!TextUtils.isEmpty(url)){
                                        posIP.append("IP:").append(url);
                                    }

                                    merchantNo=posInfo.getUserNo();
                                    if(!TextUtils.isEmpty(merchantNo)){
                                        posIP.append("    ").append("MerchantNo:").append(merchantNo);
                                    }
                                    tv_pos_ip.setText(posIP.toString());

                                    checkIp();
                                }
                            }else {
                                tv_pos_ip.setText(response.getReturnMsg());
                            }

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                            customDialog.dismiss();
                        }
                    });
        }

    }


    private void checkIp(){
        String ip="";

        String ipData= SpUtils.getInstance().getString(Constant.DATE_POS_IP, "");
        if(!TextUtils.isEmpty(ipData)){
            try {
                PosInfoResponse posInfo= JSONObject.parseObject(ipData,PosInfoResponse.class);
                if(null!=posInfo){
                    String url=posInfo.getConnectUrl();
                    if(!TextUtils.isEmpty(url)){
                        String[] tempArr=url.split(":");
                        if(tempArr.length==2){
                            ip=tempArr[0];
                        }
                    }
                    merchantNo=posInfo.getUserNo();
                }
            }catch (RuntimeException e){
                e.printStackTrace();
            }

        }

        if(!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(merchantNo)){
            HantePOSAPI.connectPOSService(TcpSettingsActivity.this, ip, "", "",merchantNo, new SocketCallback() {
                @Override
                public void connected() {
                    runOnUiThread(()->{
                        tv_pos_ip_check_net_img.setBackgroundResource(R.drawable.self_check_success);
                        refreshTransBtn(true);
                        tv_pos_connected_sn.setText("Connected");
                        //查询token
                        POSTransaction posSendMessage = new POSTransaction();
                        posSendMessage.setVersion("V2");
                        posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
                        posSendMessage.setType("searchToken");
                        posSendMessage.setMerchantNo(merchantNo);
                        sendMsg(posSendMessage);
                        new Handler().postDelayed(()->{
                            //查询签到状态
                            POSTransaction signInStatus = new POSTransaction();
                            signInStatus.setVersion("V2");
                            signInStatus.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
                            signInStatus.setType("searchCreditCardSignInStatus");
                            signInStatus.setMerchantNo(merchantNo);
                            sendMsg(signInStatus);
                        },1000*5);

                    });


                }

                @Override
                public void connectionFails(String s) {
                    runOnUiThread(()->{
//                        tv_pos_ip_check_net_img.setBackgroundResource(R.drawable.self_check_faile);
                        refreshTransBtn(false);
                    });

                }

                @Override
                public void error(int i, String s) {

                }

                @Override
                public void heartbeat(String s) {

                }

                @Override
                public void disConnected() {

                }

                @Override
                public void reconnection() {

                }


                @Override
                public void receiveMessage(int length,String s) {
                    try{
                        JSONObject jsonObject=JSONObject.parseObject(s);
                        String type=jsonObject.getString("type");
                        if("devicePair".equals(type)){

                            JSONObject token=jsonObject.getJSONObject("token");
                            String resultCode=jsonObject.getString("resultCode");
                            String resultMsg=jsonObject.getString("resultMsg");
                            if(null!=token){
                                deviceId=token.getString("deviceId");
                                String key=token.getString("token");
                                if(!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(key)){
                                    SpUtils.getInstance().save(Constant.DATE_DEVICE_TOKEN,JSONObject.toJSONString(token));
                                    //关闭连接，重新连接
                                    HantePOSAPI.refreshToken(deviceId,key,merchantNo);
                                    runOnUiThread(()->{
                                        StringBuilder sb=new StringBuilder();
                                        sb.append("deviceId:").append(deviceId).append("  token:").append(key);
                                        tv_token.setText(sb.toString());
                                        tv_pos_token_check_img.setBackgroundResource(R.drawable.self_check_success);
                                    });

                                }else{
                                    runOnUiThread(()->{
                                        tv_token.setText(JSONObject.toJSONString(token));
                                    });
                                }
                            }else {
                                runOnUiThread(()->{
                                    tv_token.setText(resultCode+":"+resultMsg);
                                });

                            }

                        }else if("searchToken".equals(type)){
                            String resultCode=jsonObject.getString("resultCode");
                            String resultMsg=jsonObject.getString("resultMsg");

                            if("SUCCESS".equals(resultCode)){
                                JSONArray tokenList= jsonObject.getJSONArray("tokenList");
                                if(null!=tokenList && !tokenList.isEmpty()){
                                    JSONObject token=tokenList.getJSONObject(0);
                                    if(null!=token){
                                        SpUtils.getInstance().save(Constant.DATE_DEVICE_TOKEN,JSONObject.toJSONString(token));
                                        String deviceId=token.getString("deviceId");
                                        String tok=token.getString("token");
                                        if(!TextUtils.isEmpty(deviceId)){
                                            //刷新 device ID
                                            HantePOSAPI.refreshToken(deviceId,tok,merchantNo);
                                            runOnUiThread(()->{
                                                //String deviceSN=jsonObject.getString("deviceSN");
                                                tv_pos_token_pair_code_tv.setVisibility(View.VISIBLE);
//                                                tv_pos_connected_sn.setText("Connected");
                                                StringBuilder sb=new StringBuilder();
                                                sb.append("deviceId:").append(deviceId).append("  token:").append(tok);
                                                tv_token.setText(sb.toString());
                                                tv_pos_token_check_img.setBackgroundResource(R.drawable.self_check_success);
                                            });
                                        }

                                    }
                                }else{

                                    runOnUiThread(()->{
                                        new PairingCodeDialog(TcpSettingsActivity.this).builder().setPositiveButton(new BaseCallBack<String>() {
                                            @Override
                                            public void callSuccess(String code) {
                                                POSTransaction posSendMessage = new POSTransaction();
                                                posSendMessage.setVersion("V2");
                                                posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
                                                posSendMessage.setType("devicePair");
                                                posSendMessage.verifyCode=code;
                                                posSendMessage.setMerchantNo(merchantNo);
                                                sendMsg(posSendMessage);
                                            }

                                            @Override
                                            public void callFail(String code, String msg) {

                                            }
                                        }).show();
                                        tv_token.setText("");
                                        tv_pos_token_check_img.setBackgroundResource(R.drawable.self_check_faile);
                                    });
                                }
                            }else{
                                runOnUiThread(()->{
                                    new PairingCodeDialog(TcpSettingsActivity.this).builder().setPositiveButton(new BaseCallBack<String>() {
                                        @Override
                                        public void callSuccess(String code) {
                                            POSTransaction posSendMessage = new POSTransaction();
                                            posSendMessage.setVersion("V2");
                                            posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
                                            posSendMessage.setType("devicePair");
                                            posSendMessage.verifyCode=code;
                                            posSendMessage.setMerchantNo(merchantNo);
                                            sendMsg(posSendMessage);
                                        }

                                        @Override
                                        public void callFail(String code, String msg) {

                                        }
                                    }).show();
                                    tv_token.setText("");
                                    tv_pos_token_check_img.setBackgroundResource(R.drawable.self_check_faile);
                                });

                            }
                        }else if("creditCardSignIn".equals(type) || "searchCreditCardSignInStatus".equals(type)){
                            runOnUiThread(()->{
                                customDialog.dismiss();
                            });
                            String resultCode=jsonObject.getString("resultCode");
                            String resultMsg=jsonObject.getString("resultMsg");
                            if("SUCCESS".equals(resultCode)){
                                runOnUiThread(()->{
                                    SpUtils.getInstance().save(Constant.DATE_DEVICE_SIGN_IN,true);
                                    credit_card_sign_img.setBackgroundResource(R.drawable.self_check_success);
                                    tv_credit_card_sign.setText(resultMsg);
                                });
                            }
                            else {
                                runOnUiThread(()->{
                                    credit_card_sign_img.setBackgroundResource(R.drawable.self_check_faile);
                                    tv_credit_card_sign.setText(resultMsg);
                                });
                            }
                        }else if("reSetPair".equals(type)){
                            String resultCode=jsonObject.getString("resultCode");
                            String resultMsg=jsonObject.getString("resultMsg");
                            if("SUCCESS".equals(resultCode)){
                                SpUtils.getInstance().save(Constant.DATE_DEVICE_TOKEN,"");
                                 runOnUiThread(()->{
                                      tv_token.setText("");
                                      tv_pos_token_check_img.setBackgroundResource(R.drawable.self_check_faile);
                                 });
                            }
                        }else if("paymentNotifyUrl".equals(type)){
                            customDialog.dismiss();
                            String resultCode=jsonObject.getString("resultCode");
                            String resultMsg=jsonObject.getString("resultMsg");
                            ToastUtils.show(resultCode);
                        }
                    }catch (RuntimeException e){
                        e.printStackTrace();
                    }


                }
            });
        }


    }


    private void checkToken(){
        String code= SpUtils.getInstance().getString(Constant.CONFIG_TOKEN_VERIFY_CODE,"");
        if(!TextUtils.isEmpty(code)){
            POSTransaction posSendMessage = new POSTransaction();
            posSendMessage.setVersion(version);
            posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
            posSendMessage.setType("devicePair");
            posSendMessage.verifyCode=token_verify_code_et.getText().toString();
            posSendMessage.setMerchantNo(merchantNo);
            sendMsg(posSendMessage);
        }
    }


    private void checkTsysSignIn(){
        String pass= SpUtils.getInstance().getString(Constant.CONFIG_CREDIT_CARD_SIGN_IN_PASSWORD,"");
        if(!TextUtils.isEmpty(pass)){
            customDialog.show();
            POSTransaction posSendMessage = new POSTransaction();
            posSendMessage.setVersion(version);
            posSendMessage.setIp(IPUtil.getInNetIp(TcpSettingsActivity.this));
            posSendMessage.setType("creditCardSignIn");
            posSendMessage.setMerchantNo(merchantNo);
            posSendMessage.setDeviceId(deviceId);
            posSendMessage.setPassword(pass);
            sendMsg(posSendMessage);
        }
    }


    private void sendMsg(POSBase msg) {
        if(HantePOSAPI.isConnected()){
            HantePOSAPI.sentMessageV2(msg);
        }else {
            Toast.makeText(this,"Please Check Device SN",Toast.LENGTH_SHORT).show();
        }
    }


    private void refreshTransBtn(boolean b) {
        try {
            findViewById(R.id.merchant_reset_pair_btn).setEnabled(b);
            findViewById(R.id.credit_card_sign_in_btn).setEnabled(b);
            findViewById(R.id.merchant_create_token_btn).setEnabled(b);
            findViewById(R.id.payment_notify_url_btn).setEnabled(b);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }


    @Override
    public void finish() {
        HantePOSAPI.stopPOSConnect();
        super.finish();
    }

}