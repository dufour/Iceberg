package iceberg.collections;

public class Box<E> {
	private E contents;
	
	public Box() {
	    this(null);
	}
	
	public Box(E contents) {
		this.contents = contents;
	}
	
	public E get() {
		return this.contents;
	}
	
	public E set(E contents) {
		E prev = this.contents;
		this.contents = contents;
		return prev;
	}
	
	@Override
	public int hashCode() {
		return 5227 + ((this.contents == null) ? 0 : this.contents.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		} else {
			Box<?> other = (Box<?>) obj;
			if (this.contents == null) {
				if (other.contents != null) {
					return false;
				}
			} else if (!this.contents.equals(other.contents)) {
				return false;
			}
		}
		
		return true;
	}

	public String toString() {
		return "Box(" + this.contents + ")";
	}
}
