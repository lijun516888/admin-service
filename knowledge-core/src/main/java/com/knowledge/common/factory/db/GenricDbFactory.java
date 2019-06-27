package com.knowledge.common.factory.db;

import lombok.extern.log4j.Log4j;

@Log4j
public class GenricDbFactory<T extends Db> implements DbFactory<T> {

    @Override
    public T getDb(Class<? extends T> clazz) {
        if(clazz == null) {
            return null;
        }
        try {
            T db = (T) Class.forName(clazz.getName()).newInstance();
            return db;
        } catch (Exception e) {
            log.error("获取数据库操作对应出错{}", e);
        }
        return null;
    }
}
