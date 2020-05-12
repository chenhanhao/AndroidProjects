package com.kakacat.minitool.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewClickListener;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.MyPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class AboutView extends MyPopupWindow implements RecycleViewClickListener{

    private static AboutView aboutView;
    private Context context;
    private View contentView;


    public AboutView(Activity activity, View contentView, int width, int height) {
        super(activity, contentView, width, height);
        this.context = activity;
        this.contentView = contentView;
    }

    public static AboutView getInstance(Activity activity, View contentView, int width, int height){
        if(aboutView == null){
            aboutView = new AboutView(activity, contentView, width, height);
            aboutView.initView();
        }
        return aboutView;
    }


    private void initView(){
        RecyclerView recyclerView = contentView.findViewById(R.id.rv_about);
        List<AboutItem> list = new ArrayList<>();

        list.add(new AboutItem(R.drawable.ic_black_star,"给我好评"));
        list.add(new AboutItem(R.drawable.ic_version,"版本状态"));

        AboutItemAdapter aboutItemAdapter = new AboutItemAdapter(list);
        aboutItemAdapter.setOnClickListener(this);
        recyclerView.setAdapter(aboutItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onClick(View v, int position) {
        switch (position){
            case 0:{
                SystemUtil.openMarket(context);
                break;
            }
            default:
                break;
        }
    }




}
