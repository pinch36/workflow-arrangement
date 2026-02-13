package com.yun.workflow_arrangement.node.entity;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseNode implements NodeDefinition {
    private String name;
    private String type;
    private String description;
    private String id;
    private String parentId;
    private String workflowId;
    private String status;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getParentId() {
        return "";
    }

    @Override
    public String getWorkflowId() {
        return "";
    }

    @Override
    public String sendEvent(BaseEvent baseEvent) {
        return "";
    }

    @Override
    public void init() {

    }

    @Override
    public void end() {

    }
}
