package com.kakacat.minitool.inquireIp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

public class InquireIpActivity extends AppCompatActivity {

    private Context context;
    private Button btInquire;
    private EditText editText;
    private TextView tvCountry;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvIsp;
    private TextView tvIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_ip);
        initWidget();
    }

    private void initWidget(){
        initToolbar();
        context = InquireIpActivity.this;
        tvCountry = findViewById(R.id.tv_country);
        tvProvince = findViewById(R.id.tv_province);
        tvCity = findViewById(R.id.tv_city);
        tvIsp = findViewById(R.id.tv_isp);
        tvIp = findViewById(R.id.tv_ip);
        btInquire = findViewById(R.id.bt_inquire);
        btInquire.setOnClickListener(v -> {
            requestIpData();
        });
    }


    private void requestIpData(){
        String ipAddress = getIp();
        String key = "ad7f15652b5f7e7799fefa92a5246d4a";
        String address ="http://apis.juhe.cn/ip/ipNew?ip=" + ipAddress + "&key=" + key;
        if(ipAddress == null){
            Toast.makeText(context, "IP地址错误", Toast.LENGTH_SHORT).show();
            Log.d("hhh","IP地址错误");
        }else{
            HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onSuccess(Response response) {
                    String s = null;
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Data data = JsonUtil.handleIpDataResponse(s);
                    if(data == null){
           //             Log.d("hhh","json解析失败");
                        Toast.makeText(context,"json解析失败",Toast.LENGTH_SHORT).show();
                    }else{
                        runOnUiThread(()->{
                            tvCountry.setText(data.getCountry());
                            tvProvince.setText(data.getProvince());
                            tvCity.setText(data.getCity());
                            tvIsp.setText(data.getIsp());
                            tvIp.setText(ipAddress);
                            Toast.makeText(context,"查询成功",Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                @Override
                public void onError() {
                    Log.d("hhh","http请求错误");
                }
            });
        }

    }


    private String getIp(){
        editText = findViewById(R.id.et_input_ip);
        String ip = editText.getText().toString();
        if(TextUtils.isEmpty(ip)){
            ip = getLocalIPAddress();
            if(ip == null || TextUtils.isEmpty(ip)){
                Log.d("hhh","获取本地ip地址失败");
                return null;
            }
        }
        //检查ip格式
        String regex =
                "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        if(matcher.find()){
            Log.d("hhh",ip);
            return ip;
        }
        return null;
    }

    private void initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar_inquire_ip));
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


    public String getLocalIPAddress()
    {
        try{
            for(Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
                NetworkInterface intf = mEnumeration.nextElement();
                for(Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
        //                Log.d("hhh",inetAddress.getHostAddress());

                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
