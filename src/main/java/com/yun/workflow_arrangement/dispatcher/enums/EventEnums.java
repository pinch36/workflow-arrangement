package com.yun.workflow_arrangement.dispatcher.enums;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
public enum EventEnums {
    WORKFLOW_EVENT("workflow_event"),
    NODE_EVENT("node_event"),
    USER_EVENT("user_event"),
    LLM_EVENT("llm_event"),
    GATEWAY_EVENT("gateway_event");
    private final String eventName;

    EventEnums(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public static EventEnums fromEventName(String name) {
        for (EventEnums e : values()) {
            if (e.eventName.equals(name)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown event: " + name);
    }
}
