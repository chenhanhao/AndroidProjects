package com.jucceed.minitool.phoneArtribution;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jucceed.minitool.R;
import com.jucceed.minitool.util.handleJson.Utility;
import com.jucceed.minitool.util.http.HttpCallbackListener;
import com.jucceed.minitool.util.http.HttpUtil;

public class PhoneAttributionActivity extends AppCompatActivity {

    private Context context;
    private EditText editText;
    private Button btInquire;
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
        initToolbar();

        context = PhoneAttributionActivity.this;
        btInquire = findViewById(R.id.bt_inquire_attribution);
        tvProvince = findViewById(R.id.tv_province);
        tvCity = findViewById(R.id.tv_city);
        tvAreaCode = findViewById(R.id.tv_area_code);
        tvZip = findViewById(R.id.tv_zip);
        tvCompany = findViewById(R.id.tv_company);
        tvNumber = findViewById(R.id.tv_number);
        btInquire.setOnClickListener(v -> {
            requestAttrData();
        });
    }


    private void requestAttrData(){
        String phoneNumber = getPhoneNumber();
        String key = "a61898e25da1484f93ccf01e2ebe6ff7";
        String address = "http://apis.juhe.cn/mobile/get?phone= " + phoneNumber + "&key=" + key;
        if(phoneNumber == null){
 //           Log.d("hhh","请输入11位有效手机号码");
            Toast.makeText(context,"请输入11位有效手机号码",Toast.LENGTH_SHORT).show();
        }else{
            HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onSuccess(String s) {
                    Data data = Utility.handleAttrDataResponse(s);
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


    private String getPhoneNumber(){
        editText = findViewById(R.id.et_input_phone);
        String phoneNumber = editText.getText().toString();

        if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11) return phoneNumber;

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
