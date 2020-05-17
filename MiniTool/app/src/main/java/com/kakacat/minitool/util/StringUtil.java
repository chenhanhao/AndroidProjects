package com.kakacat.minitool.util;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class StringUtil {

    public static String byteToMegabyte(long bytes){
        String result;
        if(bytes > 1024 * 1024 * 1024){
            String temp = String.valueOf(bytes / (1024 * 1024 * 1024.0));
            result = temp.substring(0,temp.indexOf('.') + 2) + "G";
        }else{
            String temp = String.valueOf(bytes / (1024 * 1024.0));
            result = temp.substring(0,temp.indexOf('.') + 2) + "M";
        }
        return result;
    }

    public static String getDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

}
