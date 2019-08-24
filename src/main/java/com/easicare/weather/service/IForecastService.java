package com.easicare.weather.service;


/**
 * 天气预报Service
 * @author wangkai 2019/1/21
 */
public interface IForecastService {

    Object getForecastWeatherById(Integer id,String lang,String units,Integer cnt);

}
