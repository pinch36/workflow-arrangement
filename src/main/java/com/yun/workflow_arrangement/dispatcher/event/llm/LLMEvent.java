package com.yun.workflow_arrangement.dispatcher.event.llm;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.graph.entity.BaseNode;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class LLMEvent extends BaseEvent {
    private BaseNode node;
}
