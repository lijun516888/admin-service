package com.knowledge.observe.multicaster;

import com.knowledge.observe.BaseEventListener;
import com.knowledge.observe.BaseEventPublisher;

/**
 * 基础事件广播接口
 * @param <E>
 */
public interface BaseEventMulticaster<E> extends BaseEventPublisher<E> {

    void addListener(BaseEventListener<E> listener);
}
