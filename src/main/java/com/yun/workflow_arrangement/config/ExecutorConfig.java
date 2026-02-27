package com.yun.workflow_arrangement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author raoliwen
 * @date 2026/02/26
 */
@Configuration
public class ExecutorConfig {

    @Bean(name = "gatewayThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor gatewayThreadPoolExecutor() {
        int cpuCount = Runtime.getRuntime().availableProcessors();

        int corePoolSize = cpuCount * 2;
        int maximumPoolSize = cpuCount * 4;
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2000);

        // 自定义线程工厂，设置有意义的线程名，便于排查问题
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("gateway-pool-" + counter.incrementAndGet());
                // 设为非守护线程，确保任务执行完再退出
                thread.setDaemon(false);
                return thread;
            }
        };

        RejectedExecutionHandler rejectedHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                rejectedHandler
        );

        // 允许核心线程在空闲时被回收（适合流量波动大的场景）
        executor.allowCoreThreadTimeOut(false);

        // 预启动所有核心线程，避免首次任务提交时的线程创建开销
        executor.prestartAllCoreThreads();

        return executor;
    }

    @Bean(name = "nodeSchedulerThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor nodeSchedulerThreadPoolExecutor() {
        int cpuCount = Runtime.getRuntime().availableProcessors();

        int corePoolSize = cpuCount * 2;
        int maximumPoolSize = cpuCount * 4;
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2000);

        // 自定义线程工厂，设置有意义的线程名，便于排查问题
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("nodeScheduler-pool-" + counter.incrementAndGet());
                // 设为非守护线程，确保任务执行完再退出
                thread.setDaemon(false);
                return thread;
            }
        };

        RejectedExecutionHandler rejectedHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                rejectedHandler
        );

        // 允许核心线程在空闲时被回收（适合流量波动大的场景）
        executor.allowCoreThreadTimeOut(false);

        // 预启动所有核心线程，避免首次任务提交时的线程创建开销
        executor.prestartAllCoreThreads();

        return executor;
    }

    @Bean(name = "llmSchedulerThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor llmSchedulerThreadPoolExecutor() {
        int cpuCount = Runtime.getRuntime().availableProcessors();

        int corePoolSize = cpuCount * 2;
        int maximumPoolSize = cpuCount * 4;
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2000);

        // 自定义线程工厂，设置有意义的线程名，便于排查问题
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("llmScheduler-pool-" + counter.incrementAndGet());
                // 设为非守护线程，确保任务执行完再退出
                thread.setDaemon(false);
                return thread;
            }
        };

        RejectedExecutionHandler rejectedHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                rejectedHandler
        );

        // 允许核心线程在空闲时被回收（适合流量波动大的场景）
        executor.allowCoreThreadTimeOut(false);

        // 预启动所有核心线程，避免首次任务提交时的线程创建开销
        executor.prestartAllCoreThreads();

        return executor;
    }

    @Bean(name = "workflowSchedulerThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor workflowSchedulerThreadPoolExecutor() {
        int cpuCount = Runtime.getRuntime().availableProcessors();

        int corePoolSize = cpuCount * 2;
        int maximumPoolSize = cpuCount * 4;
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2000);

        // 自定义线程工厂，设置有意义的线程名，便于排查问题
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("workflowScheduler-pool-" + counter.incrementAndGet());
                // 设为非守护线程，确保任务执行完再退出
                thread.setDaemon(false);
                return thread;
            }
        };

        RejectedExecutionHandler rejectedHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                rejectedHandler
        );

        // 允许核心线程在空闲时被回收（适合流量波动大的场景）
        executor.allowCoreThreadTimeOut(false);

        // 预启动所有核心线程，避免首次任务提交时的线程创建开销
        executor.prestartAllCoreThreads();

        return executor;
    }

    @Bean(name = "dispatcherThreadPoolExecutor", destroyMethod = "shutdown")
    public ExecutorService dispatcherThreadPoolExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
