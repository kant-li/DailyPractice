package com.coolweather.kant.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kant on 2017/4/25.
 */

public class Record extends DataSupport {

    //设置基本属性
    String name;
    String type;
    long date;

    //getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
