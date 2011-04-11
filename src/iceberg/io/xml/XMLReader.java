package iceberg.io.xml;

import iceberg.util.Pair;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLReader extends DefaultHandler {
    public static final String NO_STATE = null;

    private InputStream in;
    private Map<Pair<String,String>,Method> ins;
    private Map<Pair<String,String>,Method> outs;
    protected StringBuffer chars;
    private String state;

    private static final Class<?>[] IN_METHOD_PARAMS = {
        String.class,
        String.class,
        String.class,
        Attributes.class
    };
    private static final Class<?>[] OUT_METHOD_PARAMS = {
        String.class,
        String.class,
        String.class
    };

    public XMLReader(String filename) throws IOException {
        this(new BufferedInputStream(new FileInputStream(filename)));
    }

    public XMLReader(InputStream input) {
        this.in = input;
        this.populate();
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    protected void populate() {
        this.ins = new HashMap<Pair<String,String>, Method>();
        this.outs = new HashMap<Pair<String,String>, Method>();

        Class<?> c = this.getClass();
        while (c != null) {
            this.populate(c);
            c = c.getSuperclass();
        }
    }

    private static boolean checkParamTypes(Method m, Class<?>[] types) {
        Class<?>[] params = m.getParameterTypes();

        if (params.length != types.length) {
            return false;
        }

        for (int i = 0; i < params.length; i++) {
            if (!params[i].equals(types[i])) {
                return false;
            }
        }

        return true;
    }

    protected void populate(Class<?> c) {
        for (Method m : c.getDeclaredMethods()) {
            if (m.isAnnotationPresent(In.class)
                    && checkParamTypes(m, IN_METHOD_PARAMS)) {
                In annotation = m.getAnnotation(In.class);

                String[] states;
                if (m.isAnnotationPresent(State.class)) {
                    states = m.getAnnotation(State.class).value();
                } else {
                    states = new String[] { NO_STATE };
                }

                for (String element: annotation.value()) {
                    for (String state: states) {
                        this.ins.put(Pair.make(state, element), m);
                    }
                }
                m.setAccessible(true);
            }
            if (m.isAnnotationPresent(Out.class)
                    && checkParamTypes(m, OUT_METHOD_PARAMS)) {
        		Out annotation = m.getAnnotation(Out.class);

        		String[] states;
        		if (m.isAnnotationPresent(State.class)) {
                    states = m.getAnnotation(State.class).value();
                } else {
                    states = new String[] { NO_STATE };
                }

                for (String element: annotation.value()) {
                    for (String state: states) {
                        this.outs.put(Pair.make(state, element), m);
                    }
                }
                m.setAccessible(true);
            }
        }
    }

    public void parse() throws SAXException, IOException,
            ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.newSAXParser().parse(new InputSource(this.in), this);
    }

    public void startDocument() throws SAXException {
        // noop
    }

    public void endDocument() throws SAXException {
        // noop
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (this.chars != null) {
            this.chars.append(ch, start, length);
        }
    }

    public void ignorableWhitespace (char ch[], int start, int length) throws SAXException {
        // noop
    }

    public void processingInstruction (String target, String data) throws SAXException {
        // noop
    }

    public void skippedEntity (String name) throws SAXException {
        // noop
    }

    protected void defaultIn(String uri, String name, String qName, Attributes atts) {
        // noop
    }

    protected void defaultOut(String uri, String name, String qName) {
        // noop
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        Method handler = this.ins.get(Pair.make(this.getState(), qName));
        if (handler == null) {
            handler = this.ins.get(Pair.make(NO_STATE, qName));
        }
        if (handler != null) {
            try {
                handler.invoke(this, uri, name, qName, atts);
                return;
            } catch (IllegalAccessException e) {
                // Fall through
            } catch (InvocationTargetException e) {
                // Rethrow
                throw new RuntimeException(e.getMessage(), e.getCause());
            } catch (SecurityException e) {
                // Rethrow
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
        }

        this.defaultIn(uri, name, qName, atts);
    }

    public void endElement(String uri, String name, String qName) {
        Method handler = this.outs.get(Pair.make(this.getState(), qName));
        if (handler == null) {
            handler = this.outs.get(Pair.make(NO_STATE, qName));
        }
        if (handler != null) {
            try {
                handler.invoke(this, uri, name, qName);
                return;
            } catch (IllegalAccessException e) {
                // Fall through
            } catch (InvocationTargetException e) {
                // Rethrow
                throw new RuntimeException(e.getMessage(), e.getCause());
            } catch (SecurityException e) {
                // Fall through
            }
        }

        this.defaultOut(uri, name, qName);
    }
}
