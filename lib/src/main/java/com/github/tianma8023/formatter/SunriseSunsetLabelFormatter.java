package com.github.tianma8023.formatter;

import android.support.annotation.NonNull;

import com.github.tianma8023.model.Time;


/**
 * 日出日落标签格式化
 */
public interface SunriseSunsetLabelFormatter {

    String formatSunriseLabel(@NonNull Time sunrise);

    String formatSunsetLabel(@NonNull Time sunset);
}
