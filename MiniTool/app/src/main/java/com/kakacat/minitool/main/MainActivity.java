package com.kakacat.minitool.main;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kakacat.minitool.R;
import com.kakacat.minitool.main.navigation.AboutViewItemOn;
import com.kakacat.minitool.main.navigation.ChangeThemeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private List<MyTab> myTabList;
    private List<MyFragment> myFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillList();
        initWidget();
    }

    private void fillList() {
        myTabList = new ArrayList<>();
        myFragmentList = new ArrayList<>();
        List<MainItem> dailyItemList = new ArrayList<>();
        List<MainItem> geekItemList = new ArrayList<>();

        myTabList.add(new MyTab("日常",R.drawable.ic_daily));
        myTabList.add(new MyTab("极客",R.drawable.ic_geek));

        dailyItemList.add(new MainItem(R.string.title_currency_conversion,R.drawable.ic_money,R.string.note_currency_conversion));
        dailyItemList.add(new MainItem(R.string.title_phone_attribution,R.drawable.ic_phone,R.string.note_phone_attribution));
        dailyItemList.add(new MainItem(R.string.title_today_in_history,R.drawable.ic_today_in_history,R.string.note_today_in_history));
        dailyItemList.add(new MainItem(R.string.title_wifi_pwd_view,R.drawable.ic_wifi,R.string.note_wifi_pwd_view));
        dailyItemList.add(new MainItem(R.string.title_app_info,R.drawable.ic_app_info,R.string.note_app_info));
        dailyItemList.add(new MainItem(R.string.title_clean_file,R.drawable.ic_clean_file,R.string.note_clean_info));
        dailyItemList.add(new MainItem(R.string.title_garbage_classification,R.drawable.ic_garbage,R.string.note_garbage_classification));
        dailyItemList.add(new MainItem(R.string.title_global_outbreak,R.drawable.ic_global_outbreak,R.string.note_global_outbreak));
        dailyItemList.add(new MainItem(R.string.title_translation,R.drawable.ic_dictionary,R.string.note_translation));
        dailyItemList.add(new MainItem(R.string.title_bing_pic,R.drawable.ic_bing,R.string.note_bing_pic));

        geekItemList.add(new MainItem(R.string.title_text_encryption,R.drawable.ic_lock,R.string.note_text_encryption));
        geekItemList.add(new MainItem(R.string.title_modify_dpi,R.drawable.ic_dpi,R.string.note_modify_dpi));
        geekItemList.add(new MainItem(R.string.title_battery_info,R.drawable.ic_battery,R.string.note_battery_info));
        geekItemList.add(new MainItem(R.string.title_audio_capture,R.drawable.ic_audio_capture,R.string.note_audio_capture));
        geekItemList.add(new MainItem(R.string.title_inquire_ip,R.drawable.ic_internet,R.string.note_inquire_ip));

        myFragmentList.add(new MyDailyFragment(dailyItemList));
        myFragmentList.add(new MyGeekFragment(geekItemList));
    }
    private void initWidget(){
        setSupportActionBar(findViewById(R.id.toolbar_main));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_slide);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this, myTabList, myFragmentList);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
        for(int i = 0; i < tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(myPagerAdapter.getTabView(i));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                assert view != null;
                view.setBackgroundResource(R.color.textGrey);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                assert view != null;
                view.setBackgroundResource(R.color.white);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_theme:{
                    ChangeThemeView changeThemeView = ChangeThemeView.getInstance(MainActivity.this, View.inflate(this,R.layout.select_theme,null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    changeThemeView.showAtLocation(navigationView, Gravity.CENTER,0,0);
                    break;
                }
                case R.id.nav_setting:{

                    break;
                }
                case R.id.nav_about:{
                    AboutViewItemOn aboutView = AboutViewItemOn.getInstance(MainActivity.this,View.inflate(this,R.layout.about_layout,null), ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    aboutView.showAtLocation(navigationView,Gravity.CENTER,0,0);
                    break;
                }
                case R.id.nav_exit:{
                    finish();
                    break;
                }
                default:
                    break;
            }
            item.setChecked(false);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if (menuItem.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else
                drawerLayout.openDrawer(GravityCompat.START);

        }
        return true;
    }




}
