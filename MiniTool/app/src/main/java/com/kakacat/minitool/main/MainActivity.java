package com.kakacat.minitool.main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.kakacat.minitool.appInfo.AppInfoActivity;
import com.kakacat.minitool.inquireIp.InquireIpActivity;
import com.kakacat.minitool.currencyConversion.CurrencyConversionActivity;
import com.kakacat.minitool.R;
import com.kakacat.minitool.phoneArtribution.PhoneAttributionActivity;
import com.kakacat.minitool.textEncryption.TextEncryptionActivity;
import com.kakacat.minitool.todayInHistory.TodayInHistoryActivity;
import com.kakacat.minitool.wifipasswordview.WifiPwdViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initWidget();
        itemAdapter.setOnItemClickListener((v, position) -> {
            Intent intent = getIntent(position);
            startActivity(intent);
        });
    }


    private Intent getIntent(int position){
        Intent intent = null;
        if     (position == 0) intent = new Intent(context, CurrencyConversionActivity.class);
        else if(position == 1) intent = new Intent(context, InquireIpActivity.class);
        else if(position == 2) intent = new Intent(context, PhoneAttributionActivity.class);
        else if(position == 3) intent = new Intent(context, TodayInHistoryActivity.class);
        else if(position == 4) intent = new Intent(context, WifiPwdViewActivity.class);
        else if(position == 5) intent = new Intent(context, AppInfoActivity.class);
        else if(position == 6) intent = new Intent(context, TextEncryptionActivity.class);
        return intent;
    }

    private void initData() {
        context = MainActivity.this;

        itemList = new ArrayList();
        itemList.add(new Item(R.string.title_currency_conversion,R.drawable.currency));
        itemList.add(new Item(R.string.title_inquire_ip,R.drawable.ic_internet));
        itemList.add(new Item(R.string.title_phone_attribution,R.drawable.ic_phone));
        itemList.add(new Item(R.string.title_today_in_history,R.drawable.ic_today_in_history));
        itemList.add(new Item(R.string.title_wifi_pwd_view,R.drawable.ic_wifi));
        itemList.add(new Item(R.string.title_app_info,R.drawable.ic_app_info));
        itemList.add(new Item(R.string.title_text_encryption,R.drawable.ic_lock));
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
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setLayoutManager(gridLayoutManager);
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
}
