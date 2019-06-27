package com.knowledge.common.factory.db;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Data
@Log4j
public abstract class AbstractDb implements Db {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private String dbUrl;
    private String username;
    private String password;

    void createDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassLoader(ClassUtils.getDefaultClassLoader());
        dataSource.setUrl(getUrl(this.dbUrl));
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(2);
        dataSource.setMinIdle(1);
        dataSource.setMaxWait(20000);
        dataSource.setTimeBetweenEvictionRunsMillis(300000L);
        dataSource.setMinEvictableIdleTimeMillis(3600000L);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setValidationQueryTimeout(5);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(3600);
        dataSource.setLogAbandoned(true);
        dataSource.setValidationQuery("select 'x'");
        dataSource.setValidationQueryTimeout(2);
        dataSource.setDefaultAutoCommit(true);
        Properties properties = new Properties();
        properties.put("druid.stat.slowSqlMillis", "1000");
        properties.put("druid.stat.logSlowSql", Boolean.toString(true));
        dataSource.setConnectProperties(properties);
        try {
            dataSource.setFilters("stat");
            dataSource.init();
        } catch (SQLException var4) {
            throw new RuntimeException("druid连接池初始化失败", var4);
        }
        this.dataSource = dataSource;
    }

    void createJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    private String getUrl(String url) {
        return !url.contains("?") ? url + "?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull" +
                "&allowMultiQueries=true&useSSL=false" : url;
    }

}
