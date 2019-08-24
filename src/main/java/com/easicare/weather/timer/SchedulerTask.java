package com.easicare.weather.timer;

import com.easicare.weather.common.WeatherCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author df
 * @date 2019/8/22
 */
@Component
@Slf4j
public class SchedulerTask {

    @Scheduled(cron="0 0 0 * * ?")
    public void clearCacheTask() {
        WeatherCacheUtil.clearCache();
        log.info("定时任务，每天0点清空缓存");
    }
}
