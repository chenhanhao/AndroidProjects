package com.kakacat.minitool.cleanFile;

import android.Manifest;
import android.app.Activity;
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
import com.kakacat.minitool.util.ui.MyPopupWindow;
import com.kakacat.minitool.util.ui.UiUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CleanFileActivity extends AppCompatActivity implements View.OnClickListener,TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private Button btSelectAll;
    private FloatingActionButton fab;
    private PopupWindow popupWindowWarn1;

    private ItemAdapter bigFileAdapter;
    private ItemAdapter emptyFileAdapter;
    private ItemAdapter emptyDirAdapter;
    private ItemAdapter apkAdapter;

    private List<FileItem> bigFileList;
    private List<FileItem> emptyFileList;
    private List<FileItem> emptyDirList;
    private List<FileItem> apkList;

    private long deleteFileSize;
    private int currentTabPosition;
    private boolean isSelectAllBigFile;
    private boolean isSelectAllEmptyFile;
    private boolean isSelectAllEmptyDir;
    private boolean isSelectAllApk;
    private boolean initPopupWindowWarn1;

    Handler handle = new Handler(){
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
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        }
        progressBar = findViewById(R.id.progress_bar);
        fab = findViewById(R.id.fab_delete);
        btSelectAll = findViewById(R.id.bt_select_all);
        initTabLayout();
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        bigFileList = new CopyOnWriteArrayList<>();
        emptyFileList = new CopyOnWriteArrayList<>();
        emptyDirList = new CopyOnWriteArrayList<>();
        apkList = new CopyOnWriteArrayList<>();
        List<String> titleList = new ArrayList<>();
        List<MyFragment> fragmentList = new ArrayList<>();

        askPermissions();

        titleList.add("大文件");
        titleList.add("空文件");
        titleList.add("空文件夹");
        titleList.add("安装包");

        bigFileAdapter = new ItemAdapter(bigFileList);
        emptyFileAdapter = new ItemAdapter(emptyFileList);
        emptyDirAdapter = new ItemAdapter(emptyDirList);
        apkAdapter = new ItemAdapter(apkList);

        fragmentList.add(new MyFragment(this, bigFileAdapter,bigFileList));
        fragmentList.add(new MyFragment(this, emptyFileAdapter,emptyFileList));
        fragmentList.add(new MyFragment(this, emptyDirAdapter,emptyDirList));
        fragmentList.add(new MyFragment(this, apkAdapter,apkList));

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        int threadNum = 3;
        File[] files = Environment.getExternalStorageDirectory().listFiles();
        int startIndex = files.length - 1;
        int endIndex;
        for(int i = 0; i < threadNum; i++){
            if(i != threadNum - 1) endIndex = startIndex - files.length / threadNum;
            else endIndex = 0;
            ScannerThread scannerThread = new ScannerThread(this,files,threadNum,startIndex,endIndex,bigFileList,emptyFileList,emptyDirList,apkList);
            scannerThread.start();
            startIndex = endIndex;
        }
    }

    private void setListener(){
        tabLayout.addOnTabSelectedListener(this);
        btSelectAll.setOnClickListener(this);
        fab.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_select_all:{
                if(currentTabPosition == 0){
                    isSelectAllBigFile = !isSelectAllBigFile;
                    bigFileList.forEach(item->item.setChecked(isSelectAllBigFile));
                    if(isSelectAllBigFile)
                        btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else
                        btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    bigFileAdapter.notifyDataSetChanged();
                }else if(currentTabPosition == 1){
                    isSelectAllEmptyFile = !isSelectAllEmptyFile;
                    emptyFileList.forEach(item->item.setChecked(isSelectAllEmptyFile));
                    if(isSelectAllEmptyFile)
                        btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else
                        btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    emptyFileAdapter.notifyDataSetChanged();
                }else if(currentTabPosition == 2){
                    isSelectAllEmptyDir= !isSelectAllEmptyDir;
                    emptyDirList.forEach(item->item.setChecked(isSelectAllEmptyDir));
                    if(isSelectAllEmptyDir)
                        btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else
                        btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    emptyDirAdapter.notifyDataSetChanged();
                }else if(currentTabPosition == 3){
                    isSelectAllApk = !isSelectAllApk;
                    apkList.forEach(item -> item.setChecked(isSelectAllApk));
                    if(isSelectAllApk)
                        btSelectAll.setBackgroundResource(R.drawable.ic_clear);
                    else
                        btSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    apkAdapter.notifyDataSetChanged();
                }
                break;
            }
            case R.id.fab_delete:{
                DialogWindow dialogWindow = new DialogWindow(View.inflate(this, R.layout.popupwindow_warn1, null),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                dialogWindow.showAtLocation(viewPager, Gravity.CENTER,0,0);
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


    class DialogWindow extends MyPopupWindow implements View.OnClickListener{
        private View contentView;

        DialogWindow(View contentView, int width, int height) {
            super(CleanFileActivity.this,contentView, width, height);
            this.contentView = contentView;
            initView();
        }

        private void initView(){
            Button btDelete = contentView.findViewById(R.id.bt_delete_file);
            Button btCancel = contentView.findViewById(R.id.bt_cancel);
            btDelete.setOnClickListener(this);
            btCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_delete_file:{
                    int num = deleteSelectedFile();
                    notifyAdapter();
                    dismiss();
                    String s = "一共清理了" + num + "个文件,释放空间" + StringUtil.byteToMegabyte(deleteFileSize);
                    Snackbar.make(tabLayout, s,Snackbar.LENGTH_SHORT).show();
                    break;
                }
                case R.id.bt_cancel:{
                    dismiss();
                    break;
                }
            }
        }
    }
}
