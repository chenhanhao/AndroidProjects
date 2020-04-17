package com.kakacat.minitool.garbageClassification;

public class Garbage {

/*
    name	string	智能眼镜	废弃物名称
    type	int	0	垃圾分类，0为可回收、1为有害、2为厨余(湿)、3为其他(干)
    aipre	int	0	智能预判，0为正常结果，1为预判结果
    explain	string	适宜回收、可循环利用的生活废弃物	分类解释
    contain	string	各类废金属、玻璃瓶、易拉罐、饮料瓶......	包含类型
    tip	string	轻投轻放；清洁干燥，避免污染，费纸尽量平整......	投放提示*/

    private String name;
    private int type;
    private int aipre;
    private String explain;
    private String contain;
    private String tip;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAipre() {
        return aipre;
    }

    public void setAipre(int aipre) {
        this.aipre = aipre;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
