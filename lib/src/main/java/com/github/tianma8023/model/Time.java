package com.github.tianma8023.model;

/**
 * 日出日落相关时间
 */
public class Time {

    public static final int MINUTES_PER_HOUR = 60;

    /**
     * 时
     */
    public int hour;
    /**
     * 分
     */
    public int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int transformToMinutes() {
        return hour * MINUTES_PER_HOUR + minute;
    }
}
