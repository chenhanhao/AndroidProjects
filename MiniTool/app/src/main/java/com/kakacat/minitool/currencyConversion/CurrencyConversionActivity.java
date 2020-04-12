package com.kakacat.minitool.currencyConversion;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.JsonUtil;
import com.kakacat.minitool.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrencyConversionActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    public Context context;

    private LayoutInflater inflater;

    private SwipeRefreshLayout swipeRefreshLayout;

    private View popupWindowViw;

    private CircleImageView cv1;
    private CircleImageView cv2;

    private RecyclerView recyclerView;

    private ActionBar actionBar;

    private TextView tv_country_name1;
    private TextView tv_country_name2;
    private TextView tv_money_unit1;
    private TextView tv_money_unit2;

    private EditText editText1;
    private EditText editText2;

    private PopupWindow popupWindow;

    private boolean isFocused1;
    private boolean isFocused2;

    private TextWatcher textWatcher1;
    private TextWatcher textWatcher2;

    private Country currentCountry1;
    private Country currentCountry2;

    private List<Country> countryList;

    private boolean initPopupWindow;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);

        initWidget();
        initData();
        initListener();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        JsonUtil.writeHistoryToLocal(context,currentCountry1,currentCountry2);
    }


    private void initListener(){
        textWatcher1 = new TextWatcher() {
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
                            e.printStackTrace();
                        }
                    }else{
                        editText2.setText("");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };
        textWatcher2 = new TextWatcher() {
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
                        }
                    }else{
                        editText1.setText("");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };

        editText1.setOnFocusChangeListener(this);
        editText1.addTextChangedListener(textWatcher1);
        editText2.setOnFocusChangeListener(this);
        editText2.addTextChangedListener(textWatcher2);

        cv1.setOnClickListener((v)->{flag = 1;showSelectDialog();});
        cv2.setOnClickListener((v)->{flag = 2;showSelectDialog();});

        swipeRefreshLayout.setOnRefreshListener(()->refreshExchangeRate());
    }

    private void initWidget(){
        context = CurrencyConversionActivity.this;

        setSupportActionBar(findViewById(R.id.toolbar_currency));
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        cv1 = findViewById(R.id.ib_icon_country1);
        cv2 = findViewById(R.id.ib_icon_country2);

        editText1 = findViewById(R.id.et_input_money1);
        editText2 = findViewById(R.id.et_input_money2);

        tv_country_name1 = findViewById(R.id.tv_country_name1);
        tv_country_name2 = findViewById(R.id.tv_country_name2);
        tv_money_unit1 = findViewById(R.id.tv_money_unit1);
        tv_money_unit2 = findViewById(R.id.tv_money_unit2);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }


    private void showSelectDialog(){
        initPopupWindow();
        popupWindow.showAtLocation(swipeRefreshLayout,Gravity.BOTTOM, 0,50);
    }

    private void initData(){
        JsonUtil.readRateFromLocal(context);

        if(currentCountry1 == null) currentCountry1 = new Country();
        if(currentCountry2 == null) currentCountry2 = new Country();
        JsonUtil.readHistoryFromLocal(context,currentCountry1,currentCountry2);
        cv1.setImageResource(currentCountry1.getIconId());
        cv2.setImageResource(currentCountry2.getIconId());
        tv_country_name1.setText(currentCountry1.getNameId());
        tv_country_name2.setText(currentCountry2.getNameId());
        tv_money_unit1.setText(currentCountry1.getUnitId());
        tv_money_unit2.setText(currentCountry2.getUnitId());

        countryList = new ArrayList();
        fillList();
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

    private void initPopupWindow(){
        if(!initPopupWindow){
            inflater = LayoutInflater.from(this);
            popupWindowViw = inflater.inflate(R.layout.dialog_select_country,null);
            popupWindow = new PopupWindow(popupWindowViw, ViewGroup.LayoutParams.WRAP_CONTENT, 1000);
            UiUtil.initPopupWindow(CurrencyConversionActivity.this,popupWindow);

            CountryAdapter countryAdapter = new CountryAdapter(countryList);
            countryAdapter.setOnItemClickListener((v, position) -> {
                Country country = countryList.get(position);
                if(flag == 1){
                    cv1.setImageResource(country.getIconId());
                    tv_country_name1.setText(country.getNameId());
                    tv_money_unit1.setText(country.getUnitId());
                    currentCountry1 = country;
                }else if(flag == 2){
                    cv2.setImageResource(country.getIconId());
                    tv_country_name2.setText(country.getNameId());
                    tv_money_unit2.setText(country.getUnitId());
                    currentCountry2 = country;
                }
                popupWindow.dismiss();
            });
            recyclerView = popupWindowViw.findViewById(R.id.rv_country);
            recyclerView.setAdapter(countryAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
            initPopupWindow = true;
        }
    }


    private void fillList(){
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

        swipeRefreshLayout.setRefreshing(true);
        HttpUtil.sendOkHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onSuccess(String s) {
                if(JsonUtil.handleRateResponse(context,s))
                    for(int i = 0; i < 22; i++) countryList.get(i).setRate(Rate.getRate(i + 1));
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(swipeRefreshLayout,"更新汇率成功",Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onError() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

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
}
