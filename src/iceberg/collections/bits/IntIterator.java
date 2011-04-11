package iceberg.collections.bits;

public interface IntIterator {
	public boolean hasNext();
	public int next();
	public void remove();
}
