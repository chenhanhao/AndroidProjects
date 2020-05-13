package com.kakacat.minitool.util.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class MyPopupWindow extends PopupWindow {

    private Activity activity;
    private WindowManager.LayoutParams layoutParams;
    private float alpha;

    public MyPopupWindow(Context context, View contentView, int width, int height) {
        super(contentView, width, height);
        this.alpha = 0.6f;
        activity = (Activity) context;

        setOutsideTouchable(true);      //  设置触摸外面就取消弹窗
        setTouchable(true);
        setFocusable(true);
        setAnimationStyle(android.R.style.Animation_Dialog);
        layoutParams = activity.getWindow().getAttributes();
        setOnDismissListener(()->{
            layoutParams.alpha = 1.0f;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(layoutParams);
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        layoutParams.alpha = alpha;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(layoutParams);
        super.showAtLocation(parent, gravity, x, y);
    }


    public void setAlpha(float alpha){
        this.alpha = alpha;
    }
}
