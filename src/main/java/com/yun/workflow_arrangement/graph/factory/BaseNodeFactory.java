package com.yun.workflow_arrangement.graph.factory;

import com.yun.workflow_arrangement.graph.entity.BaseNode;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Component
public class BaseNodeFactory implements NodeFactory {
    @Override
    public BaseNode create() {
        return BaseNode.builder()
                .id(UUID.randomUUID().toString())
                .type("base")
                .build();
    }
}
