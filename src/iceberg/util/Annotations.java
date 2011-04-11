package iceberg.util;

import iceberg.util.filters.Filter;
import iceberg.util.filters.NoFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public final class Annotations {
    private Annotations() {
        // No instances
    }
    
    public static Collection<Method> findAnnotatedMethods(Class<?> klass,
            Class<? extends Annotation> annotation) {
        NoFilter<Method> filter = NoFilter.instance();
        return findAnnotatedMethods(klass, annotation, filter);
    }
    
    public static Collection<Method> findAnnotatedMethods(Class<?> klass,
            Class<? extends Annotation> annotation, Filter<Method> filter) {
        List<Method> methods = new ArrayList<Method>();
        
        Class<?> c = klass;
        while (c != null) {
            for (Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation) && filter.accepts(method)) {
                    methods.add(method);
                }
            }
            c = c.getSuperclass();
        }
        
        return methods;
    }
    
    public static Method findAnnotatedMethod(Class<?> klass,
            Class<? extends Annotation> annotation) {
        NoFilter<Method> filter = NoFilter.instance();
        return findAnnotatedMethod(klass, annotation, filter);
    }
    
    public static Method findAnnotatedMethod(Class<?> klass,
            Class<? extends Annotation> annotation, Filter<Method> filter) {
        Class<?> c = klass;
        while (c != null) {
            for (Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation) && filter.accepts(method)) {
                    return method;
                }
            }
            c = c.getSuperclass();
        }
        
        return null;
    }

	public static <A extends Annotation> A findAnnotation(Class<A> annotation, Class<?> c) {
		if (c == null) {
			return null;
		} else if (c.isAnnotationPresent(annotation)) {
			return c.getAnnotation(annotation);
		} else {
			for (Class<?> iface: c.getInterfaces()) {
				A result = findAnnotation(annotation, iface);
				if (result != null) {
					return result;
				}
			}
			return findAnnotation(annotation, c.getSuperclass());
		}
	}
}
