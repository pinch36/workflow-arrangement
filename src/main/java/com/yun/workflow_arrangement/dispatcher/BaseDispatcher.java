package com.yun.workflow_arrangement.dispatcher;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.json.JSONUtil;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.dispatcher.event.gateway.GatewayEvent;
import com.yun.workflow_arrangement.dispatcher.event.llm.LLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.NodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.WorkflowEvent;
import com.yun.workflow_arrangement.log.Log;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Slf4j
@Component
public class BaseDispatcher implements Dispatcher {
    private final Set<Class<? extends BaseEvent>> eventSet = new ConcurrentHashSet<>();
    private final Map<Class<? extends BaseEvent>, BlockingDeque<BaseEvent>> events = new ConcurrentHashMap<>();
    private final Map<Class<? extends BaseEvent>, List<EventListener>> eventListeners = new ConcurrentHashMap<>();
    @Resource
    private ExecutorService dispatcherThreadPoolExecutor;

    @Override
    public Collection<BaseEvent> getEvents(Class<? extends BaseEvent> eventType) {
        BlockingDeque<BaseEvent> baseEvents = events.get(eventType);
        if (baseEvents.isEmpty()) {
            return Collections.emptyList();
        }
        List<BaseEvent> reEvents = new ArrayList<>();
        baseEvents.drainTo(reEvents);
        return reEvents;
    }

    @Override
    public void sendEvent(BaseEvent event, Class<? extends BaseEvent> eventType) {
        if (!check(event.getClass())) {
            Log.error("事件不存在：" + event);
            return;
        }
        events.get(eventType).add(event);
        Log.info("发送事件：" + JSONUtil.toJsonStr(event));
    }

    @Override
    public void init() {
        events.put(WorkflowEvent.class, new LinkedBlockingDeque<>());
        events.put(NodeEvent.class, new LinkedBlockingDeque<>());
        events.put(LLMEvent.class, new LinkedBlockingDeque<>());
        events.put(GatewayEvent.class, new LinkedBlockingDeque<>());
        eventListeners.put(WorkflowEvent.class, new ArrayList<>());
        eventListeners.put(NodeEvent.class, new ArrayList<>());
        eventListeners.put(LLMEvent.class, new ArrayList<>());
        eventListeners.put(GatewayEvent.class, new ArrayList<>());
        registerEvent(WorkflowEvent.class);
        registerEvent(NodeEvent.class);
        registerEvent(LLMEvent.class);
        registerEvent(GatewayEvent.class);
        dispatcherThreadPoolExecutor.submit(() -> {
            while (true){
                eventListeners.forEach((eventType, eventListeners) -> {
                    if(events.get(eventType).isEmpty()){
                        return;
                    }
                    eventListeners.forEach(EventListener::handler);
                });
            }
        });
    }

    @Override
    public void end() {

    }

    @Override
    public void registerEvent(Class<? extends BaseEvent> eventType) {
        eventSet.add(eventType);
    }

    @Override
    public void unregisterEvent(Class<? extends BaseEvent> eventType) {
        if (!check(eventType)){
            Log.error("事件不存在：" + eventType);
            return;
        }
        eventSet.remove(eventType);
    }

    @Override
    public void registerListener(EventListener eventListener, Class<? extends BaseEvent> eventType) {
        if (!check(eventType)){
            Log.error("事件不存在：" + eventType);
            return;
        }
        eventListeners.get(eventType).add(eventListener);
    }

    @Override
    public void unregisterListener(EventListener eventListener, Class<? extends BaseEvent> eventType) {
        if (!check(eventType)){
            Log.error("事件不存在：" + eventType);
            return;
        }
        eventListeners.get(eventType).remove(eventListener);
    }


    private boolean check(Class<? extends BaseEvent> eventType) {
        return eventSet.contains(eventType);
    }
}
