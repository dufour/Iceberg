package iceberg.graph;

import java.util.Collection;

public final class Graphs {
    private Graphs() {
        // no instances
    }
    
    public static <N> void bypass(MutableDirectedGraph<N> graph, N node) {
        Collection<N> predecessors = graph.getPredecessors(node);
        Collection<N> successors = graph.getSuccessors(node);
        
        for (N pred: predecessors) {
            graph.removeEdge(pred, node);            
            for (N succ: successors) {
                graph.removeEdge(node, succ);
                if (node != succ) { // Make sure we discard self-loops
                    graph.addEdge(pred, succ);
                }
            }
        }
    }
    
    public static <N> boolean removeInEdges(MutableDirectedGraph<N> graph, N node) {
        boolean changed = false;
        
        for (N pred: graph.getPredecessors(node)) {
            changed = graph.removeEdge(pred, node) || changed;
        }
        
        return changed;
    }
    
    public static <N> boolean removeOutEdges(MutableDirectedGraph<N> graph, N node) {
        boolean changed = false;
        
        for (N succ: graph.getSuccessors(node)) {
            changed = graph.removeEdge(node, succ) || changed;
        }
        
        return changed;
    }
    
    public static <N> boolean removeAllEdges(MutableDirectedGraph<N> graph, N node) {
        boolean changed = removeInEdges(graph, node);
        changed = removeOutEdges(graph, node) || changed;
        return changed;
    }
}
