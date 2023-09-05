package com.hante.tcpdemo;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.hante.tcpdemo.bluetooth.BtBase;
import com.hante.tcpdemo.bluetooth.BtClient;
import com.hante.tcpdemo.event.UpdateMsgEvent;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


public class HantePayApplication extends Application {
    public static HantePayApplication application;

    private static final Handler sHandler = new Handler();
    private static Context mContext;
    /**
     * 蓝牙程序 application
     */
    public static Context getContext() {
        return mContext;
    }

    public static void runUi(Runnable runnable) {
        sHandler.post(runnable);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        mContext = getApplicationContext();
        //初始化 toast
        ToastUtils.init(application);

    }

    //蓝牙客户端服务
    public BtClient bluetoothClient = new BtClient(new BtBase.Listener() {
        @Override
        public void socketNotify(int state, Object obj) {
            String msg = null;
            switch (state) {
                case BtBase.Listener.CONNECTED:
                    BluetoothDevice dev = (BluetoothDevice) obj;
                    msg = String.format("%s(%s) Connection successful", dev.getName(), dev.getAddress());
                    ToastUtils.show(msg);
                    break;
                case BtBase.Listener.DISCONNECTED:
                    msg = "Bluetooth Connection is broken";
                    ToastUtils.show(msg);
                    break;
                case BtBase.Listener.MSG://收到消息
                    msg = String.format("\n%s", obj);
//                    ToastUtils.show("收到消息"+msg);
                    //通知页面刷新
                    if(!TextUtils.isEmpty(msg)){
                        EventBus.getDefault().post(new UpdateMsgEvent(1,msg));
                    }
                    break;
            }
        }
    });

}
