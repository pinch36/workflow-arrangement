package com.yun.workflow_arrangement.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author raoliwen
 * @date 2026/02/26
 */
@Configuration
public class LLMConfig {
    @Bean
    public ChatClient chatClient(){
        ChatClient chatClient = ChatClient.builder(new OpenAiChatModel(new OpenAiApi("test")))
                .build();
        return chatClient;
    }
}
