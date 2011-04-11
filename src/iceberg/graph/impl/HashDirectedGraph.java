package iceberg.graph.impl;

import iceberg.collections.HashMultiMap;
import iceberg.collections.MultiMap;
import iceberg.graph.Graphs;
import iceberg.graph.MutableDirectedGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HashDirectedGraph<N> implements MutableDirectedGraph<N> {
    private String name;
    private String description;
    private Set<N> nodes;
    private MultiMap<N,N> successors;
    private MultiMap<N,N> predecessors;
    
    /**
     * 
     */
    public HashDirectedGraph(){}
    
    public HashDirectedGraph(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public boolean containsEdge(N from, N to) {
        if (this.successors != null) {
            return this.successors.contains(from, to);
        }
        
        return false;
    }
    
    public Collection<N> getPredecessors(N node) {
        if (this.predecessors != null) {
            Collection<N> preds = this.predecessors.get(node);
            if (preds != null) {
                return preds;
            }
        }
        
        return Collections.emptyList();
    }
    
    public Collection<N> getSuccessors(N node) {
        if (this.successors != null) {
            Collection<N> succs = this.successors.get(node);
            if (succs != null) {
                return succs;
            }
        }
        
        return Collections.emptyList();
    }
    
    public boolean containsNode(N node) {
        if (this.nodes != null) {
            return this.nodes.contains(node);
        }
        
        return false;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public int getEdgeCount() {
        if (this.successors != null) {
            return this.successors.size();
        } else {
            return 0;
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public Collection<N> getNeighbors(N node) {
        if (this.successors != null) {
            Set<N> neighbors = new HashSet<N>(this.successors.getValueCount(node)
                    + this.predecessors.getValueCount(node));
            neighbors.addAll(this.successors.get(node));
            neighbors.addAll(this.predecessors.get(node));
            return neighbors;
        } else {
            return Collections.emptySet();
        }
    }
    
    public int getNodeCount() {
        if (this.nodes != null) {
            return this.nodes.size();
        }
        
        return 0;
    }
    
    public Collection<N> getNodes() {
        if (this.nodes != null && this.nodes.size() > 0) {
            Collection<N> nodes = new ArrayList<N>(this.nodes.size());
            nodes.addAll(this.nodes);
            return nodes;
        }
        
        return Collections.emptyList();
    }
    
    public Iterator<N> iterator() {
        return this.getNodes().iterator();
    }
    
    public boolean addEdge(N from, N to) {
        this.addNode(from);
        this.addNode(to);
        
        if (this.successors == null) {
            this.successors = new HashMultiMap<N, N>();
            this.predecessors = new HashMultiMap<N, N>();
        }
        
        this.successors.put(from, to);
        return this.predecessors.put(to, from);
    }
    
    public boolean addNode(N node) {
        if (this.nodes == null) {
            this.nodes = new HashSet<N>();
        }
        
        return this.nodes.add(node);
    }
    
    public boolean removeEdge(N from, N to) {
        if (this.successors != null) {
            this.successors.remove(from, to);
            return this.predecessors.remove(to, from);
        } else {
            return false;
        }
    }
    
    public boolean removeNode(N node) {
        if (this.nodes != null) {
            Graphs.removeAllEdges(this, node);
            return this.nodes.remove(node);
        } else {
            return false;
        }
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (N n: this.nodes) {
            for (N s: this.getSuccessors(n)) {
                builder.append(n);
                builder.append(" -> ");
                builder.append(s);
                builder.append("\n");
            }
        }
        
        return builder.toString();
    }
}
