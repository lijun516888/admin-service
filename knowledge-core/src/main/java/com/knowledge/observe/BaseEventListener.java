package com.knowledge.observe;

public interface BaseEventListener<E> {

    void onTriggerEvent(E event);
}
