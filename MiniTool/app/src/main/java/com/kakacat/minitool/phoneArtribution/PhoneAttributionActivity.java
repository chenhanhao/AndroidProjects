package com.kakacat.minitool.phoneArtribution;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.JsonUtil;
import com.kakacat.minitool.util.ui.SearchBar;

import java.io.IOException;

import okhttp3.Response;

public class PhoneAttributionActivity extends AppCompatActivity {

    private Context context;
    private SearchBar searchBar;

    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvAreaCode;
    private TextView tvZip;
    private TextView tvCompany;
    private TextView tvNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_attribution);

        initWidget();
    }

    private void initWidget(){
        context = this;
        initToolbar();

        searchBar = findViewById(R.id.search_bar);
        tvProvince = findViewById(R.id.tv_province);
        tvCity = findViewById(R.id.tv_city);
        tvAreaCode = findViewById(R.id.tv_area_code);
        tvZip = findViewById(R.id.tv_zip);
        tvCompany = findViewById(R.id.tv_company);
        tvNumber = findViewById(R.id.tv_number);

        searchBar.imageView.setOnClickListener(v -> {
            requestAttrData();
        });
    }


    private void requestAttrData(){
        CharSequence phoneNumber = getPhoneNumber();
        if(phoneNumber == null)
            Toast.makeText(context,"请输入11位有效手机号码",Toast.LENGTH_SHORT).show();
        else{
            String key = "a61898e25da1484f93ccf01e2ebe6ff7";
            String address = "http://apis.juhe.cn/mobile/get?phone= " + phoneNumber + "&key=" + key;
            HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onSuccess(Response response) {
                    String s = null;
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Data data = JsonUtil.handleAttrDataResponse(s);
                    if(data != null){
                        runOnUiThread(()->{
                            tvProvince.setText(data.getProvince());
                            tvCity.setText(data.getCity());
                            tvAreaCode.setText(data.getAreaCode());
                            tvZip.setText(data.getZip());
                            tvCompany.setText(data.getCompany());
                            tvNumber.setText(phoneNumber);
                            Toast.makeText(context,"查询成功",Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(context,"http请求错误",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private CharSequence getPhoneNumber(){
        CharSequence phoneNumber = searchBar.editText.getText();
        if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11)
            return phoneNumber;
        return null;
    }

    private void initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar_phone_attribution));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if (menuItem.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}
