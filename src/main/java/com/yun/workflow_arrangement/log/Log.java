package com.yun.workflow_arrangement.log;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/13
 */
@Slf4j
public class Log {
    public static void info(String message) {
        log.info(message);
    }
    public static void error(String message) {
        log.error(message);
    }
    public static void warn(String message) {
        log.warn(message);
    }
}
