package com.yun.workflow_arrangement.app;

import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.gateway.Gateway;
import com.yun.workflow_arrangement.gateway.GatewayListener;
import com.yun.workflow_arrangement.graph.entity.Workflow;
import com.yun.workflow_arrangement.scheduler.llm.LLMScheduler;
import com.yun.workflow_arrangement.scheduler.node.NodeScheduler;
import com.yun.workflow_arrangement.scheduler.workflow.WorkflowScheduler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/25
 */
@Component
public class WorkflowExecutor {
    @Resource
    private Gateway gateway;
    @Resource
    private WorkflowScheduler workflowScheduler;
    @Resource
    private NodeScheduler nodeScheduler;
    @Resource
    private LLMScheduler llmScheduler;
    @Resource
    private Dispatcher dispatcher;
    @PostConstruct
    public void init() {
        dispatcher.init();
        llmScheduler.init();
        nodeScheduler.init();
        workflowScheduler.init();
        gateway.init();
    }
    public void executeWorkFlow(Workflow workFlow){
        workFlow.init();
        gateway.executeWorkFlow(workFlow);
    }
    public void register(GatewayListener gatewayListener, Class<? extends BaseEvent> eventType){
        gateway.registerGatewayListener(gatewayListener, eventType);
    }
}
