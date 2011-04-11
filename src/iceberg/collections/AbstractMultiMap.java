package iceberg.collections;

import iceberg.util.Collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractMultiMap<K, V> implements MultiMap<K, V> {
    public boolean putAll(MultiMap<? extends K, ? extends V> m) {
        boolean changed = false;
        for (MultiMap.Entry<? extends K, ? extends V> e: m.entrySet()) {
            changed = this.put(e.getKey(), e.getValue()) || changed;
        }

        return changed;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("MultiMap:\n");

        for (Iterator<K> i = this.iterateKeys(); i.hasNext(); ) {
            K key = i.next();
            sb.append("  " + key + " -> {");
            for (Iterator<V> j = this.iterateValues(key); j.hasNext(); ) {
                V val = j.next();
                sb.append(val);
                if (i.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("}\n");
        }
        return sb.toString();
    }

    public Set<MultiMap.Entry<K, V>> entrySet() {
        Set<MultiMap.Entry<K,V>> s = new HashSet<MultiMap.Entry<K,V>>();
        for (Iterator<K> i = this.iterateKeys(); i.hasNext(); ) {
            K key = i.next();
            for (Iterator<V> j = this.iterateValues(key); j.hasNext(); ) {
                V value = j.next();
                s.add(new Entry<K, V>(key, value));
            }
        }
        return s;
    }

    public Set<K> keySet() {
        Set<K> s = new HashSet<K>();
        Collections.populate(s, this.iterateKeys());
        return s;
    }

    protected static class Entry<S, T> implements MultiMap.Entry<S, T> {
        private S key;
        private T value;

        public Entry(S key, T value) {
            this.key = key;
            this.value = value;
        }

        public S getKey() {
            return this.key;
        }

        public T getValue() {
            return this.value;
        }

        public String toString() {
            return "<" + this.key + ": " + this.value + ">";
        }
    }
}
