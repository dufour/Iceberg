package iceberg.util.filters;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class StaticMethodFilter implements Filter<Method> {
    private static StaticMethodFilter instance = new StaticMethodFilter();

    private StaticMethodFilter() {
        // Singleton instance
    }

    public boolean accepts(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }
    
    public static StaticMethodFilter instance() {
        return StaticMethodFilter.instance;
    }   
}
