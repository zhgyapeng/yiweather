package com.zyp.practise.model;

/**
 * Created by lenovo on 2015/12/1.
 */
public class City {
    private Integer id;
    private String name;
    private String code;
    private Integer provinceId;

    public City() {
    }

    public City(Integer id, String name, String code, Integer provinceId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.provinceId = provinceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", provinceId='" + provinceId + '\'' +
                '}';
    }
}
