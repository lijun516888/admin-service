package com.knowledge.common.factory.db;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class DbTest {

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://192.168.1.250:3305/callcenter-ksbd?useUnicode=true&amp;characterEncoding=UTF-8";
        DbFactory<MysqlDb> factory = new GenricDbFactory<>();
        MysqlDb db = factory.getDb(MysqlDb.class);
        db.init("root", "111111", dbUrl);
        JdbcTemplate dbTool = db.getJdbcTemplate();


        List<Map<String, Object>> maps = dbTool.queryForList("select * from crm_customer6000");
        maps.forEach(v -> System.out.println(v.toString()));
        System.out.println(maps.size());

    }
}
