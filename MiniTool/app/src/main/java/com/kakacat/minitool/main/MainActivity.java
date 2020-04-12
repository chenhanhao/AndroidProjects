package com.kakacat.minitool.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.appInfo.AppInfoActivity;
import com.kakacat.minitool.currencyConversion.CurrencyConversionActivity;
import com.kakacat.minitool.inquireIp.InquireIpActivity;
import com.kakacat.minitool.phoneArtribution.PhoneAttributionActivity;
import com.kakacat.minitool.textEncryption.TextEncryptionActivity;
import com.kakacat.minitool.todayInHistory.TodayInHistoryActivity;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.UiUtil;
import com.kakacat.minitool.wifipasswordview.WifiPwdViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener,View.OnClickListener{

    private Context context;

    private LayoutInflater inflater;

    private DrawerLayout drawerLayout;

    private ActionBar actionBar;

    private Button btClear;
    private Button btModifyDpi;

    private EditText editText;

    private View popupWindowView;

    private RecyclerView recyclerView;

    private ItemAdapter itemAdapter;

    private PopupWindow popupWindow;

    private List<Item> itemList;

    private boolean isInitedPopupwindow;

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
                initPopWindow();
                popupWindow.showAtLocation(drawerLayout, Gravity.BOTTOM,0,0);
                setShadow();
                break;
            }
        }

        if(intent != null) startActivity(intent);
    }

    private void initPopWindow(){
        if(!isInitedPopupwindow){
            popupWindowView = inflater.inflate(R.layout.popupwindow_modify_dpi,null);
            popupWindow = new PopupWindow(popupWindowView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            UiUtil.initPopupWindow(MainActivity.this,popupWindow);

            btClear = popupWindowView.findViewById(R.id.bt_clear);
            btModifyDpi = popupWindowView.findViewById(R.id.bt_modify_dpi);
            btClear.setOnClickListener(this);
            btModifyDpi.setOnClickListener(this);

            editText = popupWindowView.findViewById(R.id.edit_text);

            isInitedPopupwindow = true;
        }
    }


    private void setShadow(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;//代表透明程度，范围为0 - 1.0f
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_clear:{
                popupWindow.dismiss();
                break;
            }
            case R.id.bt_modify_dpi:{
                SystemUtil.modifyDpi(editText.getText().toString());
                break;
            }
        }
    }


}
