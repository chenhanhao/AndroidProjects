package com.kakacat.minitool.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.DataOutputStream;

public class SystemUtil {


    public static void modifyDpi(int val){
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
}
