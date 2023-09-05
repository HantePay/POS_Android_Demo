package com.hante.tcpdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hante.tcpdemo.R;


public class PaymentTipDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;

    private TextView dialog_tip_txt_amount;

    private TextView dialog_trans_type_tv;
    private TextView txt_msg;
    private ImageView tip_img;
    private Button btn_pos;
    private Display display;

    private ImageView img_line;
    private LinearLayout dialog_tip_btn_lay;


    OnClickListener closeListener;

    boolean canClose=true;

    public PaymentTipDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public PaymentTipDialog builder() {
        //Dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment_tip, null);

        //
        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_msg = view.findViewById(R.id.dialog_tip_txt_msg);

        dialog_tip_txt_amount=view.findViewById(R.id.dialog_tip_txt_amount);

        tip_img=view.findViewById(R.id.dialog_tip_img);
        img_line=view.findViewById(R.id.img_line);
        dialog_tip_btn_lay=view.findViewById(R.id.dialog_tip_btn_lay);
        btn_pos = view.findViewById(R.id.btn_pos);
        dialog_trans_type_tv=view.findViewById(R.id.dialog_trans_type_tv);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LayoutParams.WRAP_CONTENT));

        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                canClose=true;
                dialog.dismiss();
                if(null!=closeListener){
                    closeListener.onClick(v);
                }
            }
        });

        return this;
    }



    public PaymentTipDialog setMsg(String msg) {
        if ("".equals(msg)) {
            txt_msg.setText("");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }



    public PaymentTipDialog setPositiveButton(OnClickListener listener) {
        this.closeListener=listener;
        return this;
    }


    public PaymentTipDialog setCanClose(boolean canClose) {
        this.canClose=canClose;
        return this;
    }

    public void show(String amount,String msg) {
        dialog_trans_type_tv.setText("");
        tip_img.setVisibility(View.GONE);
        img_line.setVisibility(canClose?View.VISIBLE:View.GONE);
        dialog_tip_btn_lay.setVisibility(canClose?View.VISIBLE:View.GONE);
        dialog_tip_txt_amount.setText(amount);
        txt_msg.setText(msg);
        dialog.show();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss(){
        if(null!= dialog){
            dialog.dismiss();
        }
    }

    public boolean isShowing() {

        if(null!= dialog){
           return dialog.isShowing();
        }
        return  false;
    }

    public void refreshMsg(String resultMsg) {
        txt_msg.setText(resultMsg);
    }

    public void refreshSuccess(String transType) {
        refreshSuccess("",transType);
    }

    public void refreshSuccess(String amount,String transType) {
        tip_img.setVisibility(View.VISIBLE);
        tip_img.setBackgroundResource(R.drawable.pay_success);
        dialog_trans_type_tv.setText(transType);
        txt_msg.setText("Success");
        dialog_tip_btn_lay.setVisibility(View.VISIBLE);
        img_line.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(amount)){
            dialog_tip_txt_amount.setText(amount);
        }
    }

    public void refreshFail(String resultMsg) {
        tip_img.setVisibility(View.VISIBLE);
        tip_img.setBackgroundResource(R.drawable.credit_card_checkout_error);
        txt_msg.setText(resultMsg);
        dialog_tip_btn_lay.setVisibility(View.VISIBLE);
        img_line.setVisibility(View.VISIBLE);
    }
}
