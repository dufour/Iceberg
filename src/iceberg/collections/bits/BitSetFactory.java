package iceberg.collections.bits;

public interface BitSetFactory {
    public BitSet make();
    public BitSet make(int capacity);
}
