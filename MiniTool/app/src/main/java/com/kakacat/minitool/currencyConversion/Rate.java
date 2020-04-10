package com.kakacat.minitool.currencyConversion;

import com.kakacat.minitool.R;

public class Rate {
    public static double us = 7.0847;
    public static double eu = 7.66035;
    public static double hk = 0.91375;
    public static double jp = 0.063617;
    public static double uk = 8.3778;
    public static double au = 4.2708;
    public static double ca = 4.93065;
    public static double th = 0.2155;
    public static double sg = 4.8993;
    public static double ch = 7.21565;
    public static double dk = 1.026;
    public static double ma = 0.8871;
    public static double my = 1.61385;
    public static double no = 0.6478;
    public static double nz = 0.415735;
    public static double ph = 0.128965;
    public static double ru = 0.09125;
    public static double se = 0.7015;
    public static double tw = 0.22375;
    public static double br = 1.7669;
    public static double kr = 0.002885;
    public static double za = 0.4077;
    public static double cn = 1.0;

    public static void setRate(double rate,int num){
        switch (num){
            case 1  : us = rate; break;
            case 2  : eu = rate; break;
            case 3  : hk = rate; break;
            case 4  : jp = rate; break;
            case 5  : uk = rate; break;
            case 6  : au = rate; break;
            case 7  : ca = rate; break;
            case 8  : th = rate; break;
            case 9  : sg = rate; break;
            case 10 : ch = rate; break;
            case 11 : dk = rate; break;
            case 12 : ma = rate; break;
            case 13 : my = rate; break;
            case 14 : no = rate; break;
            case 15 : nz = rate; break;
            case 16 : ph = rate; break;
            case 17 : ru = rate; break;
            case 18 : se = rate; break;
            case 19 : tw = rate; break;
            case 20 : br = rate; break;
            case 21 : kr = rate; break;
            case 22 : za = rate; break;
            case 23 : cn = rate; break;
            default :            break;
        }
    }

    public static double getRate(int num){
        switch (num){
            case 1  : return us;
            case 2  : return eu;
            case 3  : return hk;
            case 4  : return jp;
            case 5  : return uk;
            case 6  : return au;
            case 7  : return ca;
            case 8  : return th;
            case 9  : return sg;
            case 10 : return ch;
            case 11 : return dk;
            case 12 : return ma;
            case 13 : return my;
            case 14 : return no;
            case 15 : return nz;
            case 16 : return ph;
            case 17 : return ru;
            case 18 : return se;
            case 19 : return tw;
            case 20 : return br;
            case 21 : return kr;
            case 22 : return za;
            default : return cn;
        }
    }

    public static int getNameId(int num){
        switch (num){
            case 1  : return R.string.name_us;
            case 2  : return R.string.name_eu;
            case 3  : return R.string.name_hk;
            case 4  : return R.string.name_jp;
            case 5  : return R.string.name_uk;
            case 6  : return R.string.name_au;
            case 7  : return R.string.name_ca;
            case 8  : return R.string.name_th;
            case 9  : return R.string.name_sg;
            case 10 : return R.string.name_ch;
            case 11 : return R.string.name_dk;
            case 12 : return R.string.name_ma;
            case 13 : return R.string.name_my;
            case 14 : return R.string.name_no;
            case 15 : return R.string.name_nz;
            case 16 : return R.string.name_ph;
            case 17 : return R.string.name_ru;
            case 18 : return R.string.name_se;
            case 19 : return R.string.name_tw;
            case 20 : return R.string.name_br;
            case 21 : return R.string.name_kr;
            case 22 : return R.string.name_za;
            default : return R.string.name_cn;
        }
    }

    public static int getIconId(int num){
        switch (num){
            case 1  : return R.drawable.ic_us;
            case 2  : return R.drawable.ic_eu;
            case 3  : return R.drawable.ic_hk;
            case 4  : return R.drawable.ic_jp;
            case 5  : return R.drawable.ic_uk;
            case 6  : return R.drawable.ic_au;
            case 7  : return R.drawable.ic_ca;
            case 8  : return R.drawable.ic_th;
            case 9  : return R.drawable.ic_sg;
            case 10 : return R.drawable.ic_ch;
            case 11 : return R.drawable.ic_dk;
            case 12 : return R.drawable.ic_ma;
            case 13 : return R.drawable.ic_my;
            case 14 : return R.drawable.ic_no;
            case 15 : return R.drawable.ic_nz;
            case 16 : return R.drawable.ic_ph;
            case 17 : return R.drawable.ic_ru;
            case 18 : return R.drawable.ic_se;
            case 19 : return R.drawable.ic_tw;
            case 20 : return R.drawable.ic_br;
            case 21 : return R.drawable.ic_kr;
            case 22 : return R.drawable.ic_za;
            default : return R.drawable.ic_cn;
        }
    }

    public static int getMoneyTypeId(int num){
        switch (num){
            case 1  : return R.string.unit_us;
            case 2  : return R.string.unit_eu;
            case 3  : return R.string.unit_hk;
            case 4  : return R.string.unit_jp;
            case 5  : return R.string.unit_uk;
            case 6  : return R.string.unit_au;
            case 7  : return R.string.unit_ca;
            case 8  : return R.string.unit_th;
            case 9  : return R.string.unit_sg;
            case 10 : return R.string.unit_ch;
            case 11 : return R.string.unit_dk;
            case 12 : return R.string.unit_ma;
            case 13 : return R.string.unit_my;
            case 14 : return R.string.unit_no;
            case 15 : return R.string.unit_nz;
            case 16 : return R.string.unit_ph;
            case 17 : return R.string.unit_ru;
            case 18 : return R.string.unit_se;
            case 19 : return R.string.unit_tw;
            case 20 : return R.string.unit_br;
            case 21 : return R.string.unit_kr;
            case 22 : return R.string.unit_za;
            default : return R.string.unit_cn;
        }
    }
}
