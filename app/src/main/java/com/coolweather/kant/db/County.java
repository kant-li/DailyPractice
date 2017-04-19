package com.coolweather.kant.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kant on 2017/3/23.
 */

public class County extends DataSupport {

    //名字、天气id、城市代码

    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;

    //getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
