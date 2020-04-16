package com.kakacat.minitool.wifipasswordview;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class WiFiConfigSAXHandle extends DefaultHandler {

    private StringBuilder sb;
    private List<Wifi> wifiList;
    private Wifi wifi;
    private boolean startGetName;
    private boolean startGetPwd;

    public WiFiConfigSAXHandle(List<Wifi> wifiList) {
        this.wifiList = wifiList;
        sb = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equals("string")){
            if(attributes.getValue(0).equals("SSID")){  //开始获得name
                wifi = new Wifi();
                startGetName = true;
            }else if(attributes.getValue(0).equals("PreSharedKey")){ //开始获得密码
                startGetPwd = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(startGetName){ //如果已经开始获得name,那么这时候已经获取完了name,去掉首尾的引号
            String name = sb.toString();
            name = name.substring(1,name.length() - 1);
            wifi.setWifiName(name);
            wifi.setWifiImage(name.substring(0,1));
            startGetName = false;
            sb.delete(0,sb.length());
        }else if(startGetPwd){ //如果已经开始获得pwd,那么这时候已经获取完了pwd,去掉首尾的引号
            String pwd = sb.toString();
            pwd = pwd.substring(1,pwd.length() - 1);
            wifi.setWifiPwd(pwd);
            wifiList.add(wifi);
            sb.delete(0,sb.length());
            startGetPwd = false;
            wifi = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if(startGetName){
            sb.append(ch,start,length);
        }else if(startGetPwd){
            sb.append(ch,start,length);
        }
    }
}
