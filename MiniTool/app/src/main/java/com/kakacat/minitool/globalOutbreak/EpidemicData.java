package com.kakacat.minitool.globalOutbreak;

public class EpidemicData {
/*    modifyTime	int	1584159933000	数据修改时间
    continents	string	欧洲	大洲
    provinceName	string	意大利	地区名
    currentConfirmedCount	int	14955	现存确诊人数
    confirmedCount	int	17660	累计确诊人数
    suspectedCount	int	1439	治愈人数
    deadCount	int	1266	死亡人数
    locationId	int	965008	地理位置编号
    countryShortCode	string	ITA	国家代码


    modifyTime": 1587275438000,
      "continents": "北美洲",
      "provinceName": "美国",
      "currentConfirmedCount": 629465,
      "confirmedCount": 734969,
      "confirmedCountRank": 1,
      "curedCount": 66601,
      "deadCount": 38903,
      "deadCountRank": 1,
      "deadRate": "5.29",
      "deadRateRank": 28,
      "locationId": 971002,
      "countryShortCode": "USA"


    */

    private long modifyTime;
    private String continents;
    private String provinceName;
    private int currentConfirmedCount;
    private int confirmedCount;

    private int suspectedCount;
    private int deadCount;
    private double deadCountRate;
    private String countryShortCode;
    private int deadCountRank;

    public EpidemicData() {}

    public int getDeadCountRank() {
        return deadCountRank;
    }

    public void setDeadCountRank(int deadCountRank) {
        this.deadCountRank = deadCountRank;
    }

    public double getDeadCountRate() {
        return deadCountRate;
    }

    public void setDeadCountRate(double deadCountRate) {
        this.deadCountRate = deadCountRate;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getContinents() {
        return continents;
    }

    public void setContinents(String continents) {
        this.continents = continents;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCurrentConfirmedCount() {
        return currentConfirmedCount;
    }

    public void setCurrentConfirmedCount(int currentConfirmedCount) {
        this.currentConfirmedCount = currentConfirmedCount;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(int confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public int getSuspectedCount() {
        return suspectedCount;
    }

    public void setSuspectedCount(int suspectedCount) {
        this.suspectedCount = suspectedCount;
    }

    public int getDeadCount() {
        return deadCount;
    }

    public void setDeadCount(int deadCount) {
        this.deadCount = deadCount;
    }


    public String getCountryShortCode() {
        return countryShortCode;
    }

    public void setCountryShortCode(String countryShortCode) {
        this.countryShortCode = countryShortCode;
    }
}
