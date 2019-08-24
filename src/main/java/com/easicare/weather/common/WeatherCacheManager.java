package com.easicare.weather.common;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存天气
 * @author wangkai 2019/1/21
 */
@Component
public class WeatherCacheManager {
    @Resource
    private final CacheManager cacheManager;

    @Autowired
    public WeatherCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * List添加数据
     */
    @SuppressWarnings("unchecked")
    public void add(String cacheName, String value) {
        Cache cache = cacheManager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        list.add(value);
        cache.put(cacheName, list);
    }

    /**
     * List获取数据
     * @return String
     */
    @SuppressWarnings("unchecked")
    public List<String> get(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list;
    }

    /**
     * List判断是否包含数据
     * @return String
     */
    @SuppressWarnings("unchecked")
    public boolean contains(String cacheName, String value) {
        Cache cache = cacheManager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return list.contains(value);
    }

    /**
     * List删除数据
     * @param cacheName
     * void
     */
    @SuppressWarnings("unchecked")
    public void remove(String cacheName, String value) {
        Cache cache = cacheManager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.remove(value);
        cache.put(cacheName, list);
    }

}
