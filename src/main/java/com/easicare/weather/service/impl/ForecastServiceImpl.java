package com.easicare.weather.service.impl;

import com.easicare.weather.service.IForecastService;
import com.easicare.weather.common.WeatherCommon;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author wangkai 2019/1/21
 */
@Service
public class ForecastServiceImpl extends WeatherCommon implements IForecastService {

    @Override
    public Object getForecastWeatherById(Integer id, String lang, String units, Integer cnt) {
        OWM owm = setOwm(lang,units);
        try {
            // DailyWeatherForecast dailyWeatherForecast = owm.dailyWeatherForecastByCityId(id, cnt);
            HourlyWeatherForecast hourlyWeatherForecast = owm.hourlyWeatherForecastByCityId(id);
            return hourlyWeatherForecast.toString();
            // return dailyWeatherForecast.toString();
        } catch (APIException e) {
            e.printStackTrace();
        }
        return null;
    }
}
