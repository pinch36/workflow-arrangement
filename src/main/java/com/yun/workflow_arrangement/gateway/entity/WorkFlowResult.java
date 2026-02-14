package com.yun.workflow_arrangement.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowResult <T>{
    T result;
}
