package com.knowledge.common.factory.db;

import java.sql.Connection;

public class GenricDbFactory<T> implements DbFactory<T> {

    @Override
    public Connection getConnection(Class<? extends T> clazz) {
        /*if(clazz == null) {
            return null;
        }
        try {
            return (T) Class.forName(clazz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
