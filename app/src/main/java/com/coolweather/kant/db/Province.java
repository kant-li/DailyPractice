package com.coolweather.kant.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kant on 2017/3/23.
 */

public class Province extends DataSupport {

    //名字、代码

    private int id;
    private String provinceName;
    private int provinceCode;

    //getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
