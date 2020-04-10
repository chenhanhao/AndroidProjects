package com.kakacat.minitool.currencyConversion;

public class Country {
    private int iconId;
    private int nameId;
    private int unitId;
    private double rate;

    public Country(){}

    public Country(int iconId, int nameId, int unitId, double rate) {
        this.iconId = iconId;
        this.nameId = nameId;
        this.unitId = unitId;
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int flag) {
        this.iconId = flag;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}
