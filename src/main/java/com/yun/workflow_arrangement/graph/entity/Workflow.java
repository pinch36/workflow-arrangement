package com.yun.workflow_arrangement.graph.entity;

import com.yun.workflow_arrangement.graph.enums.WorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
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
public class Workflow {
    private Map<String, BaseNode> nodes;
    private Map<String, Edge> edges;
    private Map<String, List<Edge>> nodeId2InEdges;
    private Map<String, List<Edge>> nodeId2OutEdges;
    private String id;
    private String name;
    private String description;
    private WorkflowStatus status;
    private BaseNode startNode;
    private BaseNode endNode;

    public List<Edge> getOutEdges(String nodeId){
        List<Edge> outEdges = nodeId2OutEdges.get(nodeId);
        if (Objects.isNull(outEdges)){
            return Collections.emptyList();
        }
        return outEdges;
    }
    public List<Edge> getInEdges(String nodeId){
        List<Edge> inEdges = nodeId2InEdges.get(nodeId);
        if (Objects.isNull(inEdges)){
            return Collections.emptyList();
        }
        return inEdges;
    }

    public BaseNode getNode(String nodeId){
        return nodes.get(nodeId);
    }

    public Workflow setStartNode(BaseNode startNode) {
        this.startNode = startNode;
        this.addNode(startNode);
        return this;
    }
    public Workflow setEndNode(BaseNode endNode) {
        this.endNode = endNode;
        this.addNode(endNode);
        return this;
    }

    public Workflow addNode(BaseNode node){
        if (Objects.isNull(node)){
            throw new RuntimeException("node is null");
        }
        nodes.put(node.getId(),node);
        return this;
    }
    public Workflow addEdge(Edge edge){
        if (Objects.isNull(edge)){
            throw new RuntimeException("edge is null");
        }
        edges.put(edge.getId(),edge);
        return this;
    }
    public void init(){
        status = WorkflowStatus.IDLE;
        edges.forEach((k,v)->{
            BaseNode in = nodes.get(v.getSource());
            BaseNode out = nodes.get(v.getTarget());
            if (Objects.isNull(nodeId2InEdges.get(out.getId()))){
                nodeId2InEdges.put(out.getId(),new ArrayList<>());
            }
            if (Objects.isNull(nodeId2OutEdges.get(in.getId()))){
                nodeId2OutEdges.put(in.getId(),new ArrayList<>());
            }
            nodeId2InEdges.get(out.getId()).add(v);
            nodeId2OutEdges.get(in.getId()).add(v);
        });
        nodes.forEach((k,v)->{
            v.init();
        });
    }
}
