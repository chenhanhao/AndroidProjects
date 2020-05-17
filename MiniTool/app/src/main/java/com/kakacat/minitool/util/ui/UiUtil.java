package com.kakacat.minitool.util.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.google.android.material.snackbar.Snackbar;

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
        setShadow(activity);
        popupWindow.setOnDismissListener(()->{
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 1.0f;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(lp);
        });
    }


    public static void setShadow(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;//代表透明程度，范围为0 - 1.0f
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    public static void showHint(View view,CharSequence hint){
        Snackbar.make(view,hint,Snackbar.LENGTH_SHORT).show();
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //创建对应的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //创建对应的bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        //把drawable内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }






}
