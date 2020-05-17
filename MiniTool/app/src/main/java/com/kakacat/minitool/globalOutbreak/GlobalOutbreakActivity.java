package com.kakacat.minitool.globalOutbreak;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.JsonUtil;
import com.kakacat.minitool.util.StringUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;


public class GlobalOutbreakActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvUpdateTime;
    private MyPageAdapter myPageAdapter;
    private ProgressBar progressBar;

    private List<String> titleList;
    private List<MyFragment> myFragmentList;
    private List<List<EpidemicData>> list;
    private List<EpidemicData> epidemicDataList1;
    private List<EpidemicData> epidemicDataList2;
    private List<EpidemicData> epidemicDataList3;
    private List<EpidemicData> epidemicDataList4;
    private List<EpidemicData> epidemicDataList5;
    private List<EpidemicData> epidemicDataList6;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                notifyData();
                swipeRefreshLayout.setRefreshing(false);
                UiUtil.showHint(swipeRefreshLayout,"更新数据成功");
                tvUpdateTime.setText(StringUtil.getDate(list.get(0).get(0).getModifyTime()));
                progressBar.setVisibility(View.INVISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_outbreak);

        initWidget();
        refresh();
    }

    private void initWidget() {
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }    //不知道为什么返回键没法显示...下次再填

        fillList();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        coordinatorLayout = findViewById(R.id.coordinator);
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager(),titleList,myFragmentList);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tvUpdateTime = findViewById(R.id.tv_update_time);
        progressBar = findViewById(R.id.progress_bar);

        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        swipeRefreshLayout.setOnRefreshListener(() -> refresh());
    }

    private void fillList() {
        titleList = new ArrayList<>();
        myFragmentList = new ArrayList<>();
        list = new ArrayList<>();
        epidemicDataList1 = new ArrayList<>();
        epidemicDataList2 = new ArrayList<>();
        epidemicDataList3 = new ArrayList<>();
        epidemicDataList4 = new ArrayList<>();
        epidemicDataList5 = new ArrayList<>();
        epidemicDataList6 = new ArrayList<>();

        titleList.add("亚洲");
        titleList.add("欧洲");
        titleList.add("北美洲");
        titleList.add("南美洲");
        titleList.add("非洲");
        titleList.add("其他");
        myFragmentList.add(new MyFragment(epidemicDataList1));
        myFragmentList.add(new MyFragment(epidemicDataList2));
        myFragmentList.add(new MyFragment(epidemicDataList3));
        myFragmentList.add(new MyFragment(epidemicDataList4));
        myFragmentList.add(new MyFragment(epidemicDataList5));
        myFragmentList.add(new MyFragment(epidemicDataList6));
        list.add(epidemicDataList1);
        list.add(epidemicDataList2);
        list.add(epidemicDataList3);
        list.add(epidemicDataList4);
        list.add(epidemicDataList5);
        list.add(epidemicDataList6);
    }


    private void refresh(){
        String key = "c3886d3637d56c2730a4a7066fb9fa47";
        String address = "http://api.tianapi.com/txapi/ncovabroad/index?key=" + key;

        HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onSuccess(Response response) {
                String s = null;
                try {
                    s = Objects.requireNonNull(response.body()).string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.forEach(List::clear);
                JsonUtil.handleEpidemicResponse(s,list);
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }
            @Override
            public void onError() {}
        });
    }


    private void notifyData(){
        for(MyFragment myFragment : myFragmentList)
            myFragment.refresh();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if (menuItem.getItemId() == android.R.id.home)
            finish();
        return true;
    }

}
