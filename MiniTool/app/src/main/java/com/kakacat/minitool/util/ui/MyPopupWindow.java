package com.kakacat.minitool.util.ui;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class MyPopupWindow extends PopupWindow {

    private Activity activity;

    public MyPopupWindow(Activity activity, View contentView, int width, int height) {
        super(contentView, width, height);
        this.activity = activity;
        setOutsideTouchable(true);      //  设置触摸外面就取消弹窗
        setTouchable(true);
        setFocusable(true);
        setAnimationStyle(android.R.style.Animation_Dialog);
        setOnDismissListener(()->{
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 1.0f;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(lp);
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        super.showAtLocation(parent, gravity, x, y);
    }
}
