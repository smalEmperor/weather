package com.easicare.weather.controller;

import com.easicare.weather.service.HefengCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 和风天气接口
 * @author wangkai  2019/3/7
 */
@RestController
public class HefengWeatherController {

    private final HefengCurrentService hefengCurrentServiceImpl;

    @Autowired
    public HefengWeatherController(HefengCurrentService hefengCurrentServiceImpl) {
        this.hefengCurrentServiceImpl = hefengCurrentServiceImpl;
    }

    /**
     * 根据城市id获取当前城市天气
     * @param id    城市id
     * @param lang  语言
     * @param units 单位
     */
    @RequestMapping("/weather/current")
    public String getCurrWeatherOwmByID(@RequestParam(required = false, defaultValue = "CN101010100") String id,
                                        @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                                        @RequestParam(required = false, defaultValue = "m") String units) {
        // Shanghai CN CN101020100
        // Beijing  CN CN101010100
        // Bangkok  TH TH1609350
        return hefengCurrentServiceImpl.getCurrentDataByCityId(id, lang, units);
    }

    /**
     * 根据经纬度获取当前天气
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言
     * @param units     单位
     * //http://192.168.1.162:8080/weather/he/current/coord?longitude=112.98228&latitude=28.19409
     */
    @RequestMapping("/he/current/coord")
    public String owmByCoord(@RequestParam Double longitude, @RequestParam Double latitude,
                             @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                             @RequestParam(required = false, defaultValue = "m") String units) {
        return hefengCurrentServiceImpl.getCurrentDataByCityCoord(latitude, longitude, lang, units);
    }

    /**
     * 根据城市id获取当前城市几天的天气
     * @param id    城市id
     * @param lang  语言
     * @param units 单位
     */
    @RequestMapping("/weather/forecast")
    public String getForecastWeatherOwmByID(@RequestParam(required = false, defaultValue = "CN101010100") String id,
                                        @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                                        @RequestParam(required = false, defaultValue = "m") String units) {
        return hefengCurrentServiceImpl.getForecastDataByCityId(id, lang, units);
    }

    /**
     * 根据经纬度获取几天的天气
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言
     * @param units     单位
     */
    @RequestMapping("/he/forecast/coord")
    public String forecastByCoord(@RequestParam Double longitude, @RequestParam Double latitude,
                             @RequestParam String city, @RequestParam Integer flag,
                             @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                             @RequestParam(required = false, defaultValue = "m") String units) {
        try {
            return hefengCurrentServiceImpl.getForecastDataByCityCoord(latitude, longitude, city, flag, lang, units);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
