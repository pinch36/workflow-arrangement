package com.yun.workflow_arrangement.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LLMNode extends BaseNode<LLMResult>{
    private Prompt prompt;
    private ChatOptions chatOptions;
    private String systemPrompt;
    private List<Tool> tools;
}
