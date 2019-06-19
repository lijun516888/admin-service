package com.knowledge.observe;

import com.knowledge.observe.event.BaseEvent;

public interface SmartEventListener<E extends BaseEvent> extends BaseEventListener<E> {

    boolean supportsEventType(Class<E> eventType);

}
