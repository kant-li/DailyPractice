package com.coolweather.kant.gson;

/**
 * Created by kant on 2017/3/24.
 */

public class AQI {

    public AQICity city;
    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
