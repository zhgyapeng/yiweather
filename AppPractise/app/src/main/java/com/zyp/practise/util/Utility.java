package com.zyp.practise.util;

import android.text.TextUtils;

import com.zyp.practise.db.YiWeatherDB;
import com.zyp.practise.model.City;
import com.zyp.practise.model.County;
import com.zyp.practise.model.Province;

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

    public static synchronized boolean handleCityResponse(YiWeatherDB yiWeatherDB,String response,int province_id){
        if(!TextUtils.isEmpty(response)){
            String cities[] = response.split(",");
            if(cities==null||cities.length==0){
                return false;
            }
            for(String city:cities){
                String p_info[] = city.split("|");
                City p = new City();
                p.setCode(p_info[0]);
                p.setName(p_info[1]);
                p.setProvinceId(province_id);
                yiWeatherDB.saveCity(p);
            }
            return true;
        }
        return false;
    }

    public static synchronized boolean handleCountyResponse(YiWeatherDB yiWeatherDB,String response,int city_id){
        if(!TextUtils.isEmpty(response)){
            String counties[] = response.split(",");
            if(counties==null||counties.length==0){
                return false;
            }
            for(String county:counties){
                String p_info[] = county.split("|");
                County p = new County();
                p.setCode(p_info[0]);
                p.setName(p_info[1]);
                p.setCityId(city_id);
                yiWeatherDB.saveCounty(p);
            }
            return true;
        }
        return false;
    }
}
