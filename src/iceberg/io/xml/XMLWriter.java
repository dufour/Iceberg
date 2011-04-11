package iceberg.io.xml;

import iceberg.io.DataStream;
import iceberg.io.PrintStreamAdapter;
import iceberg.io.PrintWriterAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Stack;

public class XMLWriter {
    public static final String XML_1_0_DECL = "<?xml version=\"1.0\"?>";

    public static final int NONE                  =      0;
    public static final int NEWLINE_BEFORE_ENTITY = 1 << 0;
    public static final int AUTO_CLOSE            = 1 << 1;
    public static final int AUTO_INDENT           = 1 << 2;
    public static final int NO_DECL               = 1 << 3;

    private static final int EMPTY   = 0;
    private static final int UNKNOWN = 1;
    private static final int TEXT    = 2;
    private static final int ENTITY  = 3;

    private DataStream out;
    private int options;

    private Stack<String> entities;
    private int content_type;
    private boolean need_indent;

    public XMLWriter(String filename) throws FileNotFoundException {
    	this(new PrintStream(filename));
    }
    
    public XMLWriter(File file) throws FileNotFoundException {
    	this(new PrintStream(file));
    }
    
    public XMLWriter(PrintStream out) {
        this(out, NEWLINE_BEFORE_ENTITY | AUTO_INDENT);
    }
    
    public XMLWriter(PrintStream out, int options) {
        this(new PrintStreamAdapter(out), options);
    }

    public XMLWriter(PrintWriter out) {
        this(out, NEWLINE_BEFORE_ENTITY | AUTO_INDENT);
    }
    
    public XMLWriter(PrintWriter out, int options) {
        this(new PrintWriterAdapter(out), options);
    }
    
    public XMLWriter(DataStream out, int options) {
        this.out = out;
        this.options = options;
        this.entities = new Stack<String>();
        this.need_indent = false;

        if ((this.options & NO_DECL) == 0) {
            this.writeInternal(XML_1_0_DECL);
        }
        this.println();
        this.content_type = EMPTY;
    }
    
    

    private void setContentType(int newType) {
        if (newType > this.content_type) {
            this.content_type = newType;
        }
    }

    private void indent() {
        for (int i = this.entities.size() - 1; i >= 0; i--) {
            this.out.print("  ");
        }
    }

    private void writeLine(String s) {
        if (this.need_indent) {
            this.indent();
            this.need_indent = false;
        }
        this.out.print(s);
    }

    private void writeInternal(String s) {
        if (s == null || s.length() == 0) {
            return;
        }

        if ((this.options & AUTO_INDENT) != 0) {
            int start = 0;
            int cur = s.indexOf('\n');
            while (cur >= 0) {
                this.writeLine(s.substring(start, cur + 1));
                this.need_indent = true;
                start = cur + 1;
                cur = s.indexOf('\n', start);
            }
            // Output remaining part of the string
            if (start < s.length()) {
                this.writeLine(s.substring(start));
            }
        } else {
            this.out.print(s);
        }
    }

    public static String escape(String s) {
    	if (s == null) s = "";
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            switch (c) {
                case '<':
                    sb.replace(i, i + 1, "&lt;");
                    break;
                case '>':
                    sb.replace(i, i + 1, "&gt;");
                    break;
                case '\"':
                    sb.replace(i, i + 1, "&quot;");
                    break;
                case '\'':
                    sb.replace(i, i + 1, "&apos;");
                    break;
                case '&':
                    sb.replace(i, i + 1, "&amp;");
                    break;
                default:
                    break;
            }
        }

        return sb.toString();
    }

    public void println() {
        this.writeInternal("\n");
    }

    private void ensureContent() {
        if (!this.entities.isEmpty() && this.content_type == EMPTY) {
            this.startContent();
        }
    }

    public void startEntity(String name) {
        this.ensureContent();
        if ((this.options & NEWLINE_BEFORE_ENTITY) != 0) {
            if (!this.entities.isEmpty() && !this.need_indent) {
                this.println();
            }
        }

        this.writeInternal("<" + name);

        this.entities.push(name);
        this.content_type = EMPTY;
    }

    public void addAttribute(String name, boolean value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, byte value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, char value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, double value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, float value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, int value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, long value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, short value) {
    	this.addAttribute(name, String.valueOf(value));
    }
    
    public void addAttribute(String name, Object value) {
    	this.addAttribute(name, String.valueOf(value));
    }

    public void addAttribute(String name, String value) {
        if (this.entities.isEmpty()) {
            throw new IllegalStateException("No entity started");
        }
        if (this.content_type != EMPTY) {
            throw new IllegalStateException("Content already provided for "
                    + this.entities.peek() + " entity");
        }
        this.writeInternal(" " + name + "=\"" + escape(value) + "\"");
    }

    public void startContent() {
        if (this.entities.isEmpty()) {
            throw new IllegalStateException("No entity started");
        }
        if (this.content_type != EMPTY) {
            throw new IllegalStateException("Content already provided for "
                    + this.entities.peek() + " entity");
        }
        this.writeInternal(">");
        this.content_type = UNKNOWN;
    }

    public void endEntity() {
        String name = this.entities.pop();
        if (this.content_type != EMPTY) {
            this.writeInternal("</" + name + ">");
        } else {
            this.writeInternal("/>");
        }
        this.content_type = ENTITY;
        if ((this.options & NEWLINE_BEFORE_ENTITY) != 0) {
            this.println();
        }
    }

    public void print(boolean x) {
    	this.print(String.valueOf(x));
    }  
    
    public void print(char x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(int x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(long x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(float x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(double x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(char[] x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(Object x) {
    	this.print(String.valueOf(x));
    }
    
    public void print(String text) {
        this.ensureContent();
        this.setContentType(TEXT);
        this.writeInternal(escape(text));
    }
    
    public void println(boolean x) {
    	this.println(String.valueOf(x));
    }
    
    
    public void println(char x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(int x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(long x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(float x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(double x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(char[] x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(Object x) {
    	this.println(String.valueOf(x));
    }
    
    public void println(String text) {
        this.ensureContent();
        this.print(text);
        this.println();
    }

    public void tag(String entity, boolean x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }  
    
    public void tag(String entity, char x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, int x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, long x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, float x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, double x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, char[] x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, Object x) {
    	this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void tag(String entity, String x) {
        this.startEntity(entity);
        this.print(x);
        this.endEntity();
    }
    
    public void close() {
        if ((this.options & AUTO_CLOSE) != 0) {
            while (!this.entities.isEmpty()) {
                this.endEntity();
            }
        }

        this.out.close();
    }

    public void flush() {
        this.out.flush();
    }

    public static void main(String[] args) {
    	PrintStream out = System.out;
//        Writer basewriter = new PrintWriter(System.out);
        XMLWriter w = new XMLWriter(out, NEWLINE_BEFORE_ENTITY | AUTO_INDENT);
        w.startEntity("a");
        w.startEntity("b");
        w.startEntity("c");
        w.print("x");
        w.endEntity(); // c
        w.startEntity("d");
        w.addAttribute("z", "3");
        w.endEntity(); // d
        w.endEntity(); // b
        w.startEntity("e");
        w.print("y");
        w.endEntity(); // e
        w.endEntity(); // a

        w.flush();
        w.close();
    }
}
