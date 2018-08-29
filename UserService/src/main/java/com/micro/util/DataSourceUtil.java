package com.micro.util;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

public class DataSourceUtil {

    private static Config clusterConfig = ConfigService.getAppConfig();
    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);
    private static volatile DataSource dataSource = null;

    static  {
        initDataSource();
        //配置更新监听
        Set<String> keys = new HashSet<>();
        keys.add("jdbc.url");
        keys.add("jdbc.username");
        keys.add("jdbc.password");
        clusterConfig.addChangeListener(changeEvent -> {
            initDataSource();
        }, keys);
    }

    private static void initDataSource() {
        logger.info("初始化数据库");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(clusterConfig.getProperty("jdbc.url", null));
        config.setUsername(clusterConfig.getProperty("jdbc.username", null));
        config.setPassword(clusterConfig.getProperty("jdbc.password", null));
        config.setAutoCommit(false);
        dataSource = new HikariDataSource(config);
    }

    private static class Instance{
        private static final DataSourceUtil instance = new DataSourceUtil();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }


}
