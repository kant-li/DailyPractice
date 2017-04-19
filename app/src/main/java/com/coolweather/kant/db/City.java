package com.coolweather.kant.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kant on 2017/3/23.
 */

public class City extends DataSupport {

    //名字、代码、所属省份代码

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    //getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
