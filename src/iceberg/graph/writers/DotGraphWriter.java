package iceberg.graph.writers;

import iceberg.graph.DirectedGraph;
import iceberg.graph.HashNodeIndexer;
import iceberg.graph.NodeIndexer;
import iceberg.io.IndentingDataStream;
import iceberg.io.PrintStreamAdapter;
import iceberg.util.Strings;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DotGraphWriter<N> implements GraphWriter {
	private final DirectedGraph<N> graph;
    private final IndentingDataStream out;
    private NodeIndexer<N> indexer;
    private List<NodeAttributeProvider<? super N>> node_attr_providers;
    private List<EdgeAttributeProvider<? super N>> edge_attr_providers;

    private BufferedDotAttributes attribs = null;

    public DotGraphWriter(DirectedGraph<N> graph, PrintStream out) {
        this.graph = graph;
        this.out = new IndentingDataStream(new PrintStreamAdapter(out));
    }

    public DirectedGraph<N> getGraph() {
        return this.graph;
    }

    public NodeIndexer<N> getNodeIndexer() {
        if (this.indexer == null) {
            this.indexer = new HashNodeIndexer<N>();
        }

        return this.indexer;
    }

    public void setNodeIndexer(NodeIndexer<N> indexer) {
        this.indexer = indexer;
    }

    public void addAttributeProvider(NodeAttributeProvider<? super N> provider) {
        if (this.node_attr_providers == null) {
            this.node_attr_providers = new ArrayList<NodeAttributeProvider<? super N>>(1);
        }
        this.node_attr_providers.add(provider);
    }

    public void addAttributeProvider(EdgeAttributeProvider<? super N> provider) {
        if (this.edge_attr_providers == null) {
            this.edge_attr_providers = new ArrayList<EdgeAttributeProvider<? super N>>(1);
        }
        this.edge_attr_providers.add(provider);
    }

    protected BufferedDotAttributes getAttributes() {
        if (this.attribs == null) {
            this.attribs = new BufferedDotAttributes();
        }

        return this.attribs;
    }

    protected void setAttributes(BufferedDotAttributes attribs) {
        this.attribs = attribs;
    }

    private List<NodeAttributeProvider<? super N>> getNodeAttributeProviders() {
        if (this.node_attr_providers != null) {
            return this.node_attr_providers;
        } else {
            return Collections.emptyList();
        }
    }

    private List<EdgeAttributeProvider<? super N>> getEdgeAttributeProviders() {
        if (this.edge_attr_providers != null) {
            return this.edge_attr_providers;
        } else {
            return Collections.emptyList();
        }
    }

    public void print() throws IOException {
        final DirectedGraph<N> g = this.graph;
        this.out.println("digraph \"" + g.getName() + "\" {");
        this.out.indent();

        String description = g.getDescription();
        if (description != null) {
        	this.out.println("// " + description + "\n");
        }

        this.out.println("/* Nodes */");

        for (N node: g) {
            this.startNode(node);
            for (NodeAttributeProvider<? super N> p: this.getNodeAttributeProviders()) {
                p.generateAttributes(node, this.getAttributes());
            }
            this.getAttributes().flush();
            this.end();
        }

        this.out.println("/* Edges */");

        for (N from: g) {
            for (N to: g.getSuccessors(from)) {
                this.startEdge(from, to);
                for (EdgeAttributeProvider<? super N> p: this.getEdgeAttributeProviders()) {
                    p.generateAttributes(from, to, this.getAttributes());
                }
                this.getAttributes().flush();
                this.end();
            }
        }

        this.out.unindent();
        this.out.println("}");
        this.out.flush();
    }

    private static final int IDLE = 0;
    private static final int STARTED = 1;
    private static final int ATTRIBS_ADDED = 2;

    private int status = IDLE;

    protected String getNodeID(N node) {
        return "node" + this.getNodeIndexer().getIndex(node);
    }

    protected void startNode(N node) {
        if (this.status != IDLE) throw new IllegalStateException();
        this.out.print(this.getNodeID(node));
        this.status = STARTED;
    }

    protected void end() {
        switch (this.status) {
            case STARTED:
                this.out.println();
                break;
            case ATTRIBS_ADDED:
                this.out.println("]");
                break;
            default:
                throw new IllegalStateException();
        }
        this.status = IDLE;
    }

    protected void startEdge(N from, N to) {
        this.out.print(this.getNodeID(from));
        this.out.print(" -> ");
        this.out.print(this.getNodeID(to));
        this.status = STARTED;
    }

    protected void addAttribute(String name, String value) {
        switch (this.status) {
            case STARTED:
                this.out.print("[");
                this.status = ATTRIBS_ADDED;
                break;
            case ATTRIBS_ADDED:
                this.out.print(", ");
                break;
            default:
                throw new IllegalStateException();
        }

        this.out.print(name + "=\"" + value + "\"");
    }

    protected final class BufferedDotAttributes implements DotAttributes {
    	private List<String> style = new LinkedList<String>();

    	public void addAttribute(String name, String value) {
    		if (name != null && name.equals("style")) {
    			this.addStyle(value);
    		} else {
    			DotGraphWriter.this.addAttribute(name, value);
    		}
    	}

    	public void addStyle(String style) {
    		this.style.add(style);
    	}

    	public void flush() {
    		if (!this.style.isEmpty()) {
    			DotGraphWriter.this.addAttribute("style", Strings.join(this.style, ","));
    			this.style.clear();
    		}
    	}
    }
}
