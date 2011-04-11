package iceberg.collections.primitive;

import java.util.Collection;
import java.util.Set;

public interface IntMap<V> {
    public void clear();
    public boolean containsKey(int key);
    public boolean containsValue(Object value);
    public Set<IntMap.Entry<V>> entrySet();
    public boolean equals(Object o);
    public V get(int key);
    public int hashCode();
    public boolean isEmpty();
    public IntSet keySet();
    public V put(int key, V value);
    public void putAll(IntMap<? extends V> t);
    public V remove(int key);
    public int size();
    public Collection<V> values();
    
    public static interface Entry<V> {
        public int getKey();
        public V getValue();
        public void setValue(V value);
        public boolean equals(Object o);
        public int hashCode();
    }
}
