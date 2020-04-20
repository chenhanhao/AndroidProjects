package com.kakacat.minitool.dongCiDaCi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kakacat.minitool.R;
import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

public class DongCiDaCiActivity extends AppCompatActivity {

/*    采样率一般有5个等级 : 11025,22050,24000,44100,48000
    */

    private int REQUEST_RECORD_AUDIO = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_ci_da_ci);
 //       requestPermission();
        VisualizerView visualizerView = findViewById(R.id.visualizer);

        RecordingSampler recordingSampler = new RecordingSampler();
 //       recordingSampler.setVolumeListener(this);  // for custom implements
        recordingSampler.setSamplingInterval(100); // voice sampling interval
        recordingSampler.link(visualizerView);     // link to visualizer

        recordingSampler.startRecording();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        String permission = Manifest.permission.RECORD_AUDIO;
        if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{permission},REQUEST_RECORD_AUDIO);
        }else{

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_RECORD_AUDIO){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{
                finish();
            }
        }

    }



}