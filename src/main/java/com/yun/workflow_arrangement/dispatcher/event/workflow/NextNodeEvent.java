package com.yun.workflow_arrangement.dispatcher.event.workflow;


import com.yun.workflow_arrangement.graph.entity.BaseNode;
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
@AllArgsConstructor
@NoArgsConstructor
public class NextNodeEvent extends WorkflowEvent {
    private BaseNode node;
    {
        setEventType("next_node_event");
        setDescription("节点运行成功");
    }
}
