package com.easicare.weather.service;

import com.easicare.weather.common.WeatherResult;

/**
 * 当前天气接口
 *
 * @author wangkai 2019/1/17
 */
public interface ICurrentService {

    /**
     * 获取当前天气数据 --- 城市ID
     *
     * @param id    cityId
     * @param lang  description 的语言
     * @param units 温度单位
     * @return String
     */
    WeatherResult getCurrentDataByCityId(Integer id, String lang, String units);


    /**
     * 获取当前天气数据 --- 定位/经纬度
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param lang      语言
     * @param units     温度单位
     * @return
     */
    WeatherResult getCurrentDataByCityCoord(double latitude, double longitude, String lang, String units);
}
