package com.jucceed.minitool.appinfo;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private ItemAdapter adapter;
    private RecyclerView recyclerView;

    public static String[] androidApiMap;
    public static int[] icons;


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

    }

    private void initData(){
        context = AppInfoActivity.this;
        androidApiMap = new String[]
                        {"","1.0", "1.1","Cupcake","Donut","Eclair",
                         "Eclair","Eclair","Froyo","Gingerbread","Gingerbread",
                         "Honeycomb","Honeycomb","Honeycomb","Ice Cream Sandwich","Ice Cream Sandwich",
                         "Jelly Bean","Jelly Bean","Jelly Bean","KitKat","KitKat",
                         "Lollipop","Lollipop","Marshmallow","Nougat","Nougat",
                         "Oreo","Oreo","Pie","Android Q"};
        icons = new int[]
                {0,0,0,R.drawable.android_cupcake,R.drawable.android_donut,R.drawable.android_eclair,
                 R.drawable.android_eclair,R.drawable.android_eclair,R.drawable.android_froyo,R.drawable.android_gingerbread,R.drawable.android_gingerbread,
                 R.drawable.android_honeycomb,R.drawable.android_honeycomb,R.drawable.android_honeycomb,R.drawable.android_ice_cream_sandwich,R.drawable.android_ice_cream_sandwich,
                 R.drawable.android_jelly_bean,R.drawable.android_jelly_bean,R.drawable.android_jelly_bean,R.drawable.android_kitkat,R.drawable.android_kitkat,
                 R.drawable.android_lollipop,R.drawable.android_lollipop,R.drawable.android_marshmallow,R.drawable.android_nougat,R.drawable.android_nougat,
                 R.drawable.android_oreo,R.drawable.android_oreo,R.drawable.android_pie,R.drawable.android_q};
        apiList = new ArrayList();
        float count;
        int[] apis = new int[30];
        PackageManager pm = getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);
        for(PackageInfo packageInfo : packageInfoList){
            apis[packageInfo.applicationInfo.targetSdkVersion]++;
            packageInfo.
        }
        count = packageInfoList.size();

        for(int i = apis.length - 1; i >=0; i--){
            if(apis[i] != 0){
                String s = (apis[i]/count*100 + "");
                s = s.substring(0,s.indexOf('.') + 2) + "%";
                apiList.add(new Api(icons[i],androidApiMap[i],"API " + i,apis[i],s));
            }
        }
    }


    private void initWidget(){
        initToolbar();

        tvAndroidVersion = findViewById(R.id.tv_android_version);
        tvSecurityPatchLevel = findViewById(R.id.tv_security_patch_level);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tvAndroidVersion.setText("Android " + Build.VERSION.RELEASE);
        tvSecurityPatchLevel.setText(Build.VERSION.SECURITY_PATCH);
        tvDeviceName.setText(Build.DEVICE);

        initRecycleView();
    }


    private void initRecycleView(){
        adapter = new ItemAdapter(apiList);
        recyclerView = findViewById(R.id.rv_api_percent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
