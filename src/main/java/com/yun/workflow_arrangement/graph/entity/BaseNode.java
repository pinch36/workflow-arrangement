package com.yun.workflow_arrangement.graph.entity;

import com.yun.workflow_arrangement.dispatcher.event.node.ExecuteNodeEvent;
import com.yun.workflow_arrangement.graph.enums.NodeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.function.Function;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseNode<T> implements NodeDefinition{
    protected String name;
    protected String type;
    protected String description;
    protected String id;
    protected String workflowId;
    protected NodeStatus status;
    protected int retryCount;
    protected T result;
    protected Function<List<ExecuteNodeEvent.PreResult>, T> nodeExecute;

    @Override
    public void init() {
        status = NodeStatus.IDLE;
    }

    @Override
    public void end() {

    }

    @Override
    public <T> T execute(List<ExecuteNodeEvent.PreResult> preResults) {
        return (T) nodeExecute.apply(preResults);
    }
}
