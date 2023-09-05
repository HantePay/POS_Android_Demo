package com.hante.tcpdemo.net;

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
    @POST("https://test.hantepay.cn/route/v2.0.0/order/list/{userNo}")
    Observable<BaseResponse<OrderListResponse>> getOrderList(@Path("userNo") String userNo, @Body Map<String, Object> bodyParams);

}
