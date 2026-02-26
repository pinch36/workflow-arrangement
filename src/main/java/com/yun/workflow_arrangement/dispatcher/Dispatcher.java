package com.yun.workflow_arrangement.dispatcher;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;

import java.util.Collection;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
public interface Dispatcher {
    void sendEvent(BaseEvent event,Class<? extends BaseEvent> eventType);
    void init();
    void end();
    Collection<BaseEvent> getEvents(Class<? extends BaseEvent> eventType);
    void registerEvent(Class<? extends BaseEvent> eventType);
    void unregisterEvent(Class<? extends BaseEvent> eventType);
    void registerListener(EventListener eventListener,Class<? extends BaseEvent> eventType);
    void unregisterListener(EventListener eventListener,Class<? extends BaseEvent> eventType);
}
