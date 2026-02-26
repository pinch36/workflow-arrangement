package com.yun.workflow_arrangement.graph.enums;

import lombok.Getter;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Getter
public enum WorkflowStatus {
    IDLE("idle"),
    RUNNING("running"),
    SUCCESS("success"),
    ERROR("error");

    private final String status;

    WorkflowStatus(String status) {
        this.status = status;
    }
}
