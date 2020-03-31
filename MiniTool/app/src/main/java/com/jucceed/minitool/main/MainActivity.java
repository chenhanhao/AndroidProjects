package com.jucceed.minitool.main;

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
import android.widget.Toast;

import com.jucceed.minitool.inquireIp.InquireIpActivity;
import com.jucceed.minitool.currencyConversion.CurrencyConversionActivity;
import com.jucceed.minitool.R;
import com.jucceed.minitool.phoneArtribution.PhoneAttributionActivity;
import com.jucceed.minitool.todayInHistory.TodayInHistoryActivity;

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
            Intent intent = null;
            if     (position == 0) intent = new Intent(context, CurrencyConversionActivity.class);
            else if(position == 1) intent = new Intent(context, InquireIpActivity.class);
            else if(position == 2) intent = new Intent(context, PhoneAttributionActivity.class);
            else if(position == 3) intent = new Intent(context, TodayInHistoryActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this,"启动活动成功", Toast.LENGTH_LONG).show();
        });
    }


    private void initData() {
        context = MainActivity.this;

        itemList = new ArrayList();
        itemList.add(new Item(R.string.title_currency_conversion,R.drawable.currency,R.string.note_currency_conversion));
        itemList.add(new Item(R.string.title_inquire_ip,R.drawable.ic_internet,R.string.note_inquire_ip));
        itemList.add(new Item(R.string.title_phone_attribution,R.drawable.ic_phone,R.string.note_phone_attribution));
        itemList.add(new Item(R.string.title_today_in_history,R.drawable.ic_today_in_history,R.string.note_today_in_history));
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new ItemDecoration(30,20));
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
