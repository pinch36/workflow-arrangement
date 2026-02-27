package com.yun.workflow_arrangement.gateway;

import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.dispatcher.event.gateway.GatewayEvent;
import com.yun.workflow_arrangement.dispatcher.event.gateway.WorkflowErrorEvent;
import com.yun.workflow_arrangement.dispatcher.event.gateway.WorkflowReadyEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.ExecuteWorkflowEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.WorkflowEvent;
import com.yun.workflow_arrangement.entity.context.Context;
import com.yun.workflow_arrangement.gateway.entity.WorkFlowResult;
import com.yun.workflow_arrangement.graph.entity.Workflow;
import com.yun.workflow_arrangement.log.Log;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Component
public class Gateway implements EventListener {
    @Resource
    Dispatcher dispatcher;
    @Resource
    ThreadPoolExecutor gatewayThreadPoolExecutor;
    private final Map<Class<? extends BaseEvent>, List<GatewayListener>> gatewayListeners = new ConcurrentHashMap<>();

    public void init(){
        dispatcher.registerListener(this, GatewayEvent.class);
        dispatcher.registerEvent(WorkflowReadyEvent.class);
        dispatcher.registerEvent(WorkflowErrorEvent.class);
        gatewayListeners.put(WorkflowReadyEvent.class, new ArrayList<>());
    }
    public void doWork(Context  context){
        ExecuteWorkflowEvent executeWorkEvent = ExecuteWorkflowEvent.builder()
                .context(context)
                .eventType("execute_work_flow_event")
                .description("execute_work_flow_event")
                .build();
        dispatcher.sendEvent(executeWorkEvent, WorkflowEvent.class);
    }

    public void executeWorkFlow(Workflow workFlow){
        Context context = Context.builder()
                .workFlow(workFlow)
                .message(new ArrayList<>())
                .build();
        gatewayThreadPoolExecutor.submit(() -> {
            try {
                doWork(context);
            }catch (Exception e){
                String message = "workflow failed:" + e.getMessage();
                Log.error(message);
                WorkflowErrorEvent workflowErrorEvent = WorkflowErrorEvent.builder()
                        .context(context)
                        .build();
                dispatcher.sendEvent(workflowErrorEvent, GatewayEvent.class);
            }
        });
    }


    @Override
    public void handler() {
        gatewayThreadPoolExecutor.submit(() -> {
            Collection<BaseEvent> events = dispatcher.getEvents(GatewayEvent.class);
            for (BaseEvent event : events) {
                switch (event.getType()){
                    case "workflow_ready_event" -> {
                        WorkflowReadyEvent workflowReadyEvent = (WorkflowReadyEvent) event;
                        gatewayListeners.get(WorkflowReadyEvent.class).forEach(gatewayListener -> gatewayListener.handler(workflowReadyEvent));
                    }
                    case "workflow_error_event" -> {
                        WorkflowErrorEvent workflowErrorEvent = (WorkflowErrorEvent) event;
                        gatewayListeners.get(WorkflowErrorEvent.class).forEach(gatewayListener -> gatewayListener.handler(workflowErrorEvent));
                    }
                }
            }
        });
    }

    public void registerGatewayListener(GatewayListener gatewayListener, Class<? extends BaseEvent> eventType) {
        List<GatewayListener> gatewayListenerList = gatewayListeners.get(eventType);
        if (Objects.isNull(gatewayListenerList)){
            Log.error("no eventType gatewayListenerList");
            return;
        }
        gatewayListenerList.add(gatewayListener);
    }
}
