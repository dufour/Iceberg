package iceberg.analysis.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class Types {
    private static Types instance = new Types();

    private Types() {
        // Singleton instance
    }
    
    public static Types instance() {
        return Types.instance;
    }
    
    public static String javaName(Class<?> c) {
        if (c.isArray()) {
            return javaName(c.getComponentType()) + "[]";
        } else {
            return c.getName();
        }
    }
    
    public static String internalName(Class<?> c) {
    	if (c.equals(Byte.TYPE)) {
    		return "B";
    	} else if (c.equals(Boolean.TYPE)) {
    		return "Z";
    	} else if (c.equals(Character.TYPE)) {
    		return "C";
    	} else if (c.equals(Double.TYPE)) {
    		return "D";
    	} else if (c.equals(Float.TYPE)) {
    		return "F";
    	} else if (c.equals(Integer.TYPE)) {
    		return "I";
    	} else if (c.equals(Long.TYPE)) {
    		return "J";
    	} else if (c.equals(Short.TYPE)) {
    		return "S";
    	} else if (c.equals(Void.TYPE)) {
    		return "V";
    	} else if (c.isArray()) {
    		return "[" + internalName(c.getComponentType());
    	} else {
    		return "L" + c.getName().replace('.', '/') + ";";
    	}
    }
    
    public static String getSignature(Method method) {
    	return method.getName() + getDescriptor(method);
    }
    
    public static String getDescriptor(Class<?>[] paramTypes, Class<?> returnType) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("(");
        for (Class<?> param: paramTypes) {
            sb.append(internalName(param));
        }
        sb.append(")");
        sb.append(internalName(returnType));
        
        return sb.toString();
    }
    
    public static String getDescriptor(Method method) {
    	return getDescriptor(method.getParameterTypes(), method.getReturnType());
    }
    
    public static String getDescriptor(Constructor<?> constructor) {
        return getDescriptor(constructor.getParameterTypes(), Void.TYPE);
    }
    
    public static String internalNameToJava(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid internal type name: " + name);
        }
        
        switch (name.charAt(0)) {
            case 'B':
                if (name.length() == 1) return "byte";
                break;
            case 'C':
                if (name.length() == 1) return "char";
                break; 
            case 'D':
                if (name.length() == 1) return "double";
                break;
            case 'F':
                if (name.length() == 1) return "float";
                break;
            case 'I':
                if (name.length() == 1) return "int";
                break;
            case 'J':
                if (name.length() == 1) return "long";
                break;
            case 'L':
                if (name.endsWith(";")) {
                    // FIXME: check for invalid characters in name
                    
                    return name.substring(1, name.length() - 1).replace('/', '.');
                }
                break;
            case 'S':
                if (name.length() == 1) return "short";
                break;
            case 'V':
                if (name.length() == 1) return "void";
                break;
            case 'Z':
                if (name.length() == 1) return "boolean";
                break;
            case '[': 
                try {
                    return internalNameToJava(name.substring(1)) + "[]";
                } catch (RuntimeException e) {
                    throw new RuntimeException("Invalid internal type name: " + name);
                }
            default:
                break;                
        }
        throw new RuntimeException("Invalid internal type name: " + name);
    }
    
    public static String javaNameToInternal(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid java type name: " + name);
        }
        
        if (name.endsWith("[]")) {
            return "[" + javaNameToInternal(name.substring(0, name.length() - 2));
        }
        
        switch (name.charAt(0)) {
            case 'b':
                if (name.equals("byte")) return "B";
                if (name.equals("boolean")) return "Z";
                break;
            case 'c':
                if (name.equals("char")) return "C";
                break;
            case 'd':
                if (name.equals("double")) return "D";
                break;
            case 'f':
                if (name.equals("float")) return "F";
                break;
            case 'i':
                if (name.equals("int")) return "I";
                break;
            case 'l':
                if (name.equals("long")) return "J";
                break;
            case 's':
                if (name.equals("short")) return "S";
                break;
            case 'v':
                if (name.equals("void")) return "V";
                break;
            default:
                break;                
        }
        
        // FIXME: Check for invalid characters in name
        
        return "L" + name.replace('.', '/') + ";";
    }
    
    public static String parseReturnTypeFromDescriptor(String descriptor) {
        int paren = descriptor.indexOf(')');
        return descriptor.substring(paren + 1);
    }
    
    public static String[] parseArgumentsFromDescriptor(String descriptor) {
        if (descriptor.startsWith("(")) {
            int paren = descriptor.indexOf(')');
            descriptor = descriptor.substring(1, paren);
        }
        char[] s = descriptor.toCharArray();
        List<String> params = new ArrayList<String>(5);
        
        int i = 0;
        int start = 0;
        while (i < s.length) {
            switch (s[i]) {
                case 'B':
                case 'C': 
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'V':
                case 'Z':                   
                    params.add(descriptor.substring(start, ++i));
                    start = i;
                    break;
                case 'L':
                    while (s[i] != ';') i++;
                    params.add(descriptor.substring(start, ++i));
                    start = i;
                    break;
                case '[':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Malformed signature: "
                            + descriptor.substring(i));
            }
        }
        
        return params.toArray(new String[params.size()]);
    }
    
    public static String shortJavaName(String javaName) {
        int dot = javaName.lastIndexOf('.');
        if (dot >= 0) {
            return javaName.substring(dot + 1);
        }
        
        return javaName;
    }
    
    public static String getShortSignature(String internalSignature) {
        StringBuilder builder = new StringBuilder("(");
        for (String param: parseArgumentsFromDescriptor(internalSignature)) {
            if (builder.length() > 1) {
                builder.append(",");
            }
            builder.append(shortJavaName(Types.internalNameToJava(param)));
        }
        builder.append(")");
        
        return builder.toString();
    }
}
