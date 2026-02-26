package com.yun.workflow_arrangement.dispatcher.enums;

import lombok.Getter;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Getter
public enum LLMStatus {
    IDLE("idle"),
    RUNNING("running"),
    SUCCESS("success"),
    ERROR("error");

    private final String status;

    LLMStatus(String status) {
        this.status = status;
    }
}
