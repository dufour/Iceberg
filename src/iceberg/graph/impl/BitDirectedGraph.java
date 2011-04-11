package iceberg.graph.impl;

import iceberg.collections.bits.BitRelation;
import iceberg.collections.bits.BitSet;
import iceberg.collections.bits.CompactBitRelation;
import iceberg.collections.bits.CompactBitSet;
import iceberg.collections.bits.Indexer;
import iceberg.collections.bits.MappedBitSet;
import iceberg.graph.Graphs;
import iceberg.graph.MutableDirectedGraph;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class BitDirectedGraph<N> implements MutableDirectedGraph<N> {
    private String name;
    private String description;
    private BitSet nodes;
    private BitRelation successors;
    private BitRelation predecessors;
    private Indexer<N> indexer;
    
    public BitDirectedGraph(String name, String description, Indexer<N> indexer) {
        this.name = name;
        this.description = description;
        this.indexer = indexer;
    }
    
    protected BitSet makeEmptySet() {
        return new CompactBitSet();
    }
    
    protected BitRelation makeEmptyRelation() {
        return new CompactBitRelation();
    }
    
    public boolean containsEdge(N from, N to) {
        if (this.successors != null) {
            int fromID = this.indexer.get(from);
            int toID = this.indexer.get(to);
            if (fromID >= 0 && toID >= 0) {
                return this.successors.contains(fromID, toID);
            }
        }
        
        return false;
    }
    
    public Collection<N> getPredecessors(N node) {
        if (this.predecessors != null) {
            int id = this.indexer.get(node);
            if (id >= 0) {
                BitSet preds = this.predecessors.getRelated(id);
                return new MappedBitSet<N>(this.indexer, preds.clone());
            }
        }
        
        return Collections.emptyList();
    }
    
    public Collection<N> getSuccessors(N node) {
        if (this.successors != null) {
            int id = this.indexer.get(node);
            if (id >= 0) {
                BitSet succs = this.successors.getRelated(id);
                return new MappedBitSet<N>(this.indexer, succs.clone());
            }
        }
        
        return Collections.emptyList();
    }
    
    public boolean containsNode(N node) {
        if (this.nodes != null) {
            int id = this.indexer.get(node);
            if (id >= 0) {
                return this.nodes.contains(id);
            }
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
            int id = this.indexer.get(node);
            if (id >= 0) {
                BitSet neighbors = new CompactBitSet();
                neighbors.addAll(this.successors.getRelated(id));
                neighbors.addAll(this.predecessors.getRelated(id));
                return new MappedBitSet<N>(indexer, neighbors);
            }
        }
        
        return Collections.emptySet();
    }
    
    public int getNodeCount() {
        if (this.nodes != null) {
            return this.nodes.size();
        }
        
        return 0;
    }
    
    public Collection<N> getNodes() {
        if (this.nodes != null && this.nodes.size() > 0) {
            return new MappedBitSet<N>(this.indexer, this.nodes.clone());
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
            this.successors = this.makeEmptyRelation();
            this.predecessors = this.makeEmptyRelation();
        }
        
        int fromID = this.indexer.get(from);
        int toID = this.indexer.get(to);
        this.successors.add(fromID, toID);
        return this.predecessors.add(toID, fromID);
    }
    
    public boolean addNode(N node) {
        if (this.nodes == null) {
            this.nodes = this.makeEmptySet();
        }
        
        int id = this.indexer.get(node);
        if (id < 0) {
            throw new IllegalArgumentException("Indexer return negative index for node");
        }
        return this.nodes.add(id);
    }
    
    public boolean removeEdge(N from, N to) {
        if (this.successors != null) {
            int fromID = this.indexer.get(from);
            int toID = this.indexer.get(to);
            
            if (fromID >= 0 && toID >= 0) {
                this.successors.remove(fromID, toID);
                return this.predecessors.remove(toID, fromID);
            }
        }
        
        return false;
    }
    
    public boolean removeNode(N node) {
        if (this.nodes != null) {
            int id = this.indexer.get(node);
            if (id >= 0) {
                Graphs.removeAllEdges(this, node);
                return this.nodes.remove(id);
            }
        } 

        return false;
    }
}
