package com.yun.workflow_arrangement.dispatcher.event.workflow;


import lombok.AllArgsConstructor;
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
public class SuccessWorkflowEvent extends WorkflowEvent {
    {
        setEventType("success_workflow_event");
        setDescription("节点运行完毕");
    }
}
