package com.hante.tcpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hante.tcp.util.EncryptUtils;
import com.hante.tcp.util.MD5Encoder;
import com.hante.tcpdemo.net.BaseResponse;
import com.hante.tcpdemo.net.CloudCreateOrderRequest;
import com.hante.tcpdemo.net.CloudCreateOrderResponse;
import com.hante.tcpdemo.net.RetrofitFactory;
import com.hante.tcpdemo.net.SimpleObserver;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 云订单
 */
public class CloudOrderActivity extends AppCompatActivity implements View.OnClickListener {
    CloudCreateOrderRequest request=new CloudCreateOrderRequest();
    /**
     * 商户号
     */
    EditText merchant_et;

    /**
     * 店铺号
     */
    EditText shop_no_et;

    /**
     * 机器sn
     */
    EditText sn_et;

    /**
     * 订单号
     */
    EditText reconnection_order_et;

    /**
     * token
     */
    EditText apitoken_et;


    TextView request_msg_tv;

    TextView response_msg_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_order);
        reconnection_order_et=findViewById(R.id.reconnection_order_et);
        merchant_et=findViewById(R.id.merchant_et);
        shop_no_et=findViewById(R.id.shop_no_et);
        apitoken_et=findViewById(R.id.apitoken_et);
        sn_et=findViewById(R.id.sn_et);

        request_msg_tv=findViewById(R.id.request_msg_tv);
        response_msg_tv=findViewById(R.id.response_msg_tv);

        findViewById(R.id.jump_to_tcp).setOnClickListener(this);
        findViewById(R.id.checkstand_btn).setOnClickListener(this);
        findViewById(R.id.checkout_counter_btn).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jump_to_tcp:
                startActivity(new Intent(CloudOrderActivity.this,BluetoothActivity.class));
                break;
            case R.id.checkstand_btn:
            case R.id.checkout_counter_btn:
                request.setMerchant_no(merchant_et.getText().toString());
                request.setStore_no(shop_no_et.getText().toString());
                request.setSn(sn_et.getText().toString());
                randomOrderNo();
                createCloudOrder();
                break;
        }
    }

    private String randomOrderNo(){
        long time=System.currentTimeMillis();
        request.setOut_trade_no("T"+time);
        request.setNonce_str(String.valueOf(time));
        request.setMealNumber(String.valueOf((int)(Math.random()*10000)));
        request.setName("Order"+time);
        reconnection_order_et.setText("T"+time);
        int amount=(int)(Math.random()*10);
        request.setAmount(amount>0?amount:1);
        return "T"+time;
    }


    private void createCloudOrder(){
        StringBuilder signBuilder = new StringBuilder();
        //计算 signature
        JSONObject json = JSON.parseObject(JSON.toJSONString(request));
        //参数排序
        SortedMap<String, Object> signMap = new TreeMap<>();
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            if (!"signature".equals(entry.getKey())
                    && entry.getValue() != null && entry.getValue().toString().length() != 0) {
                signMap.put(entry.getKey(), entry.getValue());
            }
        }
        try {
            Iterator var3 = signMap.entrySet().iterator();
            while(var3.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var3.next();
                signBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            signBuilder.append(apitoken_et.getText().toString());
            Log.e("========签名:",signBuilder.toString());
            //计算 signature
            request.setSignature(MD5Encoder.encode(signBuilder.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        request_msg_tv.setText("Request:"+JSONObject.toJSONString(request));

        RetrofitFactory.getInstance().createCloudOrder(request)
                .subscribe(new SimpleObserver<BaseResponse<CloudCreateOrderResponse>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }


                    @Override
                    public void onNext(@NonNull BaseResponse<CloudCreateOrderResponse> response) {
                        //请求成功
                        response_msg_tv.setText("Response:"+JSONObject.toJSONString(response));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}