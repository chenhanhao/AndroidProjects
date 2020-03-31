package com.jucceed.remoteshutdown;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    //  public static SmsReceiver2 smsReceiver2;

    public final MyBinder myBinder = new MyBinder();

    public MyService() {
        //     smsReceiver2 = new SmsReceiver2();
        //      IntentFilter intentFilter = new IntentFilter();
        //       intentFilter.addAction("com.example.test");
        //      registerReceiver(smsReceiver2,intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    @Override
    public void onCreate(){
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //      unregisterReceiver(smsReceiver2);
    }

    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }

    }



}

