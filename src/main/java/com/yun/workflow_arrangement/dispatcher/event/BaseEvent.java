package com.yun.workflow_arrangement.dispatcher.event;


import com.yun.workflow_arrangement.entity.context.Context;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEvent implements EventDefinition {
    protected String description;
    protected String eventType;
    protected Context context;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return eventType;
    }
}
