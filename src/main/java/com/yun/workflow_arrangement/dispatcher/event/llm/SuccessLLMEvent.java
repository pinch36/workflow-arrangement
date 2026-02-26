package com.yun.workflow_arrangement.dispatcher.event.llm;


import com.yun.workflow_arrangement.dispatcher.event.node.NodeEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.ai.chat.model.ChatResponse;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessLLMEvent extends LLMEvent {
    ChatResponse chatResponse;
    {
        setEventType("success_llm_event");
        setDescription("模型调用成功");
    }
}
