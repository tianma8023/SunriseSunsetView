# SunriseSunsetView
An Android view to show sunrise and sunset dynamically which is common seen in Weather App.

天气App中常见的 Android 自定义控件，可以用来动态展示日出和日落。

## Screenshot
![SunriseSunsetView Sample](/ss/ssv_ss.gif)

## Import
1. To use this lib you need add `jitpack.io` repository to your **root** `build.gradle`:
    ```groovy
    allprojects {
        repositories {
            jcenter()
            maven { url 'https://jitpack.io' } // add this line
        }
    }
    ```
    Note: don't add the jitpack.io repository under buildscript closure.

2. Add SunriseSunsetView dependency in your **module** `build.gradle`:
    ```groovy
    // ...
    // if Android Gradle Plugin after v3.0
    implementation 'com.github.tianma8023:SunriseSunsetView:0.0.1' 
    // if Android Gradle Plugin before v3.0
    // compile 'com.github.tianma8023:SunriseSunsetView:0.0.1' 
    ```

## Usage
1. SunriseSunsetView can be placed in layout xml file:
    ```xml
    <com.github.tianma8023.ssv.SunriseSunsetView
        android:id="@+id/ssv"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:layout_width="match_parent"
        app:ssv_label_text_color="@color/colorAccent"
        app:ssv_label_text_size="16sp"
        app:ssv_label_horizontal_gap="12dp"
        app:ssv_label_vertical_gap="2dp"
        app:ssv_shadow_color="@color/lightGreen"
        app:ssv_sun_color="@color/amber"
        app:ssv_sun_radius="10dp"
        app:ssv_track_color="@color/teal"
        app:ssv_track_width="1.5dp"
    />
    ```
2. You need to set sunrise and sunset time before starting animation:
    ```java
    mSunriseSunsetView = (SunriseSunsetView) findViewById(R.id.ssv);
    // ...
    mSunriseSunsetView.setSunriseTime(new Time(sunriseHour, sunriseMinute));
    mSunriseSunsetView.setSunsetTime(new Time(sunsetHour, sunsetMinute));
    // start animation
    mSunriseSunsetView.startAnimate();
    ```

Custom attributes are supported:

|attribute                  |format          |default                             |description|
|:-------------------------:|:--------------:|:----------------------------------:|-----------|
|ssv_track_color            |color\|reference|<font color="#FFFFFF">#FFFFFF</font>|track color|
|ssv_track_width            |dimension       |4px                                 |track width|
|ssv_sun_radius             |dimension       |20px                                |sun radius|
|ssv_sun_color              |color\|reference|<font color="#FFFF00">#FFFF00</font>|sun color|
|ssv_shadow_color           |color\|reference|<span style="color:rgba(255,255,255,0.125)">#32FFFFFF</span>|shadow color|
|ssv_label_text_size        |dimension       |40px                                |label text size|
|ssv_label_text_color       |color\|reference|<font color="#FFFFFF">#FFFFFF</font>|label text color|
|ssv_label_vertical_offset  |dimension       |5px                                 |label vertical offset|
|ssv_label_horizontal_offset|dimension       |20px                                |label horizontal offset|

Other api:
```java
// fotmat label by set label formatter
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
```

## Sample
There is a sample project in this repo, or you can download [demo.apk](/apk/demo.apk) directly.

## License
```txt
SunriseSunsetView	
Copyright 2018 Tianma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
``` 