package com.hante.tcpdemo.usb;

/**
 * erc 回调
 */
public interface ECRCallBack {

    /**
     * 绑定ECR服务
     * @param code 1 成功 其他失败
     * @param msg
     */
    void bindEcrService(int code,String msg);


    /**
     * 建立连接
     * @param code 1 成功 其他失败
     * @param msg
     */
    void connection(int code,String msg);


    /**
     * 发送消息
     * @param code 1 成功 其他失败
     * @param msg
     */
    void sendMessage(int code,String msg);

    /**
     * 接受消息
     * @param msg
     */
    void receiveMessage(String msg);


    /**
     * 停止ERC
     */
    void stopErcService();

}
