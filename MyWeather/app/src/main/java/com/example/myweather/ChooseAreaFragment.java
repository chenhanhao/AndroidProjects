package com.example.myweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myweather.db.City;
import com.example.myweather.db.County;
import com.example.myweather.db.Province;
import com.example.myweather.util.HttpCallbackListener;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.grid.now.GridNow;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {


    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private static final String TAG = "ChooseAreaFragment";

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;      //标识选择的省份
    private City selectedCity;             //  标识选择的城市
    private int currentLevel;            //  标识当前在哪一层选择
    public String location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(arrayAdapter);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener((parent,view,position,id)->{  // 改用lambda表达式
            if(currentLevel == LEVEL_PROVINCE){
                selectedProvince = provinceList.get(position);     // 如果选择了某个省份,则后台查询该省份的所有城市
                queryCities();
            }else if(currentLevel == LEVEL_CITY){
                selectedCity = cityList.get(position);
                queryCounties();
            }else if(currentLevel == LEVEL_COUNTY){
                location = countyList.get(position).getCountyName();
                MainActivity mainActivity = (MainActivity) getActivity();
                MainActivity.location = location;
                mainActivity.refreshWeatherData(location);
            }
        });

        backButton.setOnClickListener(view -> {
            if(currentLevel == LEVEL_COUNTY){
                currentLevel = LEVEL_CITY;
                queryCities();
            }else if(currentLevel == LEVEL_CITY){
                currentLevel = LEVEL_PROVINCE;
                queryProvinces();
            }
        });
        queryProvinces();
    }


    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);

        if(provinceList.size() > 0){        // 从数据库查询到了数据
            dataList.clear();        // 先清除原有数据,不能新建list再改变指向,因为datalist的指向内存地址不能变,数据可以变
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());  // 添加查询到的数据
  //              Log.d(TAG,province.getProvinceName());
            }
            arrayAdapter.notifyDataSetChanged();      // 刷新数据listView
            listView.setSelection(0);             // 定位到最上面
            currentLevel = LEVEL_PROVINCE;       // 更新数据
        }else{

            // 因为这里是http,所以要在manifest的application中加入"android:usesCleartextTraffic="true"",否则报错
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            arrayAdapter.notifyDataSetChanged();
            currentLevel = LEVEL_CITY;
            listView.setSelection(0);
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }

    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityid= ? ",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onSuccess(String responseData) {
                String responseText = responseData;
                boolean result = false;
                if(type.equals("province")){  // 将查询到的数据存入到对应的数据库
                    result = Utility.handleProvinceResponse(responseText);
                }else if(type.equals("city")){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if(type.equals("county")){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(result){    //从服务器查询并且保存到本地数据库之后,再次刷新listView
                    getActivity().runOnUiThread(()->{
                        closeProgressDialog();  //关闭处理进度框
                        if(type.equals("province")){
                            queryProvinces();
                        }else if(type.equals("city")){
                            queryCities();
                        }else if(type.equals("county")){
                            queryCounties();
                        }
                    });
                }
            }

            @Override
            public void onError() {
                getActivity().runOnUiThread(() ->{
                    closeProgressDialog();
                    Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }


    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }



}
