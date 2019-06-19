package com.knowledge.observe.listener;

import com.knowledge.observe.event.SystemEvent;

public class SystemEventListener extends AbstractEventListener<SystemEvent> {

    @Override
    public void onTriggerEvent(SystemEvent event) {
        System.out.println("SystemEventListener");
    }
}
