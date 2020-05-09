package com.kakacat.minitool.main;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

public class GetAudioService extends Service {

    private Context context;
    private HandleBinder mBinder = new HandleBinder();
    private Uri uri;

    public GetAudioService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        uri = intent.getParcelableExtra("uri");
        context = getBaseContext();
        return mBinder;
    }


    class HandleBinder extends Binder{
        public void audioCapture(){
            new Thread(()->{
                String[] projections = {MediaStore.Video.Media.DATA};  //  列名
                Cursor cursor = context.getContentResolver().query(uri,projections, null, null, null);
                cursor.moveToFirst();
                String filePath = cursor.getString(0);
                cursor.close();
                SystemUtil.separateAudioFromVideo(filePath);
                SystemUtil.vibrate(context,100);
            }).start();
        }

    }
}
