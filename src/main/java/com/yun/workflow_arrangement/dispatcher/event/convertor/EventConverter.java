package com.yun.workflow_arrangement.dispatcher.event.convertor;

import com.yun.workflow_arrangement.dispatcher.event.node.ExecuteNodeEvent;
import com.yun.workflow_arrangement.dispatcher.event.workflow.ExecuteWorkflowEvent;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.ai.chat.messages.UserMessage;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/24
 */
@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface EventConverter {
    EventConverter INSTANCE = Mappers.getMapper(EventConverter.class);
    @Mapping(target = "userMessage", ignore = true)
    ExecuteNodeEvent executeWorkflowEventToExecuteNodeEvent(ExecuteWorkflowEvent executeWorkflowEvent);

}