package com.kakacat.minitool.garbageClassification;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.JsonUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class GarbageClassificationActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private EditText editText;
    private TextView tvGarbageTitle1;
    private TextView tvGarbageContent1;
    private TextView tvGarbageTitle2;
    private TextView tvGarbageContent2;
    private TextView tvTip;
    private Button btSearch;
    private Button btClose;
    private PopupWindow popupWindow;

    private MyAdapter myAdapter;
    private List<Garbage> garbageList;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                myAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_classification);

        initWidget();
    }

    private void initWidget() {
        initToolbar();
        linearLayout = findViewById(R.id.linear_layout);
        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);
        btSearch = findViewById(R.id.bt_search);

        btSearch.setOnClickListener(v -> {
            showLoadingView();
            String s = editText.getText().toString();
            String key = "c3886d3637d56c2730a4a7066fb9fa47";
            String address = "https://api.tianapi.com/txapi/lajifenlei/index?key=" +
                    key +
                    "&word=" +
                    s;
            HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onSuccess(Response response) {
                    String s = null;
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    garbageList.clear();
                    JsonUtil.handleGarbageResponse(s,garbageList);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                @Override
                public void onError() {}
            });
        });

        garbageList = new ArrayList<>();
        myAdapter = new MyAdapter(garbageList);
        myAdapter.setOnClickListener((v, position) -> showContentView(position));
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void showContentView(int position){
        Garbage garbage = garbageList.get(position);
        int type = garbage.getType();
        View view = View.inflate(this,R.layout.content_view,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btClose = view.findViewById(R.id.bt_close);
        tvGarbageTitle1 = view.findViewById(R.id.tv_garbage_type_title1);
        tvGarbageContent1 = view.findViewById(R.id.tv_garbage_type_content1);
        tvGarbageTitle2 = view.findViewById(R.id.tv_garbage_type_title2);
        tvGarbageContent2 = view.findViewById(R.id.tv_garbage_type_content2);
        tvTip = view.findViewById(R.id.tv_tip);

        btClose.setOnClickListener(v -> popupWindow.dismiss());
        tvGarbageTitle1.setText(TypeMap.getTypeName(type) + "是什么?");
        tvGarbageContent1.setText(garbage.getExplain());
        tvGarbageTitle2.setText("有什么常见的" + TypeMap.getTypeName(type) + "?");
        tvGarbageContent2.setText(garbage.getContain());
        tvTip.setText(garbage.getTip());

        UiUtil.initPopupWindow(this,popupWindow);
        popupWindow.showAtLocation(linearLayout,Gravity.BOTTOM,0,0);
    }

    private void showLoadingView() {
        View view = LayoutInflater.from(this).inflate(R.layout.loading_view,null,false);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(this,popupWindow);
        popupWindow.showAtLocation(linearLayout, Gravity.CENTER,0,0);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }
}
