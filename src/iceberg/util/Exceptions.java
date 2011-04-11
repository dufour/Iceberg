package iceberg.util;

public final class Exceptions {
	private Exceptions() {
		// No instances
	}
	
	public static SoftException soften(String message, Throwable t) {
        return new SoftException(message, t);
    }
	
	public static SoftException soften(Throwable t) {
		return new SoftException(t);
	}
}
