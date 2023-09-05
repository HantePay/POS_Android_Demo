package com.hante.tcpdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hante.tcpdemo.bluetooth.BtBase;
import com.hante.tcpdemo.bluetooth.BtClient;
import com.hante.tcpdemo.bluetooth.BtDevAdapter;
import com.hante.tcpdemo.bluetooth.BtReceiver;
import com.hjq.toast.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class BluetoothChooseActivity extends AppCompatActivity {

    CommonTitleBar titlebar;

    RecyclerView mRecyclerView;

    private BtDevAdapter mBtDevAdapter;

    private BtReceiver mBtReceiver;

//    private BtClient mClient;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_choose);
        titlebar = findViewById(R.id.act_bt_client_titlebar);
        mRecyclerView = findViewById(R.id.act_bt_client_rv);

        titlebar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_BUTTON:
                        finish();
                        break;
                    case CommonTitleBar.ACTION_RIGHT_TEXT:
                        reScan();
                        break;
                }
            }
        });

//        mClient = new BtClient(this);


        mBtDevAdapter = new BtDevAdapter(new BtDevAdapter.Listener() {
            @Override
            public void onItemClick(BluetoothDevice dev) {
                if (HantePayApplication.application.bluetoothClient.isConnected(dev)) {
                    ToastUtils.show("连接成功");
                    return;
                }
                HantePayApplication.application.bluetoothClient.connect(dev);
                ToastUtils.show("正在连接...");
            }
        }, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBtDevAdapter);




        //判断是否有访问位置的权限，没有权限，直接申请位置权限
        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }



        //注册蓝牙广播
        mBtReceiver = new BtReceiver(BluetoothChooseActivity.this, (BluetoothDevice dev) -> {
            mBtDevAdapter.add(dev);
        });

        reScan();
    }


//    @Override
//    public void socketNotify(int state, Object obj) {
//        if (isDestroyed())
//            return;
//        String msg = null;
//        switch (state) {
//            case BtBase.Listener.CONNECTED:
//                BluetoothDevice dev = (BluetoothDevice) obj;
//                msg = String.format("与%s(%s)连接成功", dev.getName(), dev.getAddress());
////                  mTips.setText(msg);
//                ToastUtils.show(msg);
//
//                break;
//            case BtBase.Listener.DISCONNECTED:
//                msg = "连接断开";
////                mTips.setText(msg);
//                ToastUtils.show(msg);
//                break;
//            case BtBase.Listener.MSG:
//                msg = String.format("\n%s", obj);
//
//                break;
//        }
//    }
//
//    @Override
//    public void foundDev(BluetoothDevice dev) {
//        mBtDevAdapter.add(dev);
//    }

    // 重新扫描
    public void reScan() {
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(BluetoothChooseActivity.this, "android.permission.BLUETOOTH_SCAN") != PackageManager.PERMISSION_GRANTED) {
            if (!bt.isDiscovering()){
                bt.startDiscovery();
            }
            mBtDevAdapter.reScan();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBtReceiver);
//        mClient.unListener();
//        mClient.close();
    }
}