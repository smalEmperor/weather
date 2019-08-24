package com.easicare.weather.common;

import net.aksingh.owmjapis.core.OWM;

/**
 * 组装OWM
 *
 * @author wangkai 2019/1/21
 */
public class WeatherCommon {


    public OWM setOwm(String lang, String units) {
        OWM owm = new OWM(Constant.APP_ID);
        // 遍历语言
        for (OWM.Language language : OWM.Language.values()) {
            // 命中
            if (lang.equals(language.getValue())) {
                owm.setLanguage(language);
                break;
            }
        }

        // 遍历单位
        for (OWM.Unit unit : OWM.Unit.values()) {
            // 命中
            if (units.equals(unit.getValue())) {
                // 设置单位
                owm.setUnit(unit);
                break;
            }
        }

        return owm;
    }


}
