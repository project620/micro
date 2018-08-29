package com.micro.util;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

public class ConfigUtil {

    private static final Config config = ConfigService.getAppConfig();


    public static String getString(String key) {
        return config.getProperty(key, null);
    }

    public static Integer getInteger(String key) {
        return config.getIntProperty(key, null);
    }

}
