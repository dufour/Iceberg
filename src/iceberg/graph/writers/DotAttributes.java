package iceberg.graph.writers;

public interface DotAttributes {
	public void addStyle(String style);
    public void addAttribute(String name, String value);
}
