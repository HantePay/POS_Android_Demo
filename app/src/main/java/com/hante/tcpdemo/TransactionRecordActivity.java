package com.hante.tcpdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.hante.tcpdemo.adapter.OrderListAdapter;
import com.hante.tcpdemo.bean.Constant;
import com.hante.tcpdemo.bean.OrderInfo;
import com.hante.tcpdemo.net.BaseCallBack;
import com.hante.tcpdemo.net.BaseResponse;
import com.hante.tcpdemo.net.OrderListResponse;
import com.hante.tcpdemo.net.RetrofitFactory;
import com.hante.tcpdemo.net.SimpleObserver;
import com.hante.tcpdemo.utils.MyDateUtil;
import com.hante.tcpdemo.utils.SpUtils;
import com.hante.tcpdemo.widget.decoration.NormalDecoration;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class TransactionRecordActivity extends AppCompatActivity {

    CommonTitleBar mCtb_titleBar;

    SmartRefreshLayout refreshLayout;

    RecyclerView mRecyclerview;

    /**
     * 时区选择   默认时区
     * 洛杉矶时间
     */
    private String mTimeZone="America/Los_Angeles";
    private int mPageNum;
    private int mTotalPages;
    private Map<String, Object> mBodyParams = new HashMap<>();
    OrderListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);

        mCtb_titleBar=  findViewById(R.id.act_order_list_titlebar);
        refreshLayout= findViewById(R.id.act_order_list_refresh);
        mRecyclerview=  findViewById(R.id.act_order_list_recyclerview);

        mBodyParams.put("payStatus", "success");

        //过去半年
        String startTime = "";
        String endTime = "";
        try {
            startTime = getTimeStringBef6Months(mTimeZone);
            endTime = getUTCDayEnd(mTimeZone);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mBodyParams.put("startTime", startTime);
        mBodyParams.put("endTime", endTime);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));


        mAdapter=new OrderListAdapter(this);
        mRecyclerview.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshOrLoadMore(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshOrLoadMore(false);
            }
        });

        mCtb_titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_BUTTON:
                    case CommonTitleBar.ACTION_LEFT_TEXT:
                        finish();
                        break;
                }
            }
        });

//        refreshOrLoadMore(true);

        new Handler().postDelayed(()->{
            refreshLayout.autoRefresh();
        },500);

    }


    public void refreshOrLoadMore(boolean isRefresh){
        if(isRefresh){
            mPageNum = 1;
        }else {
            if(mTotalPages>0&&mPageNum>=mTotalPages){
                refreshLayout.finishLoadMore(true);
                ToastUtils.show("Data has been loaded");
                return;
            }
            mPageNum+=1;
        }
        mBodyParams.put("pageNum", mPageNum);

           getOrderList(mBodyParams, new BaseCallBack<OrderListResponse>() {
                @Override
                public void callSuccess(OrderListResponse messageListResponse) {
                    if(isRefresh){
                        refreshLayout.finishRefresh(true);
//                        if(null==messageListResponse.getRecords() || messageListResponse.getRecords().isEmpty()){
//                            act_order_list_msv.showEmpty();
//                        }
                    }else{
                        refreshLayout.finishLoadMore(true);
                    }

                    mTotalPages = messageListResponse.getPages();
                    mAdapter.refreshOrLoadMore(isRefresh,messageListResponse.getRecords());
                    addDecoration(isRefresh);
                }

                @Override
                public void callFail(String code,String msg) {
                    if(isRefresh){
                        refreshLayout.finishRefresh(true);
                    }else {
                        refreshLayout.finishLoadMore(true);
                    }

                }
            });
    }

    private void addDecoration(boolean isRefresh) {
        NormalDecoration normalDecoration = new NormalDecoration() {
            @Override
            public String getHeaderName(int i) {
                if (i > -1) {
                    OrderInfo item = mAdapter.getItem(i);
                    if (item != null) {
                        String createTime = mAdapter.getItem(i).getCreatetime();
                        String showDate = "";
                        try {
                            showDate = MyDateUtil.switchTimeZone(createTime, "UTC", mTimeZone, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return MyDateUtil.showDateInterval(showDate, mTimeZone, false);
                    }
                }
                return "";
            }
        };
        if (isRefresh) {
            if(mRecyclerview.getItemDecorationCount()>0){
                mRecyclerview.removeItemDecorationAt(0);
            }
        } else{
            mRecyclerview.removeItemDecorationAt(0);
        }
        mRecyclerview.addItemDecoration(normalDecoration);
    }


    public  String getTimeStringBef6Months(String timeZone){
        SimpleDateFormat sixMonthBefore = new SimpleDateFormat("yyyyMMddHHmmss");
        sixMonthBefore.setTimeZone(TimeZone.getTimeZone(timeZone));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -6);
        return  sixMonthBefore.format(c.getTime());
    }

    //指定时区当天的结束时间转UTC
    public  String getUTCDayEnd(String timeZone) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String text = sdf.format(new Date()) + "235959";
        String utcEnd = switchTimeZone(text, timeZone, "UTC");

        return utcEnd;

    }

    /**
     * 时间时区转换
     *
     * @param time
     * @param localTimeZone 需转换时间的时区
     * @param needTimeZone  转到哪个时区
     * @return
     * @throws ParseException
     */
    public String switchTimeZone(String time, String localTimeZone, String needTimeZone) throws ParseException {
        SimpleDateFormat locSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        locSdf.setTimeZone(TimeZone.getTimeZone(localTimeZone));
        SimpleDateFormat ndSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        ndSdf.setTimeZone(TimeZone.getTimeZone(needTimeZone));
        Date a = locSdf.parse(time);
        String switchTime = ndSdf.format(a);
        return switchTime;
    }


    /**
     * 查询交易订单详情
     * @param callBack
     */
    public void getOrderList(Map<String, Object> bodyParams, BaseCallBack<OrderListResponse> callBack){
            RetrofitFactory.getInstance().getOrderList(SpUtils.getInstance().getString(Constant.DATE_USER_NO,""),bodyParams)
                    .subscribe(new SimpleObserver<BaseResponse<OrderListResponse>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            super.onSubscribe(d);
                        }


                        @Override
                        public void onNext(@NonNull BaseResponse<OrderListResponse> response) {
                            //请求成功
                            if(response.isSuccess()){
                                callBack.callSuccess(response.getData());
                            }else{
                                callBack.callFail(response.getResultCode(),response.getReturnMsg());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                                callBack.callFail("",e.getMessage());
                            e.printStackTrace();
                        }
                    });

    }
}