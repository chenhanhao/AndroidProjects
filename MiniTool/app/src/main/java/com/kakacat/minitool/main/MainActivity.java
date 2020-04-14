package com.kakacat.minitool.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.R;
import com.kakacat.minitool.appInfo.AppInfoActivity;
import com.kakacat.minitool.cleanFile.CleanFileActivity;
import com.kakacat.minitool.currencyConversion.CurrencyConversionActivity;
import com.kakacat.minitool.inquireIp.InquireIpActivity;
import com.kakacat.minitool.phoneArtribution.PhoneAttributionActivity;
import com.kakacat.minitool.textEncryption.TextEncryptionActivity;
import com.kakacat.minitool.todayInHistory.TodayInHistoryActivity;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;
import com.kakacat.minitool.wifipasswordview.WifiPwdViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener,View.OnClickListener{

    private Context context;

    private LayoutInflater inflater;

    private DrawerLayout drawerLayout;

    private ActionBar actionBar;

    private SeekBar seekBarBattery;

    private Button btClear;
    private Button btModifyDpi;
    private Button btResetBattery;
    private Button btFakeBattery;

    private EditText etDpi;
    private EditText etBattery;

    private View popupWindowViewModifyDpi;
    private View popupWindowViewFakeBattery;

    private RecyclerView recyclerView;

    private ItemAdapter itemAdapter;

    private PopupWindow popupWindowModifyDpi;
    private PopupWindow popupWindowFakeBattery;

    private List<Item> itemList;

    private boolean initModifyDpiPopupWindow;
    private boolean initFakeBatteryPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initWidget();
    }


    private void initData() {
        context = MainActivity.this;
        inflater = LayoutInflater.from(context);

        itemList = new ArrayList();
        itemList.add(new Item(R.string.title_currency_conversion,R.drawable.currency,R.string.note_currency_conversion));
        itemList.add(new Item(R.string.title_inquire_ip,R.drawable.ic_internet,R.string.note_inquire_ip));
        itemList.add(new Item(R.string.title_phone_attribution,R.drawable.ic_phone,R.string.note_phone_attribution));
        itemList.add(new Item(R.string.title_today_in_history,R.drawable.ic_today_in_history,R.string.note_today_in_history));
        itemList.add(new Item(R.string.title_wifi_pwd_view,R.drawable.ic_wifi,R.string.note_wifi_pwd_view));
        itemList.add(new Item(R.string.title_app_info,R.drawable.ic_app_info,R.string.note_app_info));
        itemList.add(new Item(R.string.title_text_encryption,R.drawable.ic_lock,R.string.note_text_encryption));
        itemList.add(new Item(R.string.title_modify_dpi,R.drawable.ic_dpi,R.string.note_modify_dpi));
        itemList.add(new Item(R.string.title_battery_info,R.drawable.ic_battery,R.string.note_battery_info));
        itemList.add(new Item(R.string.title_clean_file,R.drawable.ic_clean_file,R.string.note_clean_info));
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
                intent = new Intent(context, CurrencyConversionActivity.class);
                break;
            }
            case 1:{
                intent = new Intent(context, InquireIpActivity.class);
                break;
            }
            case 2:{
                intent = new Intent(context, PhoneAttributionActivity.class);
                break;
            }
            case 3:{
                intent = new Intent(context, TodayInHistoryActivity.class);
                break;
            }
            case 4:{
                intent = new Intent(context, WifiPwdViewActivity.class);
                break;
            }
            case 5:{
                intent = new Intent(context, AppInfoActivity.class);
                break;
            }
            case 6:{
                intent = new Intent(context, TextEncryptionActivity.class);
                break;
            }
            case 7:{
                initModifyDpiPopupWindow();
                popupWindowModifyDpi.showAtLocation(drawerLayout, Gravity.CENTER,0,0);
                break;
            }
            case 8:{
                initFakeBatteryPopupWindow();
                popupWindowFakeBattery.showAtLocation(drawerLayout,Gravity.CENTER,0,0);
                break;
            }
            case 9:{
                intent = new Intent(context, CleanFileActivity.class);
                break;
            }
        }

        if(intent != null) startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initFakeBatteryPopupWindow() {
        if(!initFakeBatteryPopupWindow){
            popupWindowViewFakeBattery = inflater.inflate(R.layout.popupwindow_fake_battery,null);
            popupWindowFakeBattery = new PopupWindow(popupWindowViewFakeBattery, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            UiUtil.initPopupWindow(MainActivity.this,popupWindowFakeBattery);

            etBattery = popupWindowViewFakeBattery.findViewById(R.id.et_current_battery);

            btFakeBattery = popupWindowViewFakeBattery.findViewById(R.id.bt_fake_battery);
            btResetBattery = popupWindowViewFakeBattery.findViewById(R.id.bt_reset_battery);
            btResetBattery.setOnClickListener(this);
            btFakeBattery.setOnClickListener(this);

            seekBarBattery = popupWindowViewFakeBattery.findViewById(R.id.seek_bar_battery);
            seekBarBattery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    etBattery.setText(progress + "");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            initFakeBatteryPopupWindow = true;
        }
        int batteryLevel = SystemUtil.getElectricity(context);
        etBattery.setText(batteryLevel + "");
        seekBarBattery.setProgress(batteryLevel);
        setShadow();
    }

    private void initModifyDpiPopupWindow(){
        if(!initModifyDpiPopupWindow){
            popupWindowViewModifyDpi = inflater.inflate(R.layout.popupwindow_modify_dpi,null);
            popupWindowModifyDpi = new PopupWindow(popupWindowViewModifyDpi,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            UiUtil.initPopupWindow(MainActivity.this, popupWindowModifyDpi);

            btClear = popupWindowViewModifyDpi.findViewById(R.id.bt_clear);
            btModifyDpi = popupWindowViewModifyDpi.findViewById(R.id.bt_modify_dpi);
            btClear.setOnClickListener(this);
            btModifyDpi.setOnClickListener(this);

            etDpi = popupWindowViewModifyDpi.findViewById(R.id.edit_text);

            initModifyDpiPopupWindow = true;
        }
        setShadow();
    }


    private void setShadow(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;//代表透明程度，范围为0 - 1.0f
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
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
                int val = SystemUtil.getElectricity(context);
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
