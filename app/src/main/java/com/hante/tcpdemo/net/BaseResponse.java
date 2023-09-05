package com.hante.tcpdemo.net;

import com.google.gson.annotations.SerializedName;

/**
 * 响应基类 存放公共参数
 */
public class BaseResponse<T> {
    @SerializedName("result_code")
    private String  resultCode;
    @SerializedName("return_code")
    private String  returnCode;
    @SerializedName("return_msg")
    private String  returnMsg;

    private T data;


    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess(){
        return "SUCCESS".equals(resultCode)&&
                "ok".equals(returnCode);
    }
}
