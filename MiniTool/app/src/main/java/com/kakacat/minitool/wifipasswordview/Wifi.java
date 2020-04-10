package com.kakacat.minitool.wifipasswordview;

public class Wifi {
    private String wifiImage;
    private String wifiName;
    private String wifiPwd;

    public Wifi(){}

    public Wifi(String wifiImage, String wifiName, String wifiPwd) {
        this.wifiImage = wifiImage;
        this.wifiName = wifiName;
        this.wifiPwd = wifiPwd;
    }

    public String getWifiImage() {
        return wifiImage;
    }

    public void setWifiImage(String wifiImage) {
        this.wifiImage = wifiImage;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPwd() {
        return wifiPwd;
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
    }
}
