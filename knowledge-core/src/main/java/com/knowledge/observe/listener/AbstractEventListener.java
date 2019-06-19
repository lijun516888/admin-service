package com.knowledge.observe.listener;

import com.acooly.core.utils.GenericsUtils;
import com.knowledge.observe.SmartEventListener;
import com.knowledge.observe.event.BaseEvent;

public abstract class AbstractEventListener<E extends BaseEvent> implements SmartEventListener<E> {

    private Class<E> clazz;

    public AbstractEventListener() {
        this.clazz = GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
    }

    @Override
    public boolean supportsEventType(Class<E> eventType) {
        return this.clazz.isAssignableFrom(eventType);
    }
}
