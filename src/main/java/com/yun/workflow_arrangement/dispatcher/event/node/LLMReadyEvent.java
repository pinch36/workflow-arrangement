package com.yun.workflow_arrangement.dispatcher.event.node;


import com.yun.workflow_arrangement.dispatcher.event.workflow.WorkflowEvent;
import com.yun.workflow_arrangement.graph.entity.BaseNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.ai.chat.model.ChatResponse;

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
public class LLMReadyEvent extends NodeEvent {
    ChatResponse chatResponse;
    {
        setEventType("llm_ready_event");
        setDescription("模型调用成功");
    }
}
