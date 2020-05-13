package com.kakacat.minitool.bingPic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnLongClickListener;
import com.kakacat.minitool.util.ui.MyPopupWindow;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class BingPicActivity extends AppCompatActivity {


    private List<String> list;
    private ImageAdapter imageAdapter;
    private MyPopupWindow myPopupWindow;
    private View popupView;
    private ImageView bigImageView;

    private int d = 0;
    private int width = 320;
    private int height = 240;
    private int loadNum = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_pic);

        initView();
    }


    private void initView(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        fillList();
        imageAdapter = new ImageAdapter(list);

        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            View view = nestedScrollView.getChildAt(0);
            int bottom = view.getBottom();
            bottom -= nestedScrollView.getHeight() + nestedScrollView.getScrollY();
            if(bottom == 0){
                Log.d("hhh","加载更多");
                fillList();
                imageAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == SCROLL_STATE_IDLE && Glide.with(BingPicActivity.this).isPaused())
                    Glide.with(BingPicActivity.this).resumeRequests();
                else if(newState == SCROLL_STATE_DRAGGING){
                    Glide.with(BingPicActivity.this).pauseRequests();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        imageAdapter.setOnClickListener((v, position) -> {
            ImageView imageView = v.findViewById(R.id.image_view);
            if(popupView == null){
                popupView = LayoutInflater.from(BingPicActivity.this).inflate(R.layout.big_image_layout,null,false);
                bigImageView = popupView.findViewById(R.id.image_view);
            }
            if(myPopupWindow == null){
                myPopupWindow = new MyPopupWindow(BingPicActivity.this,popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                myPopupWindow.setAlpha(0.0f);
            }
            bigImageView.setImageDrawable(imageView.getDrawable());
            myPopupWindow.showAtLocation(recyclerView, Gravity.CENTER,0,0);
        });

        imageAdapter.setOnLongClickListener((v, position) -> {
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        });

    }


    private String getNextAddress(){
        return "https://bing.ioliu.cn/v1/rand" +
                "?d=" + (d++) +
                "?w=" + width +
                "height" + height;
    }


    private void fillList(){
        for(int i = 0; i < loadNum; i++)
            list.add(getNextAddress());
    }

}
