package com.github.tianma8023.formatter;

import android.support.annotation.NonNull;

import com.github.tianma8023.model.Time;

import java.util.Locale;

public class SimpleSunriseSunsetLabelFormatter implements SunriseSunsetLabelFormatter {
    @Override
    public String formatSunriseLabel(@NonNull Time sunrise) {
        return formatTime(sunrise);
    }

    @Override
    public String formatSunsetLabel(@NonNull Time sunset) {
        return formatTime(sunset);
    }

    private String formatTime(Time time) {
        return String.format(Locale.getDefault(), "%d:%d", time.hour, time.minute);
    }

}
