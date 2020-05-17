package com.kakacat.minitool.translation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.EncryptionUtil;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.JsonUtil;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class TranslationActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private ImageView ivOpenCollection;
    private ImageView ivClear;
    private EditText editText;
    private FloatingActionButton fab;
    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvOutput;
    private ImageView ivCopy;
    private ImageView ivCollect;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                String result = (String) msg.obj;
                tvOutput.setText(result);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        initWidget();
        setListener();
    }


    private void initWidget(){
        initToolbar();
        linearLayout = findViewById(R.id.linear_layout);
        ivOpenCollection = findViewById(R.id.iv_open_collect);
        ivClear = findViewById(R.id.iv_clear);
        editText = findViewById(R.id.edit_text);
        fab = findViewById(R.id.fab);
        tvFrom = findViewById(R.id.tv_from);
        tvTo = findViewById(R.id.tv_to);
        tvOutput = findViewById(R.id.tv_output);
        ivCopy = findViewById(R.id.iv_copy);
        ivCollect = findViewById(R.id.iv_collect);
    }

    private void initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setListener() {
        ivOpenCollection.setOnClickListener(this::onClick);
        ivClear.setOnClickListener(this::onClick);
        tvFrom.setOnClickListener(this::onClick);
        tvTo.setOnClickListener(this::onClick);
        fab.setOnClickListener(this::onClick);
        ivCopy.setOnClickListener(this::onClick);
        ivCollect.setOnClickListener(this::onClick);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_open_collect:{
                showCollectionWindow();
                break;
            }
            case R.id.iv_clear:{
                editText.setText("");
                break;
            }
            case R.id.tv_from:{
                showSelectLanguageWindow(1);
                break;
            }
            case R.id.tv_to:{
                showSelectLanguageWindow(2);
                break;
            }
            case R.id.fab:{
                translate();
                break;
            }
            case R.id.iv_copy:{
                copyToClipBoard();
                break;
            }
            case R.id.iv_collect:{
                addToMyFavourite();
                break;
            }
        }
    }

    private void showCollectionWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.collection_layout,null,false);
        LinearLayout linearLayout1 = view.findViewById(R.id.linear_layout_collection);
        SharedPreferences sharedPreferences = getSharedPreferences("MyFavourite",MODE_PRIVATE);
        Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();

        for(String key : map.keySet()){
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.collection_item_layout,null,false);
            textView.setText(map.get(key));
            linearLayout1.addView(textView);
        }

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(this,popupWindow);
        popupWindow.showAtLocation(linearLayout,Gravity.BOTTOM,0,0);
    }

    private void addToMyFavourite() {
        if(TextUtils.isEmpty(editText.getText().toString())){
            UiUtil.showHint(linearLayout,"请输入内容");
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("MyFavourite",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = editText.getText().toString() + ">" + tvOutput.getText();
        String content = editText.getText().toString() + "  >  " + tvOutput.getText();
        editor.putString(key,content);
        editor.commit();
        UiUtil.showHint(linearLayout,"收藏成功");
    }

    private void copyToClipBoard() {
        SystemUtil.copyToClipboard(this,"translate",tvOutput.getText());
        UiUtil.showHint(linearLayout,"复制完成");
    }

    private void showSelectLanguageWindow(int flag) {
        String[] languages = new String[]{
                "自动检测","中文","英语","粤语","文言文","日语",
                "韩语","法语","西班牙语","泰语","阿拉伯语","俄语",
                "葡萄牙语","德语","意大利语","希腊语","荷兰语","波兰语",
                "保加利亚语","爱沙尼亚语","丹麦语","芬兰语","捷克语","罗马尼亚语",
                "斯洛文尼亚语","瑞典语","匈牙利语","繁体中文","越南语"
        };
        List<String> languageList = new ArrayList<>();
        for(String language : languages) languageList.add(language);
        View view = LayoutInflater.from(this).inflate(R.layout.select_from_window,null,false);
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(this,popupWindow);

        Button btBack = view.findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> popupWindow.dismiss());
        RecyclerView rv = view.findViewById(R.id.rv_from);
        if(flag == 2) languageList.remove(0);
        MyAdapter myAdapter = new MyAdapter(languageList);
        rv.setAdapter(myAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        myAdapter.setOnClickListener((v, position) -> {
            if(flag == 2) tvTo.setText(languageList.get(position));
            else tvFrom.setText(languageList.get(position));
            popupWindow.dismiss();
        });
        popupWindow.showAtLocation(linearLayout, Gravity.CENTER,0,0);
    }

    private void translate(){
        String content = editText.getText().toString();
        String from = getShortCode(tvFrom.getText());
        String to = getShortCode(tvTo.getText());
        String random = getRandom();
        String appId = "20200420000425201";
        String secretKey = "qceN7y1RBpEp8x1g47_i";
        String s = appId + content + random + secretKey;
        String sign = EncryptionUtil.encryptionMD5(s.getBytes(),false);
        String address = "https://api.fanyi.baidu.com/api/trans/vip/translate?" +
                "q=" + content +
                "&from=" + from +
                "&to=" + to +
                "&appid=" + appId +
                "&salt=" + random +
                "&sign=" + sign;
        if(TextUtils.isEmpty(content)){
           UiUtil.showHint(linearLayout,"请输入内容");
           return;
        }

        HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onSuccess(Response response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result;
                result = JsonUtil.handleTranslationResponse(s);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError() {}
        });
    }


    private String getShortCode(CharSequence s){
        if ("自动检测".equals(s)) return "auto";
        else if ("中文".equals(s)) return "zh";
        else if ("英语".equals(s)) return "en";
        else if ("粤语".equals(s)) return "yue";
        else if ("文言文".equals(s)) return "wyw";
        else if ("日语".equals(s)) return "jp";
        else if ("韩语".equals(s)) return "kor";
        else if ("法语".equals(s)) return "fra";
        else if ("西班牙语".equals(s)) return "spa";
        else if ("泰语".equals(s)) return "th";
        else if ("阿拉伯语".equals(s)) return "ara";
        else if ("俄语".equals(s)) return "ru";
        else if ("葡萄牙语".equals(s)) return "pt";
        else if ("德语".equals(s)) return "de";
        else if ("意大利语".equals(s)) return "it";
        else if ("希腊语".equals(s)) return "el";
        else if ("荷兰语".equals(s)) return "nl";
        else if ("波兰语".equals(s)) return "pl";
        else if ("保加利亚语".equals(s)) return "bul";
        else if ("爱沙尼亚语".equals(s)) return "est";
        else if ("丹麦语".equals(s)) return "dan";
        else if ("芬兰语".equals(s)) return "fin";
        else if ("捷克语".equals(s)) return "cs";
        else if ("罗马尼亚语".equals(s)) return "rom";
        else if ("斯洛文尼亚语".equals(s)) return "slo";
        else if ("瑞典语".equals(s)) return "swe";
        else if ("匈牙利语".equals(s)) return "hu";
        else if ("繁体中文".equals(s)) return "cht";
        else if ("越南语".equals(s)) return "vie";
        else return "";
    }

    private String getRandom(){
        return String.valueOf((int)(Math.random() * 1000000));
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
