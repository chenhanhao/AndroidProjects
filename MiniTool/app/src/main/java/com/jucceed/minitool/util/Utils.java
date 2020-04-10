package com.jucceed.minitool.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    /**
     * MD5加密
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr,boolean addColon){
        MessageDigest messageDigest;
        String result = "";
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            result = byteToString(byteArray,addColon);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取app签名md5值,与“keytool -list -keystore D:\Desktop\app_key”‘keytool -printcert     *file D:\Desktop\CERT.RSA’获取的md5值一样
     */
    public static String getSignMd5Str(PackageInfo packageInfo) {
        Signature[] signs = packageInfo.signatures;
        Signature sign = signs[0];
        String signStr = encryptionMD5(sign.toByteArray(),true);
        return signStr;
    }


    public static String encryptBASE64(byte[] key){
        String result = Base64.encodeToString(key,Base64.DEFAULT);
        return result;
    }


    public static void copyToClipboard(Context context,String label,CharSequence content){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(label,content));
    }

    public static String encryptHmacSHA1(byte[] encryptText) {
        try{
            String encryptKey = Utils.encryptBASE64(encryptText);
            byte[] data = encryptKey.getBytes();
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);
            //完成 Mac 操作
            byte[] bytes = mac.doFinal(encryptText);
            return byteToString(bytes,false);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String byteToString(byte[] bytes,boolean addColon){
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
                sb.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            else
                sb.append(Integer.toHexString(0xFF & bytes[i]));
            if(addColon)
                if(i != bytes.length - 1)
                    sb.append(':');
        }
        return sb.toString();
    }


    public static void showKeyboard(Context context, View v){
        InputMethodManager manager = ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

    public static void closeKeyboard(Context context,View view){
        InputMethodManager manager = ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
