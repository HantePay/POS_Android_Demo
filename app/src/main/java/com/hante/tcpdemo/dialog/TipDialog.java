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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hante.tcpdemo.R;


public class TipDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;

    private TextView dialog_tip_txt_tv;


    private Button btn_pos;
    private Display display;

    private LinearLayout dialog_tip_btn_lay;



    public TipDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public TipDialog builder() {
        //Dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_tip, null);

        //
        lLayout_bg = view.findViewById(R.id.lLayout_bg);

        dialog_tip_txt_tv = view.findViewById(R.id.dialog_tip_txt_tv);


        dialog_tip_btn_lay=view.findViewById(R.id.dialog_tip_btn_lay);
        btn_pos = view.findViewById(R.id.btn_pos);


        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LayoutParams.WRAP_CONTENT));

        return this;
    }





    public TipDialog setPositiveButton(OnClickListener listener) {
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }


    public void show(String msg) {
        dialog_tip_txt_tv.setText(msg);
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

}
