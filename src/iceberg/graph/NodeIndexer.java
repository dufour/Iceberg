package iceberg.graph;

public interface NodeIndexer<N> {
    public int getIndex(N node);
}
