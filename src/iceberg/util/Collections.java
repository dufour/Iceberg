package iceberg.util;

import iceberg.util.filters.Filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class Collections {
    private Collections() {
        // No instances
    }

    private static Comparator<?> LEXICOGRAPHIC_COMPARATOR = new Comparator<Object>() {
		public int compare(Object o1, Object o2) {
			if (o1 == null) {
				return (o2 == null) ? 0 : -1;
			} else if (o2 == null) {
				return 1;
			} else {
				return o1.toString().compareTo(o2.toString());
			}
		}
    };

    @SuppressWarnings("unchecked")
	public static <T> Comparator<T> lexicographicComparator() {
    	return (Comparator<T>) LEXICOGRAPHIC_COMPARATOR;
    }

    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> basetypeIterator(Iterator<? extends T> iterator) {
        return (Iterator<T>) iterator;
    }

    public static <T> List<T> asList(T... params) {
        List<T> list = new ArrayList<T>(params.length);
        for (T p: params) {
            list.add(p);
        }
        return list;
    }

    public static <T> Set<T> asSet(T... params) {
        Set<T> set = new HashSet<T>(params.length);
        for (T p: params) {
            set.add(p);
        }
        return set;
    }

    public static <E> Set<E> asSet(Collection<E> collection) {
    	if (collection instanceof Set<?>) {
    		return (Set<E>) collection;
    	} else {
    		return new HashSet<E>(collection);
    	}
    }

    public static <T> List<T> toList(Iterable<? extends T> iterable) {
    	return toList(iterable.iterator());
    }

    public static <T> List<T> toList(Iterator<? extends T> iterator) {
        List<T> list = new ArrayList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <T> Set<T> toSet(Iterable<? extends T> iterable) {
        return toSet(iterable.iterator());
    }

    public static <T> Set<T> toSet(Iterator<? extends T> iterator) {
        Set<T> set = new HashSet<T>();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }

    public static <E> void populate(Collection<E> c, Iterable<? extends E> i) {
        for (E e: i) {
            c.add(e);
        }
    }

    public static <E> void populate(Collection<E> c, Iterator<? extends E> i) {
        while (i.hasNext()) {
            c.add(i.next());
        }
    }

    public static <E> void populate(Collection<E> c, Iterable<? extends E> i, Filter<? super E> f) {
        for (E e: i) {
        	if (f.accepts(e)) {
        		c.add(e);
        	}
        }
    }

    public static <E> void populate(Collection<E> c, Iterator<? extends E> i, Filter<? super E> f) {
        while (i.hasNext()) {
        	E next = i.next();
        	if (f.accepts(next)) {
        		c.add(next);
        	}
        }
    }

    public static int[] toPrimitiveArray(Collection<Integer> c) {
    	if (c == null) {
    		return null;
    	}

    	int[] array = new int[c.size()];
    	int pos = 0;
    	for (Integer i: c) {
    		array[pos++] = i.intValue();
    	}
    	return array;
    }
    
    // A potentially cheaper way to check two sets for a non-empty
    // intersection. Unlike retainAll, no set is modified.
    public static boolean overlap(Collection<?> s, Set<?> t) {
        for (Iterator<?> i = s.iterator(); i.hasNext(); ) {
            if (t.contains(i.next())) {
                return true;
            }
        }
        
        return false;
    }

    public static <E> Collection<E> intersectionSet(Collection<E> a, Collection<E> b) {
    	if (a == null) return b;
    	if (b == null) return a;
    	
    	Set<E> result = new HashSet<E>(a);
    	result.retainAll(b);
    	return result;
    }
    
    public static <E> Collection<E> unionSet(Collection<E> a, Collection<E> b) {
    	if (a == null) return b;
    	if (b == null) return a;
    	
    	Set<E> result = new HashSet<E>(a);
    	result.addAll(b);
    	return result;
    }
}