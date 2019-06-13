package com.knowledge.observe.multicaster;

import com.google.common.collect.Lists;
import com.knowledge.observe.BaseEventListener;

import java.util.List;

/**
 * 抽象事件多路广播
 * @param <E>
 */
public abstract class AbstractEventMulticaster<E> implements BaseEventMulticaster<E> {

    private List<BaseEventListener<E>> liseners = Lists.newArrayList();

    @Override
    public void addListener(BaseEventListener<E> listener) {
        this.liseners.add(listener);
    }

    @Override
    public void publisherEvent(E event) {
        liseners.forEach(v -> {
            v.onTriggerEvent(event);
        });
    }
}
