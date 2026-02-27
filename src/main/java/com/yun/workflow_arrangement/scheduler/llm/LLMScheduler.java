package com.yun.workflow_arrangement.scheduler.llm;

import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.dispatcher.event.llm.ErrorLLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.llm.ExecuteLLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.llm.LLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.llm.SuccessLLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.LLMErrorEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.LLMReadyEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.NodeEvent;
import com.yun.workflow_arrangement.log.Log;
import com.yun.workflow_arrangement.scheduler.Scheduler;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Component
public class LLMScheduler implements Scheduler, EventListener {
    @Resource
    private Dispatcher dispatcher;
    @Resource
    ThreadPoolExecutor llmSchedulerThreadPoolExecutor;
    @Resource
    private ChatClient chatClient;

    @Override
    public void init() {
        dispatcher.registerListener(this, LLMEvent.class);
        dispatcher.registerEvent(ErrorLLMEvent.class);
        dispatcher.registerEvent(SuccessLLMEvent.class);
        dispatcher.registerEvent(ExecuteLLMEvent.class);

    }

    @Override
    public void end() {

    }


    @Override
    public void handler() {
        llmSchedulerThreadPoolExecutor.submit(() -> {
            Collection<BaseEvent> events = dispatcher.getEvents(LLMEvent.class);
            for (BaseEvent event : events) {
                switch (event.getType()) {
                    case "execute_llm_event" -> {
                        ExecuteLLMEvent executeLLMEvent = (ExecuteLLMEvent) event;
                        doExecute(executeLLMEvent);
                    }
                    case "success_llm_event" -> {
                        SuccessLLMEvent successLLMEvent = (SuccessLLMEvent) event;
                        notifyNode(successLLMEvent);
                    }
                    case "error_llm_event" -> {
                        ErrorLLMEvent errorLLMEvent = (ErrorLLMEvent) event;
                        notifyNode(errorLLMEvent);
                    }
                }
            }
        });
    }
    private void notifyNode(ErrorLLMEvent errorLLMEvent) {
        LLMErrorEvent llmErrorEvent = LLMErrorEvent.builder()
                .node(errorLLMEvent.getNode())
                .error(errorLLMEvent.getError())
                .context(errorLLMEvent.getContext())
                .build();
        dispatcher.sendEvent(llmErrorEvent, NodeEvent.class);
    }

    private void notifyNode(SuccessLLMEvent successLLMEvent) {
        LLMReadyEvent llmReadyEvent = LLMReadyEvent.builder()
                .node(successLLMEvent.getNode())
                .chatResponse(successLLMEvent.getChatResponse())
                .context(successLLMEvent.getContext())
                .build();
        dispatcher.sendEvent(llmReadyEvent, NodeEvent.class);
    }

    private void doExecute(ExecuteLLMEvent executeLLMEvent) {
        llmSchedulerThreadPoolExecutor.submit(() -> {
            ChatResponse chatResponse = null;
            try {
                //            ChatResponse chatResponse = chatClient.prompt(executeLLMEvent.getPrompt())
//                    .system(executeLLMEvent.getSystemPrompt())
//                    .tools(executeLLMEvent.getTools())
//                    .call()
//                    .chatResponse();
                success(executeLLMEvent, chatResponse);
            }catch (Exception e){
                Log.error(e.getMessage());
                executeLLMEvent.getContext().getMessage().add(e.getMessage());
                error(executeLLMEvent, e.getMessage());
            }
        });
    }

    private void error(ExecuteLLMEvent executeLLMEvent, String message) {
        ErrorLLMEvent errorLLMEvent = ErrorLLMEvent.builder()
                .context(executeLLMEvent.getContext())
                .error(message)
                .node(executeLLMEvent.getNode())
                .build();
        dispatcher.sendEvent(errorLLMEvent, LLMEvent.class);
    }

    private void success(ExecuteLLMEvent executeLLMEvent, ChatResponse chatResponse) {
        SuccessLLMEvent successLLMEvent = SuccessLLMEvent.builder()
                .context(executeLLMEvent.getContext())
                .chatResponse(chatResponse)
                .node(executeLLMEvent.getNode())
                .build();
        dispatcher.sendEvent(successLLMEvent, LLMEvent.class);
    }
}
