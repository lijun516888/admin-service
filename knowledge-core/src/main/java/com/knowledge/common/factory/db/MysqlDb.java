package com.knowledge.common.factory.db;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Log4j
@Data
public class MysqlDb extends AbstractDb {

    @Override
    public void init(String username, String password, String dbUrl) {
        this.setUsername(username);
        this.setPassword(password);
        this.setDbUrl(dbUrl);
        this.createDataSource();
        this.createJdbcTemplate();
    }

    @Override
    public DataSource loadDataSource() {
        if(this.loadDataSource() == null) {
            this.createDataSource();
        }
        return this.loadDataSource();
    }

    @Override
    public JdbcTemplate loadJdbcTemplate() {
        return this.getJdbcTemplate();
    }
}
