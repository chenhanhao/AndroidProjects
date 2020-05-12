package com.kakacat.minitool.util;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class SystemUtil {


    public static void modifyDpi(String val){
        String[] commands = new String[]{
                "wm density " + val + "\n"
        };
        executeLinuxCommand(commands,true,false);
    }

    public static void executeLinuxCommand(String[] commands,boolean needRoot,boolean waitFor){
        try{
            if(needRoot){
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                for(String command : commands) os.writeBytes(command);
                os.flush();
                if(waitFor) process.waitFor();
            } else{
                Runtime runtime = Runtime.getRuntime();
                for(String cmd : commands)
                    runtime.exec(new String[]{"/bin/sh","-c",cmd});
            }

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


    public static void addADTStoPacket(byte[] packet, int packetLen) {
        /*
        标识使用AAC级别 当前选择的是LC
        一共有1: AAC Main 2:AAC LC (Low Complexity) 3:AAC SSR (Scalable Sample Rate) 4:AAC LTP (Long Term Prediction)
        */
        int profile = 2;
        int frequencyIndex = 0x04; //设置采样率
        int channelConfiguration = 2; //设置频道,其实就是声道

        // fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (frequencyIndex << 2) + (channelConfiguration >> 2));
        packet[3] = (byte) (((channelConfiguration & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }

    public static String separateAudioFromVideo(String filePath){
        try{
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(filePath);      //设置视频路径
            int audioIndex = 0;
            int trackCount = mediaExtractor.getTrackCount();
            MediaFormat audioFormat = null;
            for(int i = 0; i < trackCount; i++){    //得到音轨
                MediaFormat mediaFormat = mediaExtractor.getTrackFormat(i);
                String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                if(mime.startsWith("audio")){
                    audioIndex = i;
                    audioFormat = mediaFormat;
                    break;
                }
            }

            File audioFile = new File(Environment.getExternalStorageDirectory() + "/MiniTool/" + filePath.substring(filePath.lastIndexOf('/') + 1,filePath.length() - 1) + "3");
            if(audioFile.exists()){
                audioFile.delete();
            }else{
                File parentFile = audioFile.getParentFile();
                parentFile.mkdirs();
                audioFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(audioFile);
            int maxAudioBufferCount = audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
            ByteBuffer audioByteBuffer = ByteBuffer.allocate(maxAudioBufferCount);
            mediaExtractor.selectTrack(audioIndex);
            int len;

            while((len = mediaExtractor.readSampleData(audioByteBuffer,0)) != -1){
                byte[] bytes = new byte[len];
                audioByteBuffer.get(bytes);
                byte[] adtsData = new byte[len + 7];
                SystemUtil.addADTStoPacket(adtsData, len + 7);
                System.arraycopy(bytes,0,adtsData,7,len);
                fos.write(adtsData);
                audioByteBuffer.clear();
                mediaExtractor.advance();
            }
            fos.flush();
            fos.close();
            mediaExtractor.release();
            return "提取完成,保存在目录" + audioFile.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "提取失败...";
    }



    public static void vibrate(Context context, long milliseconds){
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }


    public static void openMarket(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

}
