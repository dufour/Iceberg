package iceberg.collections;

import iceberg.util.procedures.BinaryProcedure;
import iceberg.util.procedures.UnaryProcedure;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public interface MultiMap<K,V> {
	public boolean put(K key, V value);
    public boolean putAll(K key, Collection<V> values);
    public boolean putAll(K key, Iterator<V> values);
    public boolean putAll(MultiMap<? extends K, ? extends V> m);
    public boolean contains(K key);
    public boolean contains(K key, V value);
    public Set<K> keySet();
    public Collection<V> get(K key);
    public int getValueCount(K key);
    public Iterator<K> iterateKeys();
    public Iterator<V> iterateValues(K key);
    public Set<MultiMap.Entry<K,V>> entrySet();
    public int size();
    public void clear();
    public boolean remove(K key, V value);
    public boolean removeAll(K key);

    public void forEach(BinaryProcedure<K, V> proc);
    public void forEachKey(UnaryProcedure<K> proc);
    public void forEachValue(UnaryProcedure<V> proc);
    public void forEach(K key, UnaryProcedure<V> proc);
    public void forEach(K key, BinaryProcedure<K, V> proc);

    public static interface Entry<K, V> {
        public K getKey();
        public V getValue();
    }
}
