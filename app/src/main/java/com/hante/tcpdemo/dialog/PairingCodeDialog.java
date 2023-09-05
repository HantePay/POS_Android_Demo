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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hante.tcpdemo.R;
import com.hante.tcpdemo.net.BaseCallBack;
import com.hjq.toast.ToastUtils;


public class PairingCodeDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;

    private EditText dialog_pair_txt_et;


    private Button btn_pos;
    private Display display;




    public PairingCodeDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public PairingCodeDialog builder() {
        //Dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pairing_code, null);

        //
        lLayout_bg = view.findViewById(R.id.lLayout_bg);

        dialog_pair_txt_et=view.findViewById(R.id.dialog_pair_txt_et);
        btn_pos = view.findViewById(R.id.btn_pos);


        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LayoutParams.WRAP_CONTENT));


        view.findViewById(R.id.btn_cancel).setOnClickListener((View v)->{
            dismiss();
        });

        return this;
    }





    public PairingCodeDialog setPositiveButton(BaseCallBack<String> listener) {
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(dialog_pair_txt_et.getText())){
                    listener.callSuccess(dialog_pair_txt_et.getText().toString());
                    dialog.dismiss();
                }else{
                    ToastUtils.show("Please enter Pair code");
                }

            }
        });
        return this;
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
