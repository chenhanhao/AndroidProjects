package com.kakacat.minitool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.kakacat.minitool.R;
import com.kakacat.minitool.currencyConversion.Country;
import com.kakacat.minitool.currencyConversion.Rate;
import com.kakacat.minitool.inquireIp.Data;
import com.kakacat.minitool.todayInHistory.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonUtil {
    public static boolean handleRateResponse(Context context,String response){
        try{
    //        Log.d("hhh","获得数据");
            if(!TextUtils.isEmpty(response)){

     //           Log.d("hhh","数据不空");

                JSONObject jsonObject = new JSONObject(response);
   //             String errorCode = jsonObject.getString("resultcode");
    //            String reason = jsonObject.getString("reason");
                String resultCode = jsonObject.getString("resultcode");

                if(!resultCode.equals("200")) return false;

                JSONObject result = jsonObject.getJSONArray("result").getJSONObject(0);
     //           List<Data> dataList = new ArrayList();  现在只用到一个字段
   //             Gson gson = new Gson();
                for(int i = 1; i <= 22;i ++){
                    JSONObject data = result.getJSONObject("data" + i);
    //                dataList.add(gson.fromJson(data.toString(),Data.class));  //现在只用到一个字段
                    double rate = Double.parseDouble(data.getString("bankConversionPri")) / 100;
                    Rate.setRate(rate,i);
    //                Log.d("hhh",rate + "");
                }
                writeRateToLocal(context);
                return true;
            }else{
                Log.d("hhh","数据空");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Data handleIpDataResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                Data data = new Data();
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("resultcode");
                if(!resultCode.equals("200")) Log.d("hhh","查询ip数据失败");
                   else{
                    JSONObject result = jsonObject.getJSONObject("result");
                    data.setCountry(result.getString("Country"));
                    data.setProvince(result.getString("Province"));
                    data.setCity(result.getString("City"));
                    data.setIsp(result.getString("Isp"));
                    return data;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            Log.d("hhh","数据为空");
        }
        return null;
    }

    public static com.kakacat.minitool.phoneArtribution.Data handleAttrDataResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("resultcode");
                if(resultCode.equals("200")){
                    JSONObject result = jsonObject.getJSONObject("result");
                    com.kakacat.minitool.phoneArtribution.Data data = new com.kakacat.minitool.phoneArtribution.Data();
                    data.setProvince(result.getString("province"));
                    data.setCity(result.getString("city"));
                    data.setAreaCode(result.getString("areacode"));
                    data.setZip(result.getString("zip"));
                    data.setCompany(result.getString("company"));
                    return data;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void writeRateToLocal(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("rate",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("us",(float) Rate.us);
        editor.putFloat("eu",(float) Rate.eu);
        editor.putFloat("hk",(float) Rate.hk);
        editor.putFloat("jp",(float) Rate.jp);
        editor.putFloat("uk",(float) Rate.uk);
        editor.putFloat("au",(float) Rate.au);
        editor.putFloat("ca",(float) Rate.ca);
        editor.putFloat("th",(float) Rate.th);
        editor.putFloat("sg",(float) Rate.sg);
        editor.putFloat("ch",(float) Rate.ch);
        editor.putFloat("dk",(float) Rate.dk);
        editor.putFloat("ma",(float) Rate.ma);
        editor.putFloat("my",(float) Rate.my);
        editor.putFloat("no",(float) Rate.no);
        editor.putFloat("nz",(float) Rate.nz);
        editor.putFloat("ph",(float) Rate.ph);
        editor.putFloat("ru",(float) Rate.ru);
        editor.putFloat("se",(float) Rate.se);
        editor.putFloat("tw",(float) Rate.tw);
        editor.putFloat("br",(float) Rate.br);
        editor.putFloat("kr",(float) Rate.kr);
        editor.putFloat("za",(float) Rate.za);
        editor.commit();
    }

    public static void readRateFromLocal(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("rate",Context.MODE_PRIVATE);
        Rate.us = sharedPreferences.getFloat("us", (float) Rate.us);
        Rate.eu = sharedPreferences.getFloat("eu", (float) Rate.eu);
        Rate.hk = sharedPreferences.getFloat("hk", (float) Rate.hk);
        Rate.jp = sharedPreferences.getFloat("jp", (float) Rate.jp);
        Rate.uk = sharedPreferences.getFloat("uk", (float) Rate.uk);
        Rate.au = sharedPreferences.getFloat("au", (float) Rate.au);
        Rate.ca = sharedPreferences.getFloat("ca", (float) Rate.ca);
        Rate.th = sharedPreferences.getFloat("th", (float) Rate.th);
        Rate.sg = sharedPreferences.getFloat("sg", (float) Rate.sg);
        Rate.ch = sharedPreferences.getFloat("ch", (float) Rate.ch);
        Rate.dk = sharedPreferences.getFloat("dk", (float) Rate.dk);
        Rate.ma = sharedPreferences.getFloat("ma", (float) Rate.ma);
        Rate.my = sharedPreferences.getFloat("my", (float) Rate.my);
        Rate.no = sharedPreferences.getFloat("no", (float) Rate.no);
        Rate.nz = sharedPreferences.getFloat("nz", (float) Rate.nz);
        Rate.ph = sharedPreferences.getFloat("ph", (float) Rate.ph);
        Rate.ru = sharedPreferences.getFloat("ru", (float) Rate.ru);
        Rate.se = sharedPreferences.getFloat("se", (float) Rate.se);
        Rate.tw = sharedPreferences.getFloat("tw", (float) Rate.tw);
        Rate.br = sharedPreferences.getFloat("br", (float) Rate.br);
        Rate.kr = sharedPreferences.getFloat("kr", (float) Rate.kr);
        Rate.za = sharedPreferences.getFloat("za", (float) Rate.za);
    }

    public static void writeHistoryToLocal(Context context,Country country1,Country country2){
        SharedPreferences sharedPreferences = context.getSharedPreferences("history",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("icon1",country1.getIconId());
        editor.putInt("name1",country1.getNameId());
        editor.putInt("unit1",country1.getUnitId());
        editor.putFloat("rate1", (float) country1.getRate());

        editor.putInt("icon2",country2.getIconId());
        editor.putInt("name2",country2.getNameId());
        editor.putInt("unit2",country2.getUnitId());
        editor.putFloat("rate2", (float) country2.getRate());
        editor.commit();
    }


    public static void readHistoryFromLocal(Context context, Country country1,Country country2){
        SharedPreferences sharedPreferences = context.getSharedPreferences("history",Context.MODE_PRIVATE);

        country1.setIconId(sharedPreferences.getInt("icon1", R.drawable.ic_us));
        country1.setNameId(sharedPreferences.getInt("name1",R.string.name_us));
        country1.setUnitId(sharedPreferences.getInt("unit1",R.string.unit_us));
        country1.setRate(sharedPreferences.getFloat("rate1", (float) Rate.us));

        country2.setIconId(sharedPreferences.getInt("icon2", R.drawable.ic_cn));
        country2.setNameId(sharedPreferences.getInt("name2",R.string.name_cn));
        country2.setUnitId(sharedPreferences.getInt("unit2",R.string.unit_cn));
        country2.setRate(sharedPreferences.getFloat("rate2", (float) Rate.cn));
    }


    public static void handleHistoryResponse(String s, List<Article> articleList){
        if(!TextUtils.isEmpty(s)){
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");
                Gson gson = new Gson();

                for(int i = 0; i < result.length(); i++){
                    String str = result.getJSONObject(i).toString();
  //                  Log.d("hhh",str);
                    Article article = gson.fromJson(str,Article.class);
                    articleList.add(article);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }




        }


    }
}
