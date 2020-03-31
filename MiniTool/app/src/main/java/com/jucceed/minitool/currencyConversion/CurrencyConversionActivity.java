package com.jucceed.minitool.currencyConversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jucceed.minitool.R;
import com.jucceed.minitool.util.http.HttpCallbackListener;
import com.jucceed.minitool.util.http.HttpUtil;
import com.jucceed.minitool.util.handleJson.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrencyConversionActivity extends AppCompatActivity {

    public Context context;
    public static String TAG = "CurrencyConversionActivity";
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private CircleImageView countryFlag1;
    private CircleImageView countryFlag2;
    private List<Country> countryList;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private ActionBar actionBar;

    private Country currentCountry1;
    private Country currentCountry2;
    private TextView tv_country_name1;
    private TextView tv_country_name2;
    private EditText editText1;
    private EditText editText2;
    private TextView tv_money_unit1;
    private TextView tv_money_unit2;

    private boolean isFocused1;
    private boolean isFocused2;

    private int refreshFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);

        initData();
        initWidget();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(currentCountry1 == null) currentCountry1 = new Country();
        if(currentCountry2 == null) currentCountry2 = new Country();
        Utility.readHistoryFromLocal(context,currentCountry1,currentCountry2);
        countryFlag1.setImageResource(currentCountry1.getIconId());
        countryFlag2.setImageResource(currentCountry2.getIconId());
        tv_country_name1.setText(currentCountry1.getNameId());
        tv_country_name2.setText(currentCountry2.getNameId());
        tv_money_unit1.setText(currentCountry1.getUnitId());
        tv_money_unit2.setText(currentCountry2.getUnitId());
    }


    @Override
    public void onPause(){
        super.onPause();
        Utility.writeHistoryToLocal(context,currentCountry1,currentCountry2);
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                if(v.getId() == editText1.getId()){
                    isFocused1 = true;
                    isFocused2 = false;
                }else if (v.getId() == editText2.getId()){
                    isFocused1 = false;
                    isFocused2 = true;
                }
            }else{
                isFocused1 = isFocused2 = false;
            }
        }
    };

    private void initWidget(){
        initToolbar();
        initCountryFlag();
        initEditText();
        tv_country_name1 = findViewById(R.id.tv_country_name1);
        tv_country_name2 = findViewById(R.id.tv_country_name2);
        tv_money_unit1 = findViewById(R.id.tv_money_unit1);
        tv_money_unit2 = findViewById(R.id.tv_money_unit2);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(()->refreshExchangeRate());
    }

    private void initEditText(){
        editText1 = findViewById(R.id.et_input_money1);
        editText2 = findViewById(R.id.et_input_money2);
        editText1.setOnFocusChangeListener(focusChangeListener);
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isFocused1 && !isFocused2){
                    if(!TextUtils.isEmpty(s)){
                        try{
                            double value = Double.parseDouble(s.toString());
                            showResult(value,editText2);
                        }catch (Exception e){
                            Log.d(TAG,"转换错误");
                        }
                    }else{
                        editText2.setText("");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        editText2.setOnFocusChangeListener(focusChangeListener);
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isFocused2 && !isFocused1){
                    if(!TextUtils.isEmpty(s)){
                        try{
                            double value = Double.parseDouble(s.toString());
                            showResult(value,editText1);
                        }catch (Exception e){
                            Log.d(TAG,"转换错误");
                        }
                    }else{
                        editText1.setText("");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void initCountryFlag(){
        countryFlag1 = findViewById(R.id.ib_icon_country1);
        countryFlag2 = findViewById(R.id.ib_icon_country2);
        countryFlag1.setOnClickListener(v -> showSelectDialog(1));
        countryFlag2.setOnClickListener(v -> showSelectDialog(2));
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_currency);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
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

    private void refreshExchangeRate(){
        String key = "6103d9a9aeca9c09fc8f6bd4734be680";
        String address = "http://web.juhe.cn:8080/finance/exchange/rmbquot?key=" + key;
 //       Log.d("hhh","发送请求");
        swipeRefreshLayout.setRefreshing(true);
        refreshFlag = 0;
        HttpUtil.sendOkHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onSuccess(String s) {
     //           Log.d("hhh","onSuccess");
                if(Utility.handleRateResponse(context,s)){
   //                 Log.d("hhh","解析json成功");
                    for(int i = 0; i < 22; i++){
                        countryList.get(i).setRate(Rate.getRate(i + 1));
                    }
                    refreshFlag = 1;
                }else{
     //               Log.d("hhh","解析json失败");
                    refreshFlag = 2;
                }
                swipeRefreshLayout.setRefreshing(false);
                runOnUiThread(()->{
                    String remind = "";
                    if(refreshFlag == 1){
                        remind = "刷新汇率数据成功";
                    }else if(refreshFlag == 2){
                        remind = "获取数据成功,但解析失败";
                    }else{
                        remind = "错误";
                    }
                    refreshFlag = 0;
                    Toast.makeText(CurrencyConversionActivity.this,remind,Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onError() {
 //               Log.d("hhh","获取最新汇率失败");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showSelectDialog(int flag){
        if(inflater == null) inflater = LayoutInflater.from(this);
        if(rootView == null) rootView = inflater.inflate(R.layout.activity_currency_conversion,null);

        View view = inflater.inflate(R.layout.dialog_select_country,null);
        PopupWindow popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT, 1000);
//        popupWindow.setElevation(3);
        popupWindow.setOutsideTouchable(true);      //  设置触摸外面就取消弹窗
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        /*
         * 这一行必须最后,不然前面的style不起作用
         */
        popupWindow.showAtLocation(rootView,Gravity.BOTTOM, 0,0);


        /**
         * 弹出popupWindow时设置暗背景
         */
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//代表透明程度，范围为0 - 1.0f
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(()->{
            lp.alpha = 1.0f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        });

        recyclerView = view.findViewById(R.id.rv_country);
        CountryAdapter countryAdapter = new CountryAdapter(countryList);
        recyclerView.setAdapter(countryAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        countryAdapter.setOnItemClickListener((v, position) -> {
            Toast.makeText(context,"更换国家成功",Toast.LENGTH_SHORT).show();
            Country country = countryList.get(position);
            if(flag == 1){
                countryFlag1.setImageResource(country.getIconId());
                tv_country_name1.setText(country.getNameId());
                tv_money_unit1.setText(country.getUnitId());
                currentCountry1 = country;
            }else{
                countryFlag2.setImageResource(country.getIconId());
                tv_country_name2.setText(country.getNameId());
                tv_money_unit2.setText(country.getUnitId());
                currentCountry2 = country;
            }
            popupWindow.dismiss();
        });


    }

    private void initData(){
        context = CurrencyConversionActivity.this;
        Utility.readRateFromLocal(context);

        countryList = new ArrayList();
        countryList.add(new Country(R.drawable.ic_us,R.string.name_us,R.string.unit_us, Rate.us));
        countryList.add(new Country(R.drawable.ic_eu,R.string.name_eu,R.string.unit_eu, Rate.eu));
        countryList.add(new Country(R.drawable.ic_hk,R.string.name_hk,R.string.unit_hk, Rate.hk));
        countryList.add(new Country(R.drawable.ic_jp,R.string.name_jp,R.string.unit_jp, Rate.jp));
        countryList.add(new Country(R.drawable.ic_uk,R.string.name_uk,R.string.unit_uk, Rate.uk));
        countryList.add(new Country(R.drawable.ic_au,R.string.name_au,R.string.unit_au, Rate.au));
        countryList.add(new Country(R.drawable.ic_ca,R.string.name_ca,R.string.unit_ca, Rate.ca));
        countryList.add(new Country(R.drawable.ic_th,R.string.name_th,R.string.unit_th, Rate.th));
        countryList.add(new Country(R.drawable.ic_sg,R.string.name_sg,R.string.unit_sg, Rate.sg));
        countryList.add(new Country(R.drawable.ic_ch,R.string.name_ch,R.string.unit_ch, Rate.ch));
        countryList.add(new Country(R.drawable.ic_dk,R.string.name_dk,R.string.unit_dk, Rate.dk));
        countryList.add(new Country(R.drawable.ic_ma,R.string.name_ma,R.string.unit_ma, Rate.ma));
        countryList.add(new Country(R.drawable.ic_my,R.string.name_my,R.string.unit_my, Rate.my));
        countryList.add(new Country(R.drawable.ic_no,R.string.name_no,R.string.unit_no, Rate.no));
        countryList.add(new Country(R.drawable.ic_nz,R.string.name_nz,R.string.unit_nz, Rate.nz));
        countryList.add(new Country(R.drawable.ic_ph,R.string.name_ph,R.string.unit_ph, Rate.ph));
        countryList.add(new Country(R.drawable.ic_ru,R.string.name_ru,R.string.unit_ru, Rate.ru));
        countryList.add(new Country(R.drawable.ic_se,R.string.name_se,R.string.unit_se, Rate.se));
        countryList.add(new Country(R.drawable.ic_tw,R.string.name_tw,R.string.unit_tw, Rate.tw));
        countryList.add(new Country(R.drawable.ic_br,R.string.name_br,R.string.unit_br, Rate.br));
        countryList.add(new Country(R.drawable.ic_kr,R.string.name_kr,R.string.unit_kr, Rate.kr));
        countryList.add(new Country(R.drawable.ic_za,R.string.name_za,R.string.unit_za, Rate.za));
        countryList.add(new Country(R.drawable.ic_cn,R.string.name_cn,R.string.unit_cn, Rate.cn));
 //       Log.d("hello","添加数据成功");
    }

    public void showResult(double val,EditText editText){
        String s = "";
        if(editText == editText1){
            s = val * currentCountry2.getRate() / currentCountry1.getRate() + "";
        }else if(editText == editText2){
            s = val * currentCountry1.getRate() / currentCountry2.getRate() + "";
        }
        editText.setText(s);
    }
}
