package com.kakacat.minitool.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.R;
import com.kakacat.minitool.appInfo.AppInfoActivity;
import com.kakacat.minitool.cleanFile.CleanFileActivity;
import com.kakacat.minitool.currencyConversion.CurrencyConversionActivity;
import com.kakacat.minitool.translation.TranslationActivity;
import com.kakacat.minitool.garbageClassification.GarbageClassificationActivity;
import com.kakacat.minitool.globalOutbreak.GlobalOutbreakActivity;
import com.kakacat.minitool.inquireIp.InquireIpActivity;
import com.kakacat.minitool.phoneArtribution.PhoneAttributionActivity;
import com.kakacat.minitool.dongCiDaCi.DongCiDaCiActivity;
import com.kakacat.minitool.textEncryption.TextEncryptionActivity;
import com.kakacat.minitool.todayInHistory.TodayInHistoryActivity;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.ItemDecoration;
import com.kakacat.minitool.util.ui.UiUtil;
import com.kakacat.minitool.wifipasswordview.WifiPwdViewActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener,View.OnClickListener{

    private DrawerLayout drawerLayout;

    private ActionBar actionBar;

    private SeekBar seekBarBattery;

    private Button btClear;
    private Button btModifyDpi;
    private Button btResetBattery;
    private Button btFakeBattery;

    private EditText etDpi;
    private EditText etBattery;

    private PopupWindow popupWindowLoading;
    private PopupWindow popupWindowModifyDpi;

    private RecyclerView recyclerView;

    private ItemAdapter itemAdapter;

    private List<Item> itemList;

    private int SEPARATE_FINISH;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == SEPARATE_FINISH){
                popupWindowLoading.dismiss();
                String hint = (String) msg.obj;
                UiUtil.showHint(drawerLayout,hint);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initWidget();
    }

    private void initData() {
        itemList = new ArrayList();
        itemList.add(new Item(R.string.title_currency_conversion,R.drawable.ic_money,R.string.note_currency_conversion));
        itemList.add(new Item(R.string.title_inquire_ip,R.drawable.ic_internet,R.string.note_inquire_ip));
        itemList.add(new Item(R.string.title_phone_attribution,R.drawable.ic_phone,R.string.note_phone_attribution));
        itemList.add(new Item(R.string.title_today_in_history,R.drawable.ic_today_in_history,R.string.note_today_in_history));
        itemList.add(new Item(R.string.title_wifi_pwd_view,R.drawable.ic_wifi,R.string.note_wifi_pwd_view));
        itemList.add(new Item(R.string.title_app_info,R.drawable.ic_app_info,R.string.note_app_info));
        itemList.add(new Item(R.string.title_text_encryption,R.drawable.ic_lock,R.string.note_text_encryption));
        itemList.add(new Item(R.string.title_modify_dpi,R.drawable.ic_dpi,R.string.note_modify_dpi));
        itemList.add(new Item(R.string.title_battery_info,R.drawable.ic_battery,R.string.note_battery_info));
        itemList.add(new Item(R.string.title_clean_file,R.drawable.ic_clean_file,R.string.note_clean_info));
        itemList.add(new Item(R.string.title_audio_capture,R.drawable.ic_audio_capture,R.string.note_audio_capture));
        itemList.add(new Item(R.string.title_dong_ci_da_ci,R.drawable.ic_microphone,R.string.note_dong_ci_da_ci));
        itemList.add(new Item(R.string.title_garbage_classification,R.drawable.ic_garbage,R.string.note_garbage_classification));
        itemList.add(new Item(R.string.title_global_outbreak,R.drawable.ic_global_outbreak,R.string.note_global_outbreak));
        itemList.add(new Item(R.string.title_translation,R.drawable.ic_dictionary,R.string.note_translation));
    }
    private void initWidget(){
        setSupportActionBar(findViewById(R.id.toolbar_main));
        actionBar = getSupportActionBar();
        if(actionBar == null) Log.d("hhh","main actionbar is null");
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_slide);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);

        itemAdapter = new ItemAdapter(itemList);
        itemAdapter.setOnItemClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new ItemDecoration(30,30));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:{
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            }
            default:
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v, int position) {
        Intent intent = null;
        switch (position){
            case 0 :{
                intent = new Intent(this, CurrencyConversionActivity.class);
                break;
            }
            case 1:{
                intent = new Intent(this, InquireIpActivity.class);
                break;
            }
            case 2:{
                intent = new Intent(this, PhoneAttributionActivity.class);
                break;
            }
            case 3:{
                intent = new Intent(this, TodayInHistoryActivity.class);
                break;
            }
            case 4:{
                intent = new Intent(this, WifiPwdViewActivity.class);
                break;
            }
            case 5:{
                intent = new Intent(this, AppInfoActivity.class);
                break;
            }
            case 6:{
                intent = new Intent(this, TextEncryptionActivity.class);
                break;
            }
            case 7:{
                showModifyDpiPopupWindow();
                break;
            }
            case 8:{
                showFakeBatteryPopupWindow();
                break;
            }
            case 9:{
                intent = new Intent(this, CleanFileActivity.class);
                break;
            }
            case 10:{
                audioCapture();
                break;
            }
            case 11:{
                intent = new Intent(this, DongCiDaCiActivity.class);
                break;
            }
            case 12:{
                intent = new Intent(this, GarbageClassificationActivity.class);
                break;
            }
            case 13:{
                intent = new Intent(this, GlobalOutbreakActivity.class);
                break;
            }
            case 14:{
                intent = new Intent(this, TranslationActivity.class);
                break;
            }
        }

        if(intent != null) startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void audioCapture() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            startSelectVideo();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startSelectVideo();
            }
        }
    }

    private void startSelectVideo(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            initLoadingPopupWindow();

            new Thread(()->{
                Uri uri = data.getData();        //路径
                String[] projections = {MediaStore.Video.Media.DATA};  //  列名
                Cursor cursor = getContentResolver().query(uri,projections, null, null, null);
                cursor.moveToFirst();
                String filePath = cursor.getString(0);
                cursor.close();
                String hint = separate(filePath);
                Message msg = new Message();
                msg.obj = hint;
                msg.what = SEPARATE_FINISH;
                handler.sendMessage(msg);
            }).start();
        }
    }


    private void initLoadingPopupWindow(){
        View view = View.inflate(this,R.layout.popupwindow_loading,null);
        popupWindowLoading = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(this,popupWindowLoading);
        popupWindowLoading.showAtLocation(drawerLayout,Gravity.CENTER,0,0);
    }

    private String separate(String filePath){
        try{
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(filePath);      //设置视频路径
            int audioIndex = 0;
            int trackCount = mediaExtractor.getTrackCount();
            MediaFormat audioFormat = null;
            for(int i = 0; i < trackCount; i++){    //得到音轨
                MediaFormat mediaFormat = mediaExtractor.getTrackFormat(i);
                String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                if(mime.startsWith("audio")){
                    audioIndex = i;
                    audioFormat = mediaFormat;
                    break;
                }
            }

            File audioFile = new File(Environment.getExternalStorageDirectory() + "/MiniTool/" + filePath.substring(filePath.lastIndexOf('/') + 1,filePath.length() - 1) + "3");
            if(audioFile.exists()){
                audioFile.delete();
            }else{
                File parentFile = audioFile.getParentFile();
                parentFile.mkdirs();
                audioFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(audioFile);
            int maxAudioBufferCount = audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
            ByteBuffer audioByteBuffer = ByteBuffer.allocate(maxAudioBufferCount);
            mediaExtractor.selectTrack(audioIndex);
            int len;

            while((len = mediaExtractor.readSampleData(audioByteBuffer,0)) != -1){
                byte[] bytes = new byte[len];
                audioByteBuffer.get(bytes);
                byte[] adtsData = new byte[len + 7];
                SystemUtil.addADTStoPacket(adtsData, len + 7);
                System.arraycopy(bytes,0,adtsData,7,len);
                fos.write(adtsData);
                audioByteBuffer.clear();
                mediaExtractor.advance();
            }
            fos.flush();
            fos.close();
            mediaExtractor.release();
            return "提取完成,保存在目录" + audioFile.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "提取失败...";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFakeBatteryPopupWindow() {
        View view = View.inflate(this,R.layout.popupwindow_fake_battery,null);
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(this,popupWindow);
        etBattery = view.findViewById(R.id.et_current_battery);
        btFakeBattery = view.findViewById(R.id.bt_fake_battery);
        btResetBattery = view.findViewById(R.id.bt_reset_battery);
        btResetBattery.setOnClickListener(this);
        btFakeBattery.setOnClickListener(this);
        seekBarBattery = view.findViewById(R.id.seek_bar_battery);
        seekBarBattery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                etBattery.setText(progress + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        int batteryLevel = SystemUtil.getElectricity(this);
        etBattery.setText(batteryLevel + "");
        seekBarBattery.setProgress(batteryLevel);
        popupWindow.showAtLocation(drawerLayout,Gravity.CENTER,0,0);
    }

    private void showModifyDpiPopupWindow(){
        View view = View.inflate(this,R.layout.popupwindow_modify_dpi,null);
        popupWindowModifyDpi = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UiUtil.initPopupWindow(this, popupWindowModifyDpi);
        btClear = view.findViewById(R.id.bt_clear);
        btModifyDpi = view.findViewById(R.id.bt_modify_dpi);
        btClear.setOnClickListener(this);
        btModifyDpi.setOnClickListener(this);
        etDpi = view.findViewById(R.id.edit_text);
        popupWindowModifyDpi.showAtLocation(drawerLayout, Gravity.CENTER,0,0);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_clear:{
                popupWindowModifyDpi.dismiss();
                break;
            }
            case R.id.bt_modify_dpi:{
                String val = etDpi.getText().toString();
                if(TextUtils.isEmpty(val)) Snackbar.make(drawerLayout,"输入错误",Snackbar.LENGTH_SHORT).show();
                else SystemUtil.modifyDpi(val);
                break;
            }
            case R.id.bt_reset_battery:{
                SystemUtil.resetBattery();
                int val = SystemUtil.getElectricity(this);
                seekBarBattery.setProgress(val);
                break;
            }
            case R.id.bt_fake_battery:{
                int val = Integer.valueOf(etBattery.getText().toString());
                if(val < 0 || val > 100) Snackbar.make(drawerLayout,"你正常点好吗???",Snackbar.LENGTH_SHORT).show();
                else {
                    SystemUtil.setBatteryLevel(val + "");
                    seekBarBattery.setProgress(val);
                }
                break;
            }
        }
    }
}
