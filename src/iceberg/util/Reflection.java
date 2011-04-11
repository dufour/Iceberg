package iceberg.util;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public final class Reflection {
    private Reflection() {
        // No instances
    }
    
    public static InputStream getClassAsStream(final Class<?> klass) {
        final String resource_name = klass.getName().replace('.', '/') + ".class";
        final ClassLoader loader = klass.getClassLoader();
        if (loader != null) {
            return loader.getResourceAsStream(resource_name);
        }
        
        return ClassLoader.getSystemResourceAsStream(resource_name);
    }
    
    public static File getClassFile(final Class<?> klass) {
        URL url = getClassLocation(klass);
        try {
            if (url != null) {
                if (url.getProtocol().equals("file")) {
                    return new File(url.toURI());                    
                } else if (url.getProtocol().equals("jar")) {
                    String file = url.getFile();
                    int bang = file.indexOf('!');
                    if (bang >= 0) {
                        file = file.substring(0, bang);
                    }
                    return new File(new URL(file).toURI());                    
                }
            }            
        } catch (URISyntaxException e) {
            // Oh well
        } catch (MalformedURLException e) {
            // Oh well...
        }
        
        return null;
    }
    
    public static URL getClassLocation(final Class<?> klass) {
        final String resource_name = klass.getName().replace('.', '/') + ".class";

        final ProtectionDomain pd = klass.getProtectionDomain();
        if (pd != null) {
            final CodeSource source = pd.getCodeSource();
            if (source != null){
                URL url = source.getLocation();
                if (url != null && "file".equals(url.getProtocol())) {
                    try {
                        if (url.toExternalForm().endsWith(".jar")
                                || url.toExternalForm().endsWith(".zip")){
                            return new URL("jar:".concat(
                                    url.toExternalForm()).concat("!/")
                                    .concat(resource_name));
                        } else if (new File(url.getFile()).isDirectory()){
                            url = new URL(url, resource_name);
                        }
                    } catch (MalformedURLException e) {
                        // Ignore
                    }
                }
            }
        }

        final ClassLoader loader = klass.getClassLoader();
        if (loader != null) {
            return loader.getResource(resource_name);
        } else {
            return ClassLoader.getSystemResource(resource_name); 
        }
    }
    
    public static String getJavaTypeName(String internalName) {
        if (internalName == null || internalName.length() == 0) {
            return internalName;
        }

        char c = internalName.charAt(0);
        switch (c) {
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'D':
                return "double";
            case 'F':
                return "float";
            case 'I':
                return "int";
            case 'J':
                return "long";
            case 'L':
                if (!internalName.endsWith(";")) {
                    throw new RuntimeException("Invalid internal type name: "
                            + internalName);
                }
                return internalName.substring(1, internalName.length() - 1).replace('/', '.');
            case 'S':
                return "short";
            case 'V':
                return "void";
            case 'Z':
                return "boolean";
            case '[':
                return getJavaTypeName(internalName.substring(1)) + "[]";
            default:
                throw new RuntimeException("Invalid internal type name: "
                        + internalName);
        }
    }

    public static String getJavaTypeName(Class<?> c) {
        if (c == null) {
            return null;
        } else if (c.isArray()) {
            return getJavaTypeName(c.getComponentType()) + "[]";
        } else {
            return c.getName();
        }
    }
}
