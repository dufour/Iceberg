package iceberg.graph.writers;

public interface NodeAttributeProvider<N> {
    public void generateAttributes(N node, DotAttributes attribs);
}
