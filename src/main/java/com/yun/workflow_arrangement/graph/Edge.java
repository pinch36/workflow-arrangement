package com.yun.workflow_arrangement.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 *
 *
 * @author raoliwen
 * @date 2026/02/14
 */
@Data
@Builder
@AllArgsConstructor
public class Edge {
    private String source;
    private String target;
    private String type;
    private String id;

    public Edge() {
        id = UUID.randomUUID().toString();
    }
    public Edge(String type) {
        id = UUID.randomUUID().toString();
        this.type = type;
    }
    public Edge(String source,String target,String type) {
        id = UUID.randomUUID().toString();
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String other(String edge) {
        if (source.equals(edge)){
            return target;
        }
        return source;
    }
}
