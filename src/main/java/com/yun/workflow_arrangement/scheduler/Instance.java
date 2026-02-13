package com.yun.workflow_arrangement.scheduler;

import java.util.concurrent.Callable;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
public interface Instance<T> extends Callable<T> {
    String getId();
    String getName();
    String getDescription();
    String getStatus();
    void init();
    void end();
}
