package com.zyp.practise.model;

/**
 * Created by lenovo on 2015/12/1.
 */
public class Province {
    private Integer id;
    private String name;
    private String code ;

    public Province(){}
    public Province(Integer id,String name,String code){
        this.id = id;
        this.name = name;
        this.code = code;
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}

