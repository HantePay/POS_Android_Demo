package com.hante.tcpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hante.tcpdemo.utils.AppUtil;

public class MainActivity2 extends AppCompatActivity {

    TextView act_main_version_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        act_main_version_tv=findViewById(R.id.act_main_version_tv);

        findViewById(R.id.btn_tcp_v1).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity2.this, TcpV1Activity.class));
        });


        findViewById(R.id.btn_tcp_v2).setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity2.this, TcpV2Activity.class));
        });

        act_main_version_tv.setText(AppUtil.getVersionName());

//        findViewById(R.id.btn_transaction_Record).setOnClickListener((View v)->{
//            startActivity(new Intent(MainActivity2.this, TransactionRecordActivity.class));
//        });

    }
}