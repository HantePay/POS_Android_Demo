package com.hante.tcpdemo.event;

public class UpdateMsgEvent {
    /**
     * 1.登录刷新
     */
    public int type=1;



    public String msg;


    public UpdateMsgEvent(){}
    public UpdateMsgEvent(int type){
        this.type=type;
    }

    public UpdateMsgEvent(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
