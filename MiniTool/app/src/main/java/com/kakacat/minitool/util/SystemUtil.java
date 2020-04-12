package com.kakacat.minitool.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.DataOutputStream;

public class SystemUtil {


    public static void modifyDpi(String val){
        String[] commands = new String[]{
                "wm density " + val + "\n"
        };
        executeLinuxCommand(commands,true,false);
    }

    public static void executeLinuxCommand(String[] commands,boolean needRoot,boolean waitFor){
        try{
            Process process;
            if(needRoot) process = Runtime.getRuntime().exec("su");
            else process = Runtime.getRuntime().exec("");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            for(String command : commands) os.writeBytes(command);
            os.flush();
            if(waitFor) process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void copyToClipboard(Context context, String label, CharSequence content){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(label,content));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static int getElectricity(Context context) {
        BatteryManager batterymanager = (BatteryManager) context.getSystemService(context.BATTERY_SERVICE);
        batterymanager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return batterymanager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }


    public static void setBatteryLevel(String val){
        String[] commands = new String[]{
          "dumpsys battery set level " + val + "\n"
        };
        executeLinuxCommand(commands,true,false);
    }

    public static void resetBattery(){
        String[] commands = new String[]{
                "dumpsys battery reset" + "\n"
        };
        executeLinuxCommand(commands,true,false);
    }



}
