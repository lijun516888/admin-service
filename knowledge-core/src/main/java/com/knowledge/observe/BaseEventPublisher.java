package com.knowledge.observe;

import com.knowledge.observe.event.BaseEvent;

/**
 * 基础事件发布接口
 * @param <E>
 */
public interface BaseEventPublisher<E extends BaseEvent> {

    void publisherEvent(E event);
}
