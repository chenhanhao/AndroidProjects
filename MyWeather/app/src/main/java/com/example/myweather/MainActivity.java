package com.example.myweather;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import com.example.myweather.util.HttpCallbackListener;
import com.example.myweather.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import com.example.myweather.util.LocationUtils;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;


public class MainActivity extends AppCompatActivity {

    private final static String USER_NAME = "HE1911271605461336";
    private final static String KEY = "8159079f30a64f95bbd931d702649b79";

    private final static String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private LinearLayout forecastLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView titleCity;
    private TextView updateTime;
    private TextView degreeText;
    private TextView condText;
    private TextView fl;
    private TextView uvIndex;
    private TextView dateTxt;
    private TextView infoText;
    private TextView minTmpText;
    private TextView maxTmpText;
    private TextView windDir;
    private TextView windSc;
    private TextView windSpd;
    private TextView hum;
    private TextView pcpn;
    private TextView pop;
    private TextView pres;
    private TextView vis;
    private TextView lifeText;
    private ImageView condIcon;
    private Toolbar toolbar;

    private ImageView imageView;

    public static String location;

    public static WeatherData weatherData = new WeatherData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.compass);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        loadBackgroundPic();
        askPermissions();
        swipeRefreshLayout.setOnRefreshListener(()-> refreshWeatherData(location));
    }




    private void askPermissions(){
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else{
            Log.d(TAG,"get permission success!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MainActivity.this,"必须同意所有权限才能使用该程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    initLocation();
                    refreshWeatherData(location);
                }
        }
    }


    private void initWidget(){
        titleCity = findViewById(R.id.title_city);
        updateTime = findViewById(R.id.update_time);
        degreeText = findViewById(R.id.degree_text);
        condText = findViewById(R.id.cond_text);
        fl = findViewById(R.id.fl_text);
        uvIndex = findViewById(R.id.uv_index_text);
        windDir = findViewById(R.id.wind_dir);
        windSc = findViewById(R.id.wind_sc);
        windSpd = findViewById(R.id.wind_spd);
        hum = findViewById(R.id.hum);
        pcpn = findViewById(R.id.pcpn);
        pop = findViewById(R.id.pop);
        pres = findViewById(R.id.pres);
        vis = findViewById(R.id.vis);
        lifeText = findViewById(R.id.life_text);

        drawerLayout = findViewById(R.id.drawer_layout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        imageView = findViewById(R.id.bing_pic_img);
        condIcon = findViewById(R.id.cond_icon);

        toolbar = findViewById(R.id.toolbar);
        forecastLayout = findViewById(R.id.forecast_layout);
    }


    private void loadBackgroundPic(){
        final String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new HttpCallbackListener() {
            @Override
            public void onSuccess(String responseData) {
                runOnUiThread(()-> Glide.with(MainActivity.this).load(responseData).into(imageView));
            }
            @Override
            public void onError() {}
        });
    }

    private void initLocation(){
        String lonAndLat = LocationUtils.getInstance().getLocations(this);
        location = lonAndLat;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    public void refreshWeatherData(String location) {
        HeConfig.init(USER_NAME, KEY);
        HeConfig.switchToFreeServerNode();
        drawerLayout.closeDrawers();
        swipeRefreshLayout.setRefreshing(true);
        HeWeather.getWeather(getApplicationContext(), location,new HeWeather.OnResultWeatherDataListBeansListener(){
            public void onError(Throwable var1){}
            public void onSuccess(Weather var1){
                Log.d(TAG,"success");
                weatherData.setLocationName(var1.getBasic().getLocation());
                weatherData.setUpdateTime(var1.getUpdate().getLoc());
                weatherData.setNowTmp(var1.getNow().getTmp());
                weatherData.setCondTxt(var1.getNow().getCond_txt());
                weatherData.setForecastDate(var1.getDaily_forecast().get(0).getDate());
                weatherData.setForecastCondText(var1.getDaily_forecast().get(0).getCond_txt_d());
                weatherData.setForecastMaxTmp(var1.getDaily_forecast().get(0).getTmp_max());
                weatherData.setForecastMinTmp(var1.getDaily_forecast().get(0).getTmp_min());
                weatherData.setFl(var1.getNow().getFl());
                weatherData.setUv_index(var1.getDaily_forecast().get(0).getUv_index());

                weatherData.setWindDir(var1.getDaily_forecast().get(0).getWind_dir());
                weatherData.setWindSc(var1.getDaily_forecast().get(0).getWind_sc() + " 级");
                weatherData.setWindSpd(var1.getDaily_forecast().get(0).getWind_spd() + " 公里/小时");
                weatherData.setHum(var1.getDaily_forecast().get(0).getHum() + "%");
                weatherData.setPcpn(var1.getDaily_forecast().get(0).getPcpn() + " mm");
                weatherData.setPop(var1.getDaily_forecast().get(0).getPop() + "%");
                weatherData.setPres(var1.getDaily_forecast().get(0).getPres() + " pa");
                weatherData.setVis(var1.getDaily_forecast().get(0).getVis() + " 公里/小时");

                runOnUiThread(()-> {
                    updateTime.setText(weatherData.getUpdateTime().substring(weatherData.getUpdateTime().length() - 5));
                    titleCity.setText(weatherData.getLocationName());
                    degreeText.setText(weatherData.getNowTmp());
                    condText.setText(weatherData.getCondTxt());
                    fl.setText(weatherData.getFl());
                    uvIndex.setText(weatherData.getUv_index());
                    windDir.setText(weatherData.getWindDir());
                    windSc.setText(weatherData.getWindSc());
                    windSpd.setText(weatherData.getWindSpd());
                    hum.setText(weatherData.getHum());
                    pcpn.setText(weatherData.getPcpn());
                    pop.setText(weatherData.getPop());
                    pres.setText(weatherData.getPres());
                    vis.setText(weatherData.getVis());

                });
            }
        });
        HeWeather.getWeatherForecast(getApplicationContext(), location, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(Forecast forecast) {
                forecastLayout.removeAllViews();
                for(ForecastBase forecastBase: forecast.getDaily_forecast()){
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.forecast_item,forecastLayout,false);
                    dateTxt = view.findViewById(R.id.date_text);
                    infoText = view.findViewById(R.id.info_text);
                    minTmpText = view.findViewById(R.id.min_tmp_text);
                    maxTmpText = view.findViewById(R.id.max_tmp_text);
                    dateTxt.setText(forecastBase.getDate());
                    infoText.setText(forecastBase.getCond_txt_d());
                    maxTmpText.setText(forecastBase.getTmp_max());
                    minTmpText.setText(forecastBase.getTmp_min());
                    forecastLayout.addView(view);
                }
            }
        });
        HeWeather.getWeatherLifeStyle(MainActivity.this,location,new HeWeather.OnResultWeatherLifeStyleBeanListener(){
            @Override
            public void onError(Throwable var1){
                Log.d(TAG,"获取生活指数数据失败");
            }
            @Override
            public void onSuccess(Lifestyle var1){
                weatherData.setLifeText(var1.getLifestyle().get(0).getTxt());
                runOnUiThread(()->lifeText.setText(weatherData.getLifeText()));
            }
        });

        HeWeather.getWeatherNow(MainActivity.this,location,new HeWeather.OnResultWeatherNowBeanListener(){
            @Override
            public void onError(Throwable var1){
                Log.d(TAG,"获取天气code失败");
            }
            @Override
            public void onSuccess(Now var1){
                Log.d(TAG,"获取天气code成功" + var1.getNow().getCond_code());
                runOnUiThread(()->{
                    weatherData.setCondCode(var1.getNow().getCond_code());
                    String sourceId = "https://cdn.heweather.com/cond_icon/" + weatherData.getCondCode() + ".png";
                    Glide.with(MainActivity.this).load(sourceId).into(condIcon);
                });
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }
}
