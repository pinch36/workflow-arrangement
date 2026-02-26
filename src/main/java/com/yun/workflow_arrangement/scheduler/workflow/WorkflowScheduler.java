package com.yun.workflow_arrangement.scheduler.workflow;

import com.yun.workflow_arrangement.constant.WorkflowConstant;
import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.dispatcher.event.gateway.GatewayEvent;
import com.yun.workflow_arrangement.dispatcher.event.gateway.WorkflowReadyEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.ExecuteNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.NodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.SuccessWorkflowEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.ErrorWorkflowEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.ExecuteWorkflowEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.NextNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.WorkflowEvent;
import com.yun.workflow_arrangement.entity.context.Context;
import com.yun.workflow_arrangement.graph.entity.BaseNode;
import com.yun.workflow_arrangement.graph.entity.Edge;
import com.yun.workflow_arrangement.graph.entity.Workflow;
import com.yun.workflow_arrangement.graph.enums.NodeStatus;
import com.yun.workflow_arrangement.graph.enums.WorkflowStatus;
import com.yun.workflow_arrangement.log.Log;
import com.yun.workflow_arrangement.scheduler.Scheduler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Component
public class WorkflowScheduler implements Scheduler, EventListener {
    @Resource
    private Dispatcher dispatcher;

    @Override
    public void init() {
        dispatcher.registerListener(this, WorkflowEvent.class);
        dispatcher.registerEvent(SuccessWorkflowEvent.class);
        dispatcher.registerEvent(ErrorWorkflowEvent.class);
        dispatcher.registerEvent(NextNodeEvent.class);
        dispatcher.registerEvent(ExecuteWorkflowEvent.class);
    }

    @Override
    public void end() {

    }


    @Override
    public void handler() {
        Collection<BaseEvent> events = dispatcher.getEvents(WorkflowEvent.class);
        for (BaseEvent event : events) {
            switch (event.getType()) {
                case "execute_workflow_event" -> {
                    ExecuteWorkflowEvent executeWorkflowEvent = (ExecuteWorkflowEvent) event;
                    executeWorkflowEvent.getContext().getWorkFlow().setStatus(WorkflowStatus.RUNNING);
                    doExecute(executeWorkflowEvent);
                }
                case "next_node_event" -> {
                    NextNodeEvent nextNodeEvent = (NextNodeEvent) event;
                    executeNextNode(nextNodeEvent);
                }
                case "success_workflow_event" -> {
                    SuccessWorkflowEvent successWorkflowEvent = (SuccessWorkflowEvent) event;
                    successWorkflowEvent.getContext().getWorkFlow().setStatus(WorkflowStatus.SUCCESS);
                    notifyGateway(successWorkflowEvent);
                }
                case "error_workflow_event" -> {
                    ErrorWorkflowEvent errorWorkflowEvent = (ErrorWorkflowEvent) event;
                    errorWorkflowEvent.getContext().getWorkFlow().setStatus(WorkflowStatus.ERROR);
                    notifyGateway(errorWorkflowEvent);
                }
            }
        }
    }

    private void notifyGateway(WorkflowEvent workflowEvent) {
        WorkflowReadyEvent workflowReadyEvent = WorkflowReadyEvent.builder()
                .context(workflowEvent.getContext())
                .build();
        dispatcher.sendEvent(workflowReadyEvent, GatewayEvent.class);
    }

    private void executeNextNode(NextNodeEvent nextNodeEvent) {
        BaseNode node = nextNodeEvent.getNode();
        Workflow workFlow = nextNodeEvent.getContext().getWorkFlow();
        switch (node.getStatus()) {
            case NodeStatus.SUCCESS -> {
                if (workFlow.getEndNode().equals(node)) {
                    success(nextNodeEvent);
                    return;
                }
                List<Edge> outEdges = workFlow.getOutEdges(node.getId());
                for (Edge outEdge : outEdges) {
                    BaseNode nextNode = workFlow.getNode(outEdge.other(node.getId()));
                    if (Objects.isNull(nextNode)){
                        Log.error("节点" + node.getName() + "的输入节点不存在");
                        throw new RuntimeException("节点" + node.getName() + "的输入节点不存在");
                    }
                    executeNode(nextNodeEvent.getContext(), nextNode);
                }
            }
            case NodeStatus.ERROR -> {
                if (node.getRetryCount() >= WorkflowConstant.MAX_RETRY_COUNT) {
                    error(nextNodeEvent);
                    return;
                }
                node.setRetryCount(node.getRetryCount() + 1);
                node.setStatus(NodeStatus.IDLE);
                executeNode(nextNodeEvent.getContext(), node);
            }
        }
    }

    private void executeNode(Context context, BaseNode node) {
        ExecuteNodeEvent executeNodeEvent = ExecuteNodeEvent.builder()
                .context(context)
                .node(node)
                .build();
        dispatcher.sendEvent(executeNodeEvent, NodeEvent.class);
    }

    private void error(NextNodeEvent nextNodeEvent) {
        ErrorWorkflowEvent errorWorkflowEvent = ErrorWorkflowEvent.builder()
                .node(nextNodeEvent.getNode())
                .context(nextNodeEvent.getContext())
                .build();
        dispatcher.sendEvent(errorWorkflowEvent, WorkflowEvent.class);
    }

    private void success(NextNodeEvent nextNodeEvent) {
        SuccessWorkflowEvent successWorkflowEvent = SuccessWorkflowEvent.builder()
                .context(nextNodeEvent.getContext())
                .build();
        dispatcher.sendEvent(successWorkflowEvent, WorkflowEvent.class);
    }

    private void doExecute(ExecuteWorkflowEvent executeWorkflowEvent) {
        BaseNode startNode = executeWorkflowEvent.getContext().getWorkFlow().getStartNode();
        executeNode(executeWorkflowEvent.getContext(), startNode);
    }
}
