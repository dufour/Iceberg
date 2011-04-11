package iceberg.collections.bits;

public interface BitRelationFactory {
    public BitRelation make();
    public BitRelation make(int capacity);
}
