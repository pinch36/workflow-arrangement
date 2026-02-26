package com.yun.workflow_arrangement.dispatcher.event.node;


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
@NoArgsConstructor(force = true)
public class LLMErrorEvent extends NodeEvent {
    String error;
    {
        setEventType("llm_error_event");
        setDescription("模型调用失败");
    }
}
