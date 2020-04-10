package com.kakacat.minitool.util.http;

public interface HttpCallbackListener {
    void onSuccess(String s);
    void onError();
}
