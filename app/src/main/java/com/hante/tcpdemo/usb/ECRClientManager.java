package com.hante.tcpdemo.usb;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.pos.connection.bridge.ECRConnection;
import com.pos.connection.bridge.ECRListener;
import com.pos.connection.bridge.ECRRequestCallback;
import com.pos.connection.bridge.binder.ECRConstant;
import com.pos.hardware.connection.library.ECRServiceKernel;

import java.nio.charset.StandardCharsets;

/**
 * ECR 操作
 */
public class ECRClientManager {

    /**
     * 角色：
     *   默认:MASTER
     */
    private String type= ECRConstant.Type.MASTER;

    /**
     *连接方式: 默认USB。
     */
    private String mode=ECRConstant.Mode.USB;


    private static ECRClientManager instance=new ECRClientManager();

    ECRCallBack callBack;

    public static ECRClientManager getInstance() {
        return instance;
    }

    ECRListener ecrListener=new ECRListener.Stub() {
        @Override
        public void onReceive(byte[] bytes) throws RemoteException {
            //收到消息
            callBack.receiveMessage(new String(bytes));
        }
    };

    /**
     * 初始化Ecr
     */
    public void init(Context context, ECRCallBack callBack){
        //校验参数
        if(null==callBack){
            return;
        }
        this.callBack=callBack;

        bindService(context);
    }

    /**
     * 绑定服务
     * @param context
     */
    private void bindService(Context context){
        //防止重复绑定服务
        if(null!=ECRServiceKernel.getInstance().ecrService){
            stopEcr();
        }
        //重新绑定服务
         ECRServiceKernel.getInstance().bindService(context, new ECRServiceKernel.ConnectionCallback() {
            @Override
            public void onServiceConnected() {
                callBack.bindEcrService(1,"初始化成功");
                start();
            }

            @Override
            public void onServiceDisconnected() {
                callBack.bindEcrService(-1,"ECRService:断开");
            }
        });
    }

    /**
     * 启动
     */
    private void start(){

        if(null==ECRServiceKernel.getInstance().ecrService){
            return;
        }

        Bundle bundle=new Bundle();
        bundle.putString(ECRConstant.Configuration.TYPE,type);//Slave or Master
        bundle.putString(ECRConstant.Configuration.MODE,mode);//Bluetooth or USB
        try {
            ECRServiceKernel.getInstance().ecrService.connect(bundle, new ECRConnection.Stub() {
                @Override
                public void onConnected() throws RemoteException {
                    callBack.connection(1,"连接成功");
                }

                @Override
                public void onDisconnected(int code, String message) throws RemoteException {
                    //连接失败
                    callBack.connection(-1,message);
                }
            });

            //接受到消息
            ECRServiceKernel.getInstance().ecrService.register(ecrListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送消息
     * @param msg
     */
    public void sendMsg(String msg){

        if(null==ECRServiceKernel.getInstance().ecrService){
            return;
        }

        try {
            ECRServiceKernel.getInstance().ecrService.send(msg.getBytes(StandardCharsets.UTF_8), new ECRRequestCallback.Stub() {
                @Override
                public void onSuccess() throws RemoteException {
                    callBack.sendMessage(1,"发送成功");
                }

                @Override
                public void onFailure(int code, String message) throws RemoteException {
                    callBack.sendMessage(-1,"发送失败");
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止Ecr
     */
    public void stopEcr(){
        if(null==ECRServiceKernel.getInstance().ecrService){
            return;
        }
        try {
            ECRServiceKernel.getInstance().ecrService.disconnect();
            ECRServiceKernel.getInstance().ecrService.unregister(ecrListener);
            ECRServiceKernel.getInstance().unbindService();
            callBack.stopErcService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
