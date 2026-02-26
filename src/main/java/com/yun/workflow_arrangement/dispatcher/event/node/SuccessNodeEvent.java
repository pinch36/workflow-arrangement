package com.yun.workflow_arrangement.dispatcher.event.node;


import com.yun.workflow_arrangement.dispatcher.event.workflow.WorkflowEvent;
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
public class SuccessNodeEvent extends NodeEvent {
    {
        setEventType("success_node_event");
        setDescription("节点运行成功");
    }
}
