package iceberg.util;

public final class Assertions {
	private Assertions() {
		// No instances
	}
	
	public static void unreachable() {
		throw new RuntimeException("This code should have never been reached");
	}
	
	public static void unreachable(String message) {
		throw new RuntimeException(message);
	}
}
