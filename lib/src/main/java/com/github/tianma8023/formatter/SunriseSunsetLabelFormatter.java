package com.github.tianma8023.formatter;

import com.github.tianma8023.model.Time;


/**
 * 日出日落标签格式化
 */
public interface SunriseSunsetLabelFormatter {

    String formatSunriseLabel(Time sunrise);

    String formatSunsetLabel(Time sunset);
}
