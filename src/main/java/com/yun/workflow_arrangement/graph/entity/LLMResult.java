package com.yun.workflow_arrangement.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LLMResult {
    private ChatResponse chatResponse;
    private String text;
}
