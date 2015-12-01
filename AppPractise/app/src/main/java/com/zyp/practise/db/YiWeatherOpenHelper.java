package com.zyp.practise.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2015/12/1.
 */
public class YiWeatherOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_PROVINCE = "CREATE TABLE province(id integer primary key autoincrement,province_code text,province_name text)";

    private static final String CREATE_CITY = "CREATE TABLE CITY(id integer primary key autoincrement,city_code text,city_name text,province_id integer)";

    private static final String CREATE_COUNTY = "CREATE TABLE COUNTY(id integer primary key autoincrement,county_code text,county_name text,city_id integer)";
    private Context context;
    public YiWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
