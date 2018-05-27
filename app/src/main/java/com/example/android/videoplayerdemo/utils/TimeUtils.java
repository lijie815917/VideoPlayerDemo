package com.example.android.videoplayerdemo.utils;

public class TimeUtils {
    /**
     * 毫秒转字符串
     * @param seconds 毫秒数
     * @return 对应毫秒的字符串
     */
    public static String millsecondsToStr(int seconds){
        seconds = seconds / 1000;
        String result = "";
        int min = 0, second = 0;
        min = seconds / 60;
        second = seconds - min * 60;

        if (min < 1) {
            result += "0" + ":";
        } else {
            result += min + ":";
        }
        if (second < 10) {
            result += "0" + second;
        } else {
            result += second;
        }
        return result;
    }
}
