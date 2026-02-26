package com.yun.workflow_arrangement.scheduler.node;

import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.dispatcher.event.llm.ExecuteLLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.llm.LLMEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.ErrorNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.ExecuteNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.LLMErrorEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.LLMReadyEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.NodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.node.SuccessNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.NextNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.WorkflowEvent;
import com.yun.workflow_arrangement.graph.entity.BaseNode;
import com.yun.workflow_arrangement.graph.entity.Edge;
import com.yun.workflow_arrangement.graph.entity.LLMNode;
import com.yun.workflow_arrangement.graph.entity.LLMResult;
import com.yun.workflow_arrangement.graph.entity.Workflow;
import com.yun.workflow_arrangement.graph.enums.NodeStatus;
import com.yun.workflow_arrangement.log.Log;
import com.yun.workflow_arrangement.scheduler.Scheduler;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Component
public class NodeScheduler implements Scheduler, EventListener {
    @Resource
    private Dispatcher dispatcher;
    @Resource
    ThreadPoolExecutor nodeSchedulerThreadPoolExecutor;

    @Override
    public void init() {
        dispatcher.registerListener(this, NodeEvent.class);
        dispatcher.registerEvent(ErrorNodeEvent.class);
        dispatcher.registerEvent(SuccessNodeEvent.class);
        dispatcher.registerEvent(ExecuteNodeEvent.class);
        dispatcher.registerEvent(LLMReadyEvent.class);
        dispatcher.registerEvent(LLMErrorEvent.class);
    }

    @Override
    public void end() {

    }


    @Override
    public void handler() {
        Collection<BaseEvent> events = dispatcher.getEvents(NodeEvent.class);
        for (BaseEvent event : events) {
            switch (event.getType()) {
                case "execute_node_event" -> {
                    ExecuteNodeEvent executeNodeEvent = (ExecuteNodeEvent) event;
                    executeNodeEvent.getNode().setStatus(NodeStatus.RUNNING);
                    doExecute(executeNodeEvent);
                }
                case "success_node_event" -> {
                    SuccessNodeEvent successNodeEvent = (SuccessNodeEvent) event;
                    successNodeEvent.getNode().setStatus(NodeStatus.SUCCESS);
                    notifyWorkflow(successNodeEvent);
                }
                case "error_node_event" -> {
                    ErrorNodeEvent errorNodeEvent = (ErrorNodeEvent) event;
                    errorNodeEvent.getNode().setStatus(NodeStatus.ERROR);
                    notifyWorkflow(errorNodeEvent);
                }
                case "llm_ready_event" -> {
                    LLMReadyEvent llmReadyEvent = (LLMReadyEvent) event;
                    LLMNode llmNode = (LLMNode) llmReadyEvent.getNode();
//                    LLMResult llmResult = LLMResult.builder()
//                            .chatResponse(llmReadyEvent.getChatResponse())
//                            .text(llmReadyEvent.getChatResponse().getResult().getOutput().getText())
//                            .build();
//                    llmNode.setResult(llmResult);
                    llmNode.setResult(LLMResult.builder().text("test").build());
                    success(llmReadyEvent);
                }
                case "llm_error_node_event" -> {
                    LLMErrorEvent llmErrorEvent = (LLMErrorEvent) event;
                    error(llmErrorEvent);
                }
            }
        }
    }

    private void notifyWorkflow(NodeEvent nodeEvent) {
        NextNodeEvent nextNodeEvent = NextNodeEvent.builder()
                .node(nodeEvent.getNode())
                .context(nodeEvent.getContext())
                .build();
        dispatcher.sendEvent(nextNodeEvent, WorkflowEvent.class);
    }


    private void doExecute(ExecuteNodeEvent executeNodeEvent) {
        BaseNode node = executeNodeEvent.getNode();
        if (checkNode(executeNodeEvent)){
            return;
        }
        List<ExecuteNodeEvent.PreResult> preResults = getPreResults(executeNodeEvent);
        switch (node){
            case LLMNode llmNode-> {
                executeLLMNode(executeNodeEvent,preResults);
            }
            default -> {
                executeNode(executeNodeEvent, preResults,node);
            }
        }
    }

    private List<ExecuteNodeEvent.PreResult> getPreResults(ExecuteNodeEvent executeNodeEvent) {
        ArrayList<ExecuteNodeEvent.PreResult> preResults = new ArrayList<>();
        BaseNode node = executeNodeEvent.getNode();
        Workflow workFlow = executeNodeEvent.getContext().getWorkFlow();
        List<Edge> inEdges = workFlow.getInEdges(node.getId());
        for (Edge inEdge : inEdges) {
            BaseNode sourceNode = workFlow.getNode(inEdge.other(node.getId()));
            if (Objects.isNull(sourceNode)){
                Log.error("节点" + node.getName() + "的输入节点不存在");
                throw new RuntimeException("节点" + node.getName() + "的输入节点不存在");
            }
            preResults.add(ExecuteNodeEvent.PreResult.builder().preObject(sourceNode.getResult())
                            .preObjectType(sourceNode.getResult().getClass()).build());
        }
        return preResults;
    }

    private void executeLLMNode(ExecuteNodeEvent executeNodeEvent, List<ExecuteNodeEvent.PreResult> preResults) {
        LLMNode llmNode = (LLMNode) executeNodeEvent.getNode();
        StringBuilder stringBuilder = new StringBuilder();
        for (ExecuteNodeEvent.PreResult preResult : preResults) {
            Object preObject = preResult.getPreObject();
            if (preObject instanceof String) {
                stringBuilder.append(preObject);
            }
        }
        Prompt prompt = Prompt.builder().chatOptions(llmNode.getChatOptions())
                .content(stringBuilder.toString())
                .build();
        llmNode.setPrompt(prompt);
        // llmNode的事件由llmScheduler传递
        ExecuteLLMEvent executeLLMEvent = ExecuteLLMEvent.builder()
                .node(llmNode)
                .prompt(prompt)
                .systemPrompt(llmNode.getSystemPrompt())
                .tools(llmNode.getTools())
                .context(executeNodeEvent.getContext())
                .build();
        dispatcher.sendEvent(executeLLMEvent, LLMEvent.class);
    }

    private void executeNode(ExecuteNodeEvent executeNodeEvent, List<ExecuteNodeEvent.PreResult> preResults,BaseNode node) {
        getNodeSchedulerThreadPoolExecutor().submit(() -> {
            try {
                Object execute = node.execute(preResults);
                node.setResult(execute);
                success(executeNodeEvent);
            } catch (Exception e) {
                String message = "node failed:" + e.getMessage();
                Log.error(message);
                executeNodeEvent.getContext().getMessage().add(message);
                error(executeNodeEvent);
            }
        });
    }

    private ThreadPoolExecutor getNodeSchedulerThreadPoolExecutor() {
        return nodeSchedulerThreadPoolExecutor;
    }

    private static boolean checkNode(ExecuteNodeEvent executeNodeEvent) {
        BaseNode node = executeNodeEvent.getNode();
        Workflow workFlow = executeNodeEvent.getContext().getWorkFlow();
        List<Edge> inEdges = workFlow.getInEdges(node.getId());
        for (Edge inEdge : inEdges) {
            BaseNode sourceNode = workFlow.getNode(inEdge.other(node.getId()));
            if (Objects.isNull(sourceNode)){
                Log.error("节点" + node.getName() + "的输入节点不存在");
                throw new RuntimeException("节点" + node.getName() + "的输入节点不存在");
            }
            if (sourceNode.getStatus() != NodeStatus.SUCCESS) {
                Log.info("当前节点：" + sourceNode.getName() + "\n前置节点未准备好：" + sourceNode.getName());
                return true;
            }
        }
        return false;
    }

    private void success(NodeEvent nodeEvent) {
        SuccessNodeEvent successNodeEvent = SuccessNodeEvent.builder()
                .node(nodeEvent.getNode())
                .context(nodeEvent.getContext())
                .build();
        dispatcher.sendEvent(successNodeEvent, NodeEvent.class);
    }

    private void error(NodeEvent nodeEvent) {
        ErrorNodeEvent errorNodeEvent = ErrorNodeEvent.builder()
                .node(nodeEvent.getNode())
                .context(nodeEvent.getContext())
                .build();
        dispatcher.sendEvent(errorNodeEvent, NodeEvent.class);
    }
}
