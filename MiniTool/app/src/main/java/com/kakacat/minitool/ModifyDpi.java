package com.kakacat.minitool;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

public class ModifyDpi {

    private static ModifyDpi modifyDpi;
    private Activity activity;
    private View parentView;
    private PopupWindow popupWindow;
    private EditText etDpi;

    public static ModifyDpi getInstance(Activity activity,View parentView){
        if(modifyDpi == null){
            modifyDpi = new ModifyDpi();
            modifyDpi.activity = activity;
            modifyDpi.parentView = parentView;
            modifyDpi.initView();
        }
        return modifyDpi;
    }

    private void initView(){
        View popupWindowView = View.inflate(activity, R.layout.popupwindow_modify_dpi, null);
        popupWindow= new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(activity, popupWindow);
        Button btClear = popupWindowView.findViewById(R.id.iv_clear);
        Button btModifyDpi = popupWindowView.findViewById(R.id.bt_modify_dpi);
        etDpi = popupWindowView.findViewById(R.id.edit_text);
        btClear.setOnClickListener(v -> popupWindow.dismiss());
        btModifyDpi.setOnClickListener(v -> {
            String val = etDpi.getText().toString();
            if(TextUtils.isEmpty(val)) Snackbar.make(parentView,"输入错误",Snackbar.LENGTH_SHORT).show();
            else SystemUtil.modifyDpi(val);
        });
    }



    public void start(){
        popupWindow.showAtLocation(parentView, Gravity.CENTER,0,0);
    }
}
