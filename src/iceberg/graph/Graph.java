package iceberg.graph;

import java.util.Collection;

public interface Graph<N> extends Iterable<N> {
    public String getName();
    public String getDescription();

    public boolean containsNode(N node);
    public Collection<N> getNeighbors(N node);
    public Collection<N> getNodes();
    public int getNodeCount();
    public int getEdgeCount();
}
