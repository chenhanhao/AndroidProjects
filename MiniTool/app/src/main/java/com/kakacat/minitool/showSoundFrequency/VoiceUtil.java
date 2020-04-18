package com.kakacat.minitool.showSoundFrequency;

/**
 * 声音计算工具类
 * Created by shine on 18-10-15.
 */

public class VoiceUtil {
    /**
     * 获取声音的分贝
     *
     * @param bufferRead
     * @param length
     * @return
     */
    public static double getVolume(byte[] bufferRead, int length) {
        int volume = 0;

        for (int i = 0; i < bufferRead.length; i++) {
            volume += bufferRead[i] * bufferRead[i];
        }

        double mean = volume / (float) length;
        return mean;//10 * Math.log10(mean);
    }
}
