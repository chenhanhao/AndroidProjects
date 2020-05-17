package com.kakacat.minitool.util.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private List<List<View>> lineList;
    private List<Integer> heightList;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lineList = new ArrayList<>();
        heightList = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();

        for(int i = 0; i < count; i++){
            View view = getChildAt(i);
            if(view.getVisibility() == GONE)
                continue;

            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            LayoutParams lp = view.getLayoutParams();
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            if(lp instanceof MarginLayoutParams){
                MarginLayoutParams mlp = (MarginLayoutParams)view.getLayoutParams();
                childWidth += mlp.leftMargin + mlp.rightMargin;
                childHeight += mlp.bottomMargin + mlp.topMargin;
            }
            if(lineWidth + childWidth > totalWidth){
                width = Math.max(lineWidth,childWidth);
                height += lineHeight;
                lineWidth = childWidth;
                lineHeight = childHeight;
            }else{
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }

            if(i == count - 1){
                width = Math.max(width,lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? totalWidth : width,heightMode == MeasureSpec.EXACTLY ? totalHeight : height);
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        lineList.clear();
        heightList.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();
        @SuppressLint("DrawAllocation")
        List<View> childViews = new ArrayList<>();

        for(int i = 0; i < count; i++){
            View childView = getChildAt(i);
            LayoutParams lp = childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            int leftMargin = 0,rightMargin = 0;
            int topMargin = 0,bottomMargin = 0;
            if(lp instanceof  MarginLayoutParams){
                MarginLayoutParams mlp = (MarginLayoutParams)lp;
                leftMargin = mlp.leftMargin;
                rightMargin = mlp.rightMargin;
                topMargin = mlp.topMargin;
                bottomMargin = mlp.bottomMargin;
            }

            if(childWidth + leftMargin + rightMargin + lineWidth <= width){
                lineWidth += childWidth + leftMargin + rightMargin;
                childViews.add(childView);
                lineHeight = Math.max(lineHeight,childHeight + topMargin + bottomMargin);
            }else{
                lineList.add(childViews);
                heightList.add(lineHeight);
                childViews = new ArrayList<>();
                childViews.add(childView);
                lineWidth = childWidth + leftMargin + rightMargin;
                lineHeight = childHeight + topMargin + bottomMargin;
            }
        }

        heightList.add(lineHeight);
        lineList.add(childViews);

        int left = 0;
        int top = 0;
        int lineNum = lineList.size();

        for(int i = 0; i < lineNum; i++){
            childViews = lineList.get(i);
            lineHeight = heightList.get(i);
            for(int j = 0; j < childViews.size(); j++){
                View childView = childViews.get(j);
                if(childView.getVisibility() == GONE)
                    continue;

                LayoutParams lp = childView.getLayoutParams();
                int leftMargin = 0,rightMargin = 0;
                int topMargin = 0;
                if(lp instanceof MarginLayoutParams){
                    MarginLayoutParams mlp = (MarginLayoutParams)lp;
                    leftMargin = mlp.leftMargin;
                    rightMargin = mlp.rightMargin;
                    topMargin = mlp.topMargin;
                }
                int l2 = left + leftMargin;
                int t2 = top + topMargin;
                int r2 = l2 + childView.getMeasuredWidth();
                int b2 = t2 + childView.getMeasuredHeight();
                childView.layout(l2,t2,r2,b2);
                left += childView.getMeasuredWidth() + leftMargin + rightMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }
}
