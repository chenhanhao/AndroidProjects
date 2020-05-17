package com.kakacat.minitool.util.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.kakacat.minitool.R;

public class CircleProgressView extends View {

     /*
        原理是画一大一小圆,小圆填充外部边距颜色,最中间画text
     */

    private final static int PROGRESS = 0;
    private final static int CIRCLE_WIDTH = 20;
    private final static int CIRCLE_COLOR = Color.BLUE;
    private final static int TEXT_COLOR = Color.BLACK;
    private final static int TEXT_SIZE = 20;
    private final static String TEXT = "";

    private int progress;   //中间的进度数
    private int mCircleWidth;   //圆环的宽度
    private int mCircleColor;   //圆环的默认颜色
    private int mTextColor;  //中间text的颜色
    private int mTextSize; //中间text的size
    private String text;
    private Paint bigCirclePaint;   //大圆的画笔
    private Paint smallCirclePaint;//小圆的画笔
    private Paint textPaint; //text的画笔
    private RectF mRectF;


    public CircleProgressView(Context context) {
        this(context,null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleProgressView);
        progress = a.getInt(R.styleable.CircleProgressView_progress,PROGRESS);
        mCircleColor = a.getColor(R.styleable.CircleProgressView_circle_color,CIRCLE_COLOR);
        mCircleWidth = a.getInt(R.styleable.CircleProgressView_circle_width,CIRCLE_WIDTH);
        mTextColor = a.getColor(R.styleable.CircleProgressView_text_color,TEXT_COLOR);
        mTextSize = a.getInt(R.styleable.CircleProgressView_text_size,TEXT_SIZE);
        text = a.getString(R.styleable.CircleProgressView_text);
        if(TextUtils.isEmpty(text))
            text = TEXT;
        a.recycle();

        bigCirclePaint = new Paint();
        smallCirclePaint = new Paint();
        textPaint = new Paint();
        mRectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        //设置大圆
        mRectF.set(0,0,width,height);      //设置大圆rect的长宽
        bigCirclePaint.setStyle(Paint.Style.STROKE);   //设置大圆为空心,不填充内部
        bigCirclePaint.setColor(mCircleColor);   //大圆颜色
        //画弧形,从0到给定进度的弧形,100对应360度,所以进度*3.6
        canvas.drawArc(mRectF,0, (float) (progress * 3.6),false,bigCirclePaint);

        //设置小圆
        mRectF.set(mCircleWidth,mCircleWidth,width - mCircleWidth,height - mCircleWidth);
        smallCirclePaint.setStyle(Paint.Style.STROKE); //小圆设置空心
        smallCirclePaint.setStrokeWidth(mCircleWidth * 2); //设置宽度,填充圆环颜色
        smallCirclePaint.setColor(mCircleColor); //设置小圆颜色
        //画弧,原理同上
        canvas.drawArc(mRectF,0,(float) (progress * 3.6),false,smallCirclePaint);

        //设置中间的文字
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
//        textPaint.setStrokeWidth(200);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(dpToPixel(mTextSize));
        textPaint.setTextAlign(Paint.Align.CENTER);//设置对齐方式
        canvas.drawText(text,width / 2,(height - textPaint.ascent() - textPaint.descent()) / 2,textPaint);
    }


    public int getProgress(){
        return progress;
    }

    public void setProgress(int progress){
        this.progress = progress;
        invalidate();
    }


    public void setText(String text){
        this.text = text;
        invalidate();
    }


    public void setCircleColor(int color){
        this.mCircleColor = color;
        invalidate();
    }


    public void setCircleWidth(int circleWidth){
        this.mCircleWidth = circleWidth;
        invalidate();
    }


    private float dpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

}
