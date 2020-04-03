package com.jucceed.minitool.appinfo;

import android.graphics.drawable.Drawable;

public class AppDetail {

    private Drawable appIcon;
    private String appName;
    private String packageName;
    private String appVersionCode;
    private String appAndroidVersion;
    private String appApiLevel;

    public AppDetail(){}

    public AppDetail(Drawable appIcon, String appName, String packageName, String appVersionCode, String appAndroidVersion, String appApiLevel) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.packageName = packageName;
        this.appVersionCode = appVersionCode;
        this.appAndroidVersion = appAndroidVersion;
        this.appApiLevel = appApiLevel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppAndroidVersion() {
        return appAndroidVersion;
    }

    public void setAppAndroidVersion(String appAndroidVersion) {
        this.appAndroidVersion = appAndroidVersion;
    }

    public String getAppApiLevel() {
        return appApiLevel;
    }

    public void setAppApiLevel(String appApiLevel) {
        this.appApiLevel = appApiLevel;
    }
}
