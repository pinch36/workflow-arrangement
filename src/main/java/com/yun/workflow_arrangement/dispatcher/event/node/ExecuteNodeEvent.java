package com.yun.workflow_arrangement.dispatcher.event.node;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteNodeEvent extends NodeEvent {
    List<PreResult> preResults;
    {
        setEventType("execute_node_event");
        setDescription("节点执行");
    }
    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreResult{
        Object preObject;
        Class<?> preObjectType;
    }
}
