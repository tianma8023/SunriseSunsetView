package com.github.tianma8023.ssvsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.tianma8023.formatter.SunriseSunsetLabelFormatter;
import com.github.tianma8023.model.Time;
import com.github.tianma8023.ssv.SunriseSunsetView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SunriseSunsetView mSunriseSunsetView;
    private TextView mSunriseTextView;
    private TextView mSunsetTextView;

    private TimePickerDialog mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSunriseTextView = findViewById(R.id.sunrise_time_tv);
        mSunsetTextView = findViewById(R.id.sunset_time_tv);
        Button updateButton = findViewById(R.id.update_btn);

        int sunriseHour = 6;
        int sunriseMinute = 17;
        int sunsetHour = 18;
        int sunsetMinute = 32;

        mSunriseSunsetView = findViewById(R.id.ssv);
        mSunriseSunsetView.setLabelFormatter(new SunriseSunsetLabelFormatter() {
            @Override
            public String formatSunriseLabel(@NonNull Time sunrise) {
                return formatLabel(sunrise);
            }

            @Override
            public String formatSunsetLabel(@NonNull Time sunset) {
                return formatLabel(sunset);
            }

            private String formatLabel(Time time) {
                return String.format(Locale.getDefault(), "%02dh %02dm", time.hour, time.minute);
            }
        });
        // initial some custom attributions
        // mSunriseSunsetView.setLabelTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        // mSunriseSunsetView.setLabelTextSize(30);
        // mSunriseSunsetView.setTrackColor(ContextCompat.getColor(this, R.color.amber));
        // mSunriseSunsetView.setSunColor(ContextCompat.getColor(this, R.color.teal));
        // mSunriseSunsetView.setShadowColor(ContextCompat.getColor(this, R.color.indigo));

        refreshSSV(sunriseHour, sunriseMinute, sunsetHour, sunsetMinute);

        mSunriseTextView.setText(String.format("%02d:%02d", sunriseHour, sunriseMinute));
        mSunsetTextView.setText(String.format("%02d:%02d", sunsetHour, sunsetMinute));

        mSunriseTextView.setOnClickListener(new ClickListener(mSunriseTextView));
        mSunsetTextView.setOnClickListener(new ClickListener(mSunsetTextView));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] srArr = mSunriseTextView.getText().toString().split(":");
                int sunriseHour = Integer.valueOf(srArr[0]);
                int sunriseMinute = Integer.valueOf(srArr[1]);
                String[] ssArr = mSunsetTextView.getText().toString().split(":");
                int sunsetHour = Integer.valueOf(ssArr[0]);
                int sunsetMinute = Integer.valueOf(ssArr[1]);
                refreshSSV(sunriseHour, sunriseMinute, sunsetHour, sunsetMinute);
            }
        });
    }


    private class ClickListener implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

        private TextView mSource;

        ClickListener(TextView source) {
            mSource = source;
        }

        @Override
        public void onClick(View v) {
            showTimePicker();
        }

        private void showTimePicker() {
            String[] timeArr = mSource.getText().toString().split(":");
            int hourOfDay = Integer.valueOf(timeArr[0]);
            int minute = Integer.valueOf(timeArr[1]);
            boolean is24HourMode = true;
            if (mTimePicker == null) {
                mTimePicker = TimePickerDialog.newInstance(this, hourOfDay, minute, is24HourMode);
            } else {
                mTimePicker.initialize(this, hourOfDay, minute, 0, is24HourMode);
            }
            mTimePicker.enableSeconds(false);
            mTimePicker.setVersion(TimePickerDialog.Version.VERSION_2);
            mTimePicker.show(getFragmentManager(), "TimePickerDialog");
        }

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            mSource.setText(hourOfDay + ":" + minute);
        }

    }

    private void refreshSSV(int sunriseHour, int sunriseMinute, int sunsetHour, int sunsetMinute) {
        mSunriseSunsetView.setSunriseTime(new Time(sunriseHour, sunriseMinute));
        mSunriseSunsetView.setSunsetTime(new Time(sunsetHour, sunsetMinute));
        mSunriseSunsetView.startAnimate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimePicker != null) {
            mTimePicker.dismiss();
            mTimePicker = null;
        }
    }
}
