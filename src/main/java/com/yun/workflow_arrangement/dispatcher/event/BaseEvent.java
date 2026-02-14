package com.yun.workflow_arrangement.dispatcher.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEvent implements EventDefinition {
    private String description;
    private String eventType;


    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return eventType;
    }
}
