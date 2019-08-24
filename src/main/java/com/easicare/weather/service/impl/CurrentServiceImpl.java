package com.easicare.weather.service.impl;

import com.alibaba.fastjson.JSON;
import com.easicare.weather.common.WeatherResult;
import com.easicare.weather.service.ICurrentService;
import com.easicare.weather.common.WeatherCommon;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 使用OWM 获取当前天气数据
 * @author wangkai 2019/1/17
 */
@Service
public class CurrentServiceImpl extends WeatherCommon implements ICurrentService {

    private static Logger log = LoggerFactory.getLogger(CurrentServiceImpl.class);

    @Override
    public WeatherResult getCurrentDataByCityId(Integer id, String lang, String units) {

        // get data from cache
        // WeatherResult obj = WeatherCacheManager.cache.getIfPresent(id);
        // if (obj != null) {
        //     log.info("return data from cache. {}", JSON.toJSON(obj));
        //     return obj;
        // }

        WeatherResult result = new WeatherResult();

        try {
            // get data from api
            OWM owm = setOwm(lang, units);
            CurrentWeather currentWeather = owm.currentWeatherByCityId(id);

            result = getCurrentData(currentWeather);
            // WeatherCacheManager.cache.put(id, result);
        } catch (Exception e) {
            log.error("get current data by city id exception:{}", e);
        }

        return result;
    }

    @Override
    public WeatherResult getCurrentDataByCityCoord(double latitude, double longitude, String lang, String units) {
        // cache key
        String key = latitude + "," + longitude;

        // get data from cache
        // Object obj = WeatherCacheManager.cache.getIfPresent(key);
        // if (obj != null) {
        //     log.info("return data from cache. {}", JSON.toJSON(obj));
        //     return (WeatherResult) obj;
        // }

        WeatherResult result = new WeatherResult();

        try {
            // get data from api
            OWM owm = setOwm(lang, units);
            CurrentWeather currentWeather = owm.currentWeatherByCoords(latitude, longitude);

            result = getCurrentData(currentWeather);
            // WeatherCacheManager.cache.put(key, result);
        } catch (Exception e) {
            log.error("get current data by city coord exception:{}", e);
        }

        return result;
    }

    /**
     * get current weather result data
     *
     * @param currentWeather current weather data
     * @return WeatherResult
     */
    private WeatherResult getCurrentData(CurrentWeather currentWeather) {

        WeatherResult result = new WeatherResult();
        try {
            // checking data retrieval was successful or not
            if (currentWeather.hasRespCode()
                    && currentWeather.getRespCode() == 200) {
                // city
                if (currentWeather.hasCityName()) {
                    result.setCity(currentWeather.getCityName());
                }
                // temp (min-max)
                if (currentWeather.hasMainData()
                        && currentWeather.getMainData().hasTempMin()
                        && currentWeather.getMainData().hasTempMax()) {

                    result.setTemp(currentWeather.getMainData().getTempMin() + "-"
                            + currentWeather.getMainData().getTempMax());
                }
                //icon & back
                if (currentWeather.hasWeatherList()) {
                    String iconId = currentWeather.getWeatherList().get(0).getIconCode();
                    if (!StringUtils.isEmpty(iconId)) {
                        result.setImage(iconId);
                    }
                }

            }
        } catch (Exception e) {
            log.error("weather current by id exception : ", e);
        }
        log.info("return data from owm : {}", JSON.toJSON(result));
        return result;
    }


}
