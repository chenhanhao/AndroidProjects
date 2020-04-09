package com.jucceed.minitool.wifipasswordview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jucceed.minitool.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class WifiPwdViewActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Wifi> wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_pwd_view);

        initData();
        initWidget();
    }


    @SuppressLint("ResourceType")
    private void initWidget(){

        initToolbar();
        initRecycleView();
    }


    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar_wifi_pwd_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    private void initRecycleView(){
        itemAdapter = new ItemAdapter(wifiList);
        recyclerView = findViewById(R.id.rv_wifi);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
    }


    private void initData(){
        context = WifiPwdViewActivity.this;
        wifiList = new ArrayList();
        if(getSuperUserRight()){
 //           Log.d("hhh","获取root权限成功");
            try{
                String path = getExternalCacheDir().getAbsolutePath() + "/WifiConfigStore.xml";
                File wifiConfigFile = new File(path);
                BufferedReader br = new BufferedReader(new FileReader(wifiConfigFile));

                String s;

                List<String> nameList = new ArrayList();
                List<String> pwdList = new ArrayList();
                while((s = br.readLine()) != null){
   //                 Log.d("hhh",s);
                    if(s.contains("<string name=\"SSID\">")){
                        int beginIndex = s.indexOf(';') + 1;
                        int endIndex = s.lastIndexOf('&');
                        nameList.add(s.substring(beginIndex,endIndex));
   //                     Log.d("hhh",s.substring(beginIndex,endIndex));
                    }else if(s.contains("\"PreSharedKey\"")){
                        int beginIndex = s.indexOf(';') + 1;
                        int endIndex = s.lastIndexOf('&');
                        if(beginIndex == -1 || endIndex == -1){
                            pwdList.add("没密码啊");
                        }else{
                            pwdList.add(s.substring(beginIndex,endIndex));
                        }

    //                    Log.d("hhh",s.substring(beginIndex,endIndex));
                    }
                }
                for(int i = 0; i < nameList.size(); i++){
                    Wifi wifi = new Wifi();
                    wifi.setWifiImage(nameList.get(i).substring(0,1));
                    wifi.setWifiName(nameList.get(i));
                    wifi.setWifiPwd(pwdList.get(i));
                    wifiList.add(wifi);
                }
                br.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(context,"获取root权限失败",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getSuperUserRight(){
        try{
            String fileName = "/data/misc/wifi/WifiConfigStore.xml";
            String cacheDir = getExternalCacheDir().getAbsolutePath();
            DataOutputStream os;
            Process process;

            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("chmod 777 " + fileName + "\n");
            os.writeBytes("cp " + fileName + " " + cacheDir + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();

            return true;
        }catch (Exception e){
            Log.d("hhh","获取root权限失败");
            e.printStackTrace();
            return false;
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
