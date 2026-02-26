package com.yun.workflow_arrangement.entity.context;

import com.yun.workflow_arrangement.graph.entity.Workflow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.ai.chat.model.ChatResponse;

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
public class Context {
    ChatResponse chatResponse;
    Workflow workFlow;
    List<String> message;
}
