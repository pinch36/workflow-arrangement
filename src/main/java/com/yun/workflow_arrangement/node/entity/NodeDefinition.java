package com.yun.workflow_arrangement.node.entity;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
public interface NodeDefinition {
    String getName();
    String getType();
    String getDescription();
    String getId();
    String getParentId();
    String getWorkflowId();
    String getStatus();
    String sendEvent(BaseEvent baseEvent);
    void init();
    void end();
}
