package com.yun.workflow_arrangement.dispatcher;

import com.yun.workflow_arrangement.dispatcher.enums.EventEnums;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

import static com.yun.workflow_arrangement.dispatcher.enums.EventEnums.GATEWAY_EVENT;
import static com.yun.workflow_arrangement.dispatcher.enums.EventEnums.LLM_EVENT;
import static com.yun.workflow_arrangement.dispatcher.enums.EventEnums.NODE_EVENT;
import static com.yun.workflow_arrangement.dispatcher.enums.EventEnums.USER_EVENT;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseDispatcher implements Dispatcher {
    private BlockingDeque<BaseEvent> workFlowEvents;
    private BlockingDeque<BaseEvent> userEvents;
    private BlockingDeque<BaseEvent> nodeEvents;
    private BlockingDeque<BaseEvent> llmEvents;
    private BlockingDeque<BaseEvent> gatewayEvents;
    private Map<String, BaseEvent> eventMap = new ConcurrentHashMap<>();
    private Map<BaseEvent, List<EventListener>> eventListeners = new ConcurrentHashMap<>();


    @Override
    public void sendToUser(BaseEvent event) {
        if (!check(event)) {
            Log.error("event type not found");
            return;
        }
        userEvents.add(event);
        eventListeners.get(event).forEach(EventListener::handler);
    }

    @Override
    public void sendToWorkflowScheduler(BaseEvent event) {
        if (!check(event)) {
            Log.error("event type not found");
            return;
        }
        workFlowEvents.add(event);
        eventListeners.get(event).forEach(EventListener::handler);
    }

    @Override
    public void sendToNodeScheduler(BaseEvent event) {
        if (!check(event)) {
            Log.error("event type not found");
            return;
        }
        nodeEvents.add(event);
        eventListeners.get(event).forEach(EventListener::handler);
    }

    @Override
    public void sendTollmScheduler(BaseEvent event) {
        if (!check(event)) {
            Log.error("event type not found");
            return;
        }
        llmEvents.add(event);
        eventListeners.get(event).forEach(EventListener::handler);
    }

    @Override
    public void sendToGateWay(BaseEvent event) {
        if (!check(event)) {
            Log.error("event type not found");
            return;
        }
        gatewayEvents.add(event);
        eventListeners.get(event).forEach(EventListener::handler);
    }

    @Override
    public List<BaseEvent> getEvents(EventEnums eventEnums) {
        return switch (eventEnums) {
            case EventEnums.WORKFLOW_EVENT -> {
                if (workFlowEvents.isEmpty()){
                    yield new ArrayList<>();
                }
                ArrayList<BaseEvent> events = new ArrayList<>(workFlowEvents);
                workFlowEvents.clear();
                yield events;
            }
            case NODE_EVENT -> {
                if (nodeEvents.isEmpty()){
                    yield new ArrayList<>();
                }
                ArrayList<BaseEvent> events = new ArrayList<>(nodeEvents);
                nodeEvents.clear();
                yield events;
            }
            case USER_EVENT -> {
                if (userEvents.isEmpty()){
                    yield new ArrayList<>();
                }
                ArrayList<BaseEvent> events = new ArrayList<>(userEvents);
                userEvents.clear();
                yield events;
            }
            case LLM_EVENT -> {
                if (llmEvents.isEmpty()){
                    yield new ArrayList<>();
                }
                ArrayList<BaseEvent> events = new ArrayList<>(llmEvents);
                llmEvents.clear();
                yield events;
            }
            case GATEWAY_EVENT -> {
                if (gatewayEvents.isEmpty()){
                    yield new ArrayList<>();
                }
                ArrayList<BaseEvent> events = new ArrayList<>(gatewayEvents);
                gatewayEvents.clear();
                yield events;
            }
        };
    }

    @Override
    public void init() {

    }

    @Override
    public void end() {

    }

    @Override
    public void register(BaseEvent event) {
        eventMap.put(event.getType(), event);
    }

    @Override
    public void unregister(BaseEvent event) {
        eventMap.remove(event.getType());
    }

    private boolean check(BaseEvent event) {
        return eventMap.containsKey(event.getType());
    }
}
