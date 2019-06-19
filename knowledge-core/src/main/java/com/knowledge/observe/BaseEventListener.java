package com.knowledge.observe;

import com.knowledge.observe.event.BaseEvent;

public interface BaseEventListener<E extends BaseEvent> {

    void onTriggerEvent(E event);
}
