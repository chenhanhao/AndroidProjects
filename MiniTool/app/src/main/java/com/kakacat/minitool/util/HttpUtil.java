package com.kakacat.minitool.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    private static OkHttpClient okHttpClient;

    public static void sendOkHttpRequest(String address,HttpCallbackListener listener){
        new Thread(()->{
            try{
                OkHttpClient client = getInstance();
                Request request = new Request.Builder()
                        .url(address)
                        .addHeader("User-Agent","Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.9 Mobile Safari/537.36")
                        .build();
                Response response = client.newCall(request).execute();
                if(response != null){
                    listener.onSuccess(response);
                }else{
                    listener.onError();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }


    public static OkHttpClient getInstance(){
        if(okHttpClient == null)
            okHttpClient = new OkHttpClient();
        return okHttpClient;
    }
}
