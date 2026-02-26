package com.yun.workflow_arrangement.gateway;

import com.yun.workflow_arrangement.dispatcher.event.BaseEvent;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/26
 */
public interface GatewayListener {
    void handler(BaseEvent event);
}
