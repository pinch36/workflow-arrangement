package com.yun.workflow_arrangement.graph.entity;

import com.yun.workflow_arrangement.dispatcher.event.node.ExecuteNodeEvent;
import com.yun.workflow_arrangement.graph.enums.NodeStatus;

import java.util.List;

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
    String getWorkflowId();
    NodeStatus getStatus();
    void init();
    void end();
    <T> T execute(List<ExecuteNodeEvent.PreResult> preResults);
}
