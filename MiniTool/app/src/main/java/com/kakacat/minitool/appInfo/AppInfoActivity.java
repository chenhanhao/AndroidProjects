package com.kakacat.minitool.appInfo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.kakacat.minitool.R;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private Toolbar toolbar;
    private ImageView ivSort;
    private ItemAdapter adapter;
    private RecyclerView recyclerView;
    private PopupWindow popupWindow;
    private RadioButton rbSortByAppName;
    private RadioButton rbSortByTargetAPI;
    private RadioButton rbSortByMinAPI;
    private RadioButton rbSortByFirstInstallTime;
    private RadioButton rbSortByLastUpdateTime;
    private View popupWindowView;
    private View contentView1;
    private View contentView2;
    private LinearLayout linearLayout;
    private LinearLayout llOverviewContent;
    private LayoutInflater inflater;

    private PackageManager pm;
    private List<PackageInfo> packageInfoList;

    public static String[] androidApiMap;
    public static int[] icons;
    private int[] apis;
    private int SORT_BY_APP_NAME = 1;
    private int SORT_BY_TARGET_API = 2;
    private int SORT_BY_MIN_API = 3;
    private int SORT_BY_FIRST_INSTALL_TIME = 4;
    private int SORT_BY_LAST_UPDATE_TIME = 5;
    private boolean initialized = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        context = AppInfoActivity.this;
        inflater = LayoutInflater.from(context);
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
    }

    private void loadOverviewHeader(){
        View view = inflater.inflate(R.layout.header_overview,linearLayout,false);
        linearLayout.addView(view);
    }

    /* 加载统计信息

     */
    private void loadOverviewContent(){
        contentView1 = inflater.inflate(R.layout.content_overview,linearLayout,false);
        llOverviewContent = contentView1.findViewById(R.id.ll_overview_content);
        linearLayout.addView(contentView1);
    }

    private void loadDetailHeader(){
        View view = inflater.inflate(R.layout.header_detail,linearLayout,false);
        linearLayout.addView(view);
    }

    private void loadDetailContent(){
        contentView2 = inflater.inflate(R.layout.content_detail,linearLayout,false);
        linearLayout.addView(contentView2);
    }


    private void getData(){
        apis = new int[30];

        androidApiMap = new String[]    // 初始化对于API对应关系
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
        pm = getPackageManager();


        // 传入的flag不同,得到的list不一样,例如传入PackageManager.GET_ACTIVITIES可得到activity相关信息,具体可查看源码
        packageInfoList = pm.getInstalledPackages(PackageManager.GET_SIGNATURES|PackageManager.GET_PERMISSIONS);
        getData1();
        adapter = new ItemAdapter(packageInfoList,pm);
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(AppInfoActivity.this,AppDetailActivity.class);
                intent.putExtra("packageInfo",packageInfoList.get(position));
                intent.putExtra("pm",packageInfoList.get(position));
                startActivity(intent);
            }
        });
        recyclerView = contentView2.findViewById(R.id.rv_app_detail);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getData1(){
        float count = packageInfoList.size();
        for(PackageInfo packageInfo : packageInfoList) {
            apis[packageInfo.applicationInfo.targetSdkVersion]++;
        }
        for(int i = apis.length - 1; i >=0; i--) {
            if (apis[i] != 0) {
                String s = String.valueOf(apis[i] / count * 100);
                s = s.substring(0, s.indexOf('.') + 2) + "%";

                View view = inflater.inflate(R.layout.item_api_percent,llOverviewContent,false);
                CircleImageView cvAndroidLogo = view.findViewById(R.id.cv_android_logo);
                TextView tvAndroidVersionName = view.findViewById(R.id.tv_android_version_name);
                TextView tvApiLevel = view.findViewById(R.id.tv_api_level);
                TextView tvAppNum = view.findViewById(R.id.tv_app_num);
                TextView tvAppPercent = view.findViewById(R.id.tv_app_percent);
                cvAndroidLogo.setImageResource(icons[i]);
                tvAndroidVersionName.setText(androidApiMap[i]);
                tvApiLevel.setText("API " + i);
                tvAppNum.setText(apis[i] + "");
                tvAppPercent.setText(s);
                llOverviewContent.addView(view);
            }
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList(int tag){
        Collections.sort(packageInfoList, (o1, o2) -> {
            if(tag == SORT_BY_APP_NAME){
                String s1 = (String) o1.applicationInfo.loadLabel(pm);
                String s2 = (String) o2.applicationInfo.loadLabel(pm);
                return s2.compareTo(s1);
            }else if(tag == SORT_BY_TARGET_API){
                return o2.applicationInfo.targetSdkVersion - o1.applicationInfo.targetSdkVersion;
            }else if(tag == SORT_BY_MIN_API){
                return o2.applicationInfo.minSdkVersion - o1.applicationInfo.minSdkVersion;
            }else if(tag == SORT_BY_FIRST_INSTALL_TIME){
                long diff = o2.firstInstallTime - o1.firstInstallTime;
                if(diff > 0) return 1;
                else if(diff == 0) return 0;
                else return -1;
            }else if(tag == SORT_BY_LAST_UPDATE_TIME){
                long diff = o2.lastUpdateTime - o1.lastUpdateTime;
                if(diff > 0) return 1;
                else if(diff == 0) return 0;
                else return -1;
            }else {
                return 0;
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void showSortWindow(){
        if(!initialized){
            popupWindowView = getLayoutInflater().inflate(R.layout.sort_dialog_layout,null);
            rbSortByAppName = popupWindowView.findViewById(R.id.rb_sort_by_app_name);
            rbSortByTargetAPI = popupWindowView.findViewById(R.id.rb_sort_by_target_api);
            rbSortByMinAPI = popupWindowView.findViewById(R.id.rb_sort_by_min_api);
            rbSortByFirstInstallTime = popupWindowView.findViewById(R.id.rb_sort_by_first_install_time);
            rbSortByLastUpdateTime = popupWindowView.findViewById(R.id.rb_sort_by_last_update_time);
            rbSortByAppName.setOnClickListener(this);
            rbSortByTargetAPI.setOnClickListener(this);
            rbSortByMinAPI.setOnClickListener(this);
            rbSortByFirstInstallTime.setOnClickListener(this);
            rbSortByLastUpdateTime.setOnClickListener(this);
            initPopUpWindow(popupWindowView);
            initialized = true;
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//代表透明程度，范围为0 - 1.0f
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popupWindowView, Gravity.CENTER, 0,0);
    }

    private void initPopUpWindow(View view){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
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
                popupWindow.dismiss();
                sortList(SORT_BY_APP_NAME);
                break;
            }
            case R.id.rb_sort_by_target_api : {
                popupWindow.dismiss();
                sortList(SORT_BY_TARGET_API);
                break;
            }
            case R.id.rb_sort_by_min_api : {
                popupWindow.dismiss();
                sortList(SORT_BY_MIN_API);
                break;
            }
            case R.id.rb_sort_by_first_install_time : {
                popupWindow.dismiss();
                sortList(SORT_BY_FIRST_INSTALL_TIME);
                break;
            }
            case R.id.rb_sort_by_last_update_time : {
                popupWindow.dismiss();
                sortList(SORT_BY_LAST_UPDATE_TIME);
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
