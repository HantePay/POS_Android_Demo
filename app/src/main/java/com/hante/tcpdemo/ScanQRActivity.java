package com.hante.tcpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hante.tcpdemo.utils.RxPermissionsTool;

import cn.bertsir.zbar.CameraPreview;
import cn.bertsir.zbar.Qr.ScanResult;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.ScanCallback;
import cn.bertsir.zbar.utils.QRUtils;
import cn.bertsir.zbar.view.ScanLineView;
import cn.bertsir.zbar.view.ScanView;

/**
 * 扫码
 */
public class ScanQRActivity extends AppCompatActivity {

    CameraPreview cp;


    ImageView iv_flash;

    TextView tv_title;

    ScanView sv;


    private SoundPool soundPool;

    private QrConfig options;

    private ScanLineView mScanLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qractivity);
        cp= (CameraPreview) findViewById(R.id.act_scan_qr_cp);
        iv_flash= (ImageView) findViewById(R.id.act_scan_qr_iv_flash);
        tv_title= (TextView) findViewById(R.id.act_scan_qr_tv_title);
        sv= (ScanView) findViewById(R.id.act_scan_qr_sv);

        checkPermission();

        findViewById(R.id.act_scan_qr_back_lay).setOnClickListener((View v)->{
            finish();
        });
    }

    /**
     * 检测权限
     */
    private void checkPermission(){
        new RxPermissionsTool(this).initRxPermissions(new RxPermissionsTool.PermissionsListener() {
            @Override
            public void PermissionsGranted() {
                initScanView();
            }

            @Override
            public void PermissionsFail() {
                finish();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    private void initScanView() {
        try{
            options = new QrConfig.Builder()
                    .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
//                .setDesText(getString(R.string.activity_scaner_code_description))//扫描框下文字
//                .setShowDes(true)//是否显示扫描框下面文字
//                .setShowLight(true)//显示手电筒按钮
//                .setShowTitle(true)//显示Title
//                .setShowAlbum(false)//显示从相册选择按钮
//                .setNeedCrop(false)//是否从相册选择后裁剪图片
//                .setCornerColor(Color.parseColor("#408CE2"))//设置扫描框颜色
//                .setLineColor(Color.parseColor("#408CE2"))//设置扫描线颜色
//                .setLineSpeed(QrConfig.LINE_SLOW)//设置扫描线速度
//                .setScanType(QrConfig.TYPE_ALL)//设置扫码类型（二维码，条形zong码，全部，自定义，默认为二维码）
//                .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
//                .setCustombarcodeformat(QrConfig.BARCODE_EAN13)//此项只有在扫码类型为TYPE_CUSTOM时才有效
//                .setPlaySound(true)//是否扫描成功后bi~的声音
//                .setDingPath(R.raw.qrcode)//设置提示音(不设置为默认的Ding~)
//                .setTitleText(text)//设置Tilte文字
//                .setTitleBackgroudColor(Color.parseColor("#00000000"))//设置状态栏颜色
//                .setTitleTextColor(Color.WHITE)//设置Title文字颜色
//                .setShowZoom(cb_show_zoom.isChecked())//是否开始滑块的缩放
//                .setAutoZoom(false)//是否开启自动缩放(实验性功能，不建议使用)
//                .setFingerZoom(false)//是否开始双指缩放
//                .setDoubleEngine(cb_double_engine.isChecked())//是否开启双引擎识别(仅对识别二维码有效，并且开启后只识别框内功能将失效)
//                .setScreenOrientation(QrConfig.SCREEN_fullSensor)//设置屏幕方式
//                .setOpenAlbumText("选择要识别的图片")//打开相册的文字
//                .setLooperScan(false)//是否连续扫描二维码
//                .setLooperWaitTime(5 * 1000)//连续扫描间隔时间
//                .setScanLineStyle(ScanLineView.style_gridding)//扫描线样式
//                .setAutoLight(false)//自动灯光
//                .setShowVibrator(false)//是否震动提醒
                    .create();

            //数据初始化
            Symbol.is_only_scan_center = options.isOnly_center();
//        Symbol.scanType = options.getScan_type();
//        Symbol.scanFormat = options.getCustombarcodeformat();
//        Symbol.is_auto_zoom = options.isAuto_zoom();
//        Symbol.doubleEngine = options.isDouble_engine();
//        Symbol.looperScan = options.isLoop_scan();
//        Symbol.looperWaitTime = options.getLoop_wait_time();
//        Symbol.screenWidth = QRUtils.getInstance().getScreenWidth(this);
//        Symbol.screenHeight = QRUtils.getInstance().getScreenHeight(this);

            //bi~
            soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
            soundPool.load(this, options.getDing_path(), 1);

//        mo_scanner_back.setImageResource(options.getBackImgRes());

            iv_flash.setImageResource(options.getLightImageRes());
            iv_flash.setVisibility(options.isShow_light() ? View.VISIBLE : View.GONE);

            sv.setType(options.getScan_view_type());
            sv.setCornerColor(options.getCORNER_COLOR());
            sv.setLineSpeed(options.getLine_speed());
            sv.setLineColor(options.getLINE_COLOR());
            sv.setScanLineStyle(options.getLine_style());
            startScan();
        }catch (Exception e){
            e.printStackTrace();
            finish();
        }
    }

    private void startScan(){
//        tv_title.setVisibility(isScanOrder?View.GONE:View.VISIBLE);
        if (cp != null) {
            try{

                cp.stop();
                cp.setScanCallback(resultCallback);
                cp.start(CameraCharacteristics.LENS_FACING_FRONT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 识别结果回调
     */
    private ScanCallback resultCallback = new ScanCallback() {
        @Override
        public void onScanResult(ScanResult result) {
            if (options.isPlay_sound()) {
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
            if (options.isShow_vibrator()) {
                QRUtils.getInstance().getVibrator(getApplicationContext());
            }
            mScanLineView = sv.getScanLineView();
            checkScanResult(result);
        }
    };

    /**
     * 处理扫码结果
     * @param result
     */
    private void checkScanResult(ScanResult result) {
        if ( result == null || result.getContent() == null || result.getContent().length() == 0){
            return;
        }
        if (mScanLineView != null) {
            mScanLineView.pauseValueAnimator();
        }
        if (cp != null) {
            cp.stop();
        }
        String scanData=result.getContent().replaceAll("null","");

        Intent intent=new Intent();
        intent.putExtra("PosInfo",scanData);
        setResult(103,intent);
        finish();

    }

}