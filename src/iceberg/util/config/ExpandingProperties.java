package iceberg.util.config;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpandingProperties extends Properties {
    private static final long serialVersionUID = -481600327473227015L;

    public ExpandingProperties() {
        super();
    }

    public ExpandingProperties(Properties defaults) {
        super(defaults);
    }

    private String getRawProperty(String name) {
        return this.getRawProperty(name, null);
    }

    private String getRawProperty(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null) {
            value = super.getProperty(name);
            if (value == null) {
                value = defaultValue;
            }
        }

        return value;
    }

    public String expandProperties(String s) {
        return expandProperties(s, this);
    }

    public String getProperty(String key, String defaultValue) {
        return this.expandProperties(this.getRawProperty(key, defaultValue));
    }

    public String getProperty(String key) {
        return this.expandProperties(this.getRawProperty(key));
    }

    public Object get(Object key) {
        return this.expandProperties(String.valueOf(super.get(key)));
    }

    public static String expandProperties(String s, final Properties properties) {
        if (s == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher m = p.matcher(s);
        while (m.find()) {
            String property_name = m.group(1);
            Object value = properties.getProperty(property_name);

            if (value != null) {
                m.appendReplacement(sb, String.valueOf(value));
            } else {
                m.appendReplacement(sb, "");
            }
        }

        m.appendTail(sb);

        return sb.toString();
    }

    public static String makePropertyReference(String name) {
        return "${" + name + "}";
    }
}