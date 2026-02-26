package com.yun.workflow_arrangement.dispatcher.event.gateway;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class WorkflowErrorEvent extends GatewayEvent {
    {
        setEventType("workflow_error_event");
        setDescription("工作流执行失败");
    }
}
