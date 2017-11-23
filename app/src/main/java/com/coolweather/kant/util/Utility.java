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

import java.util.List;

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
            dao.setEnd_date(0l);
            dao.setRecent(0l);

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

    public static boolean createDao(String name, String type, String endDate) {

        try {
            Dao dao = new Dao();
            dao.setName(name);
            dao.setDetail("detail");

            if (type.equals("")) {
                type = type + "未指定类别";
            }
            dao.setSort(type);
            dao.setFrequency(0);
            dao.setOn(1);
            dao.setStatus("status");

            long todayCount = getTodayCount();

            dao.setStart_date(todayCount);

            int deadlineDays = Integer.valueOf(endDate);
            long deadline = Utility.getTodayCount() + deadlineDays;

            dao.setEnd_date(deadline);
            dao.setRecent(0l);

            dao.setCount(0);
            dao.setGoal(1);

            dao.save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public final static long getTodayCount() {

        long milSecondPerDay = 24 * 60 * 60 * 1000;

        long eightHours = 8 * 60 * 60 * 1000;   //一个临时的丑陋的方法，解决手机上应用时间慢8小时的问题
        java.util.Date dateNow = new java.util.Date();
        long count = ((dateNow.getTime() + eightHours)/milSecondPerDay);
        return count;

    }

    public static void refreshDaoStatus() {

       //获得需要更新状态的事项，已关闭事项不更新——这个想法是错的！状态都要更新！

       List<Dao> daoList= Dao.findAll(Dao.class);
               //DataSupport.where("on = ?", "1").find(Dao.class);

       //逐个更新状态

       for (Dao dao : daoList) {

           //首先判断是否开启，如果是关闭状态，直接设置为关闭
           if (dao.getOn() == 1) {

               long today = getTodayCount();
               long endDate = dao.getEnd_date();

               //通过截止日期是否存在来判断是否为单次事件
               if (endDate > 0) {
                   //单次事项
                   if (endDate > today) {
                       dao.setStatus("hold");
                   } else {
                       //到期或过期的都属于当日事件
                       dao.setStatus("now");
                   }
                   dao.save();

               } else {
                   //重复事项
                   //这里统一使用系统开始日期到目标日期的天数间隔进行计算
                   long recent = dao.getRecent();
                   long startDate = dao.getStart_date();
                   int fre = dao.getFrequency();
                   long newRoundStartDate = today - ((today - startDate) % fre);    //新一轮事项的开始日期
                   long newRoundEndDate = newRoundStartDate + fre - 1;              //新一轮事项的结束日期

                   //如果最近一次执行事项比新一轮开始事件早，就需要执行，否则为已完成事项
                   if (recent < newRoundStartDate) {

                       //如果今天是新一轮事项的结束日期，为今天必须完成的事项，否则为接下来完成的事项
                       if (today < newRoundEndDate) {
                           dao.setStatus("hold");
                       } else {
                           dao.setStatus("now");
                       }

                   } else {
                       dao.setStatus("done");
                   }

                   dao.save();
               }
           } else {
               dao.setStatus("close");
               dao.save();
           }
       }
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
