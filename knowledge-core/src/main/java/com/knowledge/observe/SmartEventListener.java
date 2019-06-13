package com.knowledge.observe;

public interface SmartEventListener<E> extends BaseEventListener<E> {

    boolean supportsEventType(Class<E> eventType);

}
