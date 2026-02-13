package com.yun.workflow_arrangement.dispatcher;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;

import java.util.List;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
public interface Dispatcher {
    void sendToUser(BaseEvent event);
    void sendToWorkflowScheduler(BaseEvent event);
    void sendToNodeScheduler(BaseEvent event);
    void sendTollmScheduler(BaseEvent event);
    void sendToGateWay(BaseEvent event);
    List<BaseEvent> getEvents();
    void init();
    void end();
}
