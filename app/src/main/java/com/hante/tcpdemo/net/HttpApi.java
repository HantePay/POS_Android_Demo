package com.hante.tcpdemo.net;

import com.hante.tcp.bean.v2.POSOrderQuery;
import com.hante.tcpdemo.bean.OrderInfo;
import com.hante.tcpdemo.bean.OrderInfoResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {



    /**
     * 云订单
     * https://gateway.hantepay.com/v2/gateway/create/order
     * @return
     */
    @POST("v2/gateway/create/order")
    Observable<BaseResponse<CloudCreateOrderResponse>> creditCardTransaction(@Body CloudCreateOrderRequest params);


    /**
     *
     * @return
     */
    @GET("http://test.hantepay.cn/route/v2.0.0/machine/info")
    Observable<BaseResponse<PosInfoResponse>> queryPosIP(@Query("machineCode") String sn,@Query("type") String type);

    /**
     * 普通订单列表
     *
     * @param userNo
     * @param bodyParams
     * @return
     */
    @POST("http://test.hantepay.cn/route/v2.0.0/order/list/{userNo}")
    Observable<BaseResponse<OrderListResponse>> getOrderList(@Path("userNo") String userNo, @Body Map<String, Object> bodyParams);

    /**
     * 本地POS服务，查询订单
     * @param bodyParams
     * @return
     */
    @POST("api/pos")
    Observable<OrderInfoResponse> queryOrder(@Body POSOrderQuery bodyParams);


}
