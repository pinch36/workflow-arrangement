package com.yun.workflow_arrangement.graph.factory;

import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.graph.entity.BaseNode;
import com.yun.workflow_arrangement.graph.entity.LLMNode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Component
public class LLMNodeFactory implements NodeFactory {
    @Override
    public LLMNode create() {
        return LLMNode.builder()
                .id(UUID.randomUUID().toString())
                .type("llmNode")
                .build();
    }
}
