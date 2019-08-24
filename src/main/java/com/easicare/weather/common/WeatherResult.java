package com.easicare.weather.common;

/**
 * 返回参数
 * @author wangkai 2019/1/17
 */
public class WeatherResult {

    private String temp = "";
    private String city = "";
    private String icon = "";
    private String back = "";

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIcon() {
        return icon;
    }

    private void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBack() {
        return back;
    }

    private void setBack(String back) {
        this.back = back;
    }


    public void setImage(String iconId){

        if (iconId.startsWith("01")) {
            // 晴天
            this.setIcon(Constant.SUNNY_ICON);
            this.setBack(Constant.SUNNY_BACK);
        } else if (iconId.startsWith("02")) {
            // 阴天
            this.setIcon(Constant.OVERCAST_ICON);
            this.setBack(Constant.OVERCAST_BACK);
        } else if (iconId.startsWith("03")) {
            // 多云
            this.setIcon(Constant.CLOUDY_ICON);
            this.setBack(Constant.CLOUDY_BACK);
        } else if (iconId.startsWith("09") || iconId.startsWith("10")) {
            // 雨天
            this.setIcon(Constant.RAIN_ICON);
            this.setBack(Constant.RAIN_BACK);
        } else {
            // 其他 全显示晴天
            this.setIcon(Constant.SUNNY_ICON);
            this.setBack(Constant.SUNNY_BACK);
        }
    }
}
