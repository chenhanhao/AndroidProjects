package com.kakacat.minitool.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

public class UiUtil {


    public static void showKeyboard(Context context, View v){
        InputMethodManager manager = ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

    public static void closeKeyboard(Context context,View view){
        InputMethodManager manager = ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void initPopupWindow(Activity activity,PopupWindow popupWindow){
        popupWindow.setOutsideTouchable(true);      //  设置触摸外面就取消弹窗
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setOnDismissListener(()->{
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 1.0f;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(lp);
        });
    }
}