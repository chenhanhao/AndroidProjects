package com.kakacat.minitool.util;

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
}
