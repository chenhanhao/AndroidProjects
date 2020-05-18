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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakacat.minitool.R;

public class SearchBar extends FrameLayout {
    private static final int BORDER_WIDTH = 8;
    private static final int BORDER_COLOR = Color.LTGRAY;
    private static final int BORDER_SELECTED_COLOR = Color.CYAN;
    private static final int IMAGE_ID = R.drawable.ic_search;
    private static final int IMAGE_WIDTH = 30;
    private static final int IMAGE_HEIGHT = 30;
    private static final int BORDER_RADIUS_X = 8;
    private static final int BORDER_RADIUS_Y = 8;
    private static final int EDIT_TEXT_PADDING_LEFT = 10;
    private static final int EDIT_TEXT_PADDING_TOP = 10;
    private static final int EDIT_TEXT_PADDING_BOTTOM = 10;
    private static final CharSequence HINT = "请输入内容";

    private int borderWidth;
    private int borderColor;
    private int borderSelectedColor;
    private int borderRadiusX;
    private int borderRadiusY;
    private int imageId;
    private int imageWidth;
    private int imageHeight;
    private int currentBorderColor;

    private CharSequence hint;
    public EditText editText;
    public ImageView imageView;
    private Context context;
    private Canvas canvas;
    private Paint borderPaint;
    private RectF borderRectF;

    public SearchBar(@NonNull Context context) {
        this(context,null);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawBorder();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        setFocused(true);
        return super.dispatchTouchEvent(ev);
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs){
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchBar);
        borderWidth = a.getDimensionPixelSize(R.styleable.SearchBar_border_width,BORDER_WIDTH);
        borderColor = a.getColor(R.styleable.SearchBar_border_color,BORDER_COLOR);
        borderSelectedColor = a.getColor(R.styleable.SearchBar_border_selected_color,BORDER_SELECTED_COLOR);
        borderRadiusX = a.getDimensionPixelSize(R.styleable.SearchBar_border_radius_x,BORDER_RADIUS_X);
        borderRadiusY = a.getDimensionPixelSize(R.styleable.SearchBar_border_radius_y,BORDER_RADIUS_Y);
        imageId = a.getResourceId(R.styleable.SearchBar_image_src, IMAGE_ID);
        imageWidth = a.getDimensionPixelSize(R.styleable.SearchBar_image_width,IMAGE_WIDTH);
        imageHeight = a.getDimensionPixelSize(R.styleable.SearchBar_image_height,IMAGE_HEIGHT);
        hint = a.getText(R.styleable.SearchBar_hint);
        if(!TextUtils.isEmpty(hint))
            hint = HINT;

        a.recycle();

        borderPaint = new Paint();
        borderRectF = new RectF();

        initImage();
        initEditText();
        addView(editText);
        addView(imageView);
        setWillNotDraw(false);
    }

    private void initImage(){
        imageView = new ImageView(context);

        int width = dpToPixel(imageWidth);
        int height = dpToPixel(imageHeight);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width,height);
        lp.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(lp);
        imageView.setImageResource(imageId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void initEditText(){
        editText = new EditText(context);
        int paddingLeft = dpToPixel(EDIT_TEXT_PADDING_LEFT);
        int paddingTop = dpToPixel(EDIT_TEXT_PADDING_TOP);
        int paddingRight = dpToPixel(imageWidth);
        int paddingBottom = dpToPixel(EDIT_TEXT_PADDING_BOTTOM);
        editText.setHint(hint);
        editText.setBackground(null);      //去掉下划线
        editText.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        currentBorderColor = borderColor;
    }


    private void setFocused(boolean focused){
        if(focused && currentBorderColor != borderSelectedColor){
            currentBorderColor = borderSelectedColor;
            invalidate();
        }
    }

    private void drawBorder(){
        int width = getWidth();
        int height = getHeight();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(currentBorderColor);
        borderRectF.set(0,0,width,height);
        canvas.drawRoundRect(borderRectF,dpToPixel(borderRadiusX),dpToPixel(borderRadiusY),borderPaint);
    }


    private static int dpToPixel(int dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }
}
