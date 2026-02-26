package com.yun.workflow_arrangement.app;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.gateway.WorkflowReadyEvent;
import com.yun.workflow_arrangement.gateway.GatewayListener;
import com.yun.workflow_arrangement.graph.entity.BaseNode;
import com.yun.workflow_arrangement.graph.entity.Edge;
import com.yun.workflow_arrangement.graph.entity.LLMNode;
import com.yun.workflow_arrangement.graph.entity.Workflow;
import com.yun.workflow_arrangement.graph.factory.BaseNodeFactory;
import com.yun.workflow_arrangement.graph.factory.BaseWorkflowFactory;
import com.yun.workflow_arrangement.graph.factory.LLMNodeFactory;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class WorkflowExecutorTest {
    @Resource
    WorkflowExecutor workflowExecutor;
    @Resource
    BaseNodeFactory baseNodeFactory;
    @Resource
    BaseWorkflowFactory baseWorkflowFactory;
    @Resource
    LLMNodeFactory llmNodeFactory;

    @Test
    void executeWorkFlow() {
        Workflow workFlow = baseWorkflowFactory.create();
        workFlow.setName("test_workflow");
        workFlow.setDescription("test_workflow_description");

        BaseNode startNode = baseNodeFactory.create();
        startNode.setDescription("start_node");
        startNode.setName("start_node");
        startNode.setWorkflowId(workFlow.getId());
        startNode.setNodeExecute((preResults) -> {
            return "start_node_result";
        });
        LLMNode llmNode = llmNodeFactory.create();
        llmNode.setDescription("llm_node");
        llmNode.setName("llm_node");
        llmNode.setWorkflowId(workFlow.getId());
        llmNode.setSystemPrompt("你会聊天");
        llmNode.setChatOptions(ChatOptions.builder().build());
        BaseNode endNode = baseNodeFactory.create();
        endNode.setDescription("end_node");
        endNode.setName("end_node");
        endNode.setWorkflowId(workFlow.getId());
        endNode.setNodeExecute((preResults) -> {
            System.out.println(preResults);
            return "end_node_result";
        });
        Edge edge1 = new Edge(startNode.getId(), llmNode.getId(), "default");
        Edge edge2 = new Edge(llmNode.getId(), endNode.getId(), "default");
        workFlow.addEdge(edge1);
        workFlow.addEdge(edge2);
        workFlow.addNode(llmNode);
        workFlow.setStartNode(startNode);
        workFlow.setEndNode(endNode);
        TestListener testListener = new TestListener();
        workflowExecutor.register(testListener, WorkflowReadyEvent.class);
        workflowExecutor.executeWorkFlow(workFlow);
    }
    public static class TestListener implements GatewayListener {
        @Override
        public void handler(BaseEvent event) {
            System.out.println(event);
        }
    }
}