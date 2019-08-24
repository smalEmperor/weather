package com.easicare.weather.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easicare.weather.common.*;
import com.easicare.weather.service.HefengCurrentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/**
 * 实时天气
 * @author wangkai  2019/3/7
 */
@Service
public class HefengCurrentServiceImpl implements HefengCurrentService {

    private static final Logger log = LoggerFactory.getLogger(HefengCurrentServiceImpl.class);
    /**
     * 根据城市id获取当前城市天气
     * @param id    城市id
     * @param lang  语言
     * @param units 单位
     */
    @Override
    public String getCurrentDataByCityId(String id, String lang, String units) {
        return getCurrentWeather(id, lang, units);
    }

    /**
     * 根据城市id获取当前城市几天的天气
     * @param id    城市id
     * @param lang  语言
     * @param units 单位
     */
    @Override
    public String getForecastDataByCityId(String id, String lang, String units) {
        return getForecastWeather(id, lang, units,null,0);
    }

    /**
     * 根据经纬度获取当前天气
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言
     * @param units     单位
     */
    @Override
    public String getCurrentDataByCityCoord(Double latitude, Double longitude, String lang, String units) {
        return getCurrentWeather(longitude + "," + latitude, lang, units);
    }

    /**
     * 根据经纬度获取多天的天气
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言
     * @param units     单位
     */
    @Override
    public String getForecastDataByCityCoord(Double latitude, Double longitude, String city, int flag,
                                             String lang, String units) throws CustomException  {
        return getForecastWeather(longitude + "," + latitude, lang, units, city, flag);
    }

    /**
     * 获取当天天气方法封装
     */
    private static String getCurrentWeather(String location, String lang, String units) {
        HashMap<String, String> hashMap = new HashMap<>(6);
        hashMap.put("location", location);
        hashMap.put("lang", lang);
        hashMap.put("unit", units);
        hashMap.put("username", Constant.USER_NAME);
        hashMap.put("t", String.valueOf(System.currentTimeMillis() / 1000));

        String sign = null;
        try {
            sign = SignUtil.getSignature(hashMap, Constant.PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hashMap.put("sign", sign);

        String result = HttpClientUtil.doGet(Constant.API_URL, hashMap);
        JSONObject json = JSON.parseObject(result).getJSONArray("HeWeather6").getJSONObject(0);
        JSONObject heWeatherResult = new JSONObject();
        heWeatherResult.put("city",json.getJSONObject("basic").getString("location"));
        heWeatherResult.put("temp",json.getJSONObject("now").getString("tmp"));
        Integer condCode = json.getJSONObject("now").getInteger("cond_code");
        if (condCode.equals(100)) {
            // 晴天
            heWeatherResult.put("icon",Constant.SUNNY_ICON);
            heWeatherResult.put("back",Constant.SUNNY_BACK);
        } else if (condCode.equals(104)) {
            // 阴天
            heWeatherResult.put("icon",Constant.OVERCAST_ICON);
            heWeatherResult.put("back",Constant.OVERCAST_BACK);
        } else if (condCode.equals(101) || condCode.equals(102) || condCode.equals(103)) {
            // 多云
            heWeatherResult.put("icon",Constant.CLOUDY_ICON);
            heWeatherResult.put("back",Constant.CLOUDY_BACK);
        } else if (condCode.toString().startsWith("3")) {
            // 雨
            heWeatherResult.put("icon",Constant.RAIN_ICON);
            heWeatherResult.put("back",Constant.RAIN_BACK);
        }else {
            heWeatherResult.put("icon",Constant.SUNNY_ICON);
            heWeatherResult.put("back",Constant.SUNNY_BACK);
        }
        return JSON.toJSONString(heWeatherResult);
    }

    /**
     * 获取天气缓存操作
     */
    private static String getForecastWeather(String location, String lang, String units, String key, int flag) throws CustomException {
        boolean b = WeatherCacheUtil.checkKey(key);
        if (b) {
            JSONObject jsonWeather = WeatherCacheUtil.getCache(key);
            if (flag == 0) {
                if (jsonWeather != null && !jsonWeather.isEmpty()) {
                    return jsonWeather.toJSONString();
                }
                return getWeather(location, lang, units, key, flag);
            }
            if (jsonWeather != null && !jsonWeather.isEmpty() && jsonWeather.containsKey("flag") && jsonWeather.getInteger("flag") == 1) {
                return jsonWeather.toJSONString();
            }
            return getWeather(location, lang, units, key, flag);
        } else {
            return getWeather(location, lang, units, key, flag);
        }
    }

    /**
     * 获取几天天气方法封装
     */
    private static String getWeather(String location, String lang, String units, String key, int flag) throws CustomException{
        System.out.println("接口查询");
        HashMap<String, String> hashMap = new HashMap<>(6);
        hashMap.put("location", location);
        hashMap.put("lang", lang);
        hashMap.put("unit", units);
        hashMap.put("username", Constant.USER_NAME);
        hashMap.put("t", String.valueOf(System.currentTimeMillis() / 1000));

        String sign;
        try {
            sign = SignUtil.getSignature(hashMap, Constant.PASSWORD);
        } catch (IOException e) {
            log.info("用户认证异常"+e.getMessage());
            throw new CustomException("用户认证异常");
        }
        hashMap.put("sign", sign);

        String result = HttpClientUtil.doGet(Constant.API_URL_FORECAST, hashMap);
        JSONObject heWeather = JSON.parseObject(result).getJSONArray("HeWeather6").getJSONObject(0);
        if (!"ok".equals(heWeather.getString("status"))) {
            throw new CustomException("定位异常，无法获取天气信息");
        }
        String results = HttpClientUtil.doGet(Constant.API_URL, hashMap);
        JSONObject todayHeWeather = JSON.parseObject(results).getJSONArray("HeWeather6").getJSONObject(0);
        JSONArray forecast = heWeather.getJSONArray("daily_forecast");
        JSONObject heWeatherResult = new JSONObject();
        heWeatherResult.put("city",heWeather.getJSONObject("basic").getString("location"));
        heWeatherResult.put("flag",flag);
        for (int i = 0; i < forecast.size(); i++) {
            if (i == 0) {
                forecast.getJSONObject(i).put("tmp_now",todayHeWeather.getJSONObject("now").getString("tmp"));
                forecast.getJSONObject(i).put("cond_txt_now",todayHeWeather.getJSONObject("now").getString("cond_txt"));
            }
            heWeatherResult.put(forecast.getJSONObject(i).getString("date"),forecast.getJSONObject(i));
        }
        WeatherCacheUtil.putCache(key,heWeatherResult);
        return heWeatherResult.toJSONString();
    }
}
