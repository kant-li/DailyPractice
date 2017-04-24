package com.coolweather.kant.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.kant.db.City;
import com.coolweather.kant.db.County;
import com.coolweather.kant.db.Dao;
import com.coolweather.kant.db.Province;
import com.coolweather.kant.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kant on 2017/3/23.
 */

public class Utility {

    /**
     * 创建新的修行项
     */
    public static boolean createDao(String name, String type, String fre, String goal) {

        try {
            Dao dao = new Dao();
            dao.setName(name);
            dao.setDetail("detail");
            dao.setSort(type);
            int fre1 = Integer.valueOf(fre);
            String fre2 = Integer.toString(fre1);
            dao.setFrequency(fre1);
            dao.setOn(1);
            dao.setStatus("status");

            long todayCount = getTodayCount();

            dao.setStart_date(todayCount);
            dao.setEnd_date(0);
            dao.setRecent(0);

            dao.setCount(0);
            int goal1 = Integer.valueOf(goal);
            dao.setGoal(goal1);

            dao.save();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public final static long getTodayCount() {

        long milSecondPerDay = 24 * 60 * 60 * 1000;

        java.util.Date dateNow = new java.util.Date();
        long count = (dateNow.getTime()/milSecondPerDay);
        return count;

    }

    /**
     * 解析处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject provinceObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(provinceObject.getString("name"));
                    city.setCityCode(provinceObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析处理服务器返回的县级数据
     */

    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties= new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return  new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
