"# POS_Android_Demo" Sunmi V/P 系列设备
Hante POS 服务原理：Hante APP 运行创建 Socket Server ，进行通信处理;
通信规则：<br/>
	1.发送/接收消息时，判断一条消息结束符号为: 255 或者 -1;<br/>
	2.POS 服务段，响应消息格式：XXXLen;消息内容；<br/>
		注意：<br/>
			XXX 表示消息内容的长度<br/>
			Len;是区分符<br/>
			碰到 messageId 的响应消息，如果发送收到该消息成功，服务端当天会重复多次推送该消息（使用场景：如收款成功消息，退款成功消息）<br/>

该Demo 提供快速对接 HantePOSAPI.aar 包;
	
	连接POS 服务:
		HanteSDKUtils.connectPOSService(this, ip,deviceId, key,merchantNo, new SocketCallback() {
			@Override //连接成功回调
			public void connected() {

			}
			
			
			@Override //连接失败回调
			public void connectionFails(String s) {

			}
			
			@Override //异常回调
			public void error(int code,String s) { 
			
			}
			
			@Override  //断开连接回调
			public void disConnected() { 
			
			}

			@Override //POS服务端收到消息后，立即回应心跳包
			public void heartbeat(String msg) {
			
			}
			
			@Override //重新连接回调
			public void reconnection() { }

			@Override //服务器段响应消息
			public void receiveMessage(int length,String msg) {

			}
			
		});
		
		
	发送消息：
		//发起交易
		HantePOSAPI.sale("SALE",1,0,1,"POS_PAYMENT","9845465131","测试");
