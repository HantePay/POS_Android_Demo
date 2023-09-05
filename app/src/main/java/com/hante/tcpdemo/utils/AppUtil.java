package com.hante.tcpdemo.utils;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


import com.hante.tcpdemo.HantePayApplication;

import java.io.File;
import java.util.List;


public class AppUtil {
    //远程控制 com.sunmi.remotecontrol.pro
    //设置 com.android.settings
    //硬件管理 com.woyou.hardwarekeeper
    //应用商店 woyou.market/store.ui.activity
    //系统更新 com.sunmi.ota

    //按键自定义  com.android.settings/.Settings$CustomKeyActivity t47}
    void appenAppWithPackage(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }





    /**
     * Sumi的权限管理页面
     */
    private static void gotoSumiPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.google.android.packageinstaller", "com.android.packageinstaller.permission.ui.ManagePermissionsActivity");//权限管理
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }

    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param packageName(包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return
     */

    public static PackageInfo isAvilible(String packageName) {

        PackageManager packageManager = HantePayApplication.application.getPackageManager();
        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)){
                return pinfo.get(i);
            }
        }
        return null;
    }

    /**
     * 获取App的名称
     *
     * @return 名称
     */
    public static String getAppName() {
        PackageManager pm = HantePayApplication.application.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(HantePayApplication.application.getPackageName(), 0);
            //获取应用 信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取albelRes
            int labelRes = applicationInfo.labelRes;
            //返回App的名称
            return HantePayApplication.application.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取版本名称
     *
     * @return 版本名称
     */
    public static String getVersionName() {

        //获取包管理器
        PackageManager pm = HantePayApplication.application.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(HantePayApplication.application.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     *
     *
     * @return 版本号
     */
    public static int getVersionCode() {

        //获取包管理器
        PackageManager pm = HantePayApplication.application.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(HantePayApplication.application.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }


    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning(String className) {
        ActivityManager activityManager = (ActivityManager) HantePayApplication.application.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }




    /**
     * 获取手机外部空间大小
     * @return
     */
    public static long getTotalExternalStorgeSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSizeLong();
            long totalBlocks = mStatFs.getBlockCountLong();
            return totalBlocks * blockSize;
        }
        return 0;
    }

    /**
     * 获取手机外部可用空间大小
     * @return
     */
    public static long getAvailableExternalStorgeSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSizeLong();
            long availableBlocks = mStatFs.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        }
        return 0;
    }

    /**
     * 外部存储(SDCard)是否可用
     * @return
     */
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 关闭程序
     */
    public static void killAppProcess(boolean isKillAll){
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager)HantePayApplication.application.getSystemService(Context.ACTIVITY_SERVICE);
        //获取App运行的所有进程
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList){
            //判断是否主进行
            if (runningAppProcessInfo.pid != android.os.Process.myPid()){
                //关闭子进程
                try{
                    android.os.Process.killProcess(runningAppProcessInfo.pid);
                }catch (RuntimeException e){
                    e.printStackTrace();
                }
            }

        }
        if(isKillAll){
            try{
                //可以杀死当前应用活动的进程，这一操作将会把所有该进程内的资源(包括线程全部清理掉)。
                // 当然，由于 ActivityManager 时刻监听着进程，一旦发现进程被非正常 Kill，它将会试图去重启这个进程。
//            android.os.Process.killProcess(android.os.Process.myPid());
//        结束整个虚拟机进程，注意如果在manifest里用android:process给app指定了不止一个进程，则只会结束当前进程
                System.exit(0);
            }catch (RuntimeException e){

            }
        }
    }
}
