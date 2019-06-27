package com.knowledge.common.factory.db;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public interface Db {

    void init(String username, String password, String dbUrl);

    DataSource loadDataSource();

    JdbcTemplate loadJdbcTemplate();

}
