package com.kakacat.minitool.globalOutbreak;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kakacat.minitool.R;

import java.util.ArrayList;
import java.util.List;


public class GlobalOutbreakActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvUpdateTime;
    private MyPageAdapter myPageAdapter;

    private List<String> titleList;
    private List<MyFragment> myFragmentList;
    private List<EpidemicData> epidemicDataList1;
    private List<EpidemicData> epidemicDataList2;
    private List<EpidemicData> epidemicDataList3;
    private List<EpidemicData> epidemicDataList4;
    private List<EpidemicData> epidemicDataList5;
    private List<EpidemicData> epidemicDataList6;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_outbreak);

        initWidget();


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
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager(),titleList,myFragmentList);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tvUpdateTime = findViewById(R.id.tv_update_time);

        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setVisibility(View.VISIBLE);
    }

    private void fillList() {
        titleList = new ArrayList<>();
        myFragmentList = new ArrayList<>();
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
            default:
                break;
        }
        return true;
    }

}
