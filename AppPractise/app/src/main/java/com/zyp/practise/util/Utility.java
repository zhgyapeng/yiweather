package com.zyp.practise.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zyp.practise.db.YiWeatherDB;
import com.zyp.practise.model.City;
import com.zyp.practise.model.County;
import com.zyp.practise.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.PreferencesFactory;

/**
 * Created by lenovo on 2015/12/1.
 */
public class Utility {
    public static synchronized boolean handleProvinceResponse(YiWeatherDB yiWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String provinces[] = response.split(",");
            if(provinces==null||provinces.length==0){
                return false;
            }
            for(String province:provinces){
                String p_info[] = province.split("\\|");
                Province p = new Province();
                p.setCode(p_info[0]);
                p.setName(p_info[1]);
                yiWeatherDB.saveProvince(p);
            }
            return true;
        }
        return false;
    }

    public static synchronized boolean handleCityResponse(YiWeatherDB yiWeatherDB,String response,String provinceCode){
        if(!TextUtils.isEmpty(response)){
            String cities[] = response.split(",");
            if(cities==null||cities.length==0){
                return false;
            }
            for(String city:cities){
                String p_info[] = city.split("\\|");
                City p = new City();
                p.setCode(p_info[0]);
                p.setName(p_info[1]);
                p.setProvinceId(provinceCode);
                yiWeatherDB.saveCity(p);
            }
            return true;
        }
        return false;
    }

    public static synchronized boolean handleCountyResponse(YiWeatherDB yiWeatherDB,String response,String city_code){
        if(!TextUtils.isEmpty(response)){
            String counties[] = response.split(",");
            if(counties==null||counties.length==0){
                return false;
            }
            for(String county:counties){
                String p_info[] = county.split("\\|");
                County p = new County();
                p.setCode(p_info[0]);
                p.setName(p_info[1]);
                p.setCityId(city_code);
                yiWeatherDB.saveCounty(p);
            }
            return true;
        }
        return false;
    }

    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesc = weatherInfo.getString("weather");
            String publishtime = weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesc,publishtime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesc, String publishtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍMM‘¬dd»’", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("cityName",cityName);
        editor.putString("weatherCode",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weatherDesp",weatherDesc);
        editor.putString("publishtime", publishtime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }
}
