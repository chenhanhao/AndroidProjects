package com.kakacat.minitool.globalOutbreak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.ui.MyPopupWindow;

public class DetailPopupWindow extends MyPopupWindow {

    private static DetailPopupWindow detailPopupWindow;
    private View contentView;
    private EpidemicData data;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;

    private DetailPopupWindow(Context context, View contentView, int width, int height) {
        super(context, contentView, width, height);
    }

    static DetailPopupWindow getInstance(Context context, int resId, int width, int height,EpidemicData data){
        if(detailPopupWindow == null){
            View contentView = View.inflate(context,resId,null);
            detailPopupWindow = new DetailPopupWindow(context, contentView, width, height);
            detailPopupWindow.contentView = contentView;
            detailPopupWindow.init();
        }
        detailPopupWindow.data = data;
        return detailPopupWindow;
    }

    private void init(){
        tv1 = contentView.findViewById(R.id.tv_country_current_confirmed_count);
        tv2 = contentView.findViewById(R.id.tv_country_confirmed_count);
        tv3 = contentView.findViewById(R.id.tv_country_cured_count);
        tv4 = contentView.findViewById(R.id.tv_country_dead_count);
        tv5 = contentView.findViewById(R.id.tv_country_dead_rate);
        tv6 = contentView.findViewById(R.id.tv_country_dead_count_rank);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setText();
    }

    @SuppressLint("SetTextI18n")
    private void setText(){
        tv1.setText(String.valueOf(data.getCurrentConfirmedCount()));
        tv2.setText(String.valueOf(data.getConfirmedCount()));
        tv3.setText(String.valueOf(data.getSuspectedCount()));
        tv4.setText(String.valueOf(data.getDeadCount()));
        tv5.setText(data.getDeadCountRate() + "%");
        tv6.setText(String.valueOf(data.getDeadCountRank()));
    }

}
