package com.example.myweather.db;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setProvinceName(String provinceName){
        this.provinceName = provinceName;
    }

    public String getProvinceName(){
        return provinceName;
    }

    public void setProvinceCode(int provinceCode){
        this.provinceCode = provinceCode;
    }

    public int getProvinceCode(){
        return provinceCode;
    }
}
