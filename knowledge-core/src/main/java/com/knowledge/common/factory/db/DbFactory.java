package com.knowledge.common.factory.db;

import java.sql.Connection;

public interface DbFactory<T> {

    Connection getConnection(Class<? extends T> clazz);

}
