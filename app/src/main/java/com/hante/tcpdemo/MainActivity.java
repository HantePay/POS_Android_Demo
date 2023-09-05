package com.hante.tcpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //POS V1版本
        findViewById(R.id.btn_tcp_v1).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,TcpV1Activity.class));
        });

        //POS V2版本
        findViewById(R.id.btn_tcp_v2).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,TcpV2Activity.class));
        });

        //POS 云订单
        findViewById(R.id.btn_tcp_cloud).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,CloudOrderActivity.class));
        });

        findViewById(R.id.btn_bluetooth_v2).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,BluetoothActivity.class));
        });


        findViewById(R.id.btn_usb_v2).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,UsbV2Activity.class));
        });

        findViewById(R.id.btn_usb_port).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,UsbPortActivity.class));
        });

    }
}