package com.kakacat.minitool.main;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.kakacat.minitool.FakeBattery;
import com.kakacat.minitool.ModifyDpi;
import com.kakacat.minitool.inquireIp.InquireIpActivity;
import com.kakacat.minitool.textEncryption.TextEncryptionActivity;
import com.kakacat.minitool.util.ui.UiUtil;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyGeekFragment extends MyFragment implements MyAdapter.OnItemClickListener {

    private Activity activity;
    private View rootView;
    ServiceConnection serviceConnection;

    MyGeekFragment(List<MainItem> itemList) {
        super(itemList);
        super.myAdapter.setOnItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        return rootView;
    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = null;
        switch (position){
            case 0:{
                intent = new Intent(activity, TextEncryptionActivity.class);
                break;
            }
            case 1:{
                ModifyDpi modifyDpi = ModifyDpi.getInstance(activity,rootView);
                UiUtil.setShadow(activity);
                modifyDpi.start();
                break;
            }
            case 2:{
                FakeBattery fakeBattery = FakeBattery.getInstance(activity,rootView);
                UiUtil.setShadow(activity);
                fakeBattery.start();
                break;
            }
            case 3:{
                audioCapture();
                break;
            }
            case 4:{
                intent = new Intent(activity, InquireIpActivity.class);
                break;
            }
        }

        if(intent != null) startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startSelectVideo();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int REQUEST_WRITE = 1;
        if(requestCode == REQUEST_WRITE && resultCode == RESULT_OK && data != null){
            Intent intent = new Intent(activity, GetAudioService.class);
            intent.putExtra("uri",data.getData());
            activity.bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void audioCapture() {
        if(serviceConnection == null)
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    GetAudioService.HandleBinder binder = (GetAudioService.HandleBinder)service;
                    binder.audioCapture();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if(ActivityCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            startSelectVideo();
        }
    }

    private void startSelectVideo(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent,1);
    }

}
