package com.jucceed.minitool.appinfo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jucceed.minitool.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private Toolbar toolbar;

    private ImageView ivSort;
    private List<Api> apiList;
    private List<AppDetail> appDetailList;
    private ItemAdapter1 adapter1;
    private ItemAdapter2 adapter2;
    private RecyclerView recycleView1;
    private RecyclerView recycleView2;
    private ProgressBar progressBar;
    private PopupWindow popupWindow;
    private RadioButton rbSortByAppName;
    private RadioButton rbSortByTargetAPI;
    private RadioButton rbSortByMinAPI;
    private RadioButton rbSortByFirstInstallTime;
    private RadioButton rbSortByLastUpdateTime;

    public static String[] androidApiMap;
    private PackageManager pm;
    private List<PackageInfo> packageInfoList;

    public static int[] icons;
    private int[] apis;
    private int SORT_BY_APP_NAME = 1;
    private int SORT_BY_TARGET_API = 2;
    private int SORT_BY_MIN_API = 3;
    private int SORT_BY_FIRST_INSTALL_TIME = 4;
    private int SORT_BY_LAST_UPDATE_TIME = 5;

    private LinearLayout linearLayout;
    private LayoutInflater inflater;

    private View contentView1;
    private View contentView2;

    private Handler handle = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                adapter1.notifyDataSetChanged();
            }else if(msg.what == 2){
                adapter2.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(100);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);


        context = AppInfoActivity.this;
        inflater = LayoutInflater.from(context);
        progressBar = findViewById(R.id.progress_bar_app_info);

        initToolbar();
        loadMainHeader();
        loadOverviewHeader();
        loadOverviewContent();
        loadDetailHeader();
        loadDetailContent();
        getData();

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
        ivSort = findViewById(R.id.iv_sort);
        ivSort.setOnClickListener(this);
        progressBar.setProgress(progressBar.getProgress() + 2);
    }

    private void loadMainHeader(){
        linearLayout = findViewById(R.id.linear_layout);
        View view = inflater.inflate(R.layout.header_main,linearLayout,false);

        TextView tvAndroidVersion = view.findViewById(R.id.tv_android_version);
        TextView tvSecurityPatchLevel = view.findViewById(R.id.tv_security_patch_level);
        TextView tvDeviceName = view.findViewById(R.id.tv_device_name);

        tvAndroidVersion.setText("Android " + Build.VERSION.RELEASE);
        tvSecurityPatchLevel.setText(Build.VERSION.SECURITY_PATCH);
        tvDeviceName.setText(Build.DEVICE);

        linearLayout.addView(view);
        progressBar.setProgress(progressBar.getProgress() + 2);
    }

    private void loadOverviewHeader(){
        View view = inflater.inflate(R.layout.header_overview,linearLayout,false);
        linearLayout.addView(view);
        progressBar.setProgress(progressBar.getProgress() + 2);
    }

    private void loadOverviewContent(){
        contentView1 = inflater.inflate(R.layout.content_overview,linearLayout,false);
        linearLayout.addView(contentView1);
        progressBar.setProgress(progressBar.getProgress() + 2);
    }

    private void loadDetailHeader(){
        View view = inflater.inflate(R.layout.header_detail,linearLayout,false);
        linearLayout.addView(view);
        progressBar.setProgress(progressBar.getProgress() + 2);
    }

    private void loadDetailContent(){
        contentView2 = inflater.inflate(R.layout.content_detail,linearLayout,false);
        linearLayout.addView(contentView2);
        progressBar.setProgress(progressBar.getProgress() + 2);
    }

    private void getData(){
        apiList = new ArrayList();
        adapter1 = new ItemAdapter1(apiList);
        recycleView1 = contentView1.findViewById(R.id.rv_api_percent);
        recycleView1.setAdapter(adapter1);
        recycleView1.setLayoutManager(new LinearLayoutManager(context));

        appDetailList = new ArrayList();
        adapter2 = new ItemAdapter2(appDetailList);
        recycleView2 = contentView2.findViewById(R.id.rv_app_detail);
        recycleView2.setAdapter(adapter2);
        recycleView2.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });
        progressBar.setProgress(progressBar.getProgress() + 4);

        new Thread(()->{
            pm = getPackageManager();
            packageInfoList = pm.getInstalledPackages(0);
            apis = new int[30];
            progressBar.setProgress(progressBar.getProgress() + 4);
            getData1();
            getData2();
        }).start();

    }


    private void getData1(){


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
        float count = packageInfoList.size();

        for(PackageInfo packageInfo : packageInfoList)
            apis[packageInfo.applicationInfo.targetSdkVersion]++;
        progressBar.setProgress(progressBar.getProgress() + 4);
        for(int i = apis.length - 1; i >=0; i--) {
            if (apis[i] != 0) {
                String s = (apis[i] / count * 100 + "");
                s = s.substring(0, s.indexOf('.') + 2) + "%";
                apiList.add(new Api(icons[i], androidApiMap[i], "API " + i, apis[i], s));
            }
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        Message message = new Message();
        message.what = 1;
        handle.sendMessage(message);
    }

    private void getData2(){


        for(PackageInfo packageInfo : packageInfoList){
            AppDetail appDetail = new AppDetail();
            int n = packageInfo.applicationInfo.targetSdkVersion;
            apis[n]++;
            appDetail.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
            appDetail.setAppName((String) packageInfo.applicationInfo.loadLabel(pm));
            appDetail.setPackageName(packageInfo.packageName);
            appDetail.setAppVersionCode(packageInfo.versionName);
            appDetail.setAppAndroidVersion(androidApiMap[n]);
            appDetail.setAppApiLevel(n + "");
            appDetailList.add(appDetail);
        }
        progressBar.setProgress(100);
        Message message2 = new Message();
        message2.what = 2;
        handle.sendMessage(message2);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList(int tag){
        progressBar.setVisibility(View.INVISIBLE);
        new Thread(()->{
            Collections.sort(packageInfoList, (o1, o2) -> {
                if(tag == SORT_BY_APP_NAME){
                    String s1 = (String) o1.applicationInfo.loadLabel(pm);
                    String s2 = (String) o2.applicationInfo.loadLabel(pm);
                    return s1.compareTo(s2);
                }else if(tag == SORT_BY_TARGET_API){
                    return o1.applicationInfo.targetSdkVersion - o1.applicationInfo.targetSdkVersion;
                }else if(tag == SORT_BY_MIN_API){
                    return o1.applicationInfo.minSdkVersion - o2.applicationInfo.minSdkVersion;
                }else if(tag == SORT_BY_FIRST_INSTALL_TIME){
                    return o1.firstInstallTime - o2.firstInstallTime >= 0 ? 1 : -1;
                }else if(tag == SORT_BY_LAST_UPDATE_TIME){
                    return o1.lastUpdateTime - o2.lastUpdateTime >= 0 ? 1 : -1;
                }else {
                    return 0;
                }
            });
            Message msg = new Message();
            msg.what = 2;
            handle.sendMessage(msg);
        }).start();
    }


    private void showSortWindow(){
        View view = getLayoutInflater().inflate(R.layout.sort_dialog_layout,null);
        rbSortByAppName = view.findViewById(R.id.rb_sort_by_app_name);
        rbSortByTargetAPI = view.findViewById(R.id.rb_sort_by_target_api);
        rbSortByMinAPI = view.findViewById(R.id.rb_sort_by_min_api);
        rbSortByFirstInstallTime = view.findViewById(R.id.rb_sort_by_first_install_time);
        rbSortByLastUpdateTime = view.findViewById(R.id.rb_sort_by_last_update_time);
        rbSortByAppName.setOnClickListener(this);
        rbSortByTargetAPI.setOnClickListener(this);
        rbSortByMinAPI.setOnClickListener(this);
        rbSortByFirstInstallTime.setOnClickListener(this);
        rbSortByLastUpdateTime.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);
        /**
         * 弹出popupWindow时设置暗背景
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//代表透明程度，范围为0 - 1.0f
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(()->{
            lp.alpha = 1.0f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_sort : {
                showSortWindow();
                break;
            }
            case R.id.rb_sort_by_app_name : {
                sortList(SORT_BY_APP_NAME);
                popupWindow.dismiss();
                break;
            }
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
