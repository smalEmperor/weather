package com.easicare.weather.service;

import com.easicare.weather.common.CustomException;

/**
 * @author df
 * @date 2019/8/22
 */
public interface HefengCurrentService {
    /**
     * 根据城市id获取当前城市天气
     * @param id    城市id
     * @param lang  语言
     * @param units 单位
     */
    String getCurrentDataByCityId(String id, String lang, String units);

    /**
     * 根据城市id获取当前城市几天的天气
     * @param id    城市id
     * @param lang  语言
     * @param units 单位
     */
    String getForecastDataByCityId(String id, String lang, String units);

    /**
     * 根据经纬度获取当前天气
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言
     * @param units     单位
     */
    String getCurrentDataByCityCoord(Double latitude, Double longitude, String lang, String units);

    /**
     * 根据经纬度获取多天的天气
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言
     * @param units     单位
     */
    String getForecastDataByCityCoord(Double latitude, Double longitude, String city, int flag,
                                      String lang, String units) throws CustomException;
}
