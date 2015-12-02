package com.zyp.practise.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyp.practise.R;
import com.zyp.practise.db.YiWeatherDB;
import com.zyp.practise.model.City;
import com.zyp.practise.model.County;
import com.zyp.practise.model.Province;
import com.zyp.practise.util.HttpUtil;
import com.zyp.practise.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/12/1.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private YiWeatherDB yiWeatherDB;

    private List<String> datalist = new ArrayList<String>();

    private List<County> countyList;

    private List<Province> provinceList;

    private List<City> cityList;
    private int currentLevel;

    private City selectedCity;

    private Province selectedProvince;

    private County selectedCounty;

    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity",false);
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        // �Ѿ�ѡ���˳����Ҳ��Ǵ�WeatherActivity��ת�������Ż�ֱ����ת��WeatherActivity
        if (prefs.getBoolean("city_selected", false)
                && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.listview);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        yiWeatherDB = YiWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryAllCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryAllCounties();
                }else if (currentLevel == LEVEL_COUNTY) {
                    String countyCode = countyList.get(position).getCode();
                    Intent intent = new Intent(ChooseAreaActivity.this,
                            WeatherActivity.class);
                    intent.putExtra("county_code", countyCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryAllProvinces();
    }

    /**
     * ��ѯȫ����ʡ�ݣ����ȴ����ݿ��в�ѯ��û��������Զ�̷��������в�ѯ
     */
    public void queryAllProvinces(){
        provinceList = yiWeatherDB.loadProvinces();
        if(provinceList!=null&&provinceList.size()>0){
            datalist.clear();
            for(Province province:provinceList){
                datalist.add(province.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("�й�");
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }
    }

    public void queryAllCities(){
        cityList = yiWeatherDB.loadCitiesByProvincecode(selectedProvince.getCode());
        if(cityList!=null&&cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getCode(),"city");
        }
    }

    public void queryAllCounties(){
        countyList = yiWeatherDB.loadCountiesByCitycode(selectedCity.getCode());
        if(countyList!=null&&countyList.size()>0){
            datalist.clear();
            for(County county:countyList){
                datalist.add(county.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getName());
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCode(),"county");
        }
    }

    private void queryFromServer(final String code,final String type) {
        String address ;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else{
            //��ѯ����ʡ��
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(yiWeatherDB, response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(yiWeatherDB, response,selectedProvince.getCode());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(yiWeatherDB, response,selectedCity.getCode());
                }
                if (result) {
                    // ���߳̽��д���
                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryAllProvinces();
                            } else if ("city".equals(type)) {
                                queryAllCities();
                            } else if ("county".equals(type)) {
                                queryAllCounties();
                            }
                        }
                    }));
                }

            }

            @Override
            public void onError(String error) {
                // ͨ��runOnUiThread()�����ص����̴߳����߼�
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,
                                "����ʧ��", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    /**
     * ��ʾ���ȶԻ���
     */
    private void showProgressDialog() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("��ʾ");
            progressDialog.setMessage("���ڼ�������...");
        }
        progressDialog.show();
    }

    /**
     * �رս��ȶԻ���
     */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
     ��һ�д��롪��Android
     506
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryAllCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryAllProvinces();
        } else {
            if (isFromWeatherActivity) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}
