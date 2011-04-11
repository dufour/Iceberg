package iceberg.collections.bits;

public interface Indexer<E> {
    public E get(int id);
    public int get(Object element);
}
