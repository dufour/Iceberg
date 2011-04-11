package iceberg.graph;

import java.util.Collection;

public interface DirectedGraph<N> extends Graph<N> {
    public Collection<N> getSuccessors(N node);
    public Collection<N> getPredecessors(N node);
    public boolean containsEdge(N from, N to);
}
