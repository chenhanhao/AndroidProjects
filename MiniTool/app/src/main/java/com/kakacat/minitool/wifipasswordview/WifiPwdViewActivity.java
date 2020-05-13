package com.kakacat.minitool.wifipasswordview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnLongClickListener;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WifiPwdViewActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private ArrayList wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_pwd_view);

        initData();
        initWidget();
    }


    private void initWidget(){

        initToolbar();
        initRecycleView();
    }


    private void initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar_wifi_pwd_view));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    private void initRecycleView(){
        myAdapter = new MyAdapter(wifiList);
        recyclerView = findViewById(R.id.rv_wifi);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
        myAdapter.setOnLongClickListener((v, position) -> {
            CharSequence wifiName = ((TextView)v.findViewById(R.id.tv_wifi_name)).getText();
            CharSequence pwd = ((TextView)v.findViewById(R.id.tv_wifi_pwd)).getText();
            SystemUtil.copyToClipboard(context,"wifiPwd",pwd);
            UiUtil.showHint(recyclerView,"\"" + wifiName + "\"的wifi密码已复制");
        });
    }


    private void initData(){
        context = WifiPwdViewActivity.this;
        wifiList = new ArrayList();
        if(getWifiConfig()){
            String filePath = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath() + "/WifiConfigStore.xml";
            parseDataToList(filePath);
        }
        else
            Toast.makeText(context,"获取wifi配置信息失败",Toast.LENGTH_SHORT).show();
    }

    private void parseDataToList(String filePath) {
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(filePath),new WiFiConfigSAXHandle(wifiList));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private boolean getWifiConfig(){
        try{
            String fileName = "/data/misc/wifi/WifiConfigStore.xml";
            String cacheDir = getExternalCacheDir().getAbsolutePath();
            String[] commands = new String[]{
                    "chmod 777 " + fileName + "\n",
                    "cp " + fileName + " " + cacheDir + "\n",
                    "exit\n"
            };
            SystemUtil.executeLinuxCommand(commands,true,true);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
