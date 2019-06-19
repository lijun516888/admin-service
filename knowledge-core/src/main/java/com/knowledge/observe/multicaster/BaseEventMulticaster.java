package com.knowledge.observe.multicaster;

import com.knowledge.observe.BaseEventListener;
import com.knowledge.observe.BaseEventPublisher;
import com.knowledge.observe.event.BaseEvent;

/**
 * 基础事件广播接口
 * @param <E>
 */
public interface BaseEventMulticaster<E extends BaseEvent> extends BaseEventPublisher<E> {

    void addListener(BaseEventListener<E> listener);
}
