package com.jucceed.minitool.appinfo;

public class Api {
    private int logoId;
    private String androidVersionName;
    private String apiLevel;
    private int appNum;
    private String appPercent;

    public Api(int logoId, String androidVersionName, String apiLevel, int appNum, String appPercent) {
        this.logoId = logoId;
        this.androidVersionName = androidVersionName;
        this.apiLevel = apiLevel;
        this.appNum = appNum;
        this.appPercent = appPercent;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public String getAndroidVersionName() {
        return androidVersionName;
    }

    public void setAndroidVersionName(String androidVersionName) {
        this.androidVersionName = androidVersionName;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    public int getAppNum() {
        return appNum;
    }

    public void setAppNum(int appNum) {
        this.appNum = appNum;
    }

    public String getAppPercent() {
        return appPercent;
    }

    public void setAppPercent(String appPercent) {
        this.appPercent = appPercent;
    }
}
