package com.kakacat.minitool.util;

import okhttp3.Response;

public interface HttpCallbackListener {
    void onSuccess(Response response);
    void onError();
}
