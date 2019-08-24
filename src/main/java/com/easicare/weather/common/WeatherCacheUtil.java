package com.easicare.weather.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author df
 * @date 2019/8/22
 */
public class WeatherCacheUtil {

    private static Cache<Object, JSONObject> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build();

    /**
     * 缓存添加数据
     */
    public static void putCache(String key, JSONObject json) {
        if (cache != null) {
            cache.put(key,json);
        }
    }

    /**
     * 缓存获取数据
     */
    public static JSONObject getCache(Object key){
        if (cache != null) {
            return cache.getIfPresent(key);
        }
        return new JSONObject();
    }

    /**
     * 是否包含key
     */
    public static boolean checkKey(Object key){
        if (cache != null) {
            return cache.asMap().keySet().contains(key);
        }
        return false;
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        if (cache != null) {
            cache.invalidateAll();
        }
    }

}
