package com.jucceed.remoteshutdown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyService myService = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder)service;
            myService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        final SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final EditText shutdownCommand = findViewById(R.id.shutdown_command);
        final EditText delayTime = findViewById(R.id.delay_time_shutdown);
        Button saveButton = findViewById(R.id.save_button);

        String command = sharedPreferences.getString("command","shutdown");
        String time = sharedPreferences.getString("time","120");

        shutdownCommand.setText(command);
        delayTime.setText(time);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command2 = shutdownCommand.getText().toString();
                int time2 = Integer.parseInt(delayTime.getText().toString());
                if(!command2.isEmpty() && time2 >= 0) {
                    editor.putString("command",command2);
                    editor.putString("time",String.valueOf(time2));
                    editor.apply();
                    Toast.makeText(MainActivity.this,"save successful!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"wrong input.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = new Intent(MainActivity.this,MyService.class);
        //       startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }


    public void askPermissions(){
        List<String> permissionList = new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_SMS);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(MainActivity.this,permissionList.toArray(new String[permissionList.size()]),1);
        }

    }
}

