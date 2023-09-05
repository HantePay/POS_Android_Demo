package com.hante.tcpdemo.net;


import java.io.Serializable;

public class PosInfoResponse implements Serializable {
    private String connectUrl;
    private String userNo;

    public String getConnectUrl() {
        return connectUrl;
    }

    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
}
