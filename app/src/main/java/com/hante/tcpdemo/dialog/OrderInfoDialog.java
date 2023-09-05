package com.hante.tcpdemo.dialog;

import android.app.Dialog;
import android.content.Context;
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


public class OrderInfoDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;

    private TextView dialog_trans_type_tv;

    private TextView dialog_tip_txt_amount;

    private TextView dialog_tip_txt_status;

    private TextView dialog_order_txt_trans_id;

    private TextView dialog_order_txt_pay_time;

    private Button btn_pos;
    private Display display;

    private LinearLayout dialog_tip_btn_lay;



    public OrderInfoDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public OrderInfoDialog builder() {
        //Dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_info, null);

        //
        lLayout_bg = view.findViewById(R.id.lLayout_bg);

        dialog_trans_type_tv=view.findViewById(R.id.dialog_trans_type_tv);

        dialog_order_txt_trans_id = view.findViewById(R.id.dialog_order_txt_trans_id);

        dialog_tip_txt_status=view.findViewById(R.id.dialog_tip_txt_status);

        dialog_tip_txt_amount=view.findViewById(R.id.dialog_tip_txt_amount);

        dialog_order_txt_pay_time=view.findViewById(R.id.dialog_order_txt_pay_time);

        dialog_tip_btn_lay=view.findViewById(R.id.dialog_tip_btn_lay);
        btn_pos = view.findViewById(R.id.btn_pos);


        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LayoutParams.WRAP_CONTENT));

        return this;
    }





    public OrderInfoDialog setPositiveButton(OnClickListener listener) {
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }


    public void show(String transType,String amount,String transId,String payTime,String payStatus) {
        dialog_trans_type_tv.setText(transType);
        dialog_tip_txt_amount.setText(amount);
        dialog_order_txt_trans_id.setText(transId);
        dialog_order_txt_pay_time.setText(payTime);
        dialog_tip_txt_status.setText(payStatus);
        dialog.show();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss(){
        if(null!= dialog && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public boolean isShowing() {

        if(null!= dialog){
           return dialog.isShowing();
        }
        return  false;
    }

}
