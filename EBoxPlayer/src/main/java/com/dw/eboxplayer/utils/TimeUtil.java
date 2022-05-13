package com.dw.eboxplayer.utils;


import java.util.Formatter;

/**
 * 日期：2022/5/6
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public class TimeUtil {
    private final static String VIDEO_TIME_FORMAT = "HH:mm:ss";

    /**
     * 返回 00:00:00(时:分:秒)
     *
     * @param milliSecond 毫秒值
     * @return
     */
    public static String getVideoTime(int milliSecond) {
        if (milliSecond <= 0) {
            return "00:00";
        }
        int second = milliSecond / 1000;
        int hourTime = second / 60 / 60;
        int minuteTime = second % 3600 / 60;
        int secondTime = second % 60;
        Formatter formatter = new Formatter();
        if (hourTime <= 0) {
            return formatter.format("%02d:%02d", minuteTime, secondTime).toString();
        } else {
            return formatter.format("%02d:%02d:%02d", hourTime, minuteTime, secondTime).toString();
        }
    }

    /**
     * 毫秒转秒
     * @param milliSecond
     * @return
     */
    public static int toSecond(int milliSecond) {
        return milliSecond / 1000;
    }
}
