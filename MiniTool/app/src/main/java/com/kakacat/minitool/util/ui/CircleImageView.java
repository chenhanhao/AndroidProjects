package com.kakacat.minitool.util.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {


    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = getOriginBitmap();
        Bitmap circleBitmap = getCircleBitmap(bitmap);

        setImageBitmap(circleBitmap);

    }


    private Bitmap getOriginBitmap(){
        Drawable drawable = getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        return bitmapDrawable.getBitmap();
    }

    private Bitmap getCircleBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width,height);
        float radius = size / 2;
        Bitmap circleBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0,0,width,height);

        paint.setAntiAlias(true);  //设置抗锯齿
        canvas.drawCircle(radius,radius,radius,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,paint);

        return circleBitmap;
    }




}
