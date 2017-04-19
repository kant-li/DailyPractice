package com.coolweather.kant.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kant on 2017/3/24.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
