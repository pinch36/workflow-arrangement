package com.yun.workflow_arrangement.graph;

import com.yun.workflow_arrangement.entity.context.WorkFlowContext;
import com.yun.workflow_arrangement.node.entity.BaseNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
public class WorkFlow {
    private Map<String, BaseNode> nodes;
    private Map<String, Edge> edges;
    private Map<String, List<Edge>> nodeId2InEdges;
    private Map<String, List<Edge>> nodeId2OutEdges;
    private String id;
    private String name;
    private String description;
    private String status;
    private WorkFlowContext workFlowContext;


    public void addNode(BaseNode node){
        nodes.put(node.getId(),node);
    }
    public void addEdge(Edge edge){
        edges.put(edge.getId(),edge);
    }
    public void init(){
        edges.forEach((k,v)->{
            BaseNode in = nodes.get(v.getSource());
            BaseNode out = nodes.get(v.getTarget());
            if (Objects.isNull(nodeId2InEdges.get(out.getId()))){
                nodeId2InEdges.put(out.getId(),new ArrayList<>());
            }
            if (Objects.isNull(nodeId2OutEdges.get(in.getId()))){
                nodeId2InEdges.put(in.getId(),new ArrayList<>());
            }
            nodeId2InEdges.get(out.getId()).add(v);
            nodeId2OutEdges.get(in.getId()).add(v);
        });
    }
}
