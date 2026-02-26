package com.yun.workflow_arrangement.dispatcher.event.llm;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;

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
public class ExecuteLLMEvent extends LLMEvent {
    private Prompt prompt;
    private List<Tool> tools;
    private String systemPrompt;
    {
        setEventType("execute_llm_event");
        setDescription("调用模型");
    }
}
