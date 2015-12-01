package com.zyp.practise.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zyp.practise.model.City;
import com.zyp.practise.model.County;
import com.zyp.practise.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/12/1.
 */
public class YiWeatherDB {
    public static final String DB_NAME = "yiweather";

    public static final int VERSION = 1;

    private static YiWeatherDB yiWeatherDB;

    private SQLiteDatabase db;

    private YiWeatherDB(Context context){
        YiWeatherOpenHelper dbHelper = new YiWeatherOpenHelper(context,DB_NAME, null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized YiWeatherDB getInstance(Context context){
        if(yiWeatherDB ==null){
            yiWeatherDB = new YiWeatherDB(context);
        }
        return yiWeatherDB;
    }

    public void saveProvince(Province province){
        if(province==null){
            return;
        }
        ContentValues values = new ContentValues();
        values.put("province_name",province.getName());
        values.put("province_code",province.getCode());
        db.insert("province",null,values);
    }

    public void saveCity(City city){
        if(city==null){
            return;
        }
        ContentValues values = new ContentValues();
        values.put("city_name",city.getName());
        values.put("city_code",city.getCode());
        values.put("province_id",city.getProvinceId());
        db.insert("city",null,values);
    }

    public void saveCounty(County county){
        if(county==null){
            return;
        }
        ContentValues values = new ContentValues();
        values.put("county_name",county.getName());
        values.put("county_code",county.getCode());
        values.put("city_id",county.getCityId());
        db.insert("county",null,values);
    }

    public List<Province> loadProvinces(){
        List<Province> provinceArrayList = new ArrayList<Province>();
        Cursor cursor = db.query("province", new String[]{"id", "province_name", "province_code"}, null, null, null, null, null);
        Province province = null;
        while (cursor.moveToNext()){
            province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
            provinceArrayList.add(province);
        }
        return provinceArrayList;
    }

    public List<City> loadCitiesByProvincecode(String provincecode){
        List<City> cityArrayList = new ArrayList<City>();
        Cursor cursor = db.query("city", new String[]{"id", "city_name", "city_code","province_id"}, " province_id = ?", new String[]{provincecode}, null, null, null);
        City city = null;

        while (cursor.moveToNext()){
            city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
            cityArrayList.add(city);
        }
        return cityArrayList;
    }

    public List<County> loadCountiesByCitycode(String citycode){
        List<County> countyArrayList = new ArrayList<County>();
        Cursor cursor = db.query("county", new String[]{"id", "county_name", "county_code","city_id"}, "city_id = ?", new String[]{citycode}, null, null, null);
        County county = null;
        while (cursor.moveToNext()){
            county = new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setName(cursor.getString(cursor.getColumnIndex("county_name"))) ;
            county.setCode(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
            countyArrayList.add(county);
        }
        return countyArrayList;
    }


}
