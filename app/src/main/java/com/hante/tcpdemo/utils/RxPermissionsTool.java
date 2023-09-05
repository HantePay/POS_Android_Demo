package com.hante.tcpdemo.utils;

import android.Manifest;
import android.app.Activity;

import com.hjq.toast.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 权限工具类
 */
public class RxPermissionsTool {
    private Activity  mActivity;

    private String[] getPermissionsStrings() {
        String[]  permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA

            };
        return permissions;
    }

    public RxPermissionsTool(Activity activity) {
        mActivity = activity;
    }


    public void initAllRxPermissions(PermissionsListener permissionsListener) {
       initRxPermissions(permissionsListener,getPermissionsStrings());
    }

    public void initRxPermissions(PermissionsListener permissionsListener, String... mPermissions) {
        setPermissionsListener(permissionsListener);

        new RxPermissions(mActivity)
                .request(mPermissions)
                .subscribe(new Observer<Boolean>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(Boolean aBoolean) {
                                   if (aBoolean) {
                                       if (mPermissionsListener != null) {
                                           mPermissionsListener.PermissionsGranted();
                                       }
                                   } else {
                                       ToastUtils.show("权限不通过");
                                       if (mPermissionsListener != null) {
                                           mPermissionsListener.PermissionsFail();
                                       }
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   ToastUtils.show("权限申请错误:"+e.getMessage());

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }

    private PermissionsListener mPermissionsListener;

    public void setPermissionsListener(PermissionsListener permissionsListener) {
        mPermissionsListener = permissionsListener;
    }

    public interface PermissionsListener {
        void PermissionsGranted();

        void PermissionsFail();
    }
}
