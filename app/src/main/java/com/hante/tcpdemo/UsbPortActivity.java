package com.hante.tcpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android_serialport_api.SerialPort;

public class UsbPortActivity extends AppCompatActivity {
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_port);

        try {
            SerialPort serialPort = new SerialPort(new File("/dev/ttyHSL1"), 9600, 0);//your serial port dev
            mOutputStream = serialPort.getOutputStream();
            mInputStream=serialPort.getInputStream();
            startRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Button buttonWrite = (Button)findViewById(R.id.ButtonWrite);
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] mBuffer = new byte[1024];
                Arrays.fill(mBuffer, (byte) 0x55);
                try {
                    mOutputStream.write(mBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button buttonAbout = (Button)findViewById(R.id.ButtonAbout);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UsbPortActivity.this);
                builder.setTitle("About");
                builder.setMessage("1213213");
                builder.show();
            }
        });

        final Button buttonQuit = (Button)findViewById(R.id.ButtonQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UsbPortActivity.this.finish();
            }
        });
    }

    private void startRead(){
        new Thread(()->{

            while (true){
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // 定义输入流
                    String text = "";
                    int temp  = mInputStream.read();
                    //判断开连接
                    if(temp==-1){
                        Log.e("Read","断开连接");
                        return;
                    }
                    ByteArrayOutputStream bou = new ByteArrayOutputStream();
                    while(temp != -1 && temp != 255) {
                        bou.write(temp);
                        temp = mInputStream.read();
                    }
                    text = bou.toString();
                    if(!TextUtils.isEmpty(text)) {
                        Log.e("Read","收到消息"+text);
                    }
                } catch (Exception e) {
                    //断开连接
                    Log.e("Read","断开连接");
                }
            }

        }).start();
    }
}