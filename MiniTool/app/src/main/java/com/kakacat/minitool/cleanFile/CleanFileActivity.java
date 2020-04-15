package com.kakacat.minitool.cleanFile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.StringUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CleanFileActivity extends AppCompatActivity implements View.OnClickListener,TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;

    private ProgressBar progressBar;

    private ViewPager viewPager;

    private Button btSelectAll;
    private Button btDelete;
    private Button btCancel;

    private FloatingActionButton fab;

    private View popupWindowViewWarn1;

    private PopupWindow popupWindowWarn1;

    private ItemAdapter bigFileAdapter;
    private ItemAdapter emptyFileAdapter;
    private ItemAdapter emptyDirAdapter;
    private ItemAdapter apkAdapter;

    private List<FileItem> bigFileList;
    private List<FileItem> emptyFileList;
    private List<FileItem> emptyDirList;
    private List<FileItem> apkList;
    private List<String> titleList;
    private List<MyFragment> fragmentList;

    private long deleteFileSize;

    private int currentTabPosition;

    private boolean isSelectAllBigFile;
    private boolean isSelectAllEmptyFile;
    private boolean isSelectAllEmptyDir;
    private boolean isSelectAllApk;
    private boolean initPopupWindowWarn1;

    private Handler handle = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            notifyAdapter();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_file);

        initWidget();
        setListener();
    }

    private void initWidget() {
        initToolbar();
        progressBar = findViewById(R.id.progress_bar);
        fab = findViewById(R.id.fab_delete);
        initTabLayout();
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        btSelectAll = findViewById(R.id.bt_select_all);

        bigFileList = new ArrayList<>();
        emptyFileList = new ArrayList<>();
        emptyDirList = new ArrayList<>();
        apkList = new ArrayList<>();
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        askPermissions();

        titleList.add("大文件");
        titleList.add("空文件");
        titleList.add("空文件夹");
        titleList.add("安装包");

        bigFileAdapter = new ItemAdapter(this,bigFileList);
        emptyFileAdapter = new ItemAdapter(this,emptyFileList);
        emptyDirAdapter = new ItemAdapter(this,emptyDirList);
        apkAdapter = new ItemAdapter(this,apkList);

        fragmentList.add(new MyFragment(this, bigFileAdapter,bigFileList));
        fragmentList.add(new MyFragment(this, emptyFileAdapter,emptyFileList));
        fragmentList.add(new MyFragment(this, emptyDirAdapter,emptyDirList));
        fragmentList.add(new MyFragment(this, apkAdapter,apkList));

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager(),titleList,fragmentList);
        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        new Thread(()->{
            getFileList(Environment.getExternalStorageDirectory());
            Message msg = new Message();
            handle.sendMessage(msg);
        }).start();
    }

    private void setListener(){
        tabLayout.addOnTabSelectedListener(this);
        btSelectAll.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        }
    }

    private void askPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            for(int result : grantResults)
                if(result != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"无法获得权限",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    private void getFileList(File file){
        if(file.isFile()){
            if(file.length() == 0)
                emptyFileList.add(new FileItem(file,false));
            else{
                if(file.getName().endsWith("apk")) apkList.add(new FileItem(file,false));
                if(file.length() > 30 * 1024 * 1024) bigFileList.add(new FileItem(file,false));
            }
        }else if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files == null || files.length == 0)
                emptyDirList.add(new FileItem(file,false));
            else
                for(File file1 : files)
                    getFileList(file1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_select_all:{
                if(currentTabPosition == 0){
                    isSelectAllBigFile = !isSelectAllBigFile;
                    for(FileItem fileItem : bigFileList)
                        fileItem.setChecked(isSelectAllBigFile);
                    if(isSelectAllBigFile) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    bigFileAdapter.notifyDataSetChanged();
                }else if(currentTabPosition == 1){
                    isSelectAllEmptyFile = !isSelectAllEmptyFile;
                    for(FileItem fileItem : emptyFileList)
                        fileItem.setChecked(isSelectAllEmptyFile);
                    if(isSelectAllEmptyFile) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    emptyFileAdapter.notifyDataSetChanged();
                }else if(currentTabPosition == 2){
                    isSelectAllEmptyDir= !isSelectAllEmptyDir;
                    for(FileItem fileItem : emptyDirList)
                        fileItem.setChecked(isSelectAllEmptyDir);
                    if(isSelectAllEmptyDir) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    emptyDirAdapter.notifyDataSetChanged();
                }else if(currentTabPosition == 3){
                    isSelectAllApk = !isSelectAllApk;
                    for(FileItem fileItem : apkList)
                        fileItem.setChecked(isSelectAllApk);
                    if(isSelectAllApk) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    apkAdapter.notifyDataSetChanged();
                }
                break;
            }
            case R.id.fab_delete:{
                initPopupWindow();
                popupWindowWarn1.showAtLocation(viewPager, Gravity.CENTER,0,0);
                break;
            }
            case R.id.bt_delete_file:{
                int num = deleteSelectedFile();
                notifyAdapter();
                popupWindowWarn1.dismiss();
                String s = "一共清理了" + num + "个文件,释放空间" + StringUtil.byteToMegabyte(deleteFileSize);
                Snackbar.make(viewPager, s,Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.bt_cancel:{
                popupWindowWarn1.dismiss();
                break;
            }
        }
    }

    private int deleteSelectedFile() {
        int num = 0;
        deleteFileSize = 0;
        for(int i = bigFileList.size() - 1; i >= 0; i--) {
            FileItem fileItem = bigFileList.get(i);
            if (fileItem.getChecked()) {
                num++;
                deleteFileSize += fileItem.getFile().length();
                fileItem.getFile().delete();
                bigFileList.remove(i);
            }
        }
        for(int i = emptyFileList.size() - 1; i >= 0; i--) {
            FileItem fileItem = emptyFileList.get(i);
            if (fileItem.getChecked()) {
                num++;
                deleteFileSize += fileItem.getFile().length();
                fileItem.getFile().delete();
                emptyFileList.remove(i);
            }
        }
        for(int i = emptyDirList.size() - 1; i >= 0; i--) {
            FileItem fileItem = emptyDirList.get(i);
            if (fileItem.getChecked()) {
                num++;
                deleteFileSize += fileItem.getFile().length();
                fileItem.getFile().delete();
                emptyDirList.remove(i);
            }
        }
        for(int i = apkList.size() - 1; i >= 0; i--) {
            FileItem fileItem = apkList.get(i);
            if (fileItem.getChecked()) {
                num++;
                deleteFileSize += fileItem.getFile().length();
                fileItem.getFile().delete();
                apkList.remove(i);
            }
        }
        return num;
    }

    private void initPopupWindow(){
        if(!initPopupWindowWarn1){
            popupWindowViewWarn1 = View.inflate(this,R.layout.popupwindow_warn1,null);
            popupWindowWarn1 = new PopupWindow(popupWindowViewWarn1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            UiUtil.initPopupWindow(this,popupWindowWarn1);
            btDelete = popupWindowViewWarn1.findViewById(R.id.bt_delete_file);
            btCancel = popupWindowViewWarn1.findViewById(R.id.bt_cancel);
            btDelete.setOnClickListener(this);
            btCancel.setOnClickListener(this);

            initPopupWindowWarn1 = true;
        }
        setShadow();
    }


    private void setShadow(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;//代表透明程度，范围为0 - 1.0f
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void notifyAdapter(){
        bigFileAdapter.notifyDataSetChanged();
        emptyFileAdapter.notifyDataSetChanged();
        emptyDirAdapter.notifyDataSetChanged();
        apkAdapter.notifyDataSetChanged();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentTabPosition = tab.getPosition();
        switch (currentTabPosition){
            case 0:{
                if(isSelectAllBigFile) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                break;
            }
            case 1:{
                if(isSelectAllEmptyFile) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                break;
            }
            case 2:{
                if(isSelectAllEmptyDir) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                break;
            }
            case 3:{
                if(isSelectAllApk) btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                else btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                break;
            }
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
