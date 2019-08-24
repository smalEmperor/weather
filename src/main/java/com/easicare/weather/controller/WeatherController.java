package com.easicare.weather.controller;

import com.easicare.weather.common.HttpClientUtil;
import com.easicare.weather.common.WeatherResult;
import com.easicare.weather.service.ICurrentService;
import com.easicare.weather.service.IForecastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 天气接口
 * @author wangkai 2019/1/15
 */
@RestController
// @RequestMapping("/weather")
public class WeatherController {

    private static Logger log = LoggerFactory.getLogger(WeatherController.class);

    private static final String API_FORECAST = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String APP_ID = "3f6d18581d449028b07a2c9e3b3dba88";

    @Resource
    private ICurrentService owmCurrentService;

    @Resource
    private IForecastService forecastService;

    /**
     * 根据cityId获取当前天气
     * @param id    cityId
     * @param lang  Arabic - ar, Bulgarian - bg, Catalan - ca, Czech - cz, German - de, Greek - el,
     *              English - en, Persian (Farsi) - fa, Finnish - fi, French - fr, Galician - gl, Croatian - hr,
     *              Hungarian - hu, Italian - it, Japanese - ja, Korean - kr, Latvian - la, Lithuanian - lt,
     *              Macedonian - mk, Dutch - nl, Polish - pl, Portuguese - pt, Romanian - ro, Russian - ru, Swedish - se,
     *              Slovak - sk, Slovenian - sl, Spanish - es, Turkish - tr, Ukrainian - ua, Vietnamese - vi,
     *              Chinese Simplified - zh_cn, Chinese Traditional - zh_tw.
     *              NOTE: Translation is only applied for the "description" field.
     * @param units Standard(开尔文温度), metric(℃), and imperial(℉) units are available.
     * @return WeatherResult
     */

    @RequestMapping("/current")
    public WeatherResult getCurrWeatherOwmByID(@RequestParam(required = false, defaultValue = "1816670") Integer id,
                                               @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                                               @RequestParam(required = false, defaultValue = "metric") String units) {
        // Bangkok  TH 1609350
        // Beijing  CN 1816670
        // Shanghai CN 1796236
        return owmCurrentService.getCurrentDataByCityId(id, lang, units);
    }

    /**
     * 根据定位获取当前天气
     * @param latitude  纬度
     * @param longitude 经度
     * @param lang      语言
     * @param units     温度单位
     * @return WeatherResult
     */
    @RequestMapping("/current/coord")
    public WeatherResult owmByCoord(@RequestParam Double latitude,
                                    @RequestParam Double longitude,
                                    @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                                    @RequestParam(required = false, defaultValue = "metric") String units) {
        // 根据经纬度查询
        return owmCurrentService.getCurrentDataByCityCoord(latitude, longitude, lang, units);
    }

    // @RequestMapping("/forecast")
    public String getForecastWeather(@RequestParam Long id, @RequestParam(required = false) String lang,
                                     @RequestParam(required = false) String units,
                                     @RequestParam(required = false) String mode,
                                     @RequestParam(required = false) Integer cnt) {
        return HttpClientUtil.doGet(API_FORECAST, getMap(id, lang, units, mode, cnt));
    }

    // @RequestMapping("/forecast/owm")
    public Object getForecastWeather(@RequestParam Integer id,
                                     @RequestParam(required = false, defaultValue = "zh_cn") String lang,
                                     @RequestParam(required = false, defaultValue = "metric") String units,
                                     @RequestParam(required = false, defaultValue = "10")Integer cnt){
        return forecastService.getForecastWeatherById(id,lang,units,cnt);
    }

    /**
     * 拼装请求参数
     * @param id
     * @param lang
     * @return
     */
    private static HashMap<String, String> getMap(Long id, String lang, String units, String mode, Integer cnt) {
        HashMap<String, String> param = new HashMap<>(5);
        param.put("id", String.valueOf(id));
        param.put("appid", APP_ID);
        if (!StringUtils.isEmpty(lang)) {
            param.put("lang", lang);
        }
        if (!StringUtils.isEmpty(units)) {
            param.put("units", units);
        }
        if (!StringUtils.isEmpty(mode)) {
            param.put("mode", mode);
        }
        if (!StringUtils.isEmpty(cnt)) {
            param.put("cnt", String.valueOf(cnt));
        }
        return param;
    }

}