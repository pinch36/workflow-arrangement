package com.yun.workflow_arrangement.scheduler;

import com.yun.workflow_arrangement.dispatcher.event.scheduler.SchedulerEvent;

import java.util.List;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
public interface Scheduler {
    List<Instance> getInstances();
    Instance getInstance(String id);
    void addInstance(Instance instance);
    void removeInstance(String id);
    void init();
    void end();
    void execute(String id);

}
