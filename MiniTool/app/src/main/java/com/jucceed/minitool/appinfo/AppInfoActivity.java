package com.jucceed.minitool.appinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jucceed.minitool.R;

import java.util.ArrayList;
import java.util.List;

public class AppInfoActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private TextView tvAndroidVersion;
    private TextView tvSecurityPatchLevel;
    private TextView tvDeviceName;
    private List<Api> apiList;
    private List<AppDetail> appDetailList;
    private ItemAdapter1 adapter1;
    private ItemAdapter2 adapter2;
    private RecyclerView recycleView1;
    private RecyclerView recycleView2;

    private ProgressBar progressBar;

    private boolean dataFinish1 = false;
    private boolean dataFinish2 = false;

    public static String[] androidApiMap = new String[]
    {"","1.0", "1.1","Cupcake","Donut","Eclair",
            "Eclair","Eclair","Froyo","Gingerbread","Gingerbread",
            "Honeycomb","Honeycomb","Honeycomb","Ice Cream Sandwich","Ice Cream Sandwich",
            "Jelly Bean","Jelly Bean","Jelly Bean","KitKat","KitKat",
            "Lollipop","Lollipop","Marshmallow","Nougat","Nougat",
            "Oreo","Oreo","Pie","Android Q"};

    public static int[] icons = new int[]
    {0,0,0,R.drawable.android_cupcake,R.drawable.android_donut,R.drawable.android_eclair,
            R.drawable.android_eclair,R.drawable.android_eclair,R.drawable.android_froyo,R.drawable.android_gingerbread,R.drawable.android_gingerbread,
            R.drawable.android_honeycomb,R.drawable.android_honeycomb,R.drawable.android_honeycomb,R.drawable.android_ice_cream_sandwich,R.drawable.android_ice_cream_sandwich,
            R.drawable.android_jelly_bean,R.drawable.android_jelly_bean,R.drawable.android_jelly_bean,R.drawable.android_kitkat,R.drawable.android_kitkat,
            R.drawable.android_lollipop,R.drawable.android_lollipop,R.drawable.android_marshmallow,R.drawable.android_nougat,R.drawable.android_nougat,
            R.drawable.android_oreo,R.drawable.android_oreo,R.drawable.android_pie,R.drawable.android_q};


    private Handler handle = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                adapter1.notifyDataSetChanged();
                dataFinish1 = true;
                recycleView1.setVisibility(View.VISIBLE);
            }else if(msg.what == 2){
                adapter2.notifyDataSetChanged();
                dataFinish2 = true;
                recycleView2.setVisibility(View.VISIBLE);
            }
            if(dataFinish1 && dataFinish2) progressBar.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        initData();
        initWidget();
    }

    @Override
    public void onResume(){
        super.onResume();
        getAppData();
    }


    private void getAppData(){
        new Thread(()->{
            int[] apis = new int[30];

            PackageManager pm = getPackageManager();
            List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);

            new Thread(()->{
                float count = packageInfoList.size();
                for(PackageInfo packageInfo : packageInfoList){
                    apis[packageInfo.applicationInfo.targetSdkVersion]++;
                }
                for(int i = apis.length - 1; i >=0; i--){
                    if(apis[i] != 0){
                        String s = (apis[i]/count*100 + "");
                        s = s.substring(0,s.indexOf('.') + 2) + "%";
                        apiList.add(new Api(icons[i],androidApiMap[i],"API " + i,apis[i],s));
                    }
                }
                Message message1 = new Message();
                message1.what = 1;
                handle.sendMessage(message1);
            }).start();

            new Thread(()->{
                for(PackageInfo packageInfo : packageInfoList){
                    AppDetail appDetail = new AppDetail();
                    appDetail.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
                    appDetail.setAppName((String) packageInfo.applicationInfo.loadLabel(pm));
                    appDetail.setPackageName(packageInfo.packageName);
                    appDetail.setAppVersionCode(packageInfo.versionName);
                    appDetail.setAppAndroidVersion(androidApiMap[packageInfo.applicationInfo.targetSdkVersion]);
                    appDetail.setAppApiLevel(packageInfo.applicationInfo.targetSdkVersion + "");
                    appDetailList.add(appDetail);
                }
                Message message2 = new Message();
                message2.what = 2;
                handle.sendMessage(message2);
            }).start();
        }).start();
    }

    private void initData(){
        apiList = new ArrayList();
        appDetailList = new ArrayList();
    }

    private void initWidget(){
        context = AppInfoActivity.this;

        initToolbar();

        tvAndroidVersion = findViewById(R.id.tv_android_version);
        tvSecurityPatchLevel = findViewById(R.id.tv_security_patch_level);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tvAndroidVersion.setText("Android " + Build.VERSION.RELEASE);
        tvSecurityPatchLevel.setText(Build.VERSION.SECURITY_PATCH);
        tvDeviceName.setText(Build.DEVICE);

        progressBar = findViewById(R.id.progress_bar_app_info);

        initRecycleView();
    }


    private void initRecycleView(){
        adapter1 = new ItemAdapter1(apiList);
        recycleView1 = findViewById(R.id.rv_api_percent);
        recycleView1.setAdapter(adapter1);
        recycleView1.setLayoutManager(new LinearLayoutManager(context));

        adapter2 = new ItemAdapter2(appDetailList);
        recycleView2 = findViewById(R.id.rv_app_detail);
        recycleView2.setAdapter(adapter2);
        recycleView2.setLayoutManager(new LinearLayoutManager(context));
    }


    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar_app_info);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_app_info,menu);
        return true;
    }
}
