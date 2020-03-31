package com.example.myweather.util;

public interface HttpCallbackListener {
    public void onSuccess(String responseData);
    public void onError();

}
