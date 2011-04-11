package iceberg.graph;

public interface MutableGraph<N> extends Graph<N> {
    public boolean addNode(N node);
    public boolean addEdge(N from, N to);
    public boolean removeNode(N node);
    public boolean removeEdge(N from, N to);
}
