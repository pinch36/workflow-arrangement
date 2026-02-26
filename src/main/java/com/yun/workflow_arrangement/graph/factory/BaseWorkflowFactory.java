package com.yun.workflow_arrangement.graph.factory;

import com.yun.workflow_arrangement.graph.entity.Workflow;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Component
public class BaseWorkflowFactory implements WorkflowFactory {
    @Override
    public Workflow create() {
        return Workflow.builder()
                .id(UUID.randomUUID().toString())
                .nodes(new HashMap<>())
                .edges(new HashMap<>())
                .nodeId2InEdges(new HashMap<>())
                .nodeId2OutEdges(new HashMap<>())
                .build();
    }
}
