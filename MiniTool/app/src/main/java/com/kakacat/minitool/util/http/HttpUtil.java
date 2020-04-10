package com.kakacat.minitool.util.http;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,HttpCallbackListener listener){
        new Thread(()->{
            try{
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(address).build();
                Response response = client.newCall(request).execute();
                if(response != null){
                    listener.onSuccess(response.body().string());
                }else{
                    listener.onError();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }
}
