package com.yun.workflow_arrangement.dispatcher.event.gateway;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
public class ExecuteWorkFlowEvent extends BaseEvent {
    {
        setEventType("execute_workflow_event");
        setDescription("执行工作流");
    }
}
