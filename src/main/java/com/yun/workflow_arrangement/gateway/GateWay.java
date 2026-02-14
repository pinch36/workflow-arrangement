package com.yun.workflow_arrangement.gateway;

import com.yun.workflow_arrangement.dispatcher.Dispatcher;
import com.yun.workflow_arrangement.dispatcher.enums.EventEnums;
import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;
import com.yun.workflow_arrangement.dispatcher.event.EventListener;
import com.yun.workflow_arrangement.dispatcher.event.gateway.ExecuteWorkFlowEvent;
import com.yun.workflow_arrangement.gateway.entity.WorkFlowResult;
import com.yun.workflow_arrangement.graph.WorkFlow;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

import static com.yun.workflow_arrangement.dispatcher.enums.EventEnums.EXECUTE_WORKFLOW_EVENT;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
public class GateWay implements EventListener {
    @Resource
    Dispatcher dispatcher;
    @Resource
    ThreadPoolExecutor gatewayThreadPoolExecutor;
    public WorkFlowResult doWork(WorkFlow workFlow){
        BaseEvent executeWorkEvent = ExecuteWorkFlowEvent.builder().build();
        dispatcher.sendToGateWay(executeWorkEvent);
    }


    @Override
    public void handler() {
        dispatcher
    }
}
