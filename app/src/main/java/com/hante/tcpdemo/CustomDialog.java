package com.hante.tcpdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 加载提醒对话框
 */
public class CustomDialog extends ProgressDialog {

    private TextView mTv_load_dialog;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context, int theme, String text) {
        super(context, theme);
        mText = text;
    }

    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private Context mContext;

    private void init(Context context) {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
//        setCancelable(false);
//        setCanceledOnTouchOutside(false);
        mContext = context;
        setContentView(R.layout.load_dialog);
        mTv_load_dialog = findViewById(R.id.tv_load_dialog);
        if (mTv_load_dialog != null && !TextUtils.isEmpty(mText)) {
            mTv_load_dialog.setText(mText);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //核心代码是这个属性。
        getWindow().setDimAmount(0f);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setAttributes(params);
    }

    public void setBackColor(int color) {
//        if (mRl_back != null) {
//            mRl_back.setBackgroundColor(mContext.getColor(color));
//        }
    }

    public void setProgressText(String text) {
        if (mTv_load_dialog != null)
            mTv_load_dialog.setText(text);
    }

    @Override
    public void dismiss() {
         try {
             super.dismiss();
             getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
         } catch (Exception e) {
             Log.e("error", e.getMessage());
         }
    }




    @Override
    public void show() {
        super.show();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}