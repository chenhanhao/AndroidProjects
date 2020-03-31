package com.jucceed.remoteshutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SmsReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //     Toast.makeText(context,"successful",Toast.LENGTH_SHORT).show();
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        try{
            for (Object obj : objects) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) obj);
                String msgBody = msg.getDisplayMessageBody();
                SharedPreferences sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
                String shutdownCommand = sharedPreferences.getString("command","shutdown");
                int delayTime = Integer.parseInt(sharedPreferences.getString("time","120"));

                if(msgBody.equals(shutdownCommand))
                {
                    Toast.makeText(context,"Your device will be shutdown in " + delayTime + " s.",Toast.LENGTH_SHORT).show();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            shutdown();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask,delayTime * 1000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shutdown(){
        try{
            Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

