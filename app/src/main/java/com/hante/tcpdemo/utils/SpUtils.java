package com.hante.tcpdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hante.tcpdemo.HantePayApplication;

public class SpUtils {
    private static SpUtils instance;
    private static SharedPreferences mSp;
    private static String FILENAME = "TcpDemo";

    private SpUtils() {
        if (null == mSp) {
            mSp = HantePayApplication.application.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        }
    }
    /**
     * 对象锁
     */
    private static final Object lock = new Object();

    public static SpUtils getInstance() {
        synchronized (lock) {
            if (instance == null) {
                initSpUtils();
            }
        }
        return instance;
    }

    private static synchronized void initSpUtils() {
        instance = new SpUtils();
    }

    //保存
    public void save(String key, Object value) {

        if (value instanceof String) {
            mSp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSp.edit().putInt(key, (Integer) value).commit();
        }else if (value instanceof Long) {
            mSp.edit().putLong(key, (Long) value).commit();
        }

    }

    //获取String
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    //获取Boolean
    public Boolean getBoolean(String key, Boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    //获取int
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    //获取long
    public long getLong(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    //清除
    public void clearData() {
        if (mSp != null) {
            mSp.edit().clear().apply();
        }
    }
}
