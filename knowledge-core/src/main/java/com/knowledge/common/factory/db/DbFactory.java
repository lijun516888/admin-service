package com.knowledge.common.factory.db;

public interface DbFactory<T extends Db> {

    T getDb(Class<? extends T> clazz);

}
