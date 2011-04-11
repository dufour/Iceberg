package iceberg.graph.writers;

public interface EdgeAttributeProvider<N> {
    public void generateAttributes(N from, N to, DotAttributes attribs);
}
