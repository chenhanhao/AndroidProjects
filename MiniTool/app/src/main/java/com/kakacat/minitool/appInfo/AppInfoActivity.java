package com.kakacat.minitool.appInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.ui.MyPopupWindow;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AppInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;

    private ItemAdapter adapter;
    private PopupWindow popupWindow;
    private View contentView2;
    private LinearLayout linearLayout;
    private LinearLayout llOverviewContent;
    private LayoutInflater inflater;
    private SortView sortView;

    private PackageManager pm;
    private List<PackageInfo> packageInfoList;

    public static String[] androidApiMap;
    public static int[] icons;
    private int[] apis;

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
        Toolbar toolbar = findViewById(R.id.toolbar_app_info);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageView ivSort = findViewById(R.id.iv_sort);
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

    private void loadOverviewContent(){
        View contentView = inflater.inflate(R.layout.content_overview, linearLayout, false);
        llOverviewContent = contentView.findViewById(R.id.ll_overview_content);
        linearLayout.addView(contentView);
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
        adapter.setOnClickListener((v, position) -> {
            Intent intent = new Intent(AppInfoActivity.this,AppDetailActivity.class);
            intent.putExtra("packageInfo",packageInfoList.get(position));
            intent.putExtra("pm",packageInfoList.get(position));
            startActivity(intent);
        });
        RecyclerView recyclerView = contentView2.findViewById(R.id.rv_app_detail);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_sort : {
                if(sortView == null){
                    sortView = new SortView(this,
                            View.inflate(this,R.layout.sort_dialog_layout,null),
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                }
                sortView.showAtLocation(linearLayout,Gravity.CENTER,0,0);
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


    class SortView extends MyPopupWindow implements View.OnClickListener{
        private View contentView;
        private int SORT_BY_APP_NAME = 1;
        private int SORT_BY_TARGET_API = 2;
        private int SORT_BY_MIN_API = 3;
        private int SORT_BY_FIRST_INSTALL_TIME = 4;
        private int SORT_BY_LAST_UPDATE_TIME = 5;

        private SortView(Activity activity, View contentView, int width, int height) {
            super(activity, contentView, width, height);
            this.contentView = contentView;
            initView();
        }

        private void initView(){
            RadioButton rbSortByAppName = contentView.findViewById(R.id.rb_sort_by_app_name);
            RadioButton rbSortByTargetAPI = contentView.findViewById(R.id.rb_sort_by_target_api);
            RadioButton rbSortByMinAPI = contentView.findViewById(R.id.rb_sort_by_min_api);
            RadioButton rbSortByFirstInstallTime = contentView.findViewById(R.id.rb_sort_by_first_install_time);
            RadioButton rbSortByLastUpdateTime = contentView.findViewById(R.id.rb_sort_by_last_update_time);

            rbSortByAppName.setOnClickListener(this);
            rbSortByTargetAPI.setOnClickListener(this);
            rbSortByMinAPI.setOnClickListener(this);
            rbSortByFirstInstallTime.setOnClickListener(this);
            rbSortByLastUpdateTime.setOnClickListener(this);
        }


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

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rb_sort_by_app_name : {
                    dismiss();
                    sortList(SORT_BY_APP_NAME);
                    break;
                }
                case R.id.rb_sort_by_target_api : {
                    dismiss();
                    sortList(SORT_BY_TARGET_API);
                    break;
                }
                case R.id.rb_sort_by_min_api : {
                    dismiss();
                    sortList(SORT_BY_MIN_API);
                    break;
                }
                case R.id.rb_sort_by_first_install_time : {
                    dismiss();
                    sortList(SORT_BY_FIRST_INSTALL_TIME);
                    break;
                }
                case R.id.rb_sort_by_last_update_time : {
                    dismiss();
                    sortList(SORT_BY_LAST_UPDATE_TIME);
                    break;
                }
            }
        }
    }

}
