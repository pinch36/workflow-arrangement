package com.yun.workflow_arrangement.dispatcher.event.workflow;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class ExecuteWorkflowEvent extends WorkflowEvent {
    {
        setEventType("execute_workflow_event");
        setDescription("执行工作流");
    }
}
