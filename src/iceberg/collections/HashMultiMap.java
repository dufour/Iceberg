package iceberg.collections;

import iceberg.util.procedures.BinaryProcedure;
import iceberg.util.procedures.UnaryProcedure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * A MultiMap implementation that delegates to a HashMap instance.
 *
 * @author Bruno Dufour (dufour@cs.rutgers.edu)
 *
 */
public class HashMultiMap<K, V> extends AbstractMultiMap<K, V>
        implements MultiMap<K, V> {
    private Map<K, Collection<V>> map;

    public HashMultiMap() {
        this.map = new HashMap<K, Collection<V>>();
    }

    protected Collection<V> makeCollection(K key) {
        return new HashSet<V>();
    }

    public boolean put(K key, V value) {
        Collection<V> c = this.map.get(key);
        if (c == null) {
            c = this.makeCollection(key);
            this.map.put(key, c);
        }

        return c.add(value);
    }

    public boolean putAll(K key, Collection<V> values) {
        Collection<V> c = this.map.get(key);
        if (c == null) {
            c = this.makeCollection(key);
            this.map.put(key, c);
        }

        return c.addAll(values);
    }

    public boolean putAll(K key, Iterator<V> values) {
        Collection<V> c = this.map.get(key);
        if (c == null) {
            c = this.makeCollection(key);
            this.map.put(key, c);
        }

        boolean changed = false;
        while (values.hasNext()) {
            changed = c.add(values.next()) || changed;
        }

        return changed;
    }

    public boolean contains(K key, V value) {
        Collection<V> c = this.map.get(key);
        if (c == null) {
            return false;
        }

        return c.contains(value);
    }

    public Collection<V> get(K key) {
        Collection<V> c = this.map.get(key);
        if (c != null && !c.isEmpty()) {
            Collection<V> r = new ArrayList<V>(c.size());
            r.addAll(c);
            return r;
        } else {
            return Collections.emptySet();
        }
    }

    public Iterator<K> iterateKeys() {
        return this.map.keySet().iterator();
    }

    public Iterator<V> iterateValues(K key) {
        Collection<V> c = this.map.get(key);
        if (c == null) {
            return EmptyIterator.instance();
        } else {
            return c.iterator();
        }
    }

    public int getValueCount(K key) {
        Collection<V> c = this.map.get(key);
        if (c == null) {
            return 0;
        } else {
            return c.size();
        }
    }

    public boolean contains(K key) {
        return this.map.containsKey(key);
    }

    public int size() {
        return this.map.size();
    }

    public void clear() {
        this.map.clear();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean remove(K key, V value) {
        Collection<V> c = this.map.get(key);
        if (c != null) {
            return c.remove(value);
        }

        return false;
    }

    public boolean removeAll(K key) {
        return (this.map.remove(key) != null);
    }

    public void forEach(BinaryProcedure<K, V> proc) {
        for (Map.Entry<K, Collection<V>> e: this.map.entrySet()) {
            K key = e.getKey();
            Collection<V> values = e.getValue();
            if (values != null) {
                for (V v: values) {
                    proc.execute(key, v);
                }
            }
        }
    }

    public void forEach(K key, UnaryProcedure<V> proc) {
        Collection<V> values = this.map.get(key);
        if (values != null) {
            for (V v: values) {
                proc.execute(v);
            }
        }
    }

    public void forEach(K key, BinaryProcedure<K, V> proc) {
        Collection<V> values = this.map.get(key);
        if (values != null) {
            for (V v: values) {
                proc.execute(key, v);
            }
        }
    }

    public void forEachKey(UnaryProcedure<K> proc) {
        for (K key: this.map.keySet()) {
            proc.execute(key);
        }
    }

    public void forEachValue(UnaryProcedure<V> proc) {
        for (Collection<V> values: this.map.values()) {
            if (values != null) {
                for (V v: values) {
                    proc.execute(v);
                }
            }
        }
    }
}
