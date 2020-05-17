package com.kakacat.minitool.todayInHistory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Response;

public class TodayInHistoryActivity extends AppCompatActivity {

    private MyListView listView;
    private ItemAdapter itemAdapter;
    private Context context;
    private List<Article> articleList;
    private String key;
    private String address;
    private int year;
    private int month;
    private int day;
    private boolean first;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 666){
                itemAdapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        initData();
        initWidget();
    }


    @Override
    public void onResume(){
        super.onResume();
        refreshData();
    }


    private void initData(){
        first = true;
        articleList = new ArrayList();
        key = "9aac7a73878303c4559180d1272e4a8e";
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DATE);
        address = getAddress();
    }

    private void initWidget(){
        context = TodayInHistoryActivity.this;

        initToolbar();
        initListView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showCalendarDialog());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showCalendarDialog(){
        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                month = monthOfYear + 1;
                day = dayOfMonth;
                address = getAddress();
                first = false;
                refreshData();
            }
        }, year, month - 1, day);//2013:初始年份，2：初始月份-1 ，1：初始日期
        dp.show();
    }


    private void refreshData(){
        HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onSuccess(Response response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!first) articleList.clear();
                JsonUtil.handleHistoryResponse(s,articleList);
                Message message = new Message();
                message.what = 666;      //666为刷新数据
                handler.sendMessage(message);
            }

            @Override
            public void onError() {
                Log.d("hhh","get data failed");
            }
        });
    }


    private String getAddress(){
        return "http://api.juheapi.com/japi/toh?key=" + key + "&v=1.0&month=" + month + "&day=" + day;
    }



    private void initListView(){
        listView = findViewById(R.id.list_view);
        itemAdapter = new ItemAdapter(context,R.layout.article_layout,articleList);
        listView.setAdapter(itemAdapter);
    }


    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(true);
        }
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
