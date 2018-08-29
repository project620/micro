package com.micro.util;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBUtil {



    public static Object executeQuery(ResultSetHandler resultSetHandler, String sql, Object ... param){
        QueryRunner runner = getRunner();
        try {
            return runner.query(sql, resultSetHandler, param);
        } catch (SQLException e) {
            throw new RuntimeException("数据库查询异常", e);
        }
    }

    public static int executeUpdate(String sql, Object ... param) {
        QueryRunner runner = getRunner();
        try {
            return runner.update(sql, param);
        } catch (SQLException e) {
            throw new RuntimeException("数据库更新异常", e);
        }
    }


    private static QueryRunner getRunner() {
        DataSource dataSource = DataSourceUtil.getDataSource();
        QueryRunner runner = new QueryRunner(dataSource);
        return runner;
    }

}
