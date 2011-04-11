package iceberg.collections.primitive;

import iceberg.collections.bits.IntIterator;

public interface IntCollection extends IntIterable {
    public boolean add(int i);
    public boolean addAll(IntCollection c);    
    public void clear();
    public boolean contains(int i);
    public boolean containsAll(IntCollection c);
    public boolean equals(Object o);
    public int hashCode();
    public boolean isEmpty();
    public IntIterator iterator();
    public boolean remove(int i);
    public boolean removeAll(IntCollection c);
    public boolean retainAll(IntCollection c);
    public int size();
    public int[] toArray();
    public int[] toArray(int[] a);
}
