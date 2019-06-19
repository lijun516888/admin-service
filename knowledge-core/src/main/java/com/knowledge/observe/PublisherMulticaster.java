package com.knowledge.observe;

import com.knowledge.observe.event.SystemEvent;
import com.knowledge.observe.listener.SystemEventListener;
import com.knowledge.observe.multicaster.SystemEventMulticaster;

public class PublisherMulticaster {

    private SystemEventMulticaster systemEventMulticaster = new SystemEventMulticaster();

    public static void main(String[] args) {
        PublisherMulticaster publisherMulticaster = new PublisherMulticaster();
        SystemEventListener systemEventListener = new SystemEventListener();
        publisherMulticaster.systemEventMulticaster.addListener((BaseEventListener) systemEventListener);

        SystemEvent systemEvent = new SystemEvent();
        publisherMulticaster.systemEventMulticaster.publisherEvent(systemEvent);
    }

}
