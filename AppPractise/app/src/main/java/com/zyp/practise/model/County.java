package com.zyp.practise.model;

/**
 * Created by lenovo on 2015/12/1.
 */
public class County {

    private Integer id;
    private String name;
    private String code;
    private Integer cityId;

    public County() {
    }

    public County(Integer id, String name, String code, Integer cityId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cityId = cityId;
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "County{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", cityId='" + cityId + '\'' +
                '}';
    }
}
